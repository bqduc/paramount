/**
 * 
 */
package net.paramount.esmx.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.paramount.entity.stock.StockTransaction;
import net.paramount.framework.model.SearchParameter;
import net.paramount.framework.model.SearchRequest;
import net.paramount.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class StockTransactionSpecification extends CoreSpecifications<StockTransaction, SearchRequest>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7738524515380282893L;

	public static Specification<StockTransaction> buildSpecification(final SearchParameter searchParameter) {
		return StockTransactionSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
