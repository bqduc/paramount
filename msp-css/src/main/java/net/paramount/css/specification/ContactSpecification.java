/**
 * 
 */
package net.paramount.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.entity.contact.Contact;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class ContactSpecification extends CoreSpecifications<Contact, SearchRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4351535957683794972L;

	public static Specification<Contact> buildSpecification(final SearchParameter searchParameter) {
		return ContactSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
