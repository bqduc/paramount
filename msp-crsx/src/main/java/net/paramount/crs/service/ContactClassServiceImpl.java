package net.paramount.crs.service;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.paramount.crs.entity.base.ContactClass;
import net.paramount.crs.repository.cta.ContactClassRepository;
import net.paramount.crs.repository.cta.ContactClassSpecification;
import net.paramount.exceptions.ExecutionContextException;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.service.GenericServiceImpl;


@Service
public class ContactClassServiceImpl extends GenericServiceImpl<ContactClass, Long> implements ContactService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7761477574156308888L;

	@Inject 
	private ContactClassRepository repository;
	
	protected BaseRepository<ContactClass, Long> getRepository() {
		return this.repository;
	}

	@Override
	public ContactClass getOne(String code) throws ObjectNotFoundException {
		return (ContactClass)super.getOptionalObject(repository.findByCode(code));
	}

	@Override
	protected Page<ContactClass> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<ContactClass> getObjects(SearchParameter searchParameter) {
		Page<ContactClass> pagedProducts = this.repository.findAll(ContactClassSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
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
				executionContext.setExecutionStage("There is not enough deployment specification for ContactClass. ");
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
			log.info("Start to deploy ContactClass data ......");
			
		} catch (Exception e) {
			throw new ExecutionContextException(e);
		}

		executionContext.setExecutionStage("The data deployment for project is done. ");
		log.info(executionContext.getExecutionStage());*/
		return executionContext;
	}
}
