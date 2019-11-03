/**
 * 
 */
package net.paramount.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.paramount.auth.entity.UserProfile;
import net.paramount.framework.repository.BaseRepository;

/**
 * @author ducbui
 *
 */
@Repository
public interface UserProfileRepository extends BaseRepository <UserProfile, Long>{

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.authAccount.ssoId) like LOWER(CONCAT('%',:keyword,'%'))"
			+ ")"
	)
	Page<UserProfile> search(@Param("keyword") String keyword, Pageable pageable);
}
