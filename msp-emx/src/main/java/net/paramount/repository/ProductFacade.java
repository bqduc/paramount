
package net.paramount.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import net.paramount.entity.emx.EnterpriseProduct;
import net.paramount.entity.emx.Inventory;
import net.paramount.entity.emx.ProductUom;
import net.paramount.entity.stock.ProductCategory;
import net.paramount.framework.repository.BaseDAO;

/**
 * 
 * @author MOHAMMED BOUNAGA
 * 
 * github.com/medbounaga
 */


@Component//@Stateless
public class ProductFacade extends BaseDAO{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8822453169083138986L;

		public EnterpriseProduct create(EnterpriseProduct entity) {
        em.persist(entity);
        return entity;
    }

    public EnterpriseProduct update(EnterpriseProduct entity) {
        em.merge(entity);
        return entity;
    }
    
    public Inventory update(Inventory entity) {
        em.merge(entity);
        return entity;
    }

    public void remove(EnterpriseProduct entity) {
        em.remove(em.merge(entity));
    }

    public EnterpriseProduct find(Object id) {
        return em.find(EnterpriseProduct.class, id);
    }
    
    public List<EnterpriseProduct> findSoldProducts() {
        List<EnterpriseProduct> products = em.createNamedQuery("Product.findBySaleOk")
                .getResultList();
        
        System.out.println("findSoldProducts: "+products.size());
        return products;
    }
    
    public List<EnterpriseProduct> findPurchasedProducts() {
        List<EnterpriseProduct> products = em.createNamedQuery("Product.findByPurchaseOk")
                .getResultList();
         System.out.println("findPurchasedProducts: "+products.size());
        return products;
    }
    
    public List<ProductCategory> findTopNProductCategories(int n) {
        List result = em.createNamedQuery("ProductCategory.findAll")
                .setMaxResults(n)
                .getResultList();

        return result;
    }
    
    public List<ProductUom> findTopNUnitsOfMeasure(int n) {
        List result = em.createNamedQuery("ProductUom.findAll")
                .setMaxResults(n)
                .getResultList();

        return result;
    }
    
    public Double countProductSales(Integer productId) {
        Double count = (Double) em.createNamedQuery("SaleOrderLine.countByProduct")
                .setParameter("productId", productId)
                .getSingleResult();

        if (count == null) {
            count = 0d;
        }
        return count;
    }
    
    public Double countProductPurchases(Integer productId) {
        Double count = (Double) em.createNamedQuery("PurchaseOrderLine.countByProduct")
                .setParameter("productId", productId)
                .getSingleResult();

        if (count == null) {
            count = 0d;
        }
        return count;
    }

    public List<EnterpriseProduct> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(EnterpriseProduct.class));
        return em.createQuery(cq).getResultList();
    }

    public List<EnterpriseProduct> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(EnterpriseProduct.class));
        javax.persistence.Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<EnterpriseProduct> rt = cq.from(EnterpriseProduct.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
