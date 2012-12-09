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
final public class RealServerCommunication implements IServerCommunication  {
	private static RealServerCommunication mInstance;	
	
	private RealServerCommunication() {	
	}
	
	public static RealServerCommunication getInstance() {
		if (mInstance == null) {
			mInstance = new RealServerCommunication();
		}
		return mInstance;
	}
	
	@Override
	public HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		return SwengHttpClientFactory.getInstance().execute(request);
	}
}
