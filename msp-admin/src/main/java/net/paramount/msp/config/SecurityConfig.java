package net.paramount.msp.config;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Created by aLeXcBa1990 on 24/11/2018.
 * 
 */

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Inject
	private AuthenticationEntryPoint authEntryPoint;

	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			// rest Login
			http.antMatcher("/admin/**").authorizeRequests().anyRequest().hasRole("ADMIN").and().httpBasic().and().csrf()
					.disable();
		}
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		http.authorizeRequests()
    	.antMatchers(buildUnauthorizedMatchers()).permitAll()
      .anyRequest().authenticated()
    .and()
    .formLogin().loginPage("/login.xhtml")
    .defaultSuccessUrl("/")
    .permitAll()
    .and()
.logout()
    .logoutSuccessUrl("/")
    .permitAll()
;
	    */
	    // form login
		http.authorizeRequests()
		//.antMatchers("/", "/login.xhtml", "/javax.faces.resource/**").permitAll()
    	.antMatchers(buildUnauthorizedMatchers()).permitAll()
		.anyRequest()
		.fullyAuthenticated().and().formLogin().loginPage("/login.xhtml").defaultSuccessUrl("/index.xhtml")
		.failureUrl("/login.xhtml?authfailed=true").permitAll().and().logout().logoutSuccessUrl("/login.xhtml")
		.logoutUrl("/j_spring_security_logout").and().csrf().disable();

		// allow to use resource links like pdf
		http.headers().frameOptions().sameOrigin();
		
		
        // Tất cả các request gửi tới Web Server yêu cầu phải được xác thực
        // (authenticated).
        http.authorizeRequests()
        .antMatchers(this.buildRestAPIsMatchers()).authenticated()
        ;
        
		http.httpBasic().authenticationEntryPoint(authEntryPoint);
	}
/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// form login
		http.authorizeRequests().antMatchers("/", "/login.xhtml", "/javax.faces.resource/**").permitAll().anyRequest()
				.fullyAuthenticated().and().formLogin().loginPage("/login.xhtml").defaultSuccessUrl("/index.xhtml")
				.failureUrl("/login.xhtml?authfailed=true").permitAll().and().logout().logoutSuccessUrl("/login.xhtml")
				.logoutUrl("/j_spring_security_logout").and().csrf().disable();

		// allow to use resource links like pdf
		http.headers().frameOptions().sameOrigin();
	}
*/
	
	private String[] buildUnauthorizedMatchers() {
		String[] unauthorizedPatterns = new String[] { 
				"/api/**", 
				"/*", 
				"/public/**", 
				"/resources/**", 
				"/includes/**", 
				"/pages/**", 
				"/auth/register/**",
				"/login.xhtml", 
				"/javax.faces.resource/**"
		};
		return unauthorizedPatterns;
	}

	private String[] buildRestAPIsMatchers() {
		String[] unauthorizedPatterns = new String[] { 
				"/api/**"
		};
		return unauthorizedPatterns;
	}
}
