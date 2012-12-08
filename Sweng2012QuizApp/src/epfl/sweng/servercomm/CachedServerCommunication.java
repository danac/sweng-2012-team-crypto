package epfl.sweng.servercomm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
		mCachedQuestions = new ArrayList<QuizQuestion>();
		mCachedSubmitQuestions = new ArrayList<HttpUriRequest>();
		mCachedSubmitVerdict = new ArrayList<HttpUriRequest>();
		
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
		
		try {
			
			if (SessionManager.getInstance().isOnline()) {
				response = SwengHttpClientFactory.getInstance().execute(request);
				if (url.equals(Globals.RANDOM_QUESTION_URL)) {
					cacheQuestion(response);
				} else if (url.equals(Globals.SUBMIT_QUESTION_URL)) {
					cacheQuestion(response);
				} else if (url.endsWith("rating")) {
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
		
		} catch (JSONException e) {
			
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


	private void cacheVerdict(HttpResponse response, HttpUriRequest request) 
		throws ParseException, JSONException, IOException {
		
		String url = request.getRequestLine().getUri();
		
		url = url.replace("https://sweng-quiz.appspot.com/quizquestions/", "");
		url = url.replace("/rating", "");
		
		int id = Integer.parseInt(url);
		
		JSONObject verdict =  new JSONObject();
		if (request instanceof HttpPost) {
			verdict =  new JSONObject(EntityUtils.toString(((HttpPost) request).getEntity()));
		} else {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			verdict = new JSONObject(responseHandler.handleResponse(response));	
		}
		
		
		for (QuizQuestion question : mCachedQuestions) {
			if (question.getId() == id) {
				question.setVerdict(verdict);
			}
		}
	}

	private void cacheQuestion(HttpResponse response) throws ClientProtocolException, JSONException, IOException {
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		
		QuizQuestion newQuestion = new QuizQuestion(responseHandler.handleResponse(response));
		
		for (QuizQuestion question : mCachedQuestions) {
			if (question.getId() == newQuestion.getId()) {
				return;
			}
		}
		
		mCachedQuestions.add(newQuestion);
	}
	

}
