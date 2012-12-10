package epfl.sweng.patterns;

import epfl.sweng.servercomm.ServerCommunication;
import epfl.sweng.servercomm.ServerCommunicationFactory;

/**
 * For grading purposes
 * @author cyril
 *
 */
public class CheckProxyHelper implements ICheckProxyHelper {

	@Override
	public Class<?> getServerCommunicationClass() {
		ServerCommunication commObject = new ServerCommunication();
		return commObject.getClass();
	}

	@Override
	public Class<?> getProxyClass() {
		return ServerCommunicationFactory.getInstance().getClass();
	}

}
