/**
 * 
 */
package net.paramount.controller.security;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import com.github.adminfaces.template.config.AdminConfig;

import net.paramount.msp.util.Constants;

/**
 * @author ducbq
 *
 */
@Named(value = "authenticationController")
@ViewScoped
public class AuthenticationController {
	@Inject
	private AdminConfig adminConfig;

	public void doLogout() throws IOException {
		String loginPage = adminConfig.getLoginPage();
		if (loginPage == null || "".equals(loginPage)) {
			loginPage = Constants.DEFAULT_INDEX_PAGE;
		}
		
		if (!loginPage.startsWith("/")) {
			loginPage = "/" + loginPage;
		}
		Faces.getSession().invalidate();
		ExternalContext ec = Faces.getExternalContext();
		ec.redirect(ec.getRequestContextPath() + loginPage);
	}

}
