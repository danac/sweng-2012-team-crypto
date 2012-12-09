package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * 
 * @author cyril
 *
 */
final public class ServerCommunication implements IServerCommunication  {
	private static ServerCommunication mInstance;	
	
	private ServerCommunication() {	
	}
	
	public static ServerCommunication getInstance() {
		if (mInstance == null) {
			mInstance = new ServerCommunication();
		}
		return mInstance;
	}
	
	@Override
	public HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		return SwengHttpClientFactory.getInstance().execute(request);
	}
}
