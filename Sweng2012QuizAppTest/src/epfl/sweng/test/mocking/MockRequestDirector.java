package epfl.sweng.test.mocking;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.RequestDirector;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.globals.Globals;

import android.util.Log;

/**
 * The Mocking RequestDirector pretending answer to HTTPRequest
 * like if it were the real web server. But it ain't, it's just a teapot.
 *
 * @see http://tools.ietf.org/html/rfc2324
 */
class MockRequestDirector implements RequestDirector {
    private final static int SC_TEAPOT = 418;
    private final static String SM_TEAPOT = "I'm a teapot";
	private static final int STATUSCODE_INVALIDCREDENTIALS = 400;
	private static final int STATUSCODE_OK = 200;
	private static final String STATUSMESSAGE_OK = "OK";
	
	
    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request,
            HttpContext context) throws HttpException, IOException {
        // In this method, you may examine the request and construct a
        // response.

        // You could, for example, log the request
        Log.i("TEAPOT", "Teapot received request: "
                + request.getRequestLine().toString());

        // Since we're a teapot, we will answer with an error message. In
        // your code, you'll probably want to set the content type to
        // application/json, the return code to 200 (with message OK), and
        // create a StringEntity containing a JSON string. Consider using
        // http://developer.android.com/reference/org/apache/http/impl/EnglishReasonPhraseCatalog.html#INSTANCE
        // if you don't want to specify the reason text.
        BasicHttpResponse resp = new BasicHttpResponse(
                HttpVersion.HTTP_1_1, SC_TEAPOT, SM_TEAPOT);
        resp.addHeader("Content-Type", "text/html; charset=utf-8");
        try {
            resp.setEntity(new StringEntity(
                    "<!DOCTYPE html>"
                            + "<html>"
                            + "<title>"
                            + SC_TEAPOT
                            + " "
                            + SM_TEAPOT
                            + "</title>"
                            + "<img src=http://farm8.staticflickr.com/7006/6508102407_4daeef6529_o.jpg> "
                            + "</html>", "utf-8"));
        } catch (UnsupportedEncodingException uee) {
            resp = null;
        }
        return resp;
    }
    
    private HttpResponse tequilaServer(HttpRequest request) throws IOException {
    	HttpPost post = (HttpPost) request;
    	List<NameValuePair> params = URLEncodedUtils.parse(post.getEntity());
    	
    	String username = "";
    	
    	for (NameValuePair param : params) {
    		if (param.getName().equals("username")) {
    			username = param.getValue();
    		} 
    	}
    	
    	if (username.equals("valid")) {
    		return new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_AUTHSUCCESSFUL, "Found");
    	} else {
    		return new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_INVALIDCREDENTIALS, "Invalid Credentials");	
    	} 
    	
    }
    
    private HttpResponse quizServerLogin(HttpRequest request) {
    	
    	if (request.getRequestLine().getMethod().equals("GET")) {
    		BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_OK, STATUSMESSAGE_OK);
    		response.setHeader("Content-type", "application/json");
    		JSONObject jsonResponse = new JSONObject();

    		try {
    			jsonResponse.put("token", "rqtvk5d3za2x6ocak1a41dsmywogrdlv5");
				response.setEntity(new StringEntity(jsonResponse.toString()));
    		} catch (UnsupportedEncodingException e) {
			} catch (JSONException e) {					
			}
    		return response;
    	} else {
    		BasicHttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_OK, STATUSMESSAGE_OK);
    		response.setHeader("Content-type", "application/json");
    		JSONObject jsonResponse = new JSONObject();
    		
    		try {
    			jsonResponse.put("session", "fdsafqeu");
				response.setEntity(new StringEntity(jsonResponse.toString()));
			} catch (UnsupportedEncodingException e) {
			} catch (JSONException e) {					
			}
    		return response;   		
    	}
    }
    
    
}