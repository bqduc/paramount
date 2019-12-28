/*
 * Copyright 2016-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.paramount.msp.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security Configuration.
 *
 * @author Marcelo Fernandes
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Inject
	private CustomAuthenticationProvider authProvider;

/*	@Inject
	private AuthenticationEntryPoint authEntryPoint;*/
	
	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			// rest Login
			http.antMatcher("/api/**").authorizeRequests().anyRequest().hasRole("ADMIN").and().httpBasic().and().csrf()
					.disable();

		}
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// form login
		//Original
		/*
		http.authorizeRequests().antMatchers("/", "/login.xhtml", "/javax.faces.resource/**").permitAll().anyRequest()
				.fullyAuthenticated().and().formLogin().loginPage("/login.xhtml").defaultSuccessUrl("/index.xhtml")
				.failureUrl("/login.xhtml?authfailed=true").permitAll().and().logout().logoutSuccessUrl("/login.xhtml")
				.logoutUrl("/j_spring_security_logout").and().csrf().disable();
		*/
		http.authorizeRequests()
		.antMatchers(buildUnauthorizedMatchers()).permitAll()
		.antMatchers("/", "/login.xhtml", "/javax.faces.resource/**").permitAll().anyRequest()
		.fullyAuthenticated().and().formLogin().loginPage("/login.xhtml").defaultSuccessUrl("/index.xhtml")
		.failureUrl("/login.xhtml?authfailed=true").permitAll().and().logout().logoutSuccessUrl("/index.xhtml")
		.logoutUrl("/j_spring_security_logout").and().csrf().disable();
		
		// allow to use ressource links like pdf
		http.headers().frameOptions().sameOrigin();
		
		//http.httpBasic().authenticationEntryPoint(authEntryPoint);
	}
	
	private String[] buildUnauthorizedMatchers() {
		String[] unauthorizedPatterns = new String[] { 
				//"/api/**", 
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

	/*private String[] buildRestAPIsMatchers() {
		String[] unauthorizedPatterns = new String[] { 
				"/api/**"
		};
		return unauthorizedPatterns;
	}*/

	/*@Override
	protected UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager result = new InMemoryUserDetailsManager();
		result.createUser(User.withDefaultPasswordEncoder().username("admin").password("admin").authorities("ROLE_ADMIN").build());
		result.createUser(User.withDefaultPasswordEncoder().username("nyilmaz").password("qwe").authorities("ROLE_USER").build());
		return result;
	}*/
}
