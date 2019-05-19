//package nanodegree.dfw.perm.moviesTwo.app;
package nanodegree.dfw.perm.moviesTwo.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import nanodegree.dfw.perm.moviesTwo.R;
import nanodegree.dfw.perm.moviesTwo.data.DetailsData;
import nanodegree.dfw.perm.moviesTwo.data.db.FavoritemoviesDb;

public class DetailsActivity extends AppCompatActivity {

    private String rcvd_backdrop_path;
    TextView vTitle, vOverview,vReleaseDate, vVoteAverage, vPopularity, vReview;
    ImageView thumbNail;
    ImageView imageView;

    ArrayList<String> reviews = null;
    ArrayList<String> trailers = null;

    String checkIfValidTitle = null, checkIfVaildOverview = null;     // Check if Trailer is available

    // MovieApp Stage Two
    private String secReviews;
    private FavoritemoviesDb fav_mDb;              // Temporary purpose

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        vTitle = findViewById(R.id.tvOriginalTitle);
        vOverview = findViewById(R.id.tvOverview);
        thumbNail = findViewById(R.id.tvThumbnail);
        vVoteAverage = findViewById(R.id.tvRating);
        vPopularity = findViewById(R.id.tvPopularity);
        vReleaseDate = findViewById(R.id.tvReleaseData);

        // Room_movies_db population ---
        imageView = findViewById(R.id.fav_imageView);
        fav_mDb = FavoritemoviesDb.getInstance(getApplicationContext());                   // Initialize db

        // Room_movies_db population Ends ---

        Intent intent = getIntent();
        DetailsData inDet = intent.getParcelableExtra("movieDetails");              // UnMarshalling

        // Movie Stage2, review and trailer data from MainActivity
        reviews = new ArrayList<>();
        trailers = new ArrayList<>();

        checkIfValidTitle = inDet.getDetailAct_original_title();
        checkIfVaildOverview = inDet.getDetailAct_overview();
        if(checkIfValidTitle == null && checkIfVaildOverview == null ){        // A sneak -peak into if Trailer-n-Overview would be available
            trailers = null;reviews = null;                                    // assuming the all-time non-null validity of the two fields, (not ideal app)
        }else {
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
    }


    public void onFavImageViewClicked(View view) {                          // set movie as favorite
        imageView.setImageResource(R.drawable.ic_favorite_full_24dp);
        imageView.setEnabled(false);
    }

//    public void onPlayClicked(View view) {                                  // trailer_index[0] successfully displaying the trailer
//        if(trailers != null){
//            String _movieIdExt = trailers.get(0);
//
//            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + _movieIdExt));
//            Intent webIntent = new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("http://www.youtube.com/watch?v=" + _movieIdExt));
//            try {
//                this.startActivity(appIntent);
//            } catch (ActivityNotFoundException ex) {
//                this.startActivity(webIntent);
//            }
//        }else {
//            Snackbar.make(Objects.requireNonNull(getCurrentFocus())
//                    , MessageFormat.format("Sorry, no Trailers available ",null)
//                    , Snackbar.LENGTH_LONG).setAction("Action", null).show();
//        }
//    }

}
