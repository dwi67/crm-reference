package ch.adesso.crm.persistence.dao;

import ch.adesso.crm.persistence.entities.Customer;
import java.util.List;

public interface ICustomerDao {
    Customer getCustomerById(Long id);
    
    List<Customer> getCustomers();
    
    Customer saveOrUpdate(Customer customer);
    
    void deleteCustomer(Long id);
}
