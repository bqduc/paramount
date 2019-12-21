package net.paramount.css.repository.general;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.paramount.entity.general.Catalogue;
import net.paramount.framework.repository.CodeNameBaseRepository;

@Repository
public interface CatalogueRepository extends CodeNameBaseRepository<Catalogue, Long>{
	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.translatedName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.info) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<Catalogue> search(@Param("keyword") String keyword, Pageable pageable);
}
