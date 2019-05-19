package nanodegree.dfw.perm.moviesTwo.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PhaseTwoJsonUtils extends MovieJsonUtils {

    private static final String MOVIE_RESULTS = "results";
    private static final String MOVIE_TRAILER_KEY = "key";
    private static ArrayList<String> _results = null;

    public static ArrayList<String> getPhaseTwoJsonData(Context context, String _parsedSecJsonMoviesData, String jFlag)
            throws JSONException {
        if(jFlag == "videos"){
            _results = new ArrayList<>();
            JSONArray _traileJrArray  = new JSONObject(_parsedSecJsonMoviesData).getJSONArray(MOVIE_RESULTS);

            for (int _tCount = 0; _tCount < _traileJrArray.length();_tCount++) {
                String _traileJerId  = _traileJrArray.getJSONObject(_tCount).getString(MOVIE_TRAILER_KEY);
                _results.add(_traileJerId);
            }

        }else {
            _results = new ArrayList<>();
            JSONArray _movieReviewRcvd  = new JSONObject(_parsedSecJsonMoviesData).getJSONArray(MOVIE_RESULTS);

            for (int _tCount = 0; _tCount < _movieReviewRcvd.length();_tCount++) {
                String _traileJerId  = _movieReviewRcvd.getString(_tCount);
                _results.add(_traileJerId);
            }
        }
        return _results;

    }

    private static void parseJsonArray(JSONObject oTrailer) {
    }

}
