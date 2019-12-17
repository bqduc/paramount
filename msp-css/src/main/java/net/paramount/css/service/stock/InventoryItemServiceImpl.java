package net.paramount.css.service.stock;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.css.repository.stock.InventoryItemRepository;
import net.paramount.css.specification.InventoryItemSpecification;
import net.paramount.entity.stock.InventoryItem;
import net.paramount.exceptions.ExecutionContextException;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;


@Service
public class InventoryItemServiceImpl extends GenericServiceImpl<InventoryItem, Long> implements InventoryItemService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7761477574156308888L;

	@Inject 
	private InventoryItemRepository repository;
	
	protected BaseRepository<InventoryItem, Long> getRepository() {
		return this.repository;
	}

	@Override
	public InventoryItem getOne(String code) throws ObjectNotFoundException {
		return (InventoryItem)super.getOptionalObject(repository.findByCode(code));
	}

	@Override
	public InventoryItem getByBarcode(String barcode) throws ObjectNotFoundException {
		return (InventoryItem)super.getOptionalObject(repository.findByBarcode(barcode));
	}

	@Override
	protected Page<InventoryItem> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<InventoryItem> getObjects(SearchParameter searchParameter) {
		Page<InventoryItem> pagedProducts = this.repository.findAll(InventoryItemSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
		//Perform additional operations here
		return pagedProducts;
	}

	@Override
	public ExecutionContext load(ExecutionContext executionContext) throws ExecutionContextException {
		return executionContext;
	}
}
