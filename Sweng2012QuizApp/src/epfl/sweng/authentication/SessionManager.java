package epfl.sweng.authentication;

/**
 * Simple singleton data structure holding session ID and taking care of authentication
 */
public class SessionManager {
	
	public static void authenticate(AuthenticationActivity mActivity) {
		mActivity.displayAuthError();
	}
}
	