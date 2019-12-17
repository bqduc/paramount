package net.paramount.esmx.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.paramount.entity.stock.StockTransaction;
import net.paramount.framework.repository.BaseRepository;

@Repository
public interface StockTransactionRepository extends BaseRepository<StockTransaction, Long> {
	Optional<StockTransaction> findBySerialNo(String serialNo);

	@Query("SELECT entity FROM #{#entityName} entity WHERE ("
			+ " LOWER(entity.serialNo) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.name) like LOWER(CONCAT('%',:keyword,'%')) or "
			+ " LOWER(entity.notes) like LOWER(CONCAT('%',:keyword,'%')) "
			+ ")"
	)
	Page<StockTransaction> search(@Param("keyword") String keyword, Pageable pageable);
}
