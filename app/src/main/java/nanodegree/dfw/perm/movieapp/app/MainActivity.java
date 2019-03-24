package nanodegree.dfw.perm.movieapp.app;

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
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import io.reactivex.Flowable;
import nanodegree.dfw.perm.movieapp.R;
import nanodegree.dfw.perm.movieapp.data.Constants;
import nanodegree.dfw.perm.movieapp.data.POJO_MovieData;
import nanodegree.dfw.perm.movieapp.data.Showables;
import nanodegree.dfw.perm.movieapp.ui.MovieAdapter;
import nanodegree.dfw.perm.movieapp.utilities.MovieJsonUtils;
import nanodegree.dfw.perm.movieapp.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {



    ArrayList<POJO_MovieData> posterForMainToAdapter;                                // Final - POJO
    private ArrayList<Showables> mShowingListMainActivity;

    List<HashMap<Integer, POJO_MovieData>> moviesDataList;                       // POJO - MAP

    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar mLoadIndicator;
    private ArrayList<POJO_MovieData> moviesData;


//    private ImageView testPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(null != moviesDataList ) moviesDataList.clear();

        mRecyclerView = findViewById(R.id.recyclerview_movie);
        mLoadIndicator = findViewById(R.id.mv_loading_indicator);

        GridLayoutManager gridlayoutManager
                = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(gridlayoutManager);
        mRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);

//        mShowingListMainActivity = new ArrayList<>();
//        movieAdapter.setMovieDataLocal(displayShowables());
//        mRecyclerView.setAdapter(movieAdapter);


        getPrimaryMoviesList(14);                                                // Async call

//        movieAdapter.setMovieData(posterForMainToAdapter);                        // for Now, it is Empty
//        movieAdapter.setMovieData(moviesData);                                      // trying with field-variable:
//        mRecyclerView.setAdapter(movieAdapter);


    }

    private ArrayList<Showables> displayShowables() {
        mShowingListMainActivity.add(new Showables(Constants.testImageUrl, Constants.testImageUrlRate));
        mShowingListMainActivity.add(new Showables(Constants.testImageUrlTwo, Constants.testImageUrlTwoRate));
        mShowingListMainActivity.add(new Showables(Constants.testImageUrlThree, Constants.testImageUrlThreeRate));
        mShowingListMainActivity.add(new Showables(Constants.testImageUrl, Constants.testImageUrlRate));
        mShowingListMainActivity.add(new Showables(Constants.testImageUrlTwo, Constants.testImageUrlTwoRate));
        mShowingListMainActivity.add(new Showables(Constants.testImageUrlThree, Constants.testImageUrlThreeRate));

        return mShowingListMainActivity;
    }

//    private ArrayList<POJO_MovieData> getPrimaryMoviesList(int numOfMovies) {
    private  void getPrimaryMoviesList(int numOfMovies) {
        new MovieTasking().execute(String.valueOf(numOfMovies), "How are you");

    }



    //    @Override
    public void setDataClicked(String dataClicked) {
        Toast.makeText(this,"MainActivity setDataClicked()",Toast.LENGTH_SHORT).show();
    }

