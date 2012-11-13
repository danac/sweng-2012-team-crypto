package epfl.sweng.test.mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import epfl.sweng.test.R;

/** An HTTP Client for testing*/
public class FixtureHttpClient extends MockHttpClient {

    public FixtureHttpClient(Context testContext) throws IOException, JSONException {
		super();

		String jsonString;
		try {
			InputStream is = testContext.getResources().openRawResource(R.raw.fixture_canned_responses);
	        jsonString = new Scanner(is).useDelimiter("\\A").next();
	    } catch (java.util.NoSuchElementException e) {
	        jsonString = "";
	    }

		initResponses(jsonString);
	}

    private void initResponses(String jsonString) throws IOException, JSONException {
        JSONArray json = new JSONArray(jsonString);
        for (int i = 0; i < json.length(); ++i) {
            JSONObject crJson = json.getJSONObject(i);
            String requestRegex = crJson.getString("regex");
            int status = crJson.getInt("status");
            String responseBody = null;
            if (crJson.has("response")) {
                responseBody = crJson.getJSONObject("response").toString();
            }
            String contentType = crJson.optString("contentType", null);
            pushCannedResponse(requestRegex, status, responseBody, contentType);
        }
    }
}
