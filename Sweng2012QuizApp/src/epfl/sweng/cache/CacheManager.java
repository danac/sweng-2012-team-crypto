package epfl.sweng.cache;

import java.util.ArrayList;
import java.util.List;

import epfl.sweng.quizquestions.QuizQuestion;

/**
 * Simple singleton data structure to store the cache and manage its input/output
 */
public final class CacheManager {

	private static CacheManager mCacheManager;
	
	private List<QuizQuestion> mCachedQuestions;
	private List<QuizQuestion> mCachedQuestionsToSubmit;
	private List<QuizQuestion> mCachedVerdictsToSubmit;
	
	private CacheManager() {
		mCachedQuestions = new ArrayList<QuizQuestion>();
		mCachedQuestionsToSubmit = new ArrayList<QuizQuestion>();
		mCachedVerdictsToSubmit = new ArrayList<QuizQuestion>();
	}
	
	public static CacheManager getInstance() {
		if (mCacheManager == null) {
			mCacheManager = new CacheManager();
		}
		return mCacheManager;
	}
	
	public void addCachedQuestion(QuizQuestion question) {
		mCachedQuestions.add(question);
	}
	
	public void addCachedQuestionToSubmit(QuizQuestion question) {
		mCachedQuestionsToSubmit.add(question);
	}
	
	public void addVerdictToSubmit(QuizQuestion question) {
		mCachedVerdictsToSubmit.add(question);
	}
	
	public QuizQuestion getRandomQuestion() {
		return mCachedQuestions.get((int) Math.floor(Math.random()*mCachedQuestions.size()));
	}
	
	public List<QuizQuestion> getCachedQuestions() {
		return mCachedQuestions;
	}
	
	public List<QuizQuestion> getCachedQuestionsToSubmit() {
		return mCachedQuestionsToSubmit;
	}
	
	public List<QuizQuestion> getCachedVerdictsToSubmit() {
		return mCachedVerdictsToSubmit;
	}
}
