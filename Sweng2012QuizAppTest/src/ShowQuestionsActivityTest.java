import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.showquestions.ShowQuestionsActivity;

/**
 * First test case...
 */
public class ShowQuestionsActivityTest extends
		ActivityInstrumentationTestCase2<ShowQuestionsActivity> {
	
	private Solo solo;
	
	public ShowQuestionsActivityTest() {
		super(ShowQuestionsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	/* Begin list of the different tests to be performed */
	
	public void testDisplayQuestion() {
		solo.assertCurrentActivity("A question is being displayed",
                ShowQuestionsActivity.class);
		assertTrue(solo.searchText("?"));
				
		ListView l = solo.getCurrentListViews().get(0);
		
		assertNotNull("No list views!", l);
		assertTrue("No items in list view!", l.getChildCount()>0);
		
		//Get the last list item
		View v = l.getChildAt(l.getChildCount()-1);
		
		for (int childIndex = 0; childIndex < l.getAdapter().getCount(); childIndex++) {
		     View childView = l.getChildAt(childIndex);
		     
		     if(childView == null) {
		           /// TO DO: SCROLL
		           /// reset childIndex
		     }
		     else {
			     solo.clickOnView(childView);
		    	 
		     }
		}
		
		assertTrue(solo.searchText("\u2718") || solo.searchText("\u2714"));
		
		solo.clickOnButton("Next question");
		//assertFalse(solo.searchText("\u2718") || solo.searchText("\u2714"));

		
		
	}
	
	/* End list of the different tests to be performed */
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}