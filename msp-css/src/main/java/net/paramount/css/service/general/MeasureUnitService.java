package net.paramount.css.service.general;

import java.util.Optional;

import org.springframework.data.domain.Page;

import net.paramount.entity.general.MeasureUnit;
import net.paramount.exceptions.ObjectNotFoundException;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.service.GenericService;

public interface MeasureUnitService extends GenericService<MeasureUnit, Long> {

	/**
	 * Get one MeasureUnit with the provided name.
	 * 
	 * @param code
	 *            The MeasureUnit name
	 * @return The MeasureUnit
	 * @throws ObjectNotFoundException
	 *             If no such MeasureUnit exists.
	 */
	Optional<MeasureUnit> getOne(String name) throws ObjectNotFoundException;

	/**
	 * Get one MeasureUnit with the provided name locale.
	 * 
	 * @param nameLocal
	 *            The MeasureUnit name local
	 * @return The MeasureUnit
	 * @throws ObjectNotFoundException
	 *             If no such MeasureUnit exists.
	 */
	Optional<MeasureUnit> getByNameLocale(String nameLocal) throws ObjectNotFoundException;

	/**
	 * Get one Measure units with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Measure units
	 */
	Page<MeasureUnit> getObjects(SearchParameter searchParameter);

}
