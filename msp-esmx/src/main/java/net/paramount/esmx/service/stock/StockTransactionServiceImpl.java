package net.paramount.esmx.service.stock;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.entity.stock.StockTransaction;
import net.paramount.esmx.repository.StockTransactionRepository;
import net.paramount.esmx.specification.StockTransactionSpecification;
import net.paramount.exceptions.ExecutionContextException;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;


@Service
public class StockTransactionServiceImpl extends GenericServiceImpl<StockTransaction, Long> implements StockTransactionService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5626880346902607644L;

	@Inject 
	private StockTransactionRepository repository;
	
	protected BaseRepository<StockTransaction, Long> getRepository() {
		return this.repository;
	}

	@Override
	public StockTransaction getOne(String serialNo) throws ObjectNotFoundException {
		return (StockTransaction)super.getOptionalObject(repository.findBySerialNo(serialNo));
	}

	@Override
	protected Page<StockTransaction> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<StockTransaction> getObjects(SearchParameter searchParameter) {
		Page<StockTransaction> pagedProducts = this.repository.findAll(StockTransactionSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
		//Perform additional operations here
		return pagedProducts;
	}

	@Override
	public ExecutionContext load(ExecutionContext executionContext) throws ExecutionContextException {
		return executionContext;
	}
}
