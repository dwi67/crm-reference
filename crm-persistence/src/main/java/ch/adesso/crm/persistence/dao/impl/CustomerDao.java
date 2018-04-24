package ch.adesso.crm.persistence.dao.impl;

import ch.adesso.crm.persistence.dao.ICustomerDao;
import ch.adesso.crm.persistence.entities.Customer;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class CustomerDao implements ICustomerDao {

	@PersistenceContext(unitName="customer-pu")
    EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Customer getCustomerById(Long id) {
        return getEntityManager().find(Customer.class, id);
    }

    @Override
    public Customer saveOrUpdate(Customer customer) {
        return getEntityManager().merge(customer);
    }

    @Override
    public List<Customer> getCustomers() {
        TypedQuery<Customer> query
                = getEntityManager().createQuery("SELECT c FROM Customer c", Customer.class);
        query.setFirstResult(0);
        query.setMaxResults(100);
        return query.getResultList();
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customerToDelete = getCustomerById(id);
        if (customerToDelete != null) {
            getEntityManager().remove(customerToDelete);
        }
    }
}
