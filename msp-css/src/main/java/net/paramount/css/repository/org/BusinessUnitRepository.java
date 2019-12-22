package net.paramount.css.repository.org;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.paramount.entity.general.BusinessUnit;
import net.paramount.framework.repository.BaseRepository;

@Repository
public interface BusinessUnitRepository extends BaseRepository<BusinessUnit, Long>{
	Optional<BusinessUnit> findByCode(String code);
	Long countByCode(String code);
	Long countByName(String name);
	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.nameLocal) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.spocUser.firstName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.spocUser.lastName) like LOWER(CONCAT('%',:keyword,'%'))"
			+ ")"
	)
	Page<BusinessUnit> search(@Param("keyword") String keyword, Pageable pageable);
}
