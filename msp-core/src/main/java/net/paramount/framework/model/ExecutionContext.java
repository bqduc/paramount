/**
 * 
 */
package net.paramount.framework.model;

import lombok.Builder;

/**
 * @author bqduc
 *
 */
@Builder
public class ExecutionContext extends ContextBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3206765121751074978L;

	private String executionStage;

	public String getExecutionStage() {
		return executionStage;
	}

	public void setExecutionStage(String executionStage) {
		this.executionStage = executionStage;
	}

	public ExecutionContext set(String key, Object contextData){
		super.put(key, contextData);
		return this;
	}
}
