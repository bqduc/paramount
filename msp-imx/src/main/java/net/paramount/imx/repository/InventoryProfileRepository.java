/**
 * 
 */
package net.paramount.imx.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.paramount.framework.repository.BaseRepository;
import net.paramount.imx.entity.InventoryProfile;

/**
 * @author bqduc
 *
 */
@Repository
public interface InventoryProfileRepository extends BaseRepository<InventoryProfile, Long> {
	Page<InventoryProfile> findAll(Pageable pageable);
	Page<InventoryProfile> findAllByOrderByIdAsc(Pageable pageable);
	Optional<InventoryProfile> findByName(String name);

	Optional<InventoryProfile> findByCode(String code);
	Long countByCode(String code);

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%'))"
			+ "or LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%')) "
			+ "or LOWER(entity.info) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<InventoryProfile> search(@Param("keyword") String keyword, Pageable pageable);
}
