package net.paramount.css.service.system;

import org.springframework.data.domain.Page;

import net.paramount.entity.system.SystemSequence;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface SystemSequenceService extends GenericService<SystemSequence, Long>{

  /**
   * Get one system sequence with the provided code.
   * 
   * @param code The system sequence code
   * @return The system sequence
   * @throws ObjectNotFoundException If no such system sequence exists.
   */
	SystemSequence getOne(String code) throws ObjectNotFoundException;

  /**
   * Get one system sequences with the provided search parameters.
   * 
   * @param searchParameter The search parameter
   * @return The pageable system sequences
   */
	Page<SystemSequence> getObjects(SearchParameter searchParameter);
}
