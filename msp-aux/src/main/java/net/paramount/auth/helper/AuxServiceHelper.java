/**
 * 
 */
package net.paramount.auth.helper;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.paramount.auth.domain.UserAccountProfile;
import net.paramount.auth.entity.UserAccount;
import net.paramount.auth.service.UserAuthenticationService;
import net.paramount.common.CommonUtility;

/**
 * @author ducbq
 *
 */
@Component
public class AuxServiceHelper {
	@Inject 
	private UserAuthenticationService userAuthenticationService;

	public UserAccountProfile getUserAccountProfile(final String userAccountSsoId) {
		if (CommonUtility.isEmpty(userAccountSsoId))
			return null;

		UserAccount userAccount = userAuthenticationService.getOne(userAccountSsoId);
		if (null==userAccount)
			return null;

		return UserAccountProfile.builder()
				.userAccount(userAccount)
				.build();
	}
}
