package nanodegree.dfw.perm.movieapp.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import nanodegree.dfw.perm.movieapp.R;
import nanodegree.dfw.perm.movieapp.data.DetailsData;
import nanodegree.dfw.perm.movieapp.data.MoviesData;
import nanodegree.dfw.perm.movieapp.ui.MovieAdapter;
import nanodegree.dfw.perm.movieapp.utilities.MovieJsonUtils;
import nanodegree.dfw.perm.movieapp.utilities.NetworkUtils;

import static java.util.Objects.*;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final int MOVIES_OFFSET = 545;
    private static final int NUM_OF_MOVIES = 14;
    public static final String POPULARITY = "popularity";
    public static final String RATING = "vote_average";

    private ArrayList<MoviesData> moviesToView;                               // Final - POJO
    private ArrayList<MoviesData> moviesSortedByRating;                       // To Order

    private ArrayList<HashMap<Integer, MoviesData>> moviesFromServer;
    private ArrayList<HashMap<Integer, MoviesData>> moviesInputListToOrder;

    boolean menuItemEnabled = false;
    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar mLoadIndicator;
    private long schPeriod;
    private int threadCounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(null != moviesFromServer) moviesFromServer.clear();
        if(null != moviesInputListToOrder) moviesInputListToOrder.clear();
        if(null != moviesSortedByRating) moviesSortedByRating.clear();
        schPeriod = 15;
        threadCounts = 0;

        mRecyclerView = findViewById(R.id.recyclerview_movie);
        mLoadIndicator = findViewById(R.id.mv_loading_indicator);
        GridLayoutManager gridlayoutManager
                = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(gridlayoutManager);
        mRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        new ConnectionUtilities().getConnCheckHanlde();
        mRecyclerView.setAdapter(movieAdapter);
    }

    public void getPrimaryMoviesList(boolean nwConn) {
        if(nwConn) {
            new MovieTasking().execute(String.valueOf(MainActivity.NUM_OF_MOVIES));
        }
    }

    public void setDataClicked(MoviesData dataClicked) {
       final Intent detailIntent = new Intent(MainActivity.this, DetailsActivity.class);
        detailIntent.putExtra("movieDetails", new DetailsData(
                dataClicked.getOriginal_title(),
                dataClicked.getbackDropImage_bulitPath(),
                dataClicked.getOverview(),
                dataClicked.getVote_average(),
                dataClicked.getPopularity(),
                dataClicked.getRelease_date())
        );
        startActivity(detailIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class MovieTasking extends AsyncTask<String, Void, ArrayList<HashMap<Integer, MoviesData>>> {
        HashMap<Integer, MoviesData> parsedJsonMovieData;
        String jsonMovieResponse;

        @Override
        protected void onPreExecute() {
            mLoadIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        synchronized protected ArrayList<HashMap<Integer, MoviesData>> doInBackground(String... strings) {
            moviesFromServer = new ArrayList<>();
            moviesInputListToOrder = new ArrayList<>();
            if (strings.length == 0) {
                    return null;
            }
            String moviesToOrder = strings[0];
            if(moviesToOrder.equals(POPULARITY) || moviesToOrder.equals(RATING)){                       // populaer or rated movies data - parsing
                parsedJsonMovieData = null;
                jsonMovieResponse = null;
                URL movieRequestUrl = NetworkUtils.buildToOrderPostersUrl(moviesToOrder);               // build URL
                try {
                    jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                    moviesInputListToOrder = MovieJsonUtils.getOrderingMoviesStrings(MainActivity.this, jsonMovieResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.printf("error occured: %s", e.getStackTrace().toString());
                    return null;
                }
//                moviesFromServer.add(parsedJsonMovieData);

                return moviesInputListToOrder;
            }
            else {
                Integer movieNumber = Integer.valueOf(strings[0]);
                for (int i = 0; i < movieNumber; i++) {
                    parsedJsonMovieData = null;
                    jsonMovieResponse = null;
                    URL movieRequestUrl = NetworkUtils.buildUrl(String.valueOf((MOVIES_OFFSET + i)));        //  https://api.themoviedb.org/3/movie/550?api_key=KEY
                    try {
                        jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                        parsedJsonMovieData = MovieJsonUtils.getMoviesStringsFromJson(MainActivity.this, jsonMovieResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.printf("error occured: %s", e.getStackTrace().toString());
                        if (e.getClass().getName() == "java.io.FileNotFoundException") {
                            parsedJsonMovieData = new HashMap<>();
                            parsedJsonMovieData.put((MOVIES_OFFSET + i),
                                    new MoviesData(
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            0.0,
                                            0.0,
                                            "The resource you requested could not be found"
                                    )
                            );
                            moviesFromServer.add(parsedJsonMovieData);
                            continue;
                        }
                        return null;
                    }

                    moviesFromServer.add(parsedJsonMovieData);
                }

                return moviesFromServer;
            }
        }

    @Override
        protected void onPostExecute(ArrayList<HashMap<Integer, MoviesData>> movieDataListIn) {
            mLoadIndicator.setVisibility(View.INVISIBLE);
            menuItemEnabled = true;
            sendMovieData(movieDataListIn);
            invalidateOptionsMenu();
        }
    }

    private void sendMovieData(ArrayList<HashMap<Integer, MoviesData>> localMovieData) {
        moviesToView = new ArrayList<>();
        if (localMovieData != null) {
            localMovieData.forEach(m -> {
                moviesToView.add(m.get(requireNonNull(m.keySet().toArray())[0]));
            });
        }
        movieAdapter.setMoviePosters(moviesToView, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mainMInflator = getMenuInflater();
        mainMInflator.inflate(R.menu.main_menu, menu);
        if(menuItemEnabled){
            menu.findItem(R.id.sortby_popularity).setEnabled(true);
            menu.findItem(R.id.sortby_rate).setEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sortby_rate:
                {
                    new MovieTasking().execute("vote_average");                                                                     //    TODO_ (1)Popular:
                    try {
                        Thread.sleep(280);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    movieAdapter.setMoviePosters(sortMoviesBy(moviesInputListToOrder, "vote_average"), RATING);
                    break;
                }
            case R.id.sortby_popularity:
                {
                    new MovieTasking().execute("popularity");                                                                   //    TODO_ (2) Top rated:
                    try {
                        Thread.sleep(280);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    movieAdapter.setMoviePosters(sortMoviesBy(moviesInputListToOrder, "popularity"), POPULARITY);
                    break;
                }
        }
        mRecyclerView.setAdapter(movieAdapter);
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<MoviesData> sortMoviesBy(ArrayList<HashMap<Integer, MoviesData>> unOrderedMovies, String sortBy) {

        moviesSortedByRating = new ArrayList<>();
        if(sortBy.equals("popularity")){
            unOrderedMovies.sort((o1, o2) -> {                                      // Comparator
                Double k1 = o1.get(o1.keySet().toArray()[0]).getPopularity();
                Double k2 = o2.get(o2.keySet().toArray()[0]).getPopularity();
                return k1.compareTo(k2);
            });
        }else {
            unOrderedMovies.sort((o1, o2) -> {                                      // Comparator
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

        final Runnable internNetCheck  = new Runnable() {           // 2nd Approach
            @Override
            public void run() {
                    if(checkConnection()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                while (threadCounts < 1 ){
                                    getPrimaryMoviesList(true);
                                threadCounts++;
                                schPeriod = 15;
                                    Log.d("thaeadC", "Try counter: " + threadCounts);       // check repetition
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
                        SocketAddress sockAddr = new InetSocketAddress("8.8.8.8", 53);
                        sock.connect(sockAddr, timeoutMs);
                        sock.close();
                        return true;
                    } catch (IOException e) {
                        Snackbar.make(Objects.requireNonNull(getCurrentFocus())
                                , MessageFormat.format("Ah, no internet connetion", null)
                                , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Log.e("thaeadC", "IOException counter: " + threadCounts);           // check repetition
                        threadCounts = 0;
                        return false;
                    }
                }
        }
}

