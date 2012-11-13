package epfl.sweng.test.mock;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.RequestDirector;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

/**
 * A request director which does nothing else than passing the request back to
 * the HttpClient.
 */
public class MockRequestDirector implements RequestDirector {

    private MockHttpClient httpClient;

    public MockRequestDirector(MockHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request,
            HttpContext context) throws HttpException, IOException {
        Log.v("HTTP", request.getRequestLine().toString());
        
        if(httpClient.shouldThrowIOException()) {
            throw new IOException("mock exception");            
        }

        HttpResponse response = httpClient.processRequest(request);

        if (response == null) {
            throw new RuntimeException("Request \"" + request.getRequestLine().toString()
                    + "\" did not match any known pattern");
        }

        Log.v("HTTP", response.getStatusLine().toString());
        return response;
    }

}
