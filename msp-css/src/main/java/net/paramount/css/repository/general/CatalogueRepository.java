package net.paramount.css.repository.general;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.paramount.css.entity.general.Catalogue;
import net.paramount.framework.repository.BaseRepository;

@Repository
public interface CatalogueRepository extends BaseRepository<Catalogue, Long>{
	Optional<Catalogue> findByName(String name);
	Optional<Catalogue> findByCode(String code);
	Long countByCode(String code);

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.translatedName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.description) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<Catalogue> search(@Param("keyword") String keyword, Pageable pageable);
}
