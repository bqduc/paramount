/**
 * 
 */
package net.paramount.msp.infra;

import javax.inject.Inject;

import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ibm.icu.util.Calendar;

import net.paramount.framework.component.ComponentBase;

/**
 * @author ducbq
 *
 */
@Component
public class GlobalCachingManager extends ComponentBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5561608678798386635L;

	@Inject
	private CacheManager cacheManager; 

	@Scheduled(cron = "0/30 * * * * ?") // execute after every 3 minutes
	public void clearCacheSchedule() {
		//log.info("Clear cache manager fired at: " + Calendar.getInstance().getTime());
		for (String name : cacheManager.getCacheNames()) {
			log.info("It's about to clear cache: " + name);
			//cacheManager.getCache(name).clear(); // clear cache by name
		}
	}
}
