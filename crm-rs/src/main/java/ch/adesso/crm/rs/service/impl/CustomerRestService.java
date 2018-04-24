package ch.adesso.crm.rs.service.impl;

import ch.adesso.crm.persistence.entities.Customer;
import ch.adesso.crm.rs.service.ICustomerRestService;
import ch.adesso.crm.service.ICustomerService;
import java.util.List;

public class CustomerRestService implements ICustomerRestService {

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
