package epfl.sweng.test.exam2;

import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpRequest;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.test.mock.MockHttpClient.RequestExpectationVerifier;

/**
 * Base class for the test cases that exercise search functionality (by owner or
 * by tag).
 * 
 */
public abstract class ServerSearchTest extends ServerCommunicationTest {
	private static final String BAD_JSON = "[ this is nasty json ]";
	private static final String BAD_QUESTION = "{ \"id\": \"123\", \"owner\": \"badowner\", \"solutionIndex\": 0, "
			+ "\"answers\": [ \"42\", \"24\" ], "
			+ "\"tags\": [ \"h2g2\", \"trivia\" ] }";

	private static final String NOT_FOUND_MESSAGE = "{ \"message\": \"There are no quiz questions \" }";

	/**
	 * Return a search term to use for the requests.
	 * 
	 */
	protected abstract String getSearchTerm();

	/**
	 * Return the request pattern for the search requests.
	 * 
	 */
	protected abstract String getRequestPattern();

	/**
	 * Return a JSON-encoded question that is a valid search result for the
	 * term.
	 * 
	 */
	protected abstract String getQuestionJSON(String term);

	/**
	 * Invoke the actual search query.
	 * 
	 */
	protected abstract List<QuizQuestion> invokeSearch(String term)
		throws CommunicationException;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// Pushing the default expectation
		getMockHttpClient().pushCannedResponse(getRequestPattern(),
				HTTP_NOT_FOUND, NOT_FOUND_MESSAGE, CONTENT_TYPE_JSON);
	}

	@Override
	protected void tearDown() throws Exception {
		getMockHttpClient().popCannedResponse();
		super.tearDown();
	}

	/**
	 * Check whether a given search term is correctly embedded in the request
	 * URL.
	 * 
	 * @param term
	 * @throws CommunicationException
	 */
	private void checkRequestURL(final String term)
		throws CommunicationException {
		
		getMockHttpClient().pushExpectation(getRequestPattern(),
				new RequestExpectationVerifier() {

					@Override
					public void verify(HttpRequest request) throws Exception {
						String requestLine = request.getRequestLine()
								.toString();
						Pattern uriPattern = Pattern
								.compile(getRequestPattern());
						Matcher matcher = uriPattern.matcher(requestLine);
						assertTrue("Request URI not well-formed.",
								matcher.find());
						assertEquals(
								"Request URI doesn't contain the right search term.",
								term,
								URLDecoder.decode(matcher.group(1), "UTF-8"));
					}
				});

		invokeSearch(term);
	}

	/**
	 * Test whether the request URL is well formed.
	 * 
	 * @throws CommunicationException
	 */
	public void testRequest() throws CommunicationException {
		String term = getSearchTerm();
		checkRequestURL(term);
	}

	/**
	 * Test whether the call throws IllegalArgumentException if the search term
	 * is null.
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchTermNull() throws CommunicationException {
		try {
			invokeSearch(null);
			fail("Null search term given. Should have thrown IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Pass
		}
	}

	/**
	 * Test whether the call throws IllegalArgumentException if the search term
	 * is empty ("").
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchTermEmpty() throws CommunicationException {
		try {
			invokeSearch("");
			fail("Empty search term given. Should have thrown IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Pass
		}
	}

	/**
	 * Test whether the call throws IllegalArgumentException if the search term
	 * is whitespace (" ").
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchTermBlank() throws CommunicationException {
		try {
			invokeSearch(" ");
			fail("Blank (\" \") search term given. Should have thrown IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Pass
		}
	}

	/**
	 * Test whether the call throws IllegalArgumentException if the search term
	 * contains whitespace.
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchTermMultiWord() throws CommunicationException {
		try {
			invokeSearch("this is not good");
			fail("Multi-word search term given. Should have thrown IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Pass
		}
	}

	/**
	 * Test whether the call throws IllegalArgumentException if the search term
	 * is too long (> 20 characters).
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchExtraLong() throws CommunicationException {
		try {
			invokeSearch("supercalifragilisticexpialidocious");
			fail("Extra-long search term given. Should have thrown IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Pass
		}
	}

	/**
	 * Test whether the call throws IllegalArgumentException if the search term
	 * contains non-alphanum characters.
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchTermNonAlphanum() throws CommunicationException {
		try {
			invokeSearch("a,b,c");
			fail("Non-alphanum search term given. Should have thrown IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			// Pass
		}
	}

	/**
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchTermUnicode() throws CommunicationException {
		String term = "école";
		checkRequestURL(term);
	}

	/**
	 * Test whether the call accepts a Unicode search term that has the maximum
	 * number of allowed characters.
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchTermUnicodeMaxLen() throws CommunicationException {
		String term = "Iñtërnâtiônàlizætiøn";
		checkRequestURL(term);
	}

	/**
	 * Test whether the search raises CommunicationException when bad JSON is
	 * fed.
	 */
	public void testSearchBadJSON() {
		getMockHttpClient().pushCannedResponse(getRequestPattern(), HTTP_OK,
				BAD_JSON, CONTENT_TYPE_JSON);

		try {
			invokeSearch(getSearchTerm());
			fail("Bad JSON given. Should have thrown CommunicationException");
		} catch (CommunicationException e) {
			// Pass
		} finally {
			getMockHttpClient().popCannedResponse();
		}
	}

	/**
	 * Test whether the search raises CommunicationException when the server
	 * errs with Internal Server Error.
	 * 
	 */
	public void testSearchBadResponse() {
		getMockHttpClient().pushCannedResponse(getRequestPattern(),
				HTTP_INTERNAL_ERROR, "", null);
		try {
			invokeSearch(getSearchTerm());
			fail("Bad server response. Should have thrown CommunicationException");
		} catch (CommunicationException e) {
			// Pass
		} finally {
			getMockHttpClient().popCannedResponse();
		}
	}

	/**
	 * Test whether the search method raises CommunicationException when an
	 * empty JSON answer is given back.
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchEmptyArray() throws CommunicationException {
		getMockHttpClient().pushCannedResponse(getRequestPattern(), HTTP_OK,
				"[]", CONTENT_TYPE_JSON);
		try {
			invokeSearch(getSearchTerm());
			fail("Empty JSON array. Should have thrown CommunicationException");
		} catch (CommunicationException e) {
			// Pass
		} finally {
			getMockHttpClient().popCannedResponse();
		}
	}

	/**
	 * Test whether the search method returns an empty list when a 404 Not Found
	 * message is returned.
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchNoQuestions() throws CommunicationException {
		String term = getSearchTerm();

		getMockHttpClient().pushCannedResponse(getRequestPattern(),
				HTTP_NOT_FOUND, NOT_FOUND_MESSAGE, CONTENT_TYPE_JSON);

		try {
			List<QuizQuestion> response = invokeSearch(term);
			assertTrue(response.isEmpty());
		} finally {
			getMockHttpClient().popCannedResponse();
		}
	}

	/**
	 * Checks whether a search result returning a single question is constructed
	 * correctly.
	 * 
	 * Note that this test cannot check for the correctness of the question,
	 * since the homeworks didn't define a complete interface for the
	 * QuizQuestion.
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchOneQuestion() throws CommunicationException {
		String term = getSearchTerm();
		getMockHttpClient().pushCannedResponse(getRequestPattern(), HTTP_OK,
				"[" + getQuestionJSON(term) + "]", CONTENT_TYPE_JSON);
		try {
			List<QuizQuestion> response = invokeSearch(term);
			assertEquals(1, response.size());
		} finally {
			getMockHttpClient().popCannedResponse();
		}
	}

	/**
	 * Check whether a search result returning two questions is constructed
	 * correctly.
	 * 
	 * @throws CommunicationException
	 */
	public void testSearchTwoQuestions() throws CommunicationException {
		String term = getSearchTerm();
		getMockHttpClient()
				.pushCannedResponse(
						getRequestPattern(),
						HTTP_OK,
						"[" + getQuestionJSON(term) + ","
								+ getQuestionJSON(term) + "]",
						CONTENT_TYPE_JSON);

		try {
			List<QuizQuestion> response = invokeSearch(term);
			assertEquals(2, response.size());
		} finally {
			getMockHttpClient().popCannedResponse();
		}
	}

	/**
	 * Test whether a malformed quiz question triggers a CommunicationException.
	 * 
	 */
	public void testSearchBadQuestion() {
		String term = getSearchTerm();
		getMockHttpClient().pushCannedResponse(getRequestPattern(), HTTP_OK,
				"[" + BAD_QUESTION + "]", CONTENT_TYPE_JSON);
		try {
			invokeSearch(term);
			fail("Invalid question format. Should have thrown CommunicationException");
		} catch (CommunicationException e) {
			// Pass
		} finally {
			getMockHttpClient().popCannedResponse();
		}
	}

	/**
	 * Test whether a false positive triggers a CommunicationException.
	 * 
	 */
	public void testSearchFalsePositive() {
		String term = getSearchTerm() + "x";
		getMockHttpClient().pushCannedResponse(getRequestPattern(), HTTP_OK,
				"[" + getQuestionJSON(getSearchTerm()) + "]",
				CONTENT_TYPE_JSON);
		try {
			invokeSearch(term);
			fail("False positive in search results. Should have thrown CommunicationException");
		} catch (CommunicationException e) {
			// Pass
		} finally {
			getMockHttpClient().popCannedResponse();
		}
	}
}
