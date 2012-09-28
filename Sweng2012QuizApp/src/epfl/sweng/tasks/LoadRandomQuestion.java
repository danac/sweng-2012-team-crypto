package epfl.sweng.tasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.Question;

import android.os.AsyncTask;
import android.widget.Toast;

public class LoadRandomQuestion extends AsyncTask<Void,Void,Question> {
    
	
	@Override
	protected Question doInBackground(Void... nothing) {
		// TODO Auto-generated method stub
    	Question question = new Question();
    	try {
    		HttpGet request = new HttpGet("https://sweng-quiz.appspot.com/quizquestions/random");
    		ResponseHandler<String> response = new BasicResponseHandler();
    		String responseText = SwengHttpClientFactory.getInstance().execute(request, response);
			JSONObject responseJson = new JSONObject(responseText);
			question.setQuestion(responseJson.getString("question"));
			question.setAnswers((String[]) responseJson.get("answers"));
			question.setSolutionIndex(responseJson.getInt("solutionIndex"));
			question.setTags((String[]) responseJson.get("tags"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return question;
	}
	
}
