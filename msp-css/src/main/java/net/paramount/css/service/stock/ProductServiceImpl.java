package net.paramount.css.service.stock;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.css.repository.stock.ProductRepository;
import net.paramount.css.specification.ProductSpecification;
import net.paramount.entity.stock.Product;
import net.paramount.exceptions.ExecutionContextException;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;


@Service
public class ProductServiceImpl extends GenericServiceImpl<Product, Long> implements ProductService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7785962811434327239L;
	@Inject 
	private ProductRepository repository;
	
	protected BaseRepository<Product, Long> getRepository() {
		return this.repository;
	}

	@Override
	public Product getOne(String code) throws ObjectNotFoundException {
		return (Product)super.getOptionalObject(repository.findByCode(code));
	}

	@Override
	public Product getByBarcode(String barcode) throws ObjectNotFoundException {
		return (Product)super.getOptionalObject(repository.findByBarcode(barcode));
	}

	@Override
	protected Page<Product> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<Product> getObjects(SearchParameter searchParameter) {
		Page<Product> pagedProducts = this.repository.findAll(ProductSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
		//Perform additional operations here
		return pagedProducts;
	}

	@Override
	public ExecutionContext load(ExecutionContext executionContext) throws ExecutionContextException {
		return executionContext;
	}
}
