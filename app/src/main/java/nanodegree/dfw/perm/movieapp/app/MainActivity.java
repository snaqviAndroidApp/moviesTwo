package nanodegree.dfw.perm.movieapp.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nanodegree.dfw.perm.movieapp.R;
import nanodegree.dfw.perm.movieapp.data.Constants;
import nanodegree.dfw.perm.movieapp.data.POJO_MovieData;
import nanodegree.dfw.perm.movieapp.data.Showables;
import nanodegree.dfw.perm.movieapp.ui.MovieAdapter;
import nanodegree.dfw.perm.movieapp.utilities.MovieJsonUtils;
import nanodegree.dfw.perm.movieapp.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;

    public List<HashMap<Integer, POJO_MovieData>> moviesDataList;

    private ArrayList<Showables> mShowingListMainActivity;
    private ArrayList<POJO_MovieData> moviesListForView;

    private ProgressBar mLoadIndicator;
    private Context mContext;

    private ImageView testPicasso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesDataList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerview_movie);

        mShowingListMainActivity = new ArrayList<>();
        moviesListForView = new ArrayList<>();

        mLoadIndicator = findViewById(R.id.mv_loading_indicator);

//        testPicasso = findViewById(R.id.imageViewTest);
//        Glide.with(this).load("http://goo.gl/gEgYUd").into(testPicasso);                  // Glide works

        getPrimaryMoviesList(15);                                                // Async call

        GridLayoutManager gridlayoutManager
                = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(gridlayoutManager);
        mRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        movieAdapter.setMovieData(displayMoviePoster());
        mRecyclerView.setAdapter(movieAdapter);
    }

    private void getPrimaryMoviesList(int numOfMovies) {
        new MovieTasking().execute(String.valueOf(numOfMovies), "How are you");
    }

    private ArrayList<Showables> displayMoviePoster() {
        mShowingListMainActivity.add(new Showables(Constants.testImageUrl,Constants.testImageUrlRate));             // Sample data
        return mShowingListMainActivity;
    }

    @Override
    public void setDataClicked(String dataClicked) {

        Toast.makeText(this,"MainActivity setDataClicked()",Toast.LENGTH_SHORT).show();
    }

    public class MovieTasking extends AsyncTask<String, Void, List<HashMap<Integer, POJO_MovieData>>> {

        public static final int MOVIE_OFFSET = 545;
        HashMap<Integer, POJO_MovieData> parsedJsonMovieData;
        String jsonMovieResponse;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mLoadIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected List<HashMap<Integer, POJO_MovieData>> doInBackground(String... strings) {
            if (strings.length == 0) {                                  /* If there's no zip code, there's nothing to look up. */
                return null;
            }
            Integer movieNumber = Integer.valueOf(strings[0]);
            for(int i = 0; i < movieNumber; i++){
                parsedJsonMovieData = null;
                jsonMovieResponse = null;
                URL movieRequestUrl = NetworkUtils.buildUrl(String.valueOf((MOVIE_OFFSET + i))); //  https://api.themoviedb.org/3/movie/550?api_key=fcb4ae381c4482341fc74a85ea0b071a
                try {
                    jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                    parsedJsonMovieData = MovieJsonUtils.getMoviesStringsFromJson(MainActivity.this, jsonMovieResponse);
                }
                catch (Exception e){
                    e.printStackTrace();
                    System.out.printf("error occured: %s", e.getStackTrace().toString());
                    if(e.getClass().getName() == "java.io.FileNotFoundException") {
                        parsedJsonMovieData = new HashMap<>();
                        parsedJsonMovieData.put(Integer.valueOf((MOVIE_OFFSET + i)),
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
        protected void onPostExecute(List<HashMap<Integer, POJO_MovieData>> movieDataList) {
            mLoadIndicator.setVisibility(View.INVISIBLE);
            displayMovieData(movieDataList);
        }
    }

    private ArrayList<POJO_MovieData> displayMovieData(List<HashMap<Integer, POJO_MovieData>> movieDataList) {
        if (moviesDataList != null) {
            for (HashMap<Integer, POJO_MovieData> list : movieDataList) {
                moviesListForView.add(list.get(list.keySet().toArray()[0]));
            }
        }
        return moviesListForView;
    }

}
