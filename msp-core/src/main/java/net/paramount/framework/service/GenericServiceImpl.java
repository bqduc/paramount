package net.paramount.framework.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.paramount.common.CommonBeanUtils;
import net.paramount.common.CommonConstants;
import net.paramount.common.CommonUtility;
import net.paramount.exceptions.ExecutionContextException;
import net.paramount.exceptions.MspDataException;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.entity.BizObjectBase;
import net.paramount.framework.entity.ObjectBase;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.predicator.BrilliancePredicator;
import net.paramount.framework.repository.BaseRepository;
import net.paramount.framework.specification.DefaultSpecification;


@Service
public abstract class GenericServiceImpl<ClassType extends ObjectBase, Key extends Serializable> extends BaseServiceImpl<ClassType, Key> implements GenericService<ClassType, Key>{
	private static final long serialVersionUID = 7066816485194481124L;

	protected abstract BaseRepository<ClassType, Key> getRepository();
	//protected abstract BrillianceRepository<EntityType, Key> getRepository();

	protected BrilliancePredicator<ClassType> getRepositoryPredicator(){
		return null;
	}

  @PersistenceContext
  private EntityManager em;

  protected EntityManager getEntityManager(){
  	return this.em;
  }

  protected String getLoggedInUsername() {
  	String loggedInUsername = null;
  	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  	if (principal instanceof UserDetails) {
  		loggedInUsername = ((UserDetails)principal).getUsername();
  	} else {
  		loggedInUsername = principal.toString();
  	}
  	return loggedInUsername;
  }
  
	protected Specification<ClassType> getRepoSpecification(SearchParameter searchParameter){
  	//return (Specification<EntityType>) DefaultBrilliancePredicator.builder().build().buildSpecification(searchParameter);
  	return null;
  }

	/**
	 * @param 
	 * contextParams context parameters
	 * 
	 */
	protected Optional<ClassType> getBizObject(Map<?, ?> contextParams) throws ObjectNotFoundException {
		Object operationSpec = contextParams.get(CommonConstants.PARAM_OPERATION);
		Map<?, ?> operationData = (Map<?, ?>)contextParams.get(CommonConstants.PARAM_DATA);
		
		Object fetchedObject = null;
		BaseRepository<ClassType, Key> repository = this.getRepository();
		try {
			if (operationSpec instanceof String) {
				fetchedObject = CommonBeanUtils.invokeOperation(repository, (String)operationSpec, operationData);
			} else if (operationSpec instanceof List) {
				List<String> operationSpecs = (List<String>)operationSpec;
				for (String currentOperationSpec :operationSpecs) {
					fetchedObject = CommonBeanUtils.invokeOperation(repository, (String)operationSpec, operationData);
					if (null != fetchedObject) {
						break;
					}
				}
			}
		} catch (Exception e) {
			throw new ObjectNotFoundException(e);
		}
		return Optional.of((ClassType)fetchedObject);
	}

	protected Optional<ClassType> getBizObject(String defaultOperationName, Object param) throws ObjectNotFoundException {
		Map<String, Object> paramData = CommonUtility.createParameterMap("name", param);
		Map<String, Object> contextParams = CommonUtility.createParameterMap(CommonConstants.PARAM_OPERATION, defaultOperationName, CommonConstants.PARAM_DATA, paramData);

		return getBizObject(contextParams);
	}

	protected Optional<ClassType> fetchBusinessObject(Object key) throws MspDataException {
    throw new MspDataException("Not implemented yet. ");
	}

