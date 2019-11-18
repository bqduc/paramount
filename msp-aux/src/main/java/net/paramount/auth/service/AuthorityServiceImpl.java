package net.paramount.auth.service;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.auth.entity.Authority;
import net.paramount.auth.repository.AuthorityRepository;
import net.paramount.auth.specification.AuthoritySpecification;
import net.paramount.exceptions.ExecutionContextException;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;


@Service
public class AuthorityServiceImpl extends GenericServiceImpl<Authority, Long> implements AuthorityService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7761477574156308888L;

	@Inject 
	private AuthorityRepository repository;
	
	protected BaseRepository<Authority, Long> getRepository() {
		return this.repository;
	}

	@Override
	public Authority getByName(String name) throws ObjectNotFoundException {
		return (Authority)super.getOptionalObject(repository.findByName(name));
	}

	@Override
	protected Page<Authority> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<Authority> getObjects(SearchParameter searchParameter) {
		Page<Authority> pagedProducts = this.repository.findAll(AuthoritySpecification.buildSpecification(searchParameter), searchParameter.getPageable());
		//Perform additional operations here
		return pagedProducts;
	}

	@Override
	public ExecutionContext load(ExecutionContext executionContext) throws ExecutionContextException {
		/*GlobalDataServicesRepository globalDataServicesRepository = null;
		Object projectContextData = null;
		DataInterfaceModel dataInterfaceModel = null;
		IDataContainer<String> dataContainer = null;
		try {
			if (!(executionContext.containKey(DeploymentSpecification.DEPLOYMENT_DATA_KEY) || 
					executionContext.containKey(DeploymentSpecification.DEPLOYMENT_DATA_MODEL_KEY))){
				executionContext.setExecutionStage("There is not enough deployment specification for Authority. ");
				log.info(executionContext.getExecutionStage());
				return executionContext;
			}

			globalDataServicesRepository = GlobalDataServicesRepository.builder().build();
			projectContextData = executionContext.getContextData(DeploymentSpecification.DEPLOYMENT_DATA_KEY);
			dataInterfaceModel = (DataInterfaceModel)executionContext.getContextData(DeploymentSpecification.DEPLOYMENT_DATA_MODEL_KEY);
			if (DataSourceType.CSV.equals(dataInterfaceModel.getDataSourceType())){
				dataContainer = globalDataServicesRepository.readCsvFile(
						(InputStream)projectContextData, 
						dataInterfaceModel.isProcessColumnHeaders(), 
						dataInterfaceModel.getComponentSeparator());
			} else if (DataSourceType.EXCEL.equals(dataInterfaceModel.getDataSourceType())) {
			}

			if (CommonUtility.isEmpty(dataContainer)){
				executionContext.setExecutionStage("The data container is empty. Please recheck data source. ");
				log.info(executionContext.getExecutionStage());
				return executionContext;
			}
			log.info("Start to deploy Authority data ......");
			
		} catch (Exception e) {
			throw new ExecutionContextException(e);
		}

		executionContext.setExecutionStage("The data deployment for project is done. ");
		log.info(executionContext.getExecutionStage());*/
		return executionContext;
	}
}
