package epfl.sweng.showquestions;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;

import epfl.sweng.R;
import epfl.sweng.R.layout;
import epfl.sweng.R.menu;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.tasks.LoadRandomQuestion;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class ShowQuestionsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);
        
        try {
			Question q = new LoadRandomQuestion().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_questions, menu);
        return true;
    }
    
    public boolean displayRandomQuestion() throws ClientProtocolException, IOException {
    	HttpGet request = new HttpGet("https://sweng-quiz.appspot.com/quizquestions/random");
    	ResponseHandler<String> response = new BasicResponseHandler();
    	String question = SwengHttpClientFactory.getInstance().execute(request, response);
    	Toast.makeText(this, question, Toast.LENGTH_SHORT).show();
        
        return true;
    }
    
}