	protected Page<ClassType> getPaginatedObjects(Integer page, Integer size){
		int requestedPageIdx = page-1;
		if (requestedPageIdx < 0)
			requestedPageIdx = 0;

		PageRequest pageRequest = PageRequest.of(requestedPageIdx, size, Sort.Direction.ASC, "id");
    BaseRepository<ClassType, Key> repo = getRepository();
    if (null != repo)
    	return repo.findAll(pageRequest);

    return null;
	}

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
	public void remove(Key id) {
		try {
			//getRepository().delete(id);
		} catch (EmptyResultDataAccessException e) {
			log.error("Delete object by key", e);
		}
	}

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
	public void remove(ClassType entity) {
		try {
			getRepository().delete(entity);
		} catch (EmptyResultDataAccessException e) {
			log.error("Delete object. ", e);
		}
	}

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void removeAll() {
		try {
			getRepository().deleteAll();
		} catch (EmptyResultDataAccessException e) {
			log.error("Delete all objects. ", e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long count() {
		return getRepository().count();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long count(String countByProperty, Object value) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long count(String countMethodName, Map<?, ?> parameters) {
		Object retData = null;
		long count = 0;
		try {
			retData = CommonBeanUtils.invokeOperation(this.getRepository(), countMethodName, parameters);
			count = (Long)retData;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| InstantiationException e) {
			log.error(e);
		}
		return count;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ClassType> getObjects() {
		List<ClassType> results = new ArrayList<>();
		getRepository().findAll().forEach(results::add);
		return results;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<ClassType> getObjects(Integer pageNumber) {
		return getPaginatedObjects(pageNumber, CommonConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<ClassType> getObjects(Integer pageNumber, Integer size) {
		return getPaginatedObjects(pageNumber, size);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<ClassType> searchObjects(String keyword, Pageable pageable) {
		return performSearch(keyword, pageable);
	}

	@Transactional(readOnly = true)
	public Page<ClassType> search(Map<String, Object> parameters){
		return searchObjects((String)parameters.get(CommonConstants.PARAM_KEYWORD), (Pageable)parameters.get(CommonConstants.PARAM_PAGEABLE));
	}

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public ClassType save(ClassType entity) {
  	ClassType mergedEntity = null;
  	BaseRepository<ClassType, Key> respository = getRepository();
  	if (null != respository){
  		respository.saveAndFlush(entity);
  	} else {
  		log.info("There is no implemented repository for " + this.getClass().getSimpleName());
    	if (null == entity.getId()){
    		this.em.persist(entity);
    	}else{
    		mergedEntity = this.em.merge(entity);
    		this.em.refresh(mergedEntity);
    	}
    	this.em.flush();
  		log.info("Use the persistence context entity manager object instead of repository. ");
  	}

  	return entity;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public ClassType saveOrUpdate(ClassType entity) {
  	return this.save(entity);
  }

	public List<ClassType> imports(Map<Object, Object> parameters){
		return null;
	}

	protected Page<ClassType> doGetObjects(SearchParameter searchParameter) {
		BaseRepository<ClassType, Key> repository = getRepository();
		BrilliancePredicator<ClassType> predicator = this.getRepositoryPredicator();
		Page<ClassType> pagedObjects = null;
		if (null != predicator){
			pagedObjects = repository.findAll(predicator.buildSpecification(searchParameter), searchParameter.getPageable());
		}else{
			//pagedObjects = ListUtility.createPageable(repository.findAll(), searchParameter.getPageable());
		}
		return pagedObjects;
	}

	@Override
	public Page<ClassType> getObjects(SearchParameter searchParameter) {
		//Page<EntityType> pagedEntities = doGetObjects(searchParameter);
		Page<ClassType> pagedEntities = performGetObjects(searchParameter);
		//Perform additional operations here
		return pagedEntities;
	}

	public ExecutionContext load(ExecutionContext executionContext) throws ExecutionContextException {
		return executionContext;
	}

	protected Page<ClassType> performGetObjects(SearchParameter searchParameter) {
		return getRepository().findAll(
				DefaultSpecification.<ClassType, SearchRequest>builder().build().buildRepoSpecification(searchParameter),
				searchParameter.getPageable());
	}

	public Optional<ClassType> getBusinessObject(Object key) throws MspDataException {
		Optional<ClassType> fetchedBizObject = this.fetchBusinessObject(key);
		if (!fetchedBizObject.isPresent())
			return Optional.empty();

		if (fetchedBizObject.get() instanceof BizObjectBase && 
				(!Boolean.TRUE.equals(((BizObjectBase)fetchedBizObject.get()).isActivated())||!Boolean.TRUE.equals(((BizObjectBase)fetchedBizObject.get()).isVisible()))) {
			log.error("The business object with key: [" + key + "] not activated or visible yet. ");
			return Optional.empty();
		}

		return fetchedBizObject;
	}

	public String nextSerial(String prefix) throws MspDataException {
		throw new MspDataException("Not implemented yet!");
	}
}