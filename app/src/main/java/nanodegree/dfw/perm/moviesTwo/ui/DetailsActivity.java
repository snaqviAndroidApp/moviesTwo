
package nanodegree.dfw.perm.moviesTwo.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.text.MessageFormat;
import java.util.ArrayList;

import nanodegree.dfw.perm.moviesTwo.R;
import nanodegree.dfw.perm.moviesTwo.data.DetailsData;
import nanodegree.dfw.perm.moviesTwo.data.db.FavoritemoviesDb;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class DetailsActivity extends AppCompatActivity
        implements TrailersAdapter.TrailersOnClickHandler {

    private String rcvd_backdrop_path;
    TextView vTitle, vOverview,vReleaseDate, vVoteAverage, vPopularity, vReview;
    ImageView thumbNail;
    ImageView imageView;

    // MovieApp Stage Two
    LinearLayoutManager postersLayoutManager, reviewsLayoutManager;

    private FavoritemoviesDb fav_mDb;                                               // Temporary purpose
    private RecyclerView mTrailerRecyclerView, mReviewsRecyclerView;
    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;
    ArrayList<String> reviews = null;
    ArrayList<String> trailers = null;
    String checkIfValidTitle = null, checkIfVaildOverview = null;                   // Check if Trailer is available
    WebView wtrailersView;
    String strTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        wtrailersView = findViewById(R.id.wv_trailers);
        wtrailersView.setWebViewClient(new inWebView());
        vTitle = findViewById(R.id.tvOriginalTitle);
        vOverview = findViewById(R.id.tvOverview);
        thumbNail = findViewById(R.id.tvThumbnail);
        vVoteAverage = findViewById(R.id.tvRating);
        vPopularity = findViewById(R.id.tvPopularity);
        vReleaseDate = findViewById(R.id.tvReleaseData);

        // Room_movies_db population ---
        imageView = findViewById(R.id.fav_imageViewtop);
        fav_mDb = FavoritemoviesDb.getInstance(getApplicationContext());           // Initialize db

        // Room_movies_db population Ends ---

        Intent intent = getIntent();
        DetailsData inDet = intent.getParcelableExtra("movieDetails");       // UnMarshalling
        reviews = new ArrayList<>();                                               // Movie Stage2, review and trailer data from MainActivity
        trailers = new ArrayList<>();
        checkIfValidTitle = inDet.getDetailAct_original_title();
        checkIfVaildOverview = inDet.getDetailAct_overview();
        if (checkIfValidTitle == null && checkIfVaildOverview == null) {        // A sneak -peak into if Trailer-n-Overview would be available
            trailers = null;
            reviews = null;                                    // assuming the all-time non-null validity of the two fields, (not ideal app)
        } else {
            reviews = inDet.getDetailAct_reviewsInput();
            trailers = inDet.getDetailAct_trailer();
        }
        vTitle.setText(checkIfValidTitle);
        vOverview.setText(checkIfVaildOverview);
        rcvd_backdrop_path = inDet.getDetailAct_backdrop_path();
        vOverview.setText(inDet.getDetailAct_overview());
        vVoteAverage.setText(String.valueOf(inDet.getDetailAct_vote_average()));
        vPopularity.setText(String.valueOf(inDet.getDetailAct_popularity()));
        vReleaseDate.setText(String.format("%s%s", getString(R.string.releaseData_Prepending), inDet.getDetailAct_release_date()));
        Picasso.get()
                .load(rcvd_backdrop_path)
                .fit()
                .rotate(0)
                .centerInside()
                .placeholder(R.color.colorAccentAmended)
                .error(R.drawable.ic_launcher_foreground)
                .into(thumbNail);

        mTrailerRecyclerView = findViewById(R.id.recyclerview_trailer);      // Trailers recyclerViews deployment Here
        mTrailerRecyclerView.setHasFixedSize(true);
        postersLayoutManager = new LinearLayoutManager(this, HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(postersLayoutManager);
        mReviewsRecyclerView = findViewById(R.id.recyclerview_review);      // Reviews - recyclerViews deployment Here
        mReviewsRecyclerView.setHasFixedSize(true);
        reviewsLayoutManager = new LinearLayoutManager(this, VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        if ((trailers == null) && (reviews == null)) {
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
            mTrailerRecyclerView.setLayoutManager(postersLayoutManager);
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

    public void onTrailerItemClickListener(String trailerId) {
        String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/" +
                "embed/"+trailerId+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
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

    public void onFavImageViewClicked(View view) {                          // set movie as favorite
        imageView.setImageResource(R.drawable.ic_favorite_full_24dp);
        imageView.setEnabled(false);
    }

}
