/**
 * 
 */
package net.paramount.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.entity.system.Sequence;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class SequenceSpecification extends CoreSpecifications <Sequence, SearchRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9124997437694609911L;

	public static Specification<Sequence> buildSpecification(final SearchParameter searchParameter) {
		return SequenceSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
