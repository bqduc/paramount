/**
 * 
 */
package net.paramount.auth.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.auth.entity.Authority;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class AuthoritySpecification extends CoreSpecifications<Authority, SearchRequest>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2021774340255996625L;

	public static Specification<Authority> buildSpecification(final SearchParameter searchParameter) {
		return AuthoritySpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
