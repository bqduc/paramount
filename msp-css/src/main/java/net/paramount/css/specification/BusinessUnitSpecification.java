/**
 * 
 */
package net.paramount.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.entity.general.BusinessUnit;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class BusinessUnitSpecification extends CoreSpecifications <BusinessUnit, SearchRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5864251508969605374L;

	public static Specification<BusinessUnit> buildSpecification(final SearchParameter searchParameter) {
		return BusinessUnitSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
