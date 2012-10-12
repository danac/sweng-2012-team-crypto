package epfl.sweng.tasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;

import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;


import android.os.AsyncTask;

/**
 * Class used to create a Asynchronous Task that will load a random question and display it on a ShowQuestionsActivity
 *
 */
public class LoadRandomQuestion extends AsyncTask<String, Void, QuizQuestion> {
    
	private ShowQuestionsActivity mShowQuestionsActivity;
	
	/**
	 * Constructor. Create a LoadRandomQuestion object. The loading process can 
	 * be launched by invoking the inherited execute() method. 
	 * @param ShowQuestionsActivity _showQuestionsActivity Reference to the ShowQuestionsActivity
	 * the question will be displayed in.
	 */
	public LoadRandomQuestion(ShowQuestionsActivity showQuestionsActivity) {
		mShowQuestionsActivity = showQuestionsActivity;
	}
	
	/**
	 * Method fetching the random question
	 */
	@Override
	protected QuizQuestion doInBackground(String... urls) {
		// TODO Auto-generated method stub
    	QuizQuestion question = new QuizQuestion();
    	String url;
    	try {
    		if (urls.length == 0) {
    			url = mShowQuestionsActivity.getString(R.string.random_question_url);
    		} else {
    			url = urls[0];
    		}
    		HttpGet request = new HttpGet(url);
    		ResponseHandler<String> response = new BasicResponseHandler();
    		String responseText = SwengHttpClientFactory.getInstance().execute(request, response);
			question = new QuizQuestion(responseText);
			
    	} catch (JSONException e) {
    		cancel(true);
    	} catch (ClientProtocolException e) {
    		cancel(true);
    	} catch (IOException e) {
    		cancel(true);
    	}
    	
		return question;
	}
	
	
	/**
	 * Calls back the displayQuestion Method of the ShowQuestionsActivity once 
	 * the background process loading the random message completed
	 * @param QuizQuestion question The random question to be displayed as received from the server.
	 */
	@Override
	protected void onPostExecute(QuizQuestion question) {
		mShowQuestionsActivity.displayQuestion(question);
	}

	@Override
	protected void onCancelled() {
		mShowQuestionsActivity.displayError();
	}
	
}
