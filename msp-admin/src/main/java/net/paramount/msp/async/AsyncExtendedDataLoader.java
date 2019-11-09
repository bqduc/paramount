/**
 * 
 */
package net.paramount.msp.async;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.paramount.common.DateTimeUtility;
import net.paramount.framework.async.Asynchronous;
import net.paramount.framework.model.ExecutionContext;
import net.paramount.msp.components.ResourcesServicesHelper;
import net.paramount.osx.helper.OfficeSuiteServicesHelper;

/**
 * @author bqduc_2
 */
@Component
@Scope("prototype")
public class AsyncExtendedDataLoader extends Asynchronous {
	@Inject
	private OfficeSuiteServicesHelper officeSuiteServicesHelper;

	@Inject
	private ResourcesServicesHelper resourcesServicesHelper;

	public AsyncExtendedDataLoader(ExecutionContext executionContext) {
		super();
	}

	protected void executeAsync(ExecutionContext executionContext) {
		log.info("Enter AsyncExtendedDataLoader::execute " + DateTimeUtility.getSystemDate());
		try {
			System.out.println("Started load default configuration data at: " + DateTimeUtility.getSystemDateTime());
			//officeSuiteServicesHelper.loadDefaultZipConfiguredData(ClassPathResourceUtility.builder().build().getFile("data/marshall/develop_data.zip"));
			officeSuiteServicesHelper.loadDefaultZipConfiguredData(resourcesServicesHelper.loadClasspathResourceFile("data/marshall/develop_data.zip"));
			System.out.println("Finished load default configuration data at: " + DateTimeUtility.getSystemDateTime());
		} catch (Exception e) {
			log.error(e);
		}
		log.info("Leave AsyncExtendedDataLoader::execute " + DateTimeUtility.getSystemDate());
	}
}
