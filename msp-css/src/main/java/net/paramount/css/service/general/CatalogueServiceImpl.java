package net.paramount.css.service.general;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.css.repository.general.CatalogueRepository;
import net.paramount.css.specification.CatalogueSpecification;
import net.paramount.entity.general.Catalogue;
import net.paramount.exceptions.MspDataException;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;

@Service
public class CatalogueServiceImpl extends GenericServiceImpl<Catalogue, Long> implements CatalogueService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1435351574637430464L;
	@Inject 
	private CatalogueRepository repository;
	
	protected BaseRepository<Catalogue, Long> getRepository() {
		return this.repository;
	}

	@Override
	public Optional<Catalogue> getOne(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	public Optional<Catalogue> getByCode(String code) throws ObjectNotFoundException {
		return repository.findByCode(code);
	}

	@Override
	protected Page<Catalogue> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<Catalogue> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(CatalogueSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}

	@Override
	protected Optional<Catalogue> fetchBusinessObject(Object key) throws MspDataException {
		return super.getBizObject("findByName", key);
	}
}
