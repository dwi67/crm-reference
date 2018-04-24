package ch.adesso.crm.camel.impl;

import ch.adesso.crm.camel.ICustomerImporter;
import ch.adesso.crm.persistence.entities.Customer;
import ch.adesso.crm.service.ICustomerService;
import org.apache.camel.Body;

public class CustomerImporter implements ICustomerImporter {

    private ICustomerService customerService;

    public void setCustomerService(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void addCustomer(@Body Customer customer) {
        customerService.saveOrUpdate(customer);
    }
}
