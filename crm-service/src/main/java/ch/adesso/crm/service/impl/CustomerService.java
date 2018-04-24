package ch.adesso.crm.service.impl;

import ch.adesso.crm.persistence.dao.ICustomerDao;
import ch.adesso.crm.persistence.entities.Customer;
import ch.adesso.crm.service.ICustomerService;
import java.util.List;

public class CustomerService implements ICustomerService {
    
    private ICustomerDao customerDao;
    
    public void setCustomerDao(ICustomerDao customerDao) {
        this.customerDao = customerDao;
    }
    
    @Override
    public Customer getCustomerById(Long id) {
        return customerDao.getCustomerById(id);
    }
    
    @Override
    public List<Customer> getCustomers() {
        return customerDao.getCustomers();
    }
    
    @Override
    public Customer saveOrUpdate(Customer customer) {
        return customerDao.saveOrUpdate(customer);
    }
    
    @Override
    public void deleteCustomer(Long id) {
        customerDao.deleteCustomer(id);
    }
}
