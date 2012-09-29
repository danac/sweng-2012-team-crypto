package epfl.sweng.showquestions;


import epfl.sweng.R;
import epfl.sweng.tasks.LoadRandomQuestion;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity showing a question
 */
public class ShowQuestionsActivity extends Activity {
	
	/**
	 * Method invoked at the creation of the Activity. 
	 * Triggers the display of a random question fetched from the server.
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
    
    public void nextQuestion(View currentView) {
        new LoadRandomQuestion(this).execute();
    }

    
}
