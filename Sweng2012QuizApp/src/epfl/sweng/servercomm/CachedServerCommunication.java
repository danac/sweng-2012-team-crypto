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
final public class CachedServerCommunication {
	private static CachedServerCommunication mInstance;
	
	
	
	private CachedServerCommunication() {
	}
	
	public static CachedServerCommunication getInstance() {
		if (mInstance == null) {
			mInstance = new CachedServerCommunication();
		}
		return mInstance;
	}
	
	
	public HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		return SwengHttpClientFactory.getInstance().execute(request);
	}
	

}
