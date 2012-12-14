package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;

/**
 * 
 */
public class ContentHelper {
	

	public static String getPostContent(HttpPost request) throws ParseException, IOException {
		String content = EntityUtils.toString(request.getEntity());
		request.setEntity(new StringEntity(content));
		return content;
	}
	
	public static String getResponseContent(HttpResponse response) throws ParseException, IOException {
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String content = responseHandler.handleResponse(response);
		if (content == null) {
			content = "";
		}
		response.setEntity(new StringEntity(content));
		return content;
	}
	
	
	public static int getStatusCode(HttpResponse response) {
		return response.getStatusLine().getStatusCode();
	}
}
