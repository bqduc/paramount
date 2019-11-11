package net.paramount.css.service.general;

import java.util.Optional;

import org.springframework.data.domain.Page;

import net.paramount.css.entity.general.Catalogue;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface CatalogueService extends GenericService<Catalogue, Long> {

	/**
	 * Get one Catalogue with the provided name.
	 * 
	 * @param code
	 *            The Catalogue name
	 * @return The Catalogue
	 * @throws ObjectNotFoundException
	 *             If no such Catalogue exists.
	 */
	Optional<Catalogue> getOne(String name) throws ObjectNotFoundException;

	/**
	 * Get one Catalogues with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Catalogues
	 */
	Page<Catalogue> getObjects(SearchParameter searchParameter);

}
