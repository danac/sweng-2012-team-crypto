package epfl.sweng.servercomm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.apache.http.entity.StringEntity;
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
	private List<QuizQuestion> mCachedQuestionsToSubmit;
	private List<QuizQuestion> mCachedVerdictsToSubmit;
	
	
	
	
	private CachedServerCommunication() {
		mCachedQuestions = new ArrayList<QuizQuestion>();
		mCachedQuestionsToSubmit = new ArrayList<QuizQuestion>();
		mCachedVerdictsToSubmit = new ArrayList<QuizQuestion>();
		
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
					cacheQuestion(response);
				} else if (url.endsWith("rating") && request instanceof HttpGet) {
					response = getRating(request);
				} else if (url.endsWith("rating") && request instanceof HttpPost) {
					response = cacheVerdict(response, request);
				} else if (url.endsWith("ratings")) {
					response = getRatings(request);
				}
			}
		
		} catch (JSONException e) {
			
		}
		return response;
	}

	private HttpResponse getRatings(HttpUriRequest request) throws JSONException, UnsupportedEncodingException {
		
		String url = request.getRequestLine().getUri();
		
		url = url.replace("https://sweng-quiz.appspot.com/quizquestions/", "");
		url = url.replace("/ratings", "");
		
		int id = Integer.parseInt(url);
		
		String verdict ="";
		
		for (QuizQuestion question : mCachedQuestions) {
			if (question.getId() == id) {
				HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_OK, "Ok");
				JSONObject responseJSON = new JSONObject();
				responseJSON.put("likeCount", verdict);
				responseJSON.put("dislikeCount", verdict);
				responseJSON.put("incorrectCount", verdict);
				response.setEntity(new StringEntity(responseJSON.toString()));
				return response;
			}
		}
		return new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_NOTFOUND, "Not found");
	}

	private HttpResponse getRating(HttpUriRequest request) throws JSONException, UnsupportedEncodingException {
		
		String url = request.getRequestLine().getUri();
		
		url = url.replace("https://sweng-quiz.appspot.com/quizquestions/", "");
		url = url.replace("/rating", "");
		
		int id = Integer.parseInt(url);
		
		String verdict ="";
		boolean found = false;
		
		for (QuizQuestion question : mCachedQuestions) {
			if (question.getId() == id) {
				verdict = question.getVerdict();
				found = true;
			}
		}
		if (found) {
			if (verdict.equals("")) {
				return new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_NOCONTENT, "No Content");
			} else {
				HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_OK, "Ok");
				JSONObject responseJSON = new JSONObject();
				responseJSON.put("verdict", verdict);
				response.setEntity(new StringEntity(responseJSON.toString()));
				return response;
			}
		} else {
			return new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_NOTFOUND, "Not found");
		}
		
	}
	

	private HttpResponse submitQuestion(HttpUriRequest request) throws ParseException, JSONException, IOException {
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_CREATED, "Created");
		JSONObject questionJSON = new JSONObject(getPostContent((HttpPost) request));
		
		int freeQuestionId=0;
		for (QuizQuestion question : mCachedQuestions) {
			freeQuestionId = Math.max(freeQuestionId, question.getId());
		}
		
		questionJSON.put("id", freeQuestionId++);
		QuizQuestion question = new QuizQuestion(questionJSON);
		mCachedQuestionsToSubmit.add(question);
		mCachedQuestions.add(question);
		response.setEntity(new StringEntity(question.getJSONString()));
		return response;
	}

	private HttpResponse loadRandomQuestion() throws UnsupportedEncodingException, JSONException {
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_OK, "OK");
		
		response.setEntity(new StringEntity(
				mCachedQuestions.get((int) Math.floor(Math.random()*mCachedQuestions.size())).getJSONString()));
		return response;
	}

	private void cacheRatings(HttpResponse response, HttpUriRequest request) 
		throws ParseException, JSONException, IOException {
		
		String url = request.getRequestLine().getUri();
		
		url = url.replace("https://sweng-quiz.appspot.com/quizquestions/", "");
		url = url.replace("/ratings", "");
		
		int id = Integer.parseInt(url);
		
		JSONObject ratings =  new JSONObject(getResponseContent(response));	
		
		
		for (QuizQuestion question : mCachedQuestions) {
			if (question.getId() == id) {
				question.setVerdictStats(ratings);
			}
		}
	}


	private HttpResponse cacheVerdict(HttpResponse response, HttpUriRequest request) 
		throws ParseException, JSONException, IOException {
		
		String url = request.getRequestLine().getUri();
		
		url = url.replace("https://sweng-quiz.appspot.com/quizquestions/", "");
		url = url.replace("/rating", "");
		
		int id = Integer.parseInt(url);
		
		JSONObject verdict =  new JSONObject();
		if (request instanceof HttpPost) {
			verdict =  new JSONObject(getPostContent((HttpPost) request));
		} else {
			verdict = new JSONObject(getResponseContent(response));
		}
		

		for (QuizQuestion question : mCachedQuestions) {
			if (question.getId() == id) {
				String oldverdict = question.getVerdict();
				
				if (!SessionManager.getInstance().isOnline()) {
					mCachedVerdictsToSubmit.add(question);
				}
				question.setVerdict(verdict);
				
				if (oldverdict.equals("")) {
					response = new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_CREATED, "Created");
					JSONObject responseJSON = new JSONObject();
					responseJSON.put("verdict", question.getVerdict());
					response.setEntity(new StringEntity(responseJSON.toString()));
					return response;
				} else {
					response = new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_CREATED, "Created");
					JSONObject responseJSON = new JSONObject();
					responseJSON.put("verdict", question.getVerdict());
					response.setEntity(new StringEntity(responseJSON.toString()));
					return response;
				}
					
			}
		}
		return new BasicHttpResponse(HttpVersion.HTTP_1_1, Globals.STATUSCODE_NOTFOUND, "Not found");
	}

	private void cacheQuestion(HttpResponse response) throws ClientProtocolException, JSONException, IOException {
		QuizQuestion newQuestion = new QuizQuestion(getResponseContent(response));
		
		for (QuizQuestion question : mCachedQuestions) {
			if (question.getId() == newQuestion.getId()) {
				return;
			}
		}
		
		mCachedQuestions.add(newQuestion);
	}
	

	private String getPostContent(HttpPost request) throws ParseException, IOException {
		String content = EntityUtils.toString(request.getEntity());
		request.setEntity(new StringEntity(content));
		return content;
	}
	
	private String getResponseContent(HttpResponse response) throws ParseException, IOException {
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String content = responseHandler.handleResponse(response);
		if (content == null) {
			content = "";
		}
		response.setEntity(new StringEntity(content));
		return content;
	}
	
	
}