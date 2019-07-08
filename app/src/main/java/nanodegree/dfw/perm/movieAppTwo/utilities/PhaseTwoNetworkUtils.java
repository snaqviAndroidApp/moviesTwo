package nanodegree.dfw.perm.BackingApp.utilities;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class PhaseTwoNetworkUtils extends NetworkUtils {

    protected static final String TRAILERS_BASE_URL_MAIN_POPULAR = "http://api.themoviedb.org/3/movie/popular";
    protected static final String REVIEWS_BASE_URL_MAIN_POPULAR = "http://api.themoviedb.org/3/movie/popular";


    public static URL buildSecLevelDetailedUrl(String _movieId, String extDetailedPhrase) {
        Uri builtUri = null;
        if(extDetailedPhrase == "reviews") {
            builtUri = Uri.parse(_BASE_URL).buildUpon()
                    .appendEncodedPath(_movieId)
                    .appendEncodedPath(extDetailedPhrase)
                    .appendQueryParameter(PARAM_KEY, KEY_VALUE)
                    .build();
        } else {
            builtUri = Uri.parse(_BASE_URL).buildUpon()
                    .appendEncodedPath(_movieId)
                    .appendEncodedPath(extDetailedPhrase)
                    .appendQueryParameter(PARAM_KEY, KEY_VALUE)
                    .build();
        }

        URL builtLevelTwoUrl = null;
        try {
            builtLevelTwoUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return builtLevelTwoUrl;
    }
}
