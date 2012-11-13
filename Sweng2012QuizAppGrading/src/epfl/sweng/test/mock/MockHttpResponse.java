package epfl.sweng.test.mock;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.http.ProtocolVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.http.message.BasicHttpResponse;

/** Sweng HTTP Response*/
public class MockHttpResponse extends BasicHttpResponse {
	public MockHttpResponse(int statusCode, String responseBody, String contentType) {
		super(new ProtocolVersion("HTTP", 1, 1),
				statusCode,
				EnglishReasonPhraseCatalog.INSTANCE.getReason(
						statusCode, Locale.getDefault()));
		
		if (responseBody != null) {
			try {
			    StringEntity responseBodyEntity = new StringEntity(responseBody);
			    if (contentType != null) {
			        responseBodyEntity.setContentType(contentType);
			    }
				this.setEntity(responseBodyEntity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
}
