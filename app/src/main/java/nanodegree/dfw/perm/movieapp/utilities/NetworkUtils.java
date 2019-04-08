package nanodegree.dfw.perm.movieapp.utilities;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the Movies servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private final static String PARAM_KEY = "api_key";

//    private final static String KEY_VALUE = "YOUR KEY";
    private final static String KEY_VALUE = "fcb4ae381c4482341fc74a85ea0b071a";

    private static final String BASE_MOVIES_URL =
            "https://api.themoviedb.org/3/movie";
    private static final String _BASE_URL = BASE_MOVIES_URL;

    private static final String POSTER_BASE_URL_MAIN = "http://image.tmdb.org/t/p";

    private static final String POSTER_BASE_URL_MAIN_RATED = "http://api.themoviedb.org/3/movie/top_rated";     //  http://api.themoviedb.org/3/movie/top_rated?api_key=<api_key>
    private static final String POSTER_BASE_URL_MAIN_POPULAR = "http://api.themoviedb.org/3/movie/popular";     //  http://api.themoviedb.org/3/movie/popular?api_key=<api_key>

    private static final String OPTIMUM_SIZE = "w185";
    private static final String OPTIMUM_SIZE_92 = "w92";
    private static final String OPTIMUM_SIZE_154 = "w154";

    /**
     * Builds the URL used to talk to the Movie server [api] server
     * @implNote ----> Current consumption <--------
     * @param numOfMovies The total-Movies-numbre that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String numOfMovies) {
        Uri builtUri = Uri.parse(_BASE_URL).buildUpon()
                .appendEncodedPath(numOfMovies)
                .appendQueryParameter(PARAM_KEY, KEY_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static URL buildPosterUrl(String imageLocation) {
        Uri builtUri = Uri.parse(POSTER_BASE_URL_MAIN).buildUpon()
                .appendEncodedPath(OPTIMUM_SIZE)
                .appendEncodedPath(imageLocation)
                .build();

        URL urlPoster = null;
        try {
            urlPoster = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urlPoster;
    }

    public static URL buildToOrderPostersUrl(String imageLocation) {
        URL urlPosterOrdered = null;
        if (imageLocation.equals("popularity")) {
            Uri builtUri = Uri.parse(POSTER_BASE_URL_MAIN_POPULAR).buildUpon()
                    .appendQueryParameter(PARAM_KEY, KEY_VALUE)
                    .build();
            try {
                urlPosterOrdered = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
                Uri builtUri = Uri.parse(POSTER_BASE_URL_MAIN_RATED).buildUpon()
                        .appendQueryParameter(PARAM_KEY, KEY_VALUE)
                        .build();
                try {
                    urlPosterOrdered = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }                                                           // Expected: http://api.themoviedb.org/3/movie/popular?api_key=fcb4ae381c4482341fc74a85ea0b071a
            return urlPosterOrdered;                                    // built:    http://api.themoviedb.org/3/movie/top_rated?api_key=fcb4ae381c4482341fc74a85ea0b071a
        }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}