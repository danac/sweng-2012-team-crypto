package epfl.sweng.servercomm.search;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tasks.LoadQuestionsByOwner;
import epfl.sweng.tasks.LoadQuestionsByTag;

public class DefaultQuestionSearchCommunication implements
		QuestionSearchCommunication {

	@Override
	public List<QuizQuestion> getQuestionsByOwner(String owner)
			throws CommunicationException {
		
		if (owner == null) {
    		throw new IllegalArgumentException();
		}
    	boolean validArgument = true;
    	
    	for(int i=0; i<owner.length(); i++) {
    		if (!Character.isLetterOrDigit(owner.charAt(i))) {
    			validArgument = false;
    		}
    	}
    	if (owner.length()>20) {
    		validArgument = false;
    	}
    	
    	if (!validArgument) {
    		throw new IllegalArgumentException();
    	}
		
		LoadQuestionsByOwner loadQuestionsByOwner = new LoadQuestionsByOwner();
		
		List<QuizQuestion> result = new ArrayList<QuizQuestion>();
		try {
			result = loadQuestionsByOwner.execute(owner).get();
		} catch (CancellationException e) {
			throw new CommunicationException();	
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
			
		return result;
	}

	@Override
	public List<QuizQuestion> getQuestionsByTag(String tag)
			throws CommunicationException {
		
		if (tag == null) {
    		throw new IllegalArgumentException();
		}
    	boolean validArgument = true;
    	for(int i=0; i<tag.length(); i++) {
    		if (!Character.isLetterOrDigit(tag.charAt(i))) {
    			validArgument = false;
    		}
    	}
    	if (tag.length()>20) {
    		validArgument = false;
    	}
    	
    	if (!validArgument) {
    		throw new IllegalArgumentException();
    	}
    	
		LoadQuestionsByTag loadQuestionsByTag = new LoadQuestionsByTag();
		
		List<QuizQuestion> result = new ArrayList<QuizQuestion>();
		try {
			result = loadQuestionsByTag.execute(tag).get();
		} catch (CancellationException e) {
			throw new CommunicationException();	
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
			
		return result;
	}
	

}
