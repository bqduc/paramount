package net.paramount.css.service.system;

import org.springframework.data.domain.Page;

import net.paramount.entity.system.Sequence;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface SequenceService extends GenericService<Sequence, Long>{

  /**
   * Get one system sequence with the provided serial.
   * 
   * @param serial The system sequence serial
   * @return The system sequence
   * @throws ObjectNotFoundException If no such system sequence exists.
   */
	Sequence getOne(String serial) throws ObjectNotFoundException;

  /**
   * Get one system sequences with the provided search parameters.
   * 
   * @param searchParameter The search parameter
   * @return The pageable system sequences
   */
	Page<Sequence> getObjects(SearchParameter searchParameter);
}
