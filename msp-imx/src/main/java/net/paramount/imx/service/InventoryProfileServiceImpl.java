package net.paramount.imx.service;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.exceptions.ExecutionContextException;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;
import net.paramount.imx.entity.InventoryProfile;
import net.paramount.imx.repository.InventoryProfileRepository;
import net.paramount.imx.specification.InventoryProfileSpecification;


@Service
public class InventoryProfileServiceImpl extends GenericServiceImpl<InventoryProfile, Long> implements InventoryProfileService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6295278179986436285L;

	@Inject 
	private InventoryProfileRepository repository;
	
	protected BaseRepository<InventoryProfile, Long> getRepository() {
		return this.repository;
	}

	@Override
	public InventoryProfile getOne(String code) throws ObjectNotFoundException {
		return (InventoryProfile)super.getOptionalObject(repository.findByCode(code));
	}

	@Override
	protected Page<InventoryProfile> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<InventoryProfile> getObjects(SearchParameter searchParameter) {
		Page<InventoryProfile> pagedProducts = this.repository.findAll(InventoryProfileSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
		//Perform additional operations here
		return pagedProducts;
	}

	@Override
	public ExecutionContext load(ExecutionContext executionContext) throws ExecutionContextException {
		log.info(executionContext.getExecutionStage());
		return executionContext;
	}

	@Override
	public InventoryProfile getByCode(String code) throws ObjectNotFoundException {
		Optional<InventoryProfile> optInventoryProfile = this.repository.findByCode(code);
		return optInventoryProfile.isPresent()?optInventoryProfile.get():null;
	}
}
