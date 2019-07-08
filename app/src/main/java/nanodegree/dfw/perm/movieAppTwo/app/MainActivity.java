package nanodegree.dfw.perm.BackingApp.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import nanodegree.dfw.perm.BackingApp.R;
import nanodegree.dfw.perm.BackingApp.data.background.AppExecutors;
import nanodegree.dfw.perm.BackingApp.data.handler.DetailsDataHandler;
import nanodegree.dfw.perm.BackingApp.data.handler.PrimaryMoviesDataHandler;
import nanodegree.dfw.perm.BackingApp.data.db.MovieEntries;
import nanodegree.dfw.perm.BackingApp.data.db.MoviesDatabase;
import nanodegree.dfw.perm.BackingApp.ui.DetailsActivity;
import nanodegree.dfw.perm.BackingApp.ui.MovieAdapter;
import nanodegree.dfw.perm.BackingApp.utilities.MovieJsonUtils;
import nanodegree.dfw.perm.BackingApp.utilities.NetworkUtils;
import nanodegree.dfw.perm.BackingApp.utilities.PhaseTwoJsonUtils;
import nanodegree.dfw.perm.BackingApp.utilities.PhaseTwoNetworkUtils;

import static java.util.Objects.*;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.AdapterClickHandler {

    private static final int MOVIES_OFFSET = 545;
    private static final int NUM_OF_MOVIES = 14;
    public static final String POPULARITY = "popularity";
    public static final String RATING = "vote_average";

    private ArrayList<PrimaryMoviesDataHandler> moviesToView, moviesSortedByRating;
    private ArrayList<HashMap<Integer, PrimaryMoviesDataHandler>> moviesFromServer, moviesInputListToOrder;
    boolean menuItemEnabled = false;
    private RecyclerView mRecyclerView;

    MovieAdapter movieAdapter;
    private ProgressBar mLoadIndicator;
    private long schPeriod;
    private int threadCounts;
    private MoviesDatabase mdB_MainActivity;                                 // MovieApp Stage Two Database
    ArrayList<MovieEntries> _listMoviesRetrieved = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _initialize();
        initView();
    }

    private void _initialize() {
        schPeriod = 1;
        threadCounts = 0;
        if (null != moviesFromServer) moviesFromServer.clear();
        if (null != moviesInputListToOrder) moviesInputListToOrder.clear();
        if (null != moviesSortedByRating) moviesSortedByRating.clear();
        mdB_MainActivity = MoviesDatabase.getInstance(getApplicationContext()); // Movies-Db Initialize
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerview_movie);
        mLoadIndicator = findViewById(R.id.mv_loading_indicator);
        GridLayoutManager gridlayoutManager
                = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridlayoutManager);
        mRecyclerView.setHasFixedSize(true);

        new ConnectionUtilities().getConnCheckHanlde();                     // Checking Connectivity (internet)
    }

    public void getPrimaryMoviesList(boolean nwConn) {
        if (nwConn) {
            new MovieTasking().execute(String.valueOf(MainActivity.NUM_OF_MOVIES));
        }
    }

    /*** fav-Movies onClick() Implementation worked!!
     * leaving this implementation to move forward with 'backing-app',
     * now only left part is to somehow get the data from PrimaryMoviesDataHandler alongwith
     * @param favItemClicked
     * so when this (a poster as .jpg, is passed, the relevant data is retrieved, either from - DetailedActivity
     * or from here MainActivity
     * for now, all entries are dummied as null
     ***/

    public void onFavMovieItemClickListener(String favItemClicked){

        // Experimental
        final Intent _fdetailIntent = new Intent(MainActivity.this, DetailsActivity.class);
        _fdetailIntent.putExtra("movieDetails", new DetailsDataHandler(
                        null,
                favItemClicked,
                null,
                0.0f,
                        0.0f,
                        null,
                        null,
                null
        ));
        startActivity(_fdetailIntent);
    }

    public void onMovieItemClickListener(PrimaryMoviesDataHandler dataClicked) {
        final Intent detailIntent = new Intent(MainActivity.this, DetailsActivity.class);
        detailIntent.putExtra("movieDetails", new DetailsDataHandler(
                        dataClicked.getOriginal_title(),
                        dataClicked.getbackDropImage_bulitPath(),
                        dataClicked.getOverview(),
                        dataClicked.getVote_average(),
                        dataClicked.getPopularity(),
                        dataClicked.getRelease_date(),
                        dataClicked.getTrailer_id(),
                        dataClicked.getMovie_reviews()
                )
        );
        startActivity(detailIntent);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class MovieTasking extends AsyncTask<String, Void, ArrayList<HashMap<Integer, PrimaryMoviesDataHandler>>> {
        HashMap<Integer, PrimaryMoviesDataHandler> parsedJMovieData;
        ArrayList<String> parsedJMovieReviews;
        String jsonMovieResponse,
                rawTrailers,
                rawReviews;

        @Override
        protected void onPreExecute() {
            mLoadIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        synchronized protected ArrayList<HashMap<Integer, PrimaryMoviesDataHandler>> doInBackground(String... strings) {
            moviesFromServer = new ArrayList<>();
            moviesInputListToOrder = new ArrayList<>();
            parsedJMovieReviews = new ArrayList<>();
            if (strings.length == 0) {
                return null;
            }
            String moviesToOrder = strings[0];
            if (moviesToOrder.equals(POPULARITY) || moviesToOrder.equals(RATING)) {
                jsonMovieResponse = null;
                parsedJMovieData = null;
                rawReviews = null;
                rawTrailers = null;
                URL movieRequestUrl = NetworkUtils.buildToOrderPostersUrl(moviesToOrder);
                try {
                    jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                    moviesInputListToOrder = MovieJsonUtils.getOrderingMoviesStrings(MainActivity.this, jsonMovieResponse, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.printf("error occurred: %s", e.getStackTrace().toString());
                    return null;
                }
                return moviesInputListToOrder;
            } else {
                Integer totalMovies = Integer.valueOf(strings[0]);
                for (int i = 0; i < totalMovies; i++) {
                    parsedJMovieData = null;
                    jsonMovieResponse = null;
                    URL movieRequestUrl = NetworkUtils.buildUrl(String.valueOf((MOVIES_OFFSET + i)));
                    URL movieRequestReviewsUrl = PhaseTwoNetworkUtils.buildSecLevelDetailedUrl(String.valueOf((MOVIES_OFFSET + i)), "reviews");
                    URL movieRequestTrailerUrl = PhaseTwoNetworkUtils.buildSecLevelDetailedUrl(String.valueOf((MOVIES_OFFSET + i)), "videos");
                    try {
                        jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);                                                           // MovieApp Two Implementation
                        rawReviews = NetworkUtils.getResponseFromHttpUrl(movieRequestReviewsUrl);
                        rawTrailers = NetworkUtils.getResponseFromHttpUrl(movieRequestTrailerUrl);
                        ArrayList<String> movieReviewExt = PhaseTwoJsonUtils.getPhaseTwoJsonData(MainActivity.this, rawReviews, "reviews");
                        ArrayList<String> movieTrailerExt = PhaseTwoJsonUtils.getPhaseTwoJsonData(MainActivity.this, rawTrailers, "videos");
                        if ((movieReviewExt.size() != 0) && (movieTrailerExt.size() != 0)) {                                                                // both Review & Trailer as
                            parsedJMovieData = MovieJsonUtils.getMoviesStringsFromJson(MainActivity.this, jsonMovieResponse, movieReviewExt,
                                    movieTrailerExt);                                                                                                       //sending 1st value
                        } else if ((movieReviewExt.size() == 0) && (movieTrailerExt.size() != 0)) {
                            parsedJMovieData = MovieJsonUtils.getMoviesStringsFromJson(MainActivity.this, jsonMovieResponse,
                                    null, movieTrailerExt);
                        } else {
                            parsedJMovieData = MovieJsonUtils.getMoviesStringsFromJson(MainActivity.this, jsonMovieResponse,
                                    movieReviewExt, null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.printf("error occured: %s", e.getStackTrace().toString());
                        if (e.getClass().getName() == "java.io.FileNotFoundException") {
                            parsedJMovieData = new HashMap<>();
                            parsedJMovieData.put((MOVIES_OFFSET + i),
                                    new PrimaryMoviesDataHandler(
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            0.0,
                                            0.0,
                                            "The resource requested could not be found",
                                            null,
                                            null
                                    )
                            );
                            moviesFromServer.add(parsedJMovieData);
                            continue;
                        }
                        return null;
                    }
                    moviesFromServer.add(parsedJMovieData);
                }
                return moviesFromServer;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<Integer, PrimaryMoviesDataHandler>> movieDataListIn) {
            mLoadIndicator.setVisibility(View.INVISIBLE);
            menuItemEnabled = true;
            sendMovieData(movieDataListIn);
            invalidateOptionsMenu();
        }
    }

    private void sendMovieData(ArrayList<HashMap<Integer, PrimaryMoviesDataHandler>> localMovieData) {
        moviesToView = new ArrayList<>();
        if (localMovieData != null) {
            localMovieData.forEach(m -> {
                moviesToView.add(m.get(requireNonNull(m.keySet().toArray())[0]));
            });
        }
        movieAdapter = new MovieAdapter(this, null);      // new implementation
        movieAdapter.setMoviePosters(moviesToView, "");
        mRecyclerView.setAdapter(movieAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mainMInflator = getMenuInflater();
        mainMInflator.inflate(R.menu.main_menu, menu);
        if (menuItemEnabled) {
            menu.findItem(R.id.menuItem_sortby_popularity).setEnabled(true);
            menu.findItem(R.id.menuItem_sortby_rate).setEnabled(true);
            menu.findItem(R.id.menuItem_favorites).setEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItem_sortby_rate: {
                new MovieTasking().execute("vote_average");
                try {
                    Thread.sleep(320);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                movieAdapter.setMoviePosters(sortMoviesBy(moviesInputListToOrder, "vote_average"), RATING);
                break;
            }
            case R.id.menuItem_sortby_popularity: {
                new MovieTasking().execute("popularity");
                try {
                    Thread.sleep(320);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                movieAdapter.setMoviePosters(sortMoviesBy(moviesInputListToOrder, "popularity"), POPULARITY);
                break;
            }
            case R.id.menuItem_favorites: {

                movieAdapter = new MovieAdapter(this, this);

                _listMoviesRetrieved = new ArrayList<>();
                ArrayList<String> _favList = new ArrayList<>();
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        _listMoviesRetrieved.addAll(mdB_MainActivity.dbFavoriteMoviesDao().loadAllDbFavorite());
                        if (_listMoviesRetrieved != null) {
                            for (int favCount = 0; favCount < _listMoviesRetrieved.size(); favCount++) {
                                _favList.add(_listMoviesRetrieved.get(favCount).getBfavorite_room());                   // Original passing statement
                            }
                            movieAdapter.setMovieFavPosters(_favList, "favoritesList");
                        }
                        runOnUiThread(new Runnable() {                                              // favorite movies poster populaes to same recylcerView
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1400);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (movieAdapter.getItemCount() == 0) {
                                    Snackbar.make(Objects.requireNonNull(getCurrentFocus())
                                            , MessageFormat.format("No movie designated as favorite", (Object) null)
                                            , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                                mRecyclerView.setAdapter(movieAdapter);
                            }
                        });
                    }
                });
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<PrimaryMoviesDataHandler> sortMoviesBy(ArrayList<HashMap<Integer, PrimaryMoviesDataHandler>> unOrderedMovies, String sortBy) {
        moviesSortedByRating = new ArrayList<>();
        if (sortBy.equals("popularity")) {
            unOrderedMovies.sort((o1, o2) -> {
                Double k1 = o1.get(o1.keySet().toArray()[0]).getPopularity();
                Double k2 = o2.get(o2.keySet().toArray()[0]).getPopularity();
                return k1.compareTo(k2);
            });
        } else {
            unOrderedMovies.sort((o1, o2) -> {
                Double k1 = o1.get(o1.keySet().toArray()[0]).getVote_average();
                Double k2 = o2.get(o2.keySet().toArray()[0]).getVote_average();
                return k1.compareTo(k2);
            });
        }
        if (unOrderedMovies != null) {
            unOrderedMovies.forEach(m -> {
                moviesSortedByRating.add(m.get(m.keySet().toArray()[0]));
            });
        }
        return moviesSortedByRating;
    }

    public final class ConnectionUtilities {
        private ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);
        final Runnable internNetCheck = new Runnable() {
            @Override
            public void run() {
                if (checkConnection()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            while (threadCounts < 1) {
                                getPrimaryMoviesList(true);
                                threadCounts++;
                            }
                        }
                    });
                }
            }
        };
        final ScheduledFuture<?> connCheckHanlde =
                scheduler.scheduleAtFixedRate(internNetCheck, 5, schPeriod, SECONDS);

        public ScheduledFuture<?> getConnCheckHanlde() {
            return connCheckHanlde;
        }

        private boolean checkConnection() {
            try {
                int timeoutMs = 1500;
                Socket sock = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                sock.connect(socketAddress, timeoutMs);
                sock.close();
                return true;
            } catch (IOException e) {
                Snackbar.make(Objects.requireNonNull(getCurrentFocus())
                        , MessageFormat.format("Ah, no internet connetion", (Object) null)
                        , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                threadCounts = 0;

                return false;
            }
        }
    }
}

