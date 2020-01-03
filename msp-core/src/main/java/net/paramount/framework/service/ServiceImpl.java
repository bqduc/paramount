package net.paramount.framework.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.paramount.common.CommonBeanUtils;
import net.paramount.common.CommonConstants;
import net.paramount.common.ListUtility;
import net.paramount.exceptions.ExecutionContextException;
import net.paramount.exceptions.MspRuntimeException;
import net.paramount.framework.component.ComponentBase;
import net.paramount.framework.entity.ObjectBase;
import net.paramount.framework.repository.BaseRepository;


@Service
public abstract class ServiceImpl<ClassType extends ObjectBase, Key extends Serializable> extends ComponentBase implements IService<ClassType, Key>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7920908481607510076L;

	protected abstract BaseRepository<ClassType, Key> getRepository();

  /**
   * Get entity with the provided key.
   * 
   * @param id The entity key
   * @return The entity
   */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ClassType getObject(Key id) {
		ClassType entity = getRepository().getOne(id);
		return entity;
	}

	protected ClassType getOptionalObject(Optional<ClassType> optObject) {
		if (optObject.isPresent())
			return optObject.get();

		return null;
	}

	//////////////////////////Revise and exclude as soon as possible
	protected final Page<ClassType> DUMMY_PAGEABLE = new PageImpl<ClassType>(new ArrayList<ClassType>());
	protected final List<ClassType> DUMMY_LIST = ListUtility.createDataList();

	//protected abstract Page<ClassType> performSearch(String keyword, Pageable pageable);

	public Page<ClassType> search(String keyword, Pageable pageable){
		return performSearch(keyword, pageable);
	}

	public Page<ClassType> search(Map<String, Object> parameters) {
		String keyword = (String)parameters.get(CommonConstants.PARAM_KEYWORD);
		Pageable pageable = (Pageable)parameters.get(CommonConstants.PARAM_PAGEABLE);
		return performSearch(keyword, pageable);
	}

	protected Page<ClassType> performSearch(String keyword, Pageable pageable){
		throw new MspRuntimeException("Not implemented yet!!!");//DUMMY_PAGEABLE;
	}

	protected List<ClassType> performSearch(Object parameter){
		Object findingResult = null;
		List<ClassType> searchResult = null;
		try {
			findingResult = CommonBeanUtils.callMethod(this.getRepository(), "find", ListUtility.createMap("keyword", parameter));
			if (findingResult instanceof List) {
				searchResult = (List<ClassType>)findingResult;
			}
		} catch (ExecutionContextException e) {
			e.printStackTrace();
		}
		return (null==searchResult)?DUMMY_LIST:searchResult;
	}

	@Override
	public List<ClassType> search(Object searchParam) {
		return performSearch(searchParam);
	}
}