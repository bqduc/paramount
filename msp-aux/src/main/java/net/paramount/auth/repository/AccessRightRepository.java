/**
 * 
 */
package net.paramount.auth.repository;

import org.springframework.stereotype.Repository;

import net.paramount.auth.entity.AccessRight;
import net.paramount.framework.repository.BaseRepository;

/**
 * @author bqduc
 *
 */
@Repository
public interface AccessRightRepository extends BaseRepository<AccessRight, Long> {
	AccessRight findByName(String name);
}
