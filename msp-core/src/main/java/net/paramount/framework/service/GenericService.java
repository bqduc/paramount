package net.paramount.framework.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.paramount.exceptions.ExecutionContextException;
import net.paramount.exceptions.MspDataException;
import net.paramount.framework.entity.ObjectBase;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.framework.model.SearchParameter;

public interface GenericService<T extends ObjectBase, K extends Serializable> extends IService<T, K> {
	T save(T entity);
	T saveOrUpdate(T entity);
	List<T> saveObjects(List<T> objects);

	void remove(K id);
	void remove(T entity);
	void removeAll();

	long count();
	long count(String countByProperty, Object value);

	long count(String countMethodName, Map<?, ?> parameters);

	String nextSerial(String prefix) throws MspDataException;

	Optional<T> getBusinessObject(Object key) throws MspDataException;
	List<T> getObjects();

	/**
	 * Get objects with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable objects
	 */
	Page<T> getObjects(SearchParameter searchParameter);

	Page<T> getObjects(Integer pageNumber);
	Page<T> getObjects(Integer pageNumber, Integer size);
	Page<T> searchObjects(String keyword, Pageable pageable);
	Page<T> search(Map<String, Object> parameters);

	List<T> imports(Map<Object, Object> parameters);

	ExecutionContext load(ExecutionContext executionContext) throws ExecutionContextException;
}