package net.paramount.css.repository.general;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.paramount.entity.general.Currency;
import net.paramount.framework.repository.BaseRepository;

@Repository
public interface CurrencyRepository extends BaseRepository<Currency, Long>{
	Optional<Currency> findByAlphabeticCode(String alphabeticCode);
	Optional<Currency> findByNumericCode(String numericCode);
	Long countByAlphabeticCode(String alphabeticCode);
	Long countByNumericCode(String numericCode);

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.alphabeticCode) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.numericCode) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.info) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<Currency> search(@Param("keyword") String keyword, Pageable pageable);
}
