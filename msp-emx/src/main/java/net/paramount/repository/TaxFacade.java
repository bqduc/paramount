
package net.paramount.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import net.paramount.entity.emx.EnterpriseTax;
import net.paramount.framework.repository.BaseDAO;

/**
 * 
 * @author MOHAMMED BOUNAGA
 * 
 * github.com/medbounaga
 */


@Component//@Stateless
public class TaxFacade extends BaseDAO{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2040454149756281288L;

		public EnterpriseTax create(EnterpriseTax entity) {
        em.persist(entity);
        return entity;
    }

    public EnterpriseTax update(EnterpriseTax entity) {
        em.merge(entity);
        return entity;
    }

    public void remove(EnterpriseTax entity) {
        em.remove(em.merge(entity));
    }

    public EnterpriseTax find(Object id) {
        return em.find(EnterpriseTax.class, id);
    }
    
    public List<EnterpriseTax> findSaleTaxes() {
        List<EnterpriseTax> taxes = em.createNamedQuery("Tax.findByType")
                .setParameter("typeTaxUse", "Sale")
                .getResultList();
        return taxes;
    }
    
    public List<EnterpriseTax> findPurchaseTaxes() {
        List<EnterpriseTax> taxes = em.createNamedQuery("Tax.findByType")
                .setParameter("typeTaxUse", "Purchase")
                .getResultList();
        return taxes;
    }

    public List<EnterpriseTax> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(EnterpriseTax.class));
        return em.createQuery(cq).getResultList();
    }

    public List<EnterpriseTax> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(EnterpriseTax.class));
        javax.persistence.Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<EnterpriseTax> rt = cq.from(EnterpriseTax.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
