package ch.adesso.crm.service;

import ch.adesso.crm.persistence.entities.Customer;
import java.util.List;

public interface ICustomerService {
    Customer getCustomerById(Long id);

    List<Customer> getCustomers();
    
    Customer saveOrUpdate(Customer customer);
    
    void deleteCustomer(Long id);    
}
