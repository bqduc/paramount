package net.paramount.auth.service;

import org.springframework.data.domain.Page;

import net.paramount.auth.entity.Authority;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface AuthorityService extends GenericService<Authority, Long> {

	/**
	 * Get one Authority with the provided code.
	 * 
	 * @param name
	 *            The Authority name
	 * @return The Authority
	 * @throws ObjectNotFoundException
	 *             If no such Authority exists.
	 */
	Authority getByName(String name) throws ObjectNotFoundException;

	/**
	 * Get one Authoritys with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Authorities
	 */
	Page<Authority> getObjects(SearchParameter searchParameter);
}
