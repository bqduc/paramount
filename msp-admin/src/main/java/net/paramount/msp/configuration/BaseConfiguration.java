/**
 * 
 */
package net.paramount.msp.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author bqduc
 *
 */
//@Slf4j
@EnableCaching
@Configuration
@EnableJpaRepositories(basePackages = {"net.paramount"})
@ComponentScan(basePackages = {"net.paramount"})
@EntityScan(basePackages={"net.paramount"})
@EnableTransactionManagement
public class BaseConfiguration {
	/*
	@Inject
	private ApplicationContext applicationContext;
	*/

	/**
	 * {@link PasswordEncoder} bean.
	 * 
	 * @return <b>{@code BCryptPasswordEncoder}</b> with strength (passed as
	 *         argument) the log rounds to use, between 4 and 31
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	/*@Bean
	  public MessageSource messageSource() {
	  	String[] resourceBundles = new String[]{
	  			"classpath:/i18n/messages-menu", 
	    		"classpath:/i18n/messages-stock", 
	    		"classpath:/i18n/messages-catalog", 
	    		"classpath:/i18n/messages-general",
	    		"classpath:/i18n/messages-hrcx",
	    		"classpath:/i18n/messages-master",
	    		"classpath:/i18n/messages-contact",
	    		"classpath:/i18n/messages",
	    		"classpath:/i18n/messages-crx",
	    		"classpath:/i18n/messages-admin"
	    };
	  	
	  	//log.info("Initialize the message source......");
	  	
	  	ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	      messageSource.setBasenames(resourceBundles);
	      messageSource.setDefaultEncoding("UTF-8");
	      return messageSource;
	  }*/


	/**
	 * i18n bean support for switching locale through a request param. <br />
	 * Users who are authenticated can change their default locale to another
	 * when they pass in a<br />
	 * url (http://example.com/&lt;contextpath&gt;/<em>lang=&lt;locale&gt;</em>)
	 * 
	 * @return
	 *//*
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
*/
	/**
		 * i18n support bean. The locale resolver being used is Cookie.<br />
		 * When locale is changed and intercepted by the
		 * {@link WebApplicationStarter#localeChangeInterceptor localeChangeInterceptor}.
		 * <br />
		 * The new locale is stored in a Cookie and remains active even after
		 * session timeout<br />
		 * or session being invalidated
		 * <p>
		 * Set a fixed Locale to <em>US</em> that this resolver will return if no
		 * cookie found.
		 * </p>
		 * 
		 * @return {@code LocaleResolver}
		 * @see WebApplicationStarter#localeChangeInterceptor
		 *//*
		@Bean
		public LocaleResolver localeResolver() {
			CookieLocaleResolver clr = new CookieLocaleResolver();
			//clr.setDefaultLocale(Locale.US);
			clr.setDefaultLocale(getDefaultLocale());
			return clr;
		}

		private Locale getDefaultLocale(){
			return new Locale("vi", "VN");
		}

		@Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
*/		

		@Bean
		public ThreadPoolTaskExecutor taskExecutor() {
			ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
			taskExecutor.setCorePoolSize(5);
			taskExecutor.setMaxPoolSize(25);
			taskExecutor.setQueueCapacity(100);
			taskExecutor.initialize();
			return taskExecutor;
		}
}
