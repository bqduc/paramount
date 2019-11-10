/**
 * 
 */
package net.paramount.imx.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;
import net.paramount.imx.entity.InventoryProfile;

/**
 * @author bqduc
 *
 */
@Builder
public class InventoryProfileSpecification extends CoreSpecifications<InventoryProfile, SearchRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1103372435995733806L;

	public static Specification<InventoryProfile> buildSpecification(final SearchParameter searchParameter) {
		return InventoryProfileSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
