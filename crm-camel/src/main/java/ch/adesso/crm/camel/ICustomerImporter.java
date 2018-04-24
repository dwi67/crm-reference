/*
 * Copyright (c) 2015 adesso Schweiz AG. All rights reserved.
 * This software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.
 */
package ch.adesso.crm.camel;

import ch.adesso.crm.persistence.entities.Customer;

public interface ICustomerImporter {
    void addCustomer(Customer customer);
}
