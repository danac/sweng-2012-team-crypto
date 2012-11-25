package epfl.sweng.test.mocking;


import java.io.IOException;
import org.apache.http.client.RequestDirector;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;



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
		// TODO Auto-generated method stub
		return ServerSimulatorFactory.getInstance().execute(request);
	}

}
