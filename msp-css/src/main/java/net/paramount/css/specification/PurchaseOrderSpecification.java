/**
 * 
 */
package net.paramount.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.entity.emx.PurchaseOrder;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class PurchaseOrderSpecification extends CoreSpecifications<PurchaseOrder, SearchRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4608364667950258805L;

	public static Specification<PurchaseOrder> buildSpecification(final SearchParameter searchParameter) {
		return PurchaseOrderSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
