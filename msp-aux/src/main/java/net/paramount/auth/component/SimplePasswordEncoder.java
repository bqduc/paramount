/**
 * 
 */
package net.paramount.auth.component;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author bqduc
 *
 */
public interface SimplePasswordEncoder extends PasswordEncoder {
	boolean comparePassword(String userPassword, String repositoryPassword, String algorithm);
}
