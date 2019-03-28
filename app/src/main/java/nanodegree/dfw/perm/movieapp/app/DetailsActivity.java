package nanodegree.dfw.perm.movieapp.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import nanodegree.dfw.perm.movieapp.R;
import nanodegree.dfw.perm.movieapp.data.DetailsData;

public class DetailsActivity extends AppCompatActivity {

    private String rcvd_backdrop_path;
    TextView vTitle, vOverview,vReleaseDate, vVoteAverage;
    ImageView thumbNail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        vTitle = findViewById(R.id.tvOriginalTitle);
        vOverview = findViewById(R.id.tvOverview);
        thumbNail = findViewById(R.id.tvThumbnail);
        vVoteAverage = findViewById(R.id.tvRating);
        vReleaseDate = findViewById(R.id.tvReleaseData);
        Intent intent = getIntent();
        DetailsData inDet = intent.getParcelableExtra("movieDetails");          // UnMarshalling
        vTitle.setText(inDet.getDetailAct_original_title());
        rcvd_backdrop_path = inDet.getDetailAct_backdrop_path();
        vOverview.setText(inDet.getDetailAct_overview());
        vVoteAverage.setText(String.valueOf(inDet.getDetailAct_vote_average()));
        vReleaseDate.setText(String.format("%s%s", getString(R.string.releaseData_Prepending), inDet.getDetailAct_release_date()));
        Picasso.get()
                .load(rcvd_backdrop_path)
                .fit()
                .rotate(0)
                .centerInside()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(thumbNail);
    }
}
