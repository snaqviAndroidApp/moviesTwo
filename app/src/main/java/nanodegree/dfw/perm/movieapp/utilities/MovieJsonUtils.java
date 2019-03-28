package nanodegree.dfw.perm.movieapp.utilities;

//import android.content.ContentValues;
import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import nanodegree.dfw.perm.movieapp.data.MoviesData;

public class MovieJsonUtils {

    private static final String MOVIE_ID = "id";
    private static final String MOVIE_STATUS_CODE = "status_code";
    private static final String MOVIE_STATUS_CODE_VALUE = "status_message";
    private static final String MOVIE_POSTER_PATH = "poster_path";
    private static final String MOVIE_POSTER_THUMB = "backdrop_path"; // string
    private static final String MOVIE_ORIGINAL_TITLE = "original_title";  // string
    private static final String TITLE = "title";                    // string
    private static final String MOVIE_OVERVIEW = "overview";              // String
    private static final String MOVIE_RELEASE_DATE = "release_date";      //String
    private static final String MOVIE_RATING = "vote_average";      // double
    private static final String MOVIE_POPULARITY = "popularity";    // double

    public static HashMap<Integer, MoviesData> getMoviesStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        HashMap movieData = new HashMap();
        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        /* Is there an error? */
        if (forecastJson.has(MOVIE_STATUS_CODE)) {
//        if (forecastJson.has()) {
            int errorCode = forecastJson.getInt(MOVIE_STATUS_CODE);
            switch (errorCode) {
//                case HttpURLConnection.HTTP_OK:
//                    break;
//                case HttpURLConnection.HTTP_NOT_FOUND:
                case 34:
                    /* Location invalid */
//                    return null;
                    movieData.put(Integer.valueOf(forecastJson.getString(MOVIE_ID)),
                            new MoviesData(
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    0.0,
                                    0.0,
                                    forecastJson.getString(MOVIE_STATUS_CODE_VALUE))
                    );
                    return movieData;

                default:
                    return null;                                            /* Server probably down */
            }
        }

        movieData.put(Integer.valueOf(forecastJson.getString(MOVIE_ID)),
                new MoviesData(
                        forecastJson.getString(MOVIE_POSTER_PATH),
                        forecastJson.getString(MOVIE_POSTER_THUMB),
                        forecastJson.getString(MOVIE_ORIGINAL_TITLE),
                        forecastJson.getString(MOVIE_OVERVIEW),
                        forecastJson.getString(MOVIE_RELEASE_DATE),
                        forecastJson.getDouble(MOVIE_RATING),
                        forecastJson.getDouble(MOVIE_POPULARITY),
                        null)
        );

        return movieData;
    }


    /**
     * Parse the JSON and convert it into ContentValues that can be inserted into our database.
     *
     * @param context         An application context, such as a service or activity context.
     * @param forecastJsonStr The JSON to parse into ContentValues.
     *
     * @return An array of ContentValues parsed from the JSON.
     */
//    public static ContentValues[] getFullWeatherDataFromJson(Context context, String forecastJsonStr) {
//        /** This will be implemented in a future lesson **/
//        return null;
//    }
}

