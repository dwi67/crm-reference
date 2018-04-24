package ch.adesso.crm.ws.service;

import ch.adesso.crm.persistence.entities.Customer;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(targetNamespace = "http://impl.service.ws.crm.adesso.ch/")
public interface ICustomerWebService {

    @WebMethod
    @WebResult(name = "Customer")
    Customer getCustomerById(@WebParam(name = "id") Long id);

    @WebMethod
    List<Customer> getCustomers();

    @WebMethod
    @WebResult(name = "Customer")
    Customer saveOrUpdate(@WebParam(name = "customer") Customer customer);

    @WebMethod
    void deleteCustomer(@WebParam(name = "id") Long id);    
}

