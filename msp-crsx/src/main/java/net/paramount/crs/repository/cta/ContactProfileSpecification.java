/**
 * 
 */
package net.paramount.crs.repository.cta;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.entity.contact.ContactProfile;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class ContactProfileSpecification extends CoreSpecifications<ContactProfile, SearchRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6218648803366234962L;

	public static Specification<ContactProfile> buildSpecification(final SearchParameter searchParameter) {
		return ContactProfileSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
