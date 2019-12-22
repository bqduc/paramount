package net.paramount.css.service.system;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.css.repository.system.SystemSequenceRepository;
import net.paramount.css.specification.SequenceSpecification;
import net.paramount.entity.system.Sequence;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;

@Service
public class SequenceServiceImpl extends GenericServiceImpl<Sequence, Long> implements SequenceService{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5209863588217204283L;

	@Inject 
	private SystemSequenceRepository repository;
	
	protected BaseRepository<Sequence, Long> getRepository() {
		return this.repository;
	}

	@Override
	public Sequence getOne(String serial) throws ObjectNotFoundException {
		return super.getOptionalObject(repository.findBySerial(serial));
	}

	@Override
	protected Page<Sequence> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<Sequence> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(SequenceSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}
}
