package nanodegree.dfw.perm.moviesTwo.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nanodegree.dfw.perm.moviesTwo.R;
import nanodegree.dfw.perm.moviesTwo.data.db.MovieEntries;
import nanodegree.dfw.perm.moviesTwo.data.handler.PrimaryMoviesDataHandler;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<PrimaryMoviesDataHandler> mMoviesClickedList;
    private ArrayList<String> mMovieFavReceived;
    private MovieAdapterOnClickHandler mClickHandler;
    int adapterPosition = 0;
    private String sorting = null;
    String imageUrl = null;

    public interface MovieAdapterOnClickHandler {                                       //Interface for OnClick Handling
        default void onMovieItemClickListener(PrimaryMoviesDataHandler dataClicked) {
        }
    }
    public MovieAdapter(MovieAdapterOnClickHandler movieAdapterOnClickHandler){
        mClickHandler = movieAdapterOnClickHandler;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

       ImageView mMovieImageView;
        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.poster_master);
            mMovieImageView.setBackground(null);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mMovieFavReceived != null){                          // disabling the onClick() event on Favorite-movies posters
                return;
            }else{
                adapterPosition = getAdapterPosition();
                mClickHandler.onMovieItemClickListener(mMoviesClickedList.get(adapterPosition));
            }
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
        if(!sorting.equals("favoritesList")){
            PrimaryMoviesDataHandler mImagSelected = mMoviesClickedList.get(position);
            imageUrl = mImagSelected.getPoster_builtPath();
        }else {
            imageUrl = mMovieFavReceived.get(position);
        }
        Picasso.get()                                     // Successful -implementation, includes offline (cached)data retrieval
                .load(imageUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .rotate(0)
                .centerCrop(5)
                .placeholder(R.drawable.ic_download_icon)
                .error(R.drawable.ic_launcher_foreground)
                .into(movieViewHolder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesClickedList && (null == mMovieFavReceived)) return 0;
        else if (null == mMoviesClickedList && (null != mMovieFavReceived)) {
            return mMovieFavReceived.size();
        } else
        return mMoviesClickedList.size();
    }

    public void setMoviePosters(ArrayList<PrimaryMoviesDataHandler> movieDataRcvd, String ifSorting){
        mMoviesClickedList = movieDataRcvd;
        sorting = ifSorting;
        notifyDataSetChanged();
    }

    /** Favorite Movies Handler
     *
     * **/
    public void setMovieFavPosters(ArrayList<String> favList, String ifSorting) {
        mMovieFavReceived = favList;
        sorting = ifSorting;
        notifyDataSetChanged();
    }

}
