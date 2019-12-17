package net.paramount.css.service.general;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.css.repository.general.CatalogueSubtypeRepository;
import net.paramount.css.specification.CatalogueSubtypeSpecification;
import net.paramount.entity.general.CatalogueSubtype;
import net.paramount.exceptions.MspDataException;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;

@Service
public class CatalogueSubtypeServiceImpl extends GenericServiceImpl<CatalogueSubtype, Long> implements CatalogueSubtypeService{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1435351574637430464L;
	@Inject 
	private CatalogueSubtypeRepository repository;
	
	protected BaseRepository<CatalogueSubtype, Long> getRepository() {
		return this.repository;
	}

	@Override
	public Optional<CatalogueSubtype> getOne(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	protected Page<CatalogueSubtype> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<CatalogueSubtype> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(CatalogueSubtypeSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}

	@Override
	protected Optional<CatalogueSubtype> fetchBusinessObject(Object key) throws MspDataException {
		return super.getBizObject("findByName", key);
	}
}
