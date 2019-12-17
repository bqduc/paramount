/**
 * 
 */
package net.paramount.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.entity.contact.ContactAddress;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class ContactAddressSpecification extends CoreSpecifications <ContactAddress, SearchRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4984760941753997036L;

	public static Specification<ContactAddress> buildSpecification(final SearchParameter searchParameter) {
		return ContactAddressSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
