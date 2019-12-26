package net.paramount.service;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.entity.emx.PurchaseOrder;
import net.paramount.exceptions.ExecutionContextException;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;
import net.paramount.repository.PurchaseOrderRepository;


@Service
public class PurchaseOrderServiceImpl extends GenericServiceImpl<PurchaseOrder, Long> implements PurchaseOrderService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7785962811434327239L;
	@Inject 
	private PurchaseOrderRepository repository;
	
	protected BaseRepository<PurchaseOrder, Long> getRepository() {
		return this.repository;
	}

	@Override
	public PurchaseOrder getByName(String name) throws ObjectNotFoundException {
		return (PurchaseOrder)super.getOptionalObject(repository.findByName(name));
	}

	@Override
	protected Page<PurchaseOrder> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	/*@Override
	public Page<PurchaseOrder> getObjects(SearchParameter searchParameter) {
		Page<PurchaseOrder> pagedPurchaseOrders = this.repository.findAll(PurchaseOrderSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
		//Perform additional operations here
		return pagedPurchaseOrders;
	}*/

	@Override
	public ExecutionContext load(ExecutionContext executionContext) throws ExecutionContextException {
		return executionContext;
	}
}
