package net.paramount.css.service.system;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.css.entity.system.SystemSequence;
import net.paramount.css.repository.system.SystemSequenceRepository;
import net.paramount.css.specification.SystemSequenceSpecification;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;

@Service
public class SystemSequenceServiceImpl extends GenericServiceImpl<SystemSequence, Long> implements SystemSequenceService{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5209863588217204283L;

	@Inject 
	private SystemSequenceRepository repository;
	
	protected BaseRepository<SystemSequence, Long> getRepository() {
		return this.repository;
	}

	@Override
	public SystemSequence getOne(String code) throws ObjectNotFoundException {
		return super.getOptionalObject(repository.findByCode(code));
	}

	@Override
	protected Page<SystemSequence> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<SystemSequence> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(SystemSequenceSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}
}
