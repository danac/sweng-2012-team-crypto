package epfl.sweng.tools;

import java.util.ArrayList;
import java.util.List;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Various GUI manipulation tools...
 */
public class GUITools {

	/**
     * Finds recursively all the EditTexts inside a ViewGroup
     * @param v ViewGroup in which to search
     * @return The list of all the Buttons
     */
    static public List<EditText> findAllEditTexts(ViewGroup v) {
    	List<EditText> list = new ArrayList<EditText>();
    	int nbChildren = v.getChildCount();
    	for (int i=0; i<nbChildren; i++) {
    		if (v.getChildAt(i) instanceof EditText) {
    			list.add((EditText) v.getChildAt(i));
    		} else if (v.getChildAt(i) instanceof ViewGroup) {
    			list.addAll(findAllEditTexts((ViewGroup) v.getChildAt(i)));
    		}
    	}
    	return list;
    }

    /**
     * Finds recursively all the tagged buttons inside a ViewGroup
     * @param v ViewGroup in which to search
     * @param tag The tag that buttons must have
     * @return The list of all the Buttons
     */
    public static List<Button> findAllButtons(ViewGroup v, Object tag) {
    	List<Button> list = new ArrayList<Button>();
    	int nbChildren = v.getChildCount();
    	for (int i=0; i<nbChildren; i++) {
    		if (v.getChildAt(i) instanceof Button && v.getChildAt(i).getTag() == tag) {
    			list.add((Button) v.getChildAt(i));
    		} else if (v.getChildAt(i) instanceof ViewGroup) {
    			list.addAll(findAllButtons((ViewGroup) v.getChildAt(i), tag));
    		}
    	}
    	return list;
    }
	
    /**
     * Finds recursively all the buttons inside a ViewGroup
     * @param v ViewGroup in which to search
     * @return The list of all the Buttons
     */
    public static List<Button> findAllButtons(ViewGroup v) {
    	List<Button> list = new ArrayList<Button>();
    	int nbChildren = v.getChildCount();
    	for (int i=0; i<nbChildren; i++) {
    		if (v.getChildAt(i) instanceof Button) {
    			list.add((Button) v.getChildAt(i));
    		} else if (v.getChildAt(i) instanceof ViewGroup) {
    			list.addAll(findAllButtons((ViewGroup) v.getChildAt(i)));
    		}
    	}
    	return list;
    }
    
}