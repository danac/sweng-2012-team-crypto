package epfl.sweng.servercomm;

/**
 * This factory creates objects implementing the IServerCommunication interface, whether proxied or not.
 */
final public class ServerCommunicationFactory {

	private static IServerCommunication commObject;
	
	
	public static synchronized IServerCommunication getInstance() {
		if (commObject== null) {
			commObject = new ServerCommunicationProxy();
		}		
		return commObject;
	}
}