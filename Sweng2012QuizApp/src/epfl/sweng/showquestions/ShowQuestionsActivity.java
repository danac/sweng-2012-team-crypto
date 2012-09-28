package epfl.sweng.showquestions;

import epfl.sweng.R;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity showing a question
 */
public class ShowQuestionsActivity extends Activity {
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
	
    	Question testQuestion = new Question();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);
        
        ListView listview = (ListView) findViewById(R.id.listView);
        TextView questionTxt = (TextView) findViewById(R.id.question);
        
        questionTxt.setText(testQuestion.getQuestion());
                       
        // Instantiating array adapter to populate the listView
        // The layout android.R.layout.simple_list_item_single_choice creates radio button for each listview item
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        														android.R.layout.simple_list_item_single_choice,
        														testQuestion.getAnswers());

        listview.setAdapter(adapter);
        
    }

    
}
