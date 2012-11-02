package epfl.sweng.test.mocking;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.RequestDirector;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HttpContext;

import epfl.sweng.globals.Globals;

import android.util.Log;

/**
 * The Mocking RequestDirector pretending answer to HTTPRequest
 * like if it were the real web server. But it ain't, it's just a teapot.
 *
 * @see http://tools.ietf.org/html/rfc2324
 */
class MockRequestDirector implements RequestDirector {

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request,
            HttpContext context) throws HttpException, IOException {
        // In this method, you may examine the request and construct a
        // response.

        // You could, for example, log the request
        Log.i("TEAPOT", "Teapot received request: "
                + request.getRequestLine().toString());

        if (target.toURI().equals(Globals.RANDOM_QUESTION_URL)) {
        	return quizServer();
        } else if (target.toURI().equals(Globals.AUTHSERVER_LOGIN_URL)) {
        	return tequilaServer(request);
        }
    }

	private final static int STATUSCODE_OK = 200;
	private final static String STATUSMESSAGE_OK = "OK";
	
    private HttpResponse quizServer() {
    	
    	BasicHttpResponse resp = new BasicHttpResponse(
                HttpVersion.HTTP_1_1, STATUSCODE_OK, STATUSMESSAGE_OK);
        resp.addHeader("Content-Type", "application/json; charset=utf-8");
        try {
            resp.setEntity(new StringEntity("{"
            		+ "\"tags\": ["
            		+ "\"capitals\", "
            		+ "\"geography\", "
            		+ "\"countries\" ], "
            		+ "\"solutionIndex\": 3, "
            		+ "\"question\": \"What is the capital of Niger?\", "
            		+ "\"answers\": ["
            		+ "\"Riga\", "
            		+ "\"Madrid\", "
            		+ "\"Vienna\", "
            		+ "\"Niamey\" ], "
            		+ "\"owner\": \"sehaag\"," 
            		+ "\"id\": 16026 }", "utf-8"));
        } catch (UnsupportedEncodingException uee) {
            resp = null;
        }
        return resp;
    }
    
}