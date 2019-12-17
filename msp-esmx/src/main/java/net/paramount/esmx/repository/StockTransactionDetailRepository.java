package net.paramount.esmx.repository;

import org.springframework.stereotype.Repository;

import net.paramount.entity.stock.StockTransactionDetail;
import net.paramount.framework.repository.BaseRepository;

@Repository
public interface StockTransactionDetailRepository extends BaseRepository<StockTransactionDetail, Long> {
}
