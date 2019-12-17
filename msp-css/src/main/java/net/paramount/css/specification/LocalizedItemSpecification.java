/**
 * 
 */
package net.paramount.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.entity.general.LocalizedItem;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class LocalizedItemSpecification extends CoreSpecifications<LocalizedItem, SearchRequest>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 267373264038078704L;

	public static Specification<LocalizedItem> buildSpecification(final SearchParameter searchParameter) {
		return LocalizedItemSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