//    public class MovieTasking extends AsyncTask<String, Void, List<HashMap<Integer, POJO_MovieData>>> {
    class MovieTasking extends AsyncTask<String, Void, List<HashMap<Integer, POJO_MovieData>>> {
        public static final int MOVIE_OFFSET = 545;
        HashMap<Integer, POJO_MovieData> parsedJsonMovieData;
        String jsonMovieResponse;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadIndicator.setVisibility(View.VISIBLE);
        }

        @Override
            protected List<HashMap<Integer, POJO_MovieData>> doInBackground(String... strings) {

            moviesDataList = new ArrayList<>();
            if (strings.length == 0) {                                  /* If there's no zip code, there's nothing to look up. */
                    return null;
            }
            Integer movieNumber = Integer.valueOf(strings[0]);
            for (int i = 0; i < movieNumber; i++) {
                parsedJsonMovieData = null;
                jsonMovieResponse = null;
                URL movieRequestUrl = NetworkUtils.buildUrl(String.valueOf((MOVIE_OFFSET + i))); //  https://api.themoviedb.org/3/movie/550?api_key=fcb4ae381c4482341fc74a85ea0b071a
                try {
                    jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                    parsedJsonMovieData = MovieJsonUtils.getMoviesStringsFromJson(MainActivity.this, jsonMovieResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.printf("error occured: %s", e.getStackTrace().toString());
                    if (e.getClass().getName() == "java.io.FileNotFoundException") {
                        parsedJsonMovieData = new HashMap<>();
                        parsedJsonMovieData.put((MOVIE_OFFSET + i),
                                new POJO_MovieData(
                                        null,
                                        null,
                                        null,
                                        null,
                                        0.0,
                                        0.0,
                                        "The resource you requested could not be found"
                                )
                        );
                        moviesDataList.add(parsedJsonMovieData);
                        continue;
                    }
                        return null;
                }
                moviesDataList.add(parsedJsonMovieData);

            }
                return moviesDataList;
        }

            @Override
            protected void onPostExecute(List<HashMap<Integer, POJO_MovieData>> movieDataListIn) {
            mLoadIndicator.setVisibility(View.INVISIBLE);

//            staticMovies = movieDataListIn;
//            movieAdapter.setMovieData(sendMovieData(movieDataListIn));
              sendMovieData(movieDataListIn);

        }
    }

//    private ArrayList<POJO_MovieData> sendMovieData(List<HashMap<Integer, POJO_MovieData>> localMovieData) {
    private void sendMovieData(List<HashMap<Integer, POJO_MovieData>> localMovieData) {
        posterForMainToAdapter = new ArrayList<>();

//        ------------------------- Lambda Approach --------------------------                      // works   March 23, 1:23 AM
        if (localMovieData != null) {
            localMovieData.forEach(m -> {
                posterForMainToAdapter.add(m.get(m.keySet().toArray()[0]));
//                System.out.println("\nLambda at work: " + String.valueOf(posterForMainToAdapter) + "\t"
//                        + posterForMainToAdapter.toString());
            });
        }
        movieAdapter.setMovieData(posterForMainToAdapter);
        mRecyclerView.setAdapter(movieAdapter);

        localMovieData = null;
//        return posterForMainToAdapter;
    }

    private ArrayList<String> orderPosters(ArrayList<POJO_MovieData> unOorderedPostersList) {

//        Iterator<POJO_MovieData> moviePoster = orderPosters.iterator();
//        int iter = 0;
//        for (POJO_MovieData poster: orderPosters){
//            System.out.println("\nIterator at work: " + String.valueOf(iter) + ": \t" + poster.getPoster_path());   // working
//            if(!moviePoster.hasNext()) {
//                continue;
//            }
//            iter++;
//        }

        System.out.println("\n\n ------- Lambda Approach ------------\n\n");

        int lambdaCounter = 0;
        ArrayList<String> movieLambDatPoster = new ArrayList<>();

        unOorderedPostersList.forEach(movieLamb->{
            movieLambDatPoster.add(movieLamb.getPoster_path());
            System.out.println("\nLambda at work: " + String.valueOf(lambdaCounter) + "\t"
                    + movieLambDatPoster.toString());
        });

        return movieLambDatPoster;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mainMInflator = getMenuInflater();
        mainMInflator.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        ArrayList<POJO_MovieData> posterForMainView;
//        posterForMainView = posterForMainToAdapter;


//        startActivity(new Intent(this,DetailsActivity.class));

//        movieAdapter.setMovieData(orderPosters(posterForMainView));
//        mRecyclerView.setAdapter(movieAdapter);

        return super.onOptionsItemSelected(item);
    }


}
