package net.paramount.esmx.service.stock;

import org.springframework.data.domain.Page;

import net.paramount.entity.stock.StockTransaction;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface StockTransactionService extends GenericService<StockTransaction, Long> {

	/**
	 * Get one StockTransaction with the provided code.
	 * 
	 * @param serialNo
	 *            The StockTransaction serial no
	 * @return The StockTransaction
	 * @throws ObjectNotFoundException
	 *             If no such StockTransaction exists.
	 */
	StockTransaction getOne(String serialNo) throws ObjectNotFoundException;

	/**
	 * Get one Enterprises with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Enterprises
	 */
	Page<StockTransaction> getObjects(SearchParameter searchParameter);
}
