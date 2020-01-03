package net.paramount.css.service.general;

import net.paramount.entity.general.ItemType;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.service.GenericService;

public interface ItemTypeService extends GenericService<ItemType, Long>{

  /**
   * Get one item with the provided code.
   * 
   * @param code The item code
   * @return The item
   * @throws ObjectNotFoundException If no such account exists.
   */
	ItemType getByCode(String code) throws ObjectNotFoundException;

  /**
   * Get one item with the provided code.
   * 
   * @param name The item type name
   * @return The item
   * @throws ObjectNotFoundException If no such account exists.
   */
	ItemType getByName(String name) throws ObjectNotFoundException;
}
