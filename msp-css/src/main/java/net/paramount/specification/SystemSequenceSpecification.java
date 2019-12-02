/**
 * 
 */
package net.paramount.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.css.entity.system.SystemSequence;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class SystemSequenceSpecification extends CoreSpecifications <SystemSequence, SearchRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9124997437694609911L;

	public static Specification<SystemSequence> buildSpecification(final SearchParameter searchParameter) {
		return SystemSequenceSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
