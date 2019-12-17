package net.paramount.crs.repository.cta;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.paramount.entity.contact.ContactProfile;
import net.paramount.framework.repository.BaseRepository;

@Repository
public interface ContactProfileRepository extends BaseRepository<ContactProfile, Long> {
	Optional<ContactProfile> findByEmail(String email);

	Optional<ContactProfile> findByCode(String code);
	Long countByCode(String code);
	
  @Query(value = "SELECT entity.code FROM #{#entityName} entity ", nativeQuery = true)
  List<String> findCode();

  @Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.code) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.firstName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.lastName) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.email) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<ContactProfile> search(@Param("keyword") String keyword, Pageable pageable);
}
