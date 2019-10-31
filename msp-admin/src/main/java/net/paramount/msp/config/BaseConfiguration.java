/**
 * 
 */
package net.paramount.msp.config;

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

}
