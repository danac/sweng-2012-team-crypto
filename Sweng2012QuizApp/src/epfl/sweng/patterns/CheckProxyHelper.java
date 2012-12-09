package epfl.sweng.patterns;

import epfl.sweng.servercomm.CachedServerCommunication;
import epfl.sweng.servercomm.ServerCommunication;

/**
 * For grading purposes
 * @author cyril
 *
 */
public class CheckProxyHelper implements ICheckProxyHelper {

	@Override
	public Class<?> getServerCommunicationClass() {
		return ServerCommunication.getInstance().getClass();
	}

	@Override
	public Class<?> getProxyClass() {
		return CachedServerCommunication.getInstance().getClass();
	}

}
