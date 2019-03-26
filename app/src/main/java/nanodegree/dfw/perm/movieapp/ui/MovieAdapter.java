package nanodegree.dfw.perm.movieapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nanodegree.dfw.perm.movieapp.R;
import nanodegree.dfw.perm.movieapp.data.MoviesData;
import nanodegree.dfw.perm.movieapp.data.Showables;

import static android.app.PendingIntent.getActivity;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<MoviesData> mMoviesList;
    final private MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {                                       //Interface for OnCickHanlding
        void setDataClicked(String dataClicked);
    }

    public MovieAdapter(MovieAdapterOnClickHandler movieAdapterOnClickHandler){
        mClickHandler = movieAdapterOnClickHandler;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        public final ImageView mMovieImageView;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.poster_master);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.setDataClicked(mMoviesList.get(adapterPosition).toString());   // Dummy Implementation
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context vContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_poster_item;
        LayoutInflater mainPosterInflaltor = LayoutInflater.from(vContext);
        boolean shouldAttachToParentImmediately = false;
        View viewPoster = mainPosterInflaltor.inflate(layoutIdForListItem,viewGroup,shouldAttachToParentImmediately);
        return new MovieViewHolder(viewPoster);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        MoviesData mImagSelected = mMoviesList.get(position);
        String imageUrl = mImagSelected.getPosterBulit_path();
        Picasso.get()
                .load(imageUrl)
                .fit()
                .rotate(0)
                .centerInside()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(movieViewHolder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesList) return 0;
        return mMoviesList.size();
    }

    public void setMoviePosters(ArrayList<MoviesData> movieDataRcvd){
        mMoviesList = movieDataRcvd;
        notifyDataSetChanged();
    }
}
