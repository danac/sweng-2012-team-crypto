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
import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity showing a question
 */
public class ShowQuestionsActivity extends Activity {
		
	/**
	 * Method invoked at the creation of the Activity. Triggers the display of a random question fetched from the server.
	 * @param 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);
        new LoadRandomQuestion(this).execute();
    }
    
    
    /**
     * Display a question on the screen
     * @param Question question The question to be displayed
     */
    public void displayQuestion(Question question) {
    	
    	ListView listview = (ListView) findViewById(R.id.listView);
        TextView questionTxt = (TextView) findViewById(R.id.question);
        
        questionTxt.setText(question.getQuestion());
                       
        // Instantiating array adapter to populate the listView
        // The layout android.R.layout.simple_list_item_single_choice creates radio button for each listview item
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        														android.R.layout.simple_list_item_single_choice,
        														question.getAnswers());

        listview.setAdapter(adapter);
    }

    
}
