package net.paramount.css.service.stock;

import org.springframework.data.domain.Page;

import net.paramount.entity.stock.Product;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface ProductService extends GenericService<Product, Long> {

	/**
	 * Get one Product with the provided code.
	 * 
	 * @param code
	 *            The Product code
	 * @return The Product
	 * @throws ObjectNotFoundException
	 *             If no such Product exists.
	 */
	Product getOne(String code) throws ObjectNotFoundException;

	/**
	 * Get one Product with the provided barcode.
	 * 
	 * @param barcode
	 *            The Product barcode
	 * @return The Product
	 * @throws ObjectNotFoundException
	 *             If no such Product exists.
	 */
	Product getByBarcode(String barcode) throws ObjectNotFoundException;

	/**
	 * Get one Enterprises with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Enterprises
	 */
	Page<Product> getObjects(SearchParameter searchParameter);
}
