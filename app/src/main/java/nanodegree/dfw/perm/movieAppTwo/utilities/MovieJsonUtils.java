package nanodegree.dfw.perm.BackingApp.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import nanodegree.dfw.perm.BackingApp.data.handler.PrimaryMoviesDataHandler;

public class MovieJsonUtils {
    private static final String MOVIE_ID = "id";

    private static final String MOVIE_POSTER_PATH = "poster_path";
    private static final String MOVIE_POSTER_THUMB = "backdrop_path";
    private static final String MOVIE_ORIGINAL_TITLE = "original_title";
    private static final String TITLE = "title";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_RATING = "vote_average";
    private static final String MOVIE_POPULARITY = "popularity";

    public static HashMap<Integer, PrimaryMoviesDataHandler> getMoviesStringsFromJson(Context context, String forecastJsonStr,
                                                                                      ArrayList<String> _reviewsIn, ArrayList<String> _trailerId)
            throws JSONException {
        HashMap movieData = new HashMap();
        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        movieData.put(Integer.valueOf(forecastJson.getString(MOVIE_ID)),
                new PrimaryMoviesDataHandler(
                        forecastJson.getString(MOVIE_POSTER_PATH),
                        forecastJson.getString(MOVIE_POSTER_THUMB),
                        forecastJson.getString(MOVIE_ORIGINAL_TITLE),
                        forecastJson.getString(MOVIE_OVERVIEW),
                        forecastJson.getString(MOVIE_RELEASE_DATE),
                        forecastJson.getDouble(MOVIE_RATING),
                        forecastJson.getDouble(MOVIE_POPULARITY),
                        null,
                        _reviewsIn,
                        _trailerId
                )
        );
        return movieData;
    }

    public static ArrayList<HashMap<Integer, PrimaryMoviesDataHandler>> getOrderingMoviesStrings(Context context, String toOrderJsonStr,
                                                                                                 ArrayList<String> _reviewsIn, ArrayList _traildIdIn)
            throws JSONException {
        ArrayList<HashMap<Integer, PrimaryMoviesDataHandler>> moviesListToOrder = new ArrayList<>();
        JSONArray inMoviesJson_UnOrdered = new JSONObject(toOrderJsonStr).getJSONArray("results");
        for (int toOderMovies = 0; toOderMovies < inMoviesJson_UnOrdered.length(); toOderMovies++) {
            HashMap orderedMovieData = new HashMap();
            orderedMovieData.put(toOderMovies,
                    new PrimaryMoviesDataHandler(
                            inMoviesJson_UnOrdered.getJSONObject(toOderMovies).getString(MOVIE_POSTER_PATH),
                            inMoviesJson_UnOrdered.getJSONObject(toOderMovies).getString(MOVIE_POSTER_THUMB),
                            inMoviesJson_UnOrdered.getJSONObject(toOderMovies).getString(MOVIE_ORIGINAL_TITLE),
                            inMoviesJson_UnOrdered.getJSONObject(toOderMovies).getString(MOVIE_OVERVIEW),
                            inMoviesJson_UnOrdered.getJSONObject(toOderMovies).getString(MOVIE_RELEASE_DATE),
                            inMoviesJson_UnOrdered.getJSONObject(toOderMovies).getDouble(MOVIE_RATING),
                            inMoviesJson_UnOrdered.getJSONObject(toOderMovies).getDouble(MOVIE_POPULARITY),
                            null,
                            _reviewsIn,
                            _traildIdIn
                    )
            );
            moviesListToOrder.add(orderedMovieData);
        }
        return moviesListToOrder;
    }

}

