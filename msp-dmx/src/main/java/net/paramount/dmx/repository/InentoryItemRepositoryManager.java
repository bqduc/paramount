/**
 * 
 */
package net.paramount.dmx.repository;

import org.springframework.stereotype.Component;

import net.paramount.dmx.repository.base.DmxRepositoryBase;
import net.paramount.exceptions.DataLoadingException;
import net.paramount.framework.model.ExecutionContext;

/**
 * @author ducbui
 *
 */
@Component
public class InentoryItemRepositoryManager extends DmxRepositoryBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4990550616110685770L;

	@Override
	protected ExecutionContext doMarshallingBusinessObjects(ExecutionContext executionContext) throws DataLoadingException {
		return executionContext;
	}
}
