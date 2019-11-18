/**
 * 
 */
package net.paramount.auth.exception;

import org.springframework.security.core.AuthenticationException;

import net.paramount.auth.model.AuthenticationCode;

/**
 * @author bqduc
 *
 */
public class UserAuthenticationException extends AuthenticationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2145491649369081533L;

	private AuthenticationCode errorId;

	public UserAuthenticationException(String msg) {
		super(msg);
	}

	public UserAuthenticationException(AuthenticationCode errorId, String msg) {
		super(msg);
		this.errorId = errorId;
	}

	public AuthenticationCode getErrorId() {
		return errorId;
	}

	public void setErrorId(AuthenticationCode errorId) {
		this.errorId = errorId;
	}
}
