package epfl.sweng.test.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;

import android.util.Log;

/** The Sweng HTTP Client */
public class MockHttpClient extends DefaultHttpClient {

    /** This interface specifies a class that can verify requests */
    public interface RequestExpectationVerifier {
        /**
         * This method should use assert methods to ensure that the request
         * satisfies expectations
         * 
         * @throws Exception
         */
        void verify(HttpRequest request) throws Exception;
    }

    /** Expectation */
    private class RequestExpectation {
        private final Pattern pattern;
        private final RequestExpectationVerifier verifier;

        public RequestExpectation(Pattern pattern, RequestExpectationVerifier verifier) {
            super();
            this.pattern = pattern;
            this.verifier = verifier;
        }
    }

    /** Prepared response */
    private class CannedResponse {
        private final Pattern pattern;
        private final int statusCode;
        private final String responseBody;
        private final String contentType;

        public CannedResponse(Pattern pattern, int statusCode, String responseBody, String contentType) {
            this.pattern = pattern;
            this.statusCode = statusCode;
            this.responseBody = responseBody;
            this.contentType = contentType;
        }
    }

    private List<CannedResponse> responses = new ArrayList<CannedResponse>();
    private List<RequestExpectation> expectations = new ArrayList<RequestExpectation>();
    
    private boolean throwIOException = false;
    
    public void pushCannedResponse(String requestRegex, int status, String responseBody, String contentType) {
        responses.add(0, new CannedResponse(Pattern.compile(requestRegex), status, responseBody, contentType));
    }

    public void pushExpectation(String requestRegex, RequestExpectationVerifier verifier) {
        expectations.add(0, new RequestExpectation(Pattern.compile(requestRegex), verifier));
    }

    public void popCannedResponse() {
        if (responses.isEmpty()) {
            throw new IllegalStateException("Canned response stack is empty!");
        }
        responses.remove(0);
    }

    @Override
    protected RequestDirector createClientRequestDirector(final HttpRequestExecutor requestExec,
            final ClientConnectionManager conman, final ConnectionReuseStrategy reustrat,
            final ConnectionKeepAliveStrategy kastrat, final HttpRoutePlanner rouplan,
            final HttpProcessor httpProcessor, final HttpRequestRetryHandler retryHandler,
            final RedirectHandler redirectHandler, final AuthenticationHandler targetAuthHandler,
            final AuthenticationHandler proxyAuthHandler, final UserTokenHandler stateHandler, final HttpParams params) {
        return new MockRequestDirector(this);
    }

    public void pushIOException() {
        throwIOException = true;
    }
    
    public void popIOException() {
        throwIOException = false;
    }
    
    public boolean shouldThrowIOException() {
        return throwIOException;
    }
    
    public HttpResponse processRequest(HttpRequest request) {
        for (RequestExpectation ex : new ArrayList<RequestExpectation>(expectations)) {
            if (ex.pattern.matcher(request.getRequestLine().toString()).find()) {
                try {
                    ex.verifier.verify(request);
                } catch (Exception e) {
                    Assert.assertTrue("Request matched expectation \"" + ex.pattern + "\", but could not be verified",
                            false);
                }
                expectations.remove(ex);
            }
        }
        for (CannedResponse cr : responses) {
            if (cr.pattern.matcher(request.getRequestLine().toString()).find()) {
                Log.v("HTTP", "Mocking request since it matches pattern " + cr.pattern);
                Log.v("HTTP", "Response body: " + cr.responseBody);
                return new MockHttpResponse(cr.statusCode, cr.responseBody, cr.contentType);
            }
        }

        return null;
    }

    /** Ensures that all expectations have been fulfilled */
    public boolean verifyExpectations() {
        return expectations.isEmpty();
    }

    public void logUnfulfilledExpectations() {
        for (RequestExpectation ex : expectations) {
            Log.e("UNFULFILLED", ex.pattern.toString());
        }
    }
}
