package epfl.sweng.cache;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tasks.ReloadPersonalRating;
import epfl.sweng.tasks.ReloadQuestionRating;
import epfl.sweng.tasks.SubmitQuestion;
import epfl.sweng.tasks.SubmitQuestionVerdict;
import epfl.sweng.tasks.interfaces.IQuestionPersonalRatingReloadedCallback;
import epfl.sweng.tasks.interfaces.IQuestionRatingReloadedCallback;
import epfl.sweng.tasks.interfaces.IQuizQuestionSubmittedCallback;
import epfl.sweng.tasks.interfaces.IQuizQuestionVerdictSubmittedCallback;

/**
 * Simple singleton data structure to store the cache and manage its input/output
 */
public final class CacheManager {

	private static CacheManager mCacheManager;
	
	private List<QuizQuestion> mCachedQuestions;
	private List<QuizQuestion> mCachedQuestionsToSubmit;
	private List<QuizQuestion> mCachedVerdictsToSubmit;
	
	private int mRunningTasks;
	private int mFailedTasks;
	
	private boolean mCacheLocked = false;
	
	
	private CacheManager() {
		mCachedQuestions = new ArrayList<QuizQuestion>();
		mCachedQuestionsToSubmit = new ArrayList<QuizQuestion>();
		mCachedVerdictsToSubmit = new ArrayList<QuizQuestion>();
		mCacheLocked = false;
	}
	
	public static CacheManager getInstance() {
		if (mCacheManager == null) {
			mCacheManager = new CacheManager();
		}
		return mCacheManager;
	}
	
	public void addCachedQuestion(QuizQuestion question) {
		if (!mCacheLocked) {
			mCachedQuestions.add(question);
		}
	}
	
	public void addCachedQuestionToSubmit(QuizQuestion question) {
		if (!mCacheLocked) {
			mCachedQuestionsToSubmit.add(question);
		}
	}
	
	public void addVerdictToSubmit(QuizQuestion question) {
		if (!mCacheLocked) {
			mCachedVerdictsToSubmit.add(question);
		}
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
	
	public void removeSubmittedQuestion(QuizQuestion question) {
		mCachedQuestionsToSubmit.remove(question);
	}
	
	public void removeSubmittedVerdict(QuizQuestion question) {
		mCachedVerdictsToSubmit.remove(question);
	}
	
	public void doNetworkCommunication(IDoNetworkCommunication callback) {
		mCacheLocked = true;
		launchSubmitQuestionTasks(callback);
	}

	private void launchSubmitQuestionTasks(final IDoNetworkCommunication callback) {
		List<QuizQuestion> cachedQuestionsToSubmit = getCachedQuestionsToSubmit();
		
		mRunningTasks = cachedQuestionsToSubmit.size();
		mFailedTasks = 0;

		Log.i("Onlinetransition", "Starting to submit cached new Questions");
		
		if (mRunningTasks == 0) {
			launchSubmitVerdictTasks(callback);
		} else {
			
			for (final QuizQuestion question : cachedQuestionsToSubmit) {
				new SubmitQuestion(new IQuizQuestionSubmittedCallback() {
					
					@Override
					public synchronized void onSubmitSuccess(QuizQuestion q) {
						question.setId(q.getId());
						removeSubmittedQuestion(question);
						mRunningTasks--;
						if (mRunningTasks == 0) {
							if (mFailedTasks == 0) {
								launchSubmitVerdictTasks(callback);
							} else {
								callback.onError();
								mCacheLocked = false;
							}
						}
					}
					
					@Override
					public synchronized void onError() {
						mFailedTasks++;
						mRunningTasks--;
						if (mRunningTasks == 0) {
							callback.onError();
							mCacheLocked = false;
						}
					}
				}).execute(question);
			}
		}
	}
	
	private void launchSubmitVerdictTasks(final IDoNetworkCommunication callback) {
		List<QuizQuestion> cachedVerdictsToSubmit = getCachedVerdictsToSubmit();
		
		mRunningTasks = cachedVerdictsToSubmit.size();
		mFailedTasks = 0;

		Log.i("Onlinetransition", "Starting to submit cached new Verdicts");
		
		if (mRunningTasks == 0) {
			launchReloadRatings(callback);
		} else {
			for (final QuizQuestion question : cachedVerdictsToSubmit) {
				new SubmitQuestionVerdict(new IQuizQuestionVerdictSubmittedCallback() {
					@Override
					public synchronized void onSubmitSuccess(QuizQuestion question) {
						removeSubmittedVerdict(question);
						mRunningTasks--;
						if (mRunningTasks == 0) {
							if (mFailedTasks == 0) {
								launchReloadRatings(callback);
							} else {
								callback.onError();
								mCacheLocked = false;
							}
						}
					}
	
	
					@Override
					public synchronized void onSubmitError() {
						mFailedTasks++;
						mRunningTasks--;
						if (mRunningTasks == 0) {
							callback.onError();
							mCacheLocked = false;
						}
					}
					
					@Override
					public void onReloadedSuccess(QuizQuestion question) {
					}
					
					@Override
					public void onReloadedError() {
					}
				}, question, question.getVerdict(), false).execute(question);
			}
		}
	}
	
	
	private void launchReloadRatings(final IDoNetworkCommunication callback) {
		List<QuizQuestion> cachedQuestions = getCachedQuestions();
		
		mRunningTasks = cachedQuestions.size()*2;
		mFailedTasks = 0;

		Log.i("Onlinetransition", "Starting to reload rating stats");
		
		if (mRunningTasks == 0) {
			callback.onSuccess();
			mCacheLocked = false;
		} else {
			for (QuizQuestion question : cachedQuestions) {
				new ReloadPersonalRating(new IQuestionPersonalRatingReloadedCallback() {
					
					@Override
					public synchronized void onReloadedSuccess(QuizQuestion question) {
						mRunningTasks--;
						if (mRunningTasks == 0) {
							if (mFailedTasks == 0) {
								callback.onSuccess();
								mCacheLocked = false;
							} else {
								callback.onError();
								mCacheLocked = false;
							}
						}
					}
					
					@Override
					public synchronized void onError() {
						mFailedTasks++;
						mRunningTasks--;
						if (mRunningTasks == 0) {
							callback.onError();
							mCacheLocked = false;
						}
					}
				}, question).execute();
				
				new ReloadQuestionRating(new IQuestionRatingReloadedCallback() {
					
					@Override
					public synchronized void onReloadedSuccess(QuizQuestion question) {
						mRunningTasks--;
						if (mRunningTasks == 0) {
							if (mFailedTasks == 0) {
								callback.onSuccess();
								mCacheLocked = false;
							} else {
								callback.onError();
								mCacheLocked = false;
							}
						}
					}
					
					@Override
					public synchronized void onError() {
						mFailedTasks++;
						mRunningTasks--;
						if (mRunningTasks == 0) {
							callback.onError();
							mCacheLocked = false;
						}
					}
				}, question).execute();
			}
		}
	}
	
}
