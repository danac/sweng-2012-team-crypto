package epfl.sweng.test.mocking;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

/**
 * 
 * @author cyril
 *
 */
public abstract class ServerSimulator {

	abstract public HttpResponse execute(HttpRequest request)  throws HttpException, IOException;
}
