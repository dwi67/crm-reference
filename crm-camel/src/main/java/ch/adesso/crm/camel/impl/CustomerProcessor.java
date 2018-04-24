package ch.adesso.crm.camel.impl;

import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import ch.adesso.crm.persistence.entities.Customer;


/**
 * A bean that creates a customer from a String.
 */
public class CustomerProcessor implements Processor {

	private static Random randomGenerator = new Random();
	
	@Override
	public void process(final Exchange exchange) throws Exception {

		String line = exchange.getIn().getBody(String.class);
		
		// Get the first 14 characters
		String name = line.substring(0, 14).trim();
		name = name.substring(0, 1) + name.substring(1).toLowerCase();
		
		Customer customer = new Customer();
		customer.setName(name);
		customer.setAge(Integer.valueOf(randomGenerator.nextInt(100)));
		
		exchange.getIn().setBody(customer, Customer.class);
	}

}
