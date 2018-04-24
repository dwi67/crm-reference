package ch.adesso.crm.ws.service.impl;

import ch.adesso.crm.persistence.entities.Customer;
import ch.adesso.crm.service.ICustomerService;
import ch.adesso.crm.ws.service.ICustomerWebService;
import java.util.List;

import javax.jws.WebService;

@WebService(targetNamespace = "http://impl.service.ws.crm.adesso.ch/")
public class CustomerWebService implements ICustomerWebService {

    private ICustomerService customerService;

    public void setCustomerService(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerService.getCustomerById(id);
    }

    @Override
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @Override
    public Customer saveOrUpdate(Customer customer) {
        return customerService.saveOrUpdate(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerService.deleteCustomer(id);
    }
}
