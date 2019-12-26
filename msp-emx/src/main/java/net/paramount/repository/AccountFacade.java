
package net.paramount.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import net.paramount.entity.emx.EnterpriseAccount;
import net.paramount.framework.repository.BaseDAO;

/**
 * 
 * @author MOHAMMED BOUNAGA
 * 
 *         github.com/medbounaga
 */

@Component // @Stateless
public class AccountFacade extends BaseDAO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7086317564136002833L;

	@Transactional
	public EnterpriseAccount create(EnterpriseAccount entity) {
		em.persist(entity);
		return entity;
	}

	@Transactional
	public EnterpriseAccount update(EnterpriseAccount entity) {
		em.merge(entity);
		return entity;
	}

	@Transactional
	public void remove(EnterpriseAccount entity) {
		em.remove(em.merge(entity));
	}

	public EnterpriseAccount find(Object id) {
		EnterpriseAccount entity = em.find(EnterpriseAccount.class, id);
		return entity;
	}

	public List<EnterpriseAccount> findByType(String type) {
		List result = em.createNamedQuery("Account.findByType").setParameter("type", type).getResultList();

		return result;
	}

	public List<EnterpriseAccount> findByName(String name) {
		List result = em.createNamedQuery("Account.findByName").setParameter("name", name).getResultList();

		return result;
	}

	public List<EnterpriseAccount> findAll() {
		javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(EnterpriseAccount.class));
		return em.createQuery(cq).getResultList();
	}

	public Double getTotalCredit(Integer accountId) {
		Double totalCredit = (Double) em.createNamedQuery("JournalItem.CreditSum").setParameter("accountId", accountId)
				.getSingleResult();

		if (totalCredit == null) {
			totalCredit = 0d;
		}
		return totalCredit;
	}

	public Double getTotalDebit(Integer accountId) {
		Double totalDebit = (Double) em.createNamedQuery("JournalItem.DebitSum").setParameter("accountId", accountId)
				.getSingleResult();

		if (totalDebit == null) {
			totalDebit = 0d;
		}
		return totalDebit;
	}
}
