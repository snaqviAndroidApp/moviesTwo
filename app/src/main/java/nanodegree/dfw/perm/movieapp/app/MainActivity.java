package nanodegree.dfw.perm.movieapp.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import nanodegree.dfw.perm.movieapp.R;
import nanodegree.dfw.perm.movieapp.data.DetailsData;
import nanodegree.dfw.perm.movieapp.data.MoviesData;
import nanodegree.dfw.perm.movieapp.ui.MovieAdapter;
import nanodegree.dfw.perm.movieapp.utilities.MovieJsonUtils;
import nanodegree.dfw.perm.movieapp.utilities.NetworkUtils;

import static java.util.Objects.*;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final int MOVIES_OFFSET = 545;
    private static final int NUM_OF_MOVIES = 14;

    private ArrayList<MoviesData> moviesToView;                               // Final - POJO
    private ArrayList<HashMap<Integer, MoviesData>> moviesFromServer;
    private ArrayList<HashMap<Integer, MoviesData>> moviesDataToSort;
    private static ArrayList<MoviesData> moviesSortedByPopularity;
    private static ArrayList<MoviesData> moviesSortedByRating;

    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar mLoadIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(null != moviesFromServer) moviesFromServer.clear();
        if(null != moviesDataToSort ) moviesDataToSort.clear();
        if(null != moviesSortedByPopularity) moviesSortedByPopularity.clear();
        if(null != moviesSortedByPopularity) moviesSortedByRating.clear();

        mRecyclerView = findViewById(R.id.recyclerview_movie);
        mLoadIndicator = findViewById(R.id.mv_loading_indicator);
        GridLayoutManager gridlayoutManager
                = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(gridlayoutManager);
//        mRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        getPrimaryMoviesList();                                                // Async call
        mRecyclerView.setAdapter(movieAdapter);
    }

    private  void getPrimaryMoviesList() {
        new MovieTasking().execute(String.valueOf(MainActivity.NUM_OF_MOVIES), "How are you");
    }

    public void setDataClicked(MoviesData dataClicked) {
       final Intent detailIntent = new Intent(MainActivity.this, DetailsActivity.class);
        detailIntent.putExtra("movieDetails", new DetailsData(
                dataClicked.getOriginal_title(),
                dataClicked.getbackDropImage_bulitPath(),
                dataClicked.getOverview(),
                dataClicked.getVote_average(),
                dataClicked.getRelease_date())
        );
        startActivity(detailIntent);
    }

    class MovieTasking extends AsyncTask<String, Void, ArrayList<HashMap<Integer, MoviesData>>> {
        HashMap<Integer, MoviesData> parsedJsonMovieData;
        String jsonMovieResponse;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadIndicator.setVisibility(View.VISIBLE);
        }

        @Override
            protected ArrayList<HashMap<Integer, MoviesData>> doInBackground(String... strings) {
            moviesFromServer = new ArrayList<>();
            if (strings.length == 0) {                                  /* If there's no zip code, there's nothing to look up. */
                    return null;
            }
            Integer movieNumber = Integer.valueOf(strings[0]);
            for (int i = 0; i < movieNumber; i++) {
                parsedJsonMovieData = null;
                jsonMovieResponse = null;
                URL movieRequestUrl = NetworkUtils.buildUrl(String.valueOf((MOVIES_OFFSET + i)));                            //  https://api.themoviedb.org/3/movie/550?api_key=fcb4ae381c4482341fc74a85ea0b071a
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

    @Override
        protected void onPostExecute(ArrayList<HashMap<Integer, MoviesData>> movieDataListIn) {
            mLoadIndicator.setVisibility(View.INVISIBLE);
            sendMovieData(movieDataListIn);
        }
    }

    private void sendMovieData(ArrayList<HashMap<Integer, MoviesData>> localMovieData) {                 // a method or function
        moviesToView = new ArrayList<>();
        if (localMovieData != null) {
            moviesDataToSort = new ArrayList<>();
            localMovieData.forEach(m -> {
                moviesToView.add(m.get(requireNonNull(m.keySet().toArray())[0]));
            });
        }
        movieAdapter.setMoviePosters(moviesToView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mainMInflator = getMenuInflater();
        mainMInflator.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sortby_rate:
                {
                    movieAdapter.setMoviePosters(sortMoviesBy(moviesFromServer, "rate"));
                    return true;
                }
            case R.id.sortby_popularity:
            {
                movieAdapter.setMoviePosters(sortMoviesBy(moviesFromServer, "popularity"));
            }
        }
        mRecyclerView.setAdapter(movieAdapter);
        return super.onOptionsItemSelected(item);
    }

    static private ArrayList<MoviesData> sortMoviesBy(ArrayList<HashMap<Integer, MoviesData>> unOrderedMovies, String sortBy) {
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

}


