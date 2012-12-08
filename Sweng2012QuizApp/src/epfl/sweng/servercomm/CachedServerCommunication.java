package epfl.sweng.servercomm;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHttpResponse;

import epfl.sweng.authentication.SessionManager;
import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;

/**
 * 
 * @author cyril
 *
 */
final public class CachedServerCommunication {
	private static CachedServerCommunication mInstance;
	
	private List<QuizQuestion> mCachedQuestions;
	private List<HttpUriRequest> mCachedSubmitQuestions;
	private List<HttpUriRequest> mCachedSubmitVerdict;
	
	
	
	private CachedServerCommunication() {
	}
	
	public static CachedServerCommunication getInstance() {
		if (mInstance == null) {
			mInstance = new CachedServerCommunication();
		}
		return mInstance;
	}
	
	
	public HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		
		String url = request.getRequestLine().getUri();
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_NOTFOUND, "Not found");
		
		if (SessionManager.getInstance().isOnline()) {
			response = SwengHttpClientFactory.getInstance().execute(request);
			if (url.equals(Globals.RANDOM_QUESTION_URL)) {
				cacheQuestion(response);
			} else if (url.equals(Globals.SUBMIT_QUESTION_URL)) {
				cacheQuestion(response);
			} else if (url.endsWith("rating") && request instanceof HttpPost) {
				updateVerdictInCache(request);
			} else if (url.endsWith("rating") && request instanceof HttpPost) {
				cacheVerdict(response, request);
			} else if (url.endsWith("ratings")) {
				cacheRatings(response, request);
			}
		} else {			
			if (url.equals(Globals.RANDOM_QUESTION_URL)) {
				response = loadRandomQuestion();
			} else if (url.equals(Globals.SUBMIT_QUESTION_URL)) {
				response = submitQuestion(request);
			} else if (url.endsWith("rating")) {
				response = getRating(request);
			} else if (url.endsWith("ratings")) {
				response = getRatings(request);
			}
		}
		
		return response;
	}

	private HttpResponse getRatings(HttpUriRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private HttpResponse getRating(HttpUriRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private HttpResponse submitQuestion(HttpUriRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private HttpResponse loadRandomQuestion() {
		// TODO Auto-generated method stub
		return null;
	}

	private void cacheRatings(HttpResponse response, HttpUriRequest request) {
		// TODO Auto-generated method stub
		
	}

	private void cacheVerdict(HttpResponse response, HttpUriRequest request) {
		// TODO Auto-generated method stub
		
	}

	private void updateVerdictInCache(HttpUriRequest request) {
		// TODO Auto-generated method stub
		
	}

	private void cacheQuestion(HttpResponse response) {
		// TODO Auto-generated method stub
		
	}
	

}
