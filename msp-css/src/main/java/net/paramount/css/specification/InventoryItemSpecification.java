/**
 * 
 */
package net.paramount.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.entity.stock.InventoryItem;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class InventoryItemSpecification extends CoreSpecifications<InventoryItem, SearchRequest>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4450845076335839837L;

	public static Specification<InventoryItem> buildSpecification(final SearchParameter searchParameter) {
		return InventoryItemSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
