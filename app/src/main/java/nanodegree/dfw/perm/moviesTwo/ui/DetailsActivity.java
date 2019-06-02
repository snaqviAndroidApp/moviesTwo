
package nanodegree.dfw.perm.moviesTwo.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import static android.widget.LinearLayout.VERTICAL;

import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;

import nanodegree.dfw.perm.moviesTwo.R;
import nanodegree.dfw.perm.moviesTwo.data.background.AppExecutors;
import nanodegree.dfw.perm.moviesTwo.data.handler.DetailsDataHandler;
import nanodegree.dfw.perm.moviesTwo.data.db.MoviesDatabase;
import nanodegree.dfw.perm.moviesTwo.data.db.MovieEntries;


public class DetailsActivity extends AppCompatActivity
        implements TrailersAdapter.TrailersOnClickHandler {

    // Extra for the mdovie Id to be received in the Intent                           MovieApp Stage Two
    public static final String EXTRA_MOVIE_ID = "extrafMovieId";
    // Extra for the movie Id to be received after rotation
    public static final String INSTANCE_MOVIE_ID = "instancefMovieId";

    LinearLayoutManager reviewsLayoutManager;
    GridLayoutManager gPostersLayoutManager;

    /**
     * Favorite - movies Storage, Room-Database
     **/
    private MoviesDatabase fav_mDb;
    private RecyclerView mTrailerRecyclerView, mReviewsRecyclerView;
    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;
    ArrayList<String> reviews = null;
    ArrayList<String> trailers = null;
    String checkIfValidTitle = null, checkIfVaildOverview = null;                   // Trailers-nullability parameters
    WebView wtrailersView;
    String strTrailer;

    private String rcvd_backdrop_path;
    TextView vTitle, vOverview, vReleaseDate, vVoteAverage, vPopularity;
    private ImageView thumbNail;
    private ImageView imageView;                                                    // MovieApp Stage Two Ends Here

    private Intent mIntent;
    private DetailsDataHandler inDetetails = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initViews();
        fav_mDb = MoviesDatabase.getInstance(getApplicationContext());           // Initialize db

        mIntent = getIntent();
        inDetetails = mIntent.getParcelableExtra("movieDetails");
        reviews = new ArrayList<>();                                             // Movie Stage2, review and trailer data from MainActivity
        trailers = new ArrayList<>();
        checkIfValidTitle = inDetetails.getDetailAct_original_title();
        checkIfVaildOverview = inDetetails.getDetailAct_overview();
        if (checkIfValidTitle == null && checkIfVaildOverview == null) {        // A sneak -peak into if Trailer-n-Overview would be available
            trailers = null;
            reviews = null;                                                     // assuming the all-time non-null validity of the two fields, (not ideal app)
        } else {
            reviews = inDetetails.getDetailAct_reviewsInput();
            trailers = inDetetails.getDetailAct_trailer();
        }
        vTitle.setText(checkIfValidTitle);
        vOverview.setText(checkIfVaildOverview);
        rcvd_backdrop_path = inDetetails.getDetailAct_backdrop_path();
        vOverview.setText(inDetetails.getDetailAct_overview());
        vVoteAverage.setText(String.valueOf(inDetetails.getDetailAct_vote_average()));
        vPopularity.setText(String.valueOf(inDetetails.getDetailAct_popularity()));
        vReleaseDate.setText(String.format("%s%s", getString(R.string.releaseData_Prepending), inDetetails.getDetailAct_release_date()));
        Picasso.get()
                .load(rcvd_backdrop_path)
                .fit()
                .rotate(0)
                .centerInside()
                .placeholder(R.color.colorAccentAmended)
                .error(R.drawable.ic_launcher_foreground)
                .into(thumbNail);

        if (trailers != null && (trailers.size() != 0))
            gPostersLayoutManager = new GridLayoutManager(this, trailers.size(), VERTICAL, false);
        mTrailerRecyclerView.setLayoutManager(gPostersLayoutManager);
        reviewsLayoutManager = new LinearLayoutManager(this, VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        if ((trailers == null || (trailers.size() == 0)) && (reviews == null)) {
            Snackbar.make(getWindow().getDecorView().getRootView()
                    , MessageFormat.format("No Trailers, Reviews Available", (Object) null)
                    , Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if ((trailers != null) && (reviews != null)) {
            trailersAdapter = new TrailersAdapter(this);
            trailersAdapter.setValidTrailers(trailers);
            mTrailerRecyclerView.setAdapter(trailersAdapter);
            reviewsAdapter = new ReviewsAdapter();
            reviewsAdapter.setValidReviews(reviews);
            mReviewsRecyclerView.setAdapter(reviewsAdapter);
        } else if (trailers != null) {
            mTrailerRecyclerView.setLayoutManager(gPostersLayoutManager);

            mTrailerRecyclerView.setHasFixedSize(true);
            trailersAdapter = new TrailersAdapter(this);
            trailersAdapter.setValidTrailers(trailers);
            mTrailerRecyclerView.setAdapter(trailersAdapter);
            Snackbar.make(getWindow().getDecorView().getRootView()
                    , MessageFormat.format("No Reviews Available", (Object) null)
                    , Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
            mReviewsRecyclerView.setHasFixedSize(true);
            reviewsAdapter = new ReviewsAdapter();
            reviewsAdapter.setValidReviews(reviews);
            mReviewsRecyclerView.setAdapter(reviewsAdapter);
            Snackbar.make(getWindow().getDecorView().getRootView()
                    , MessageFormat.format("No Reviews Available", (Object) null)
                    , Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private void initViews() {
        wtrailersView = findViewById(R.id.wv_trailers);
        wtrailersView.setWebViewClient(new inWebView());
        vTitle = findViewById(R.id.tvOriginalTitle);
        vOverview = findViewById(R.id.tvOverview);
        thumbNail = findViewById(R.id.tvThumbnail);
        vVoteAverage = findViewById(R.id.tvRating);
        vPopularity = findViewById(R.id.tvPopularity);
        vReleaseDate = findViewById(R.id.tvReleaseData);
        imageView = findViewById(R.id.fav_imageViewtop);
        mTrailerRecyclerView = findViewById(R.id.recyclerview_trailer);      // Trailers
        mTrailerRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView = findViewById(R.id.recyclerview_review);      // Reviews d
        mReviewsRecyclerView.setHasFixedSize(true);
    }

    public void onTrailerItemClickListener(String trailerId, int adapterItem) {
        adapterItem++;
        String frameVideo = "<html><body> Trailer no." + adapterItem + " <br><iframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/" + trailerId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        WebSettings webSettings = wtrailersView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wtrailersView.loadData(frameVideo, "text/html", "utf-8");
    }

    private class inWebView extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(strTrailer);
            return true;
        }
    }

    public void onFavImageViewClicked(View view) {                              // ddset favorite movies over all
        String favMovie = inDetetails.getDetailAct_backdrop_path();
        Date date = new Date();
        final MovieEntries movieEntries = new MovieEntries(favMovie, date);      // os 'run() could have access to Entities
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {                                                 // implemented DiskIO to handle multi-thread (race) scenario using Singleton Approach (getInstance())
                fav_mDb.dbFavoriteMoviesDao().insertFavorites(movieEntries);
                finish();                                                           // returning to MainActivity
            }
        });
        imageView.setImageResource(R.drawable.ic_favorite_full_24dp);
        imageView.setEnabled(false);                                             // Favorite control button
    }
}
