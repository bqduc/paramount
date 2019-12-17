package net.paramount.css.service.stock;

import org.springframework.data.domain.Page;

import net.paramount.entity.stock.InventoryItem;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface InventoryItemService extends GenericService<InventoryItem, Long> {

	/**
	 * Get one InventoryItem with the provided code.
	 * 
	 * @param code
	 *            The InventoryItem code
	 * @return The InventoryItem
	 * @throws ObjectNotFoundException
	 *             If no such InventoryItem exists.
	 */
	InventoryItem getOne(String code) throws ObjectNotFoundException;

	/**
	 * Get one InventoryItem with the provided barcode.
	 * 
	 * @param barcode
	 *            The InventoryItem barcode
	 * @return The InventoryItem
	 * @throws ObjectNotFoundException
	 *             If no such InventoryItem exists.
	 */
	InventoryItem getByBarcode(String barcode) throws ObjectNotFoundException;

	/**
	 * Get one Enterprises with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Enterprises
	 */
	Page<InventoryItem> getObjects(SearchParameter searchParameter);
}
