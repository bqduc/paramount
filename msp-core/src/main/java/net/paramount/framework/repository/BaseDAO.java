/**
 * 
 */
package net.paramount.framework.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.paramount.framework.service.ServiceImpl;

/**
 * @author ducbq
 *
 */
public abstract class BaseDAO extends ServiceImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3317395762045169767L;
	@PersistenceContext /* (unitName = GlobalConstants.APPLICATION_PERSISTENCE_UNIT) */
	protected EntityManager em;

	@Override
	protected BaseRepository<?, ?> getRepository() {
		return null;
	}
}
