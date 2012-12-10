package epfl.sweng.test.mocking;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import android.util.Log;
import epfl.sweng.globals.Globals;

/**
 * 
 * @author cyril
 *
 */
public class QuestionOnlyServerSimulator extends ServerSimulator {

    private static final int STATUSCODE_OK = 200;
    private static final String STATUSMESSAGE_OK = "OK";
    private static final int STATUSCODE_NOTFOUND = 404;
    private static final String STATUSMESSAGE_NOTFOUND = "OK";
    @Override
    public HttpResponse execute(HttpRequest request) throws HttpException, IOException {
		String requestUri = request.getRequestLine().getUri();
    	
    	Log.i("TEAPOT", "Question-only teapot received request: "
                + request.getRequestLine().toString());
        HttpResponse resp = new BasicHttpResponse(HttpVersion.HTTP_1_1, STATUSCODE_NOTFOUND, STATUSMESSAGE_NOTFOUND);
        if (requestUri.equals(Globals.RANDOM_QUESTION_URL) || requestUri.equals(Globals.SUBMIT_QUESTION_URL)) {
        	resp = quizServer();
        } else {
        	throw new IOException();
        }
        return resp;
	}
        
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
	
	