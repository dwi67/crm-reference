package ch.adesso.crm.camel;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Dictionary;
import java.util.Map;

import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.junit.Test;
import org.mockito.Mockito;

import ch.adesso.crm.persistence.entities.Customer;
import ch.adesso.crm.service.ICustomerService;

public class CustomerSedaReaderTest extends CamelBlueprintTestSupport {
	
	private static final int DEAD_LETTER_CHANNEL = 3;

	private static final int TO_DATABASE_ROUTE = 2;

	private static final int FLAT_FILE_IMPORTER_ROUTE = 1;

	private static final int XML_FILE_IMPORTER_ROUTE = 0;

	private static final String STARTTXT_ENDPOINT = "direct:starttxt";

	private static final String STARTXML_ENDPOINT = "direct:startxml";

	private static final String TO_DATABASE_ENDPOINT = "direct:toDatabaseEndpoint";

	private static final String CUSTOMER_TO_DATABASE_WRITER_ENDPOINT = "seda:customerToDatabaseWriter";

	private static final String MOCK_CUSTOMER_TO_DATABASE_WRITER_ENDPOINT = "mock:" + CUSTOMER_TO_DATABASE_WRITER_ENDPOINT;
	
	private static final String MOCK_DEAD_LETTER_CHANNEL = "mock:deadLetterChannel";

	private static final String MOCK_TODATABASE = "mock:toDatabase";

	private static final Customer TEST_CUSTOMER_50 = new Customer("test", 50);
	
	private static final Customer TEST_CUSTOMER_NO_NAME = new Customer(null, 50);

	private ICustomerService customerService;
	
	@Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/blueprint.xml,/OSGI-INF/blueprint/blueprint-services.xml,/OSGI-INF/blueprint/blueprint-seda-reader.xml";
    }

	@Override
	public void setUp() throws Exception {
		super.setUp();

		context.getRouteDefinitions().get(XML_FILE_IMPORTER_ROUTE).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

            	// remove file endpoint
            	replaceFromWith(STARTXML_ENDPOINT);
            	
            	// Mock add customer
            	mockEndpointsAndSkip(CUSTOMER_TO_DATABASE_WRITER_ENDPOINT);
            }
        });

		context.getRouteDefinitions().get(FLAT_FILE_IMPORTER_ROUTE).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

            	// remove file endpoint
            	replaceFromWith(STARTTXT_ENDPOINT);
            	
            	// Mock add customer
            	mockEndpointsAndSkip(CUSTOMER_TO_DATABASE_WRITER_ENDPOINT);
            }
        });
		
		context.getRouteDefinitions().get(TO_DATABASE_ROUTE).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

            	replaceFromWith(TO_DATABASE_ENDPOINT);
            	
            	weaveById("customerImporter").after().to(MOCK_TODATABASE);
            }
        });
		
		context.getRouteDefinitions().get(DEAD_LETTER_CHANNEL).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

            	weaveAddLast().to(MOCK_DEAD_LETTER_CHANNEL);
            }
        });
	}
    
    @Override
	protected void addServicesOnStartup(@SuppressWarnings("rawtypes") Map<String, KeyValueHolder<Object, Dictionary>> services) {

    	customerService =  Mockito.mock(ICustomerService.class);
    	
    	services.put(ICustomerService.class.getName(), asService(customerService, null));
    	
    	super.addServicesOnStartup(services);
	}
    
    @Test
    public void testXmlImporter() throws Exception {

        MockEndpoint mock = getMockEndpoint(MOCK_CUSTOMER_TO_DATABASE_WRITER_ENDPOINT);
        
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived(TEST_CUSTOMER_50);

        template.sendBody(STARTXML_ENDPOINT, "<customers><customer><name>test</name><age>50</age></customer></customers>");
        
        // assert expectations
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testXmlImporterSaxParserError() throws Exception {

        MockEndpoint mock = getMockEndpoint(MOCK_DEAD_LETTER_CHANNEL);
        
        mock.expectedMessageCount(1);

        template.sendBody(STARTXML_ENDPOINT, "<customers><custoer><name>test</name><age>50</age></customer></customers>");
        
        // assert expectations
        assertMockEndpointsSatisfied();
    }
   
    
    @Test
    public void testTxtImporter() throws Exception {

        MockEndpoint mock = getMockEndpoint(MOCK_CUSTOMER_TO_DATABASE_WRITER_ENDPOINT);
        
        mock.expectedMessageCount(1);

        template.sendBody(STARTTXT_ENDPOINT, "CUSTOMER            .........");
        
        // assert expectations
        assertMockEndpointsSatisfied();
        
        // Only checking the actual mock data is possible because of the random generation
        // of the customers age
        Customer customer = mock.getExchanges().get(0).getIn().getBody(Customer.class);
        
        assertEquals("Customer", customer.getName());
        assertNotNull(customer.getAge());
    }
    
    @Test
    public void testCustomerToDatabase() throws Exception {

    	// Use mockito to mock the actual service call
    	when(customerService.saveOrUpdate(TEST_CUSTOMER_50)).thenReturn(TEST_CUSTOMER_50);
    	
        MockEndpoint mock = getMockEndpoint(MOCK_TODATABASE);
        
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived(TEST_CUSTOMER_50);

        template.sendBody(TO_DATABASE_ENDPOINT, TEST_CUSTOMER_50);
        
        // assert expectations
        assertMockEndpointsSatisfied();
        
        verify(customerService, times(1)).saveOrUpdate(TEST_CUSTOMER_50);
    }   

    @Test
    public void testCustomerToDatabaseWithIllegalNme() throws Exception {

    	// Use mockito to mock the actual service call
    	when(customerService.saveOrUpdate(TEST_CUSTOMER_NO_NAME)).thenThrow(new IllegalStateException("Empty name is not allowed"));
    	
        MockEndpoint mock = getMockEndpoint(MOCK_DEAD_LETTER_CHANNEL);
        
        mock.expectedMessageCount(1);
   
        template.sendBody(TO_DATABASE_ENDPOINT, TEST_CUSTOMER_NO_NAME);
        
        // assert expectations
        assertMockEndpointsSatisfied();
    }     
}
