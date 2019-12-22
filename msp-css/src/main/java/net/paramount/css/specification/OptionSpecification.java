/**
 * 
 */
package net.paramount.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.entity.system.Option;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class OptionSpecification extends CoreSpecifications<Option, SearchRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3900233288875704002L;

	public static Specification<Option> buildSpecification(final SearchParameter searchParameter) {
		return OptionSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
