package epfl.sweng.patterns;

import epfl.sweng.servercomm.CachedServerCommunication;
import epfl.sweng.servercomm.RealServerCommunication;

/**
 * For grading purposes
 * @author cyril
 *
 */
public class CheckProxyHelper implements ICheckProxyHelper {

	@Override
	public Class<?> getServerCommunicationClass() {
		return RealServerCommunication.getInstance().getClass();
	}

	@Override
	public Class<?> getProxyClass() {
		return CachedServerCommunication.getInstance().getClass();
	}

}
