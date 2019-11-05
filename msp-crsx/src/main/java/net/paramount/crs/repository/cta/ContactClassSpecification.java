/**
 * 
 */
package net.paramount.crs.repository.cta;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.crs.entity.base.ContactClass;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class ContactClassSpecification extends CoreSpecifications<ContactClass, SearchRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4351535957683794972L;

	public static Specification<ContactClass> buildSpecification(final SearchParameter searchParameter) {
		return ContactClassSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
