package net.paramount.css.service.general;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.paramount.css.entity.general.MeasureUnit;
import net.paramount.css.repository.general.MeasureUnitRepository;
import net.paramount.css.specification.MeasureUnitSpecification;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;

@Service
public class MeasureUnitServiceImpl extends GenericServiceImpl<MeasureUnit, Long> implements MeasureUnitService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4804189794303411453L;
	@Inject 
	private MeasureUnitRepository repository;
	
	protected BaseRepository<MeasureUnit, Long> getRepository() {
		return this.repository;
	}

	@Override
	public Optional<MeasureUnit> getOne(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	protected Page<MeasureUnit> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<MeasureUnit> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(MeasureUnitSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}

	@Override
	protected Optional<MeasureUnit> fetchBusinessObject(Object key) throws ObjectNotFoundException {
		return super.getBizObject("findByName", key);
	}

	@Override
	public Optional<MeasureUnit> getByNameLocale(String nameLocal) throws ObjectNotFoundException {
		return super.getBizObject("findByNameLocal", nameLocal);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long count(String countByProperty, Object value) {
		if ("code".equalsIgnoreCase(countByProperty))
			return this.repository.countByCode((String)value);

		if ("name".equalsIgnoreCase(countByProperty))
			return this.repository.countByName((String)value);

		throw new RuntimeException(String.join("Count by property[", countByProperty, "] with value[", (String)value, "] Not implemented yet!"));
	}
}
