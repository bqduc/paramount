/**
 * 
 */
package net.paramount.dmx.repository;

import javax.inject.Inject;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import net.paramount.framework.component.ComponentBase;

/**
 * @author ducbq
 *
 */
@Component
public class GlobaRepositoryManagement extends ComponentBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4395890941278552748L;

	/*@Inject
	private TaskExecutor asyncExecutor;*/

	public void initMasterData() {
		log.info(String.join(LOG_ENTRY_ENTER, "GlobalDataRepository", "::", "initMasterData"));
		initInventoryItems();
		log.info(String.join(LOG_ENTRY_LEAVE, "GlobalDataRepository", "::", "initMasterData"));
	}

	protected void initInventoryItems() {
		//String logSpec = String.join(this.getClass().getSimpleName(), "::", Thread.currentThread().getStackTrace()[1].getMethodName());
		//log.info(String.join(LOG_ENTRY_ENTER, logSpec));
		//log.info(String.join(LOG_ENTRY_LEAVE, logSpec));
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		//System.out.println("AsyncExecutor: [" + asyncExecutor + "]");
		System.out.println("Hello world, the application just started up");
	}

	public void initiateMasterData() {
		
	}
}
