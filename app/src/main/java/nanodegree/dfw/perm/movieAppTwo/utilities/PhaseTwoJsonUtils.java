package nanodegree.dfw.perm.BackingApp.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhaseTwoJsonUtils extends MovieJsonUtils {

    private static final String MOVIE_RESULTS = "results";
    private static final String MOVIE_REVIEWS_CONTENT = "content";
    private static final String MOVIE_TRAILER_KEY = "key";
    private static ArrayList<String> _results = null;
    private static JSONArray _trailerArray, _movieReviewRcvd;

    public static ArrayList<String> getPhaseTwoJsonData(Context context, String _parsedSecJsonMoviesData, String jFlag)
            throws JSONException {
        _results = new ArrayList<>();
        if (jFlag == "videos") {
            _trailerArray = new JSONObject(_parsedSecJsonMoviesData).getJSONArray(MOVIE_RESULTS);
            for (int _tCount = 0; _tCount < _trailerArray.length(); _tCount++) {
                String _traileJerId = _trailerArray.getJSONObject(_tCount).getString(MOVIE_TRAILER_KEY);
                _results.add(_traileJerId);
            }
        } else {
            _movieReviewRcvd = new JSONObject(_parsedSecJsonMoviesData).getJSONArray(MOVIE_RESULTS);
            for (int _tCount = 0; _tCount < _movieReviewRcvd.length(); _tCount++) {
                String _review = _movieReviewRcvd.getJSONObject(_tCount).getString(MOVIE_REVIEWS_CONTENT);
                _results.add(_review);
            }
        }
        return _results;
    }

    private static void parseJsonArray(JSONObject oTrailer) {
    }

}
