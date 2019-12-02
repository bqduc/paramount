package net.paramount.css.repository.system;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.paramount.css.entity.system.SystemSequence;
import net.paramount.framework.repository.BaseRepository;

@Repository
public interface SystemSequenceRepository extends BaseRepository<SystemSequence, Long>{
	Optional<SystemSequence> findByName(String name);
	Optional<SystemSequence> findByCode(String code);

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.info) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<SystemSequence> search(@Param("keyword") String keyword, Pageable pageable);
}
