package ch.adesso.crm.camel;

import static org.mockito.Mockito.mock;
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

import ch.adesso.crm.persistence.entities.Customer;
import ch.adesso.crm.service.ICustomerService;

public class CustomerSimpleReaderTest extends CamelBlueprintTestSupport {
	
	private static final String MOCK_RESULT = "mock:result";

	private static final String DIRECT_ADD_CUSTOMER = "direct:addCustomer";

	private static final String MOCK_DIRECT_ADD_CUSTOMER = "mock:direct:addCustomer";

	private static final String DIRECT_STARTWITHXML = "direct:startwithxml";

	private static final Customer TEST_CUSTOMER_50 = new Customer("test", 50);
	
	private ICustomerService customerService;
	
	@Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/blueprint.xml,/OSGI-INF/blueprint/blueprint-services.xml,/OSGI-INF/blueprint/blueprint-simple-reader.xml";
    }

	@Override
	public void setUp() throws Exception {
		super.setUp();

		context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

            	// remove file endpoint
            	replaceFromWith(DIRECT_STARTWITHXML);
            	
            	// Mock add customer
            	weaveByToUri(DIRECT_ADD_CUSTOMER).replace().to(MOCK_DIRECT_ADD_CUSTOMER);
            }
        });
		
		context.getRouteDefinitions().get(1).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

            	weaveAddLast().to(MOCK_RESULT);
            }
        });
	}
    
    @Override
	protected void addServicesOnStartup(@SuppressWarnings("rawtypes") Map<String, KeyValueHolder<Object, Dictionary>> services) {

    	customerService =  mock(ICustomerService.class);
    	
    	services.put(ICustomerService.class.getName(), asService(customerService, null));
    	
    	super.addServicesOnStartup(services);
	}

	@Test
    public void testXmlImporter() throws Exception {

        MockEndpoint mock = getMockEndpoint(MOCK_DIRECT_ADD_CUSTOMER);
        
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived(TEST_CUSTOMER_50);

        template.sendBody(DIRECT_STARTWITHXML, "<customers><customer><name>test</name><age>50</age></customer></customers>");
        
        // assert expectations
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testCustomerToDatabase() throws Exception {

    	// Use mockito to mock the actual service call
    	when(customerService.saveOrUpdate(TEST_CUSTOMER_50)).thenReturn(TEST_CUSTOMER_50);
    	
        MockEndpoint mock = getMockEndpoint(MOCK_RESULT);
        
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived(TEST_CUSTOMER_50);

        template.sendBody(DIRECT_ADD_CUSTOMER, TEST_CUSTOMER_50);
        
        // assert expectations
        assertMockEndpointsSatisfied();
        
        verify(customerService, times(1)).saveOrUpdate(TEST_CUSTOMER_50);
    }   
}
