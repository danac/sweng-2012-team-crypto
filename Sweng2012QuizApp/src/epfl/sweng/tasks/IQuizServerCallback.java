package epfl.sweng.tasks;

import epfl.sweng.quizquestions.QuizQuestion;
/**
 * Provides Callback functions for Server Communication Response
 */
public interface IQuizServerCallback {
	void onSuccess(QuizQuestion question);
	void onError();
}
