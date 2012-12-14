package epfl.sweng.test.mocking;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpResponse;

import epfl.sweng.globals.Globals;

/**
 * 
 * @author cyril
 *
 */
public class InternalErrorServerSimulator extends ServerSimulator {
	
	@Override
    public HttpResponse execute(HttpRequest request) throws HttpException, IOException {
    	return new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_SERVERERROR, "Internal Server Error");
	}

}
