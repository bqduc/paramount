package net.paramount.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import net.paramount.entity.emx.EnterpriseAccount;
import net.paramount.entity.emx.EnterprisePayment;
import net.paramount.entity.emx.Journal;
import net.paramount.entity.emx.JournalEntry;
import net.paramount.entity.emx.Partner;
import net.paramount.framework.repository.BaseDAO;
import net.paramount.utility.IdGenerator;

/**
 * 
 * @author MOHAMMED BOUNAGA
 * 
 * github.com/medbounaga
 */

@Component//@Stateless
public class PaymentFacade extends BaseDAO{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4384797523311756834L;
		private IdGenerator idGeerator = new IdGenerator();

    public EnterprisePayment create(EnterprisePayment entity) {
        em.persist(entity);
        return entity;
    }

    public EnterprisePayment update(EnterprisePayment entity) {
        em.merge(entity);
        return entity;
    }

    public void remove(EnterprisePayment entity) {
        em.remove(em.merge(entity));
    }

    public EnterprisePayment find(Object id) {
        return em.find(EnterprisePayment.class, id);
    }

    public List<EnterprisePayment> findAll() {
        List<EnterprisePayment> partners = em.createNamedQuery("Payment.findAll")
                .getResultList();
        return partners;
    }

    public List<EnterprisePayment> findSupplierPayment() {
        List<EnterprisePayment> payments = em.createNamedQuery("Payment.findByPartnerType")
                .setParameter("partnerType", "supplier")
                .getResultList();
        return payments;
    }

    public List<EnterprisePayment> findCustomerPayment() {
        List<EnterprisePayment> payments = em.createNamedQuery("Payment.findByPartnerType")
                .setParameter("partnerType", "customer")
                .getResultList();
        return payments;
    }

    public List<Partner> findTopNCustomers(int n) {
        List result = em.createNamedQuery("Partner.findByCustomer")
                .setMaxResults(n)
                .getResultList();

        return result;
    }
    
    public List<Partner> findTopNSuppliers(int n) {
        List result = em.createNamedQuery("Partner.findBySupplier")
                .setMaxResults(n)
                .getResultList();

        return result;
    }

    public EnterprisePayment create(EnterprisePayment entity, String partnerType, String type) {
        if (entity != null) {

            em.persist(entity);
            em.flush();

            if (partnerType.equals("Customer") && type.equals("in")) {
                entity.setName(idGeerator.generateCustomerInPayment(entity.getId()));
            } else if (partnerType.equals("Customer") && type.equals("out")) {
                entity.setName(idGeerator.generateCustomerOutPayment(entity.getId()));
            } else if (partnerType.equals("Supplier") && type.equals("in")) {
                entity.setName(idGeerator.generateSupplierInPayment(entity.getId()));
            } else if (partnerType.equals("Supplier") && type.equals("out")) {
                entity.setName(idGeerator.generateSupplierOutPayment(entity.getId()));
            }
        }
        return entity;
    }
  
    
    public List<EnterprisePayment> findByPartner(Integer partnerId, String partnerType) {
        List<EnterprisePayment> payments = em.createNamedQuery("Payment.findByPartner")
                .setParameter("partnerId", partnerId)
                .setParameter("partnerType", partnerType)
                .getResultList();

        return payments;
    }

    public EnterpriseAccount findAccount(Object name) {

       List<EnterpriseAccount> accounts = em.createNamedQuery("Account.findByName")
                .setParameter("name", name)
                .getResultList();

        if (accounts != null && !accounts.isEmpty()) {
            return accounts.get(0);
        }

        return null;
    }

    public Journal findJournal(Object code) {
         List<Journal> journals = em.createNamedQuery("Journal.findByCode")
                .setParameter("code", code)
                .getResultList();

        if (journals != null && !journals.isEmpty()) {
            return journals.get(0);
        }

        return null;
    }

    public JournalEntry create(JournalEntry entity, String account) {
        em.persist(entity);
        em.flush();
        if (account.equals("Cash")) {
            entity.setName(idGeerator.generatePaymentCashEntryId(entity.getId()));
        } else if (account.equals("Bank")) {
            entity.setName(idGeerator.generatePaymentBankEntryId(entity.getId()));
        }
        return entity;
    }
}
