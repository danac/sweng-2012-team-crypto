package epfl.sweng.patterns;

import epfl.sweng.servercomm.ServerCommunication;
import epfl.sweng.servercomm.ServerCommunicationFactory;
import epfl.sweng.servercomm.ServerCommunicationProxy;

/**
 * For grading purposes
 * @author cyril
 *
 */
public class CheckProxyHelper implements ICheckProxyHelper {

	@Override
	public Class<?> getServerCommunicationClass() {
		return ServerCommunication.class;
	}

	@Override
	public Class<?> getProxyClass() {
		return ServerCommunicationProxy.class;
	}

}
