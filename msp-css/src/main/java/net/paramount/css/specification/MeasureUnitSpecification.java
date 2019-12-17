/**
 * 
 */
package net.paramount.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.entity.general.MeasureUnit;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class MeasureUnitSpecification extends CoreSpecifications<MeasureUnit, SearchRequest>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7455339506976096038L;

	public static Specification<MeasureUnit> buildSpecification(final SearchParameter searchParameter) {
		return MeasureUnitSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
