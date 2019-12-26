package net.paramount.service;

import org.springframework.data.domain.Page;

import net.paramount.entity.emx.PurchaseOrder;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface PurchaseOrderService extends GenericService<PurchaseOrder, Long> {

	/**
	 * Get one PurchaseOrder with the provided name.
	 * 
	 * @param name
	 *            The PurchaseOrder name
	 * @return The PurchaseOrder
	 * @throws ObjectNotFoundException
	 *             If no such PurchaseOrder exists.
	 */
	PurchaseOrder getByName(String name) throws ObjectNotFoundException;

	/**
	 * Get one Enterprises with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Enterprises
	 */
	Page<PurchaseOrder> getObjects(SearchParameter searchParameter);
}
