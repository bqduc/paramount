/**
 * 
 */
package net.paramount.css.helper;

/**
 * @author bqduc
 *
 */
/*@Service
public class ClientServicesHelper {
	@Inject 
	private ClientProfileManager businessServiceManager;

	@Inject 
	private AuthenticationServiceManager authenticationServiceManager;

	@Inject
  private EmailServiceHelper emailServiceHelper;

	@Inject
	private AuthorityManager authorityServiceManager;

	@Inject
	private CustomPasswordEncoder passwordEncoder;
	@Inject
	private SimpleEncryptionManager simpleEncryptor;

	public ClientProfile getClient(String userSsoId) throws AuthenticationException {
		UserAccount user = authenticationServiceManager.getUser(userSsoId);
		return businessServiceManager.getClientProfile(user);
	}
	
	public ClientProfile confirmClient(String token) throws AuthenticationException {
		UserAccount confirmUser = null;
		ClientProfile confirmClientProfile = null;
		String decodedToken = null;
		try {
			decodedToken = Base64Utils.decode(token);//Base64Utils.decodeFromString(token);
			//decoded token is user's email
			confirmUser = authenticationServiceManager.getUser(decodedToken);
			if (CommonUtility.isNull(confirmUser))
				throw new AuthenticationException("Could not get the associated user base on token: [" + decodedToken +"]");
	
			confirmClientProfile = businessServiceManager.getClientProfile(confirmUser);
			if (CommonUtility.isNull(confirmClientProfile))
				throw new AuthenticationException("Could not get the connected client profile of user: [" + confirmUser.getEmail() +"]");

			confirmUser.setActivated(true);
			confirmUser.setActivationDate(DateTimeUtility.getSystemDateTime());
			authenticationServiceManager.save(confirmUser);

			confirmClientProfile.setActivationKey(token);
			confirmClientProfile.setActivationDate(ZonedDateTime.now());
			confirmClientProfile.setActivated(Boolean.TRUE);
			businessServiceManager.save(confirmClientProfile);
		} catch (Exception e) {
			throw new AuthenticationException(e);
		}
		return confirmClientProfile;
	}

	public ClientProfile registerBasicClient(UserAccount user) throws AuthenticationException {
		if (CommonUtility.isNull(user))
			throw new AuthenticationException("Could not register the basic client with the empty user dto!!");

		ClientProfile basicClientProfile = ClientProfile.getInstance(user);
		businessServiceManager.save(basicClientProfile);
		return basicClientProfile;
	}

	public ClientProfile getClient(UserAccount user) throws AuthenticationException {
		if (CommonUtility.isNull(user))
			throw new AuthenticationException("Could not get client profile with the empty user!");

		return businessServiceManager.getClientProfile(user);
	}
}*/
