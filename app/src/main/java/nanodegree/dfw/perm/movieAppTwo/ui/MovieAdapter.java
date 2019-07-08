package nanodegree.dfw.perm.BackingApp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;

import nanodegree.dfw.perm.BackingApp.R;
import nanodegree.dfw.perm.BackingApp.data.handler.PrimaryMoviesDataHandler;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<PrimaryMoviesDataHandler> mMoviesClickedList;
    private ArrayList<String> mFavMovieReceived;
    private AdapterClickHandler mClickHandler, mFavoriteClickHandler;
    int adapterPosition = 0;
    private String sorting = null;
    String imageUrl = null;

    public interface AdapterClickHandler {

        /** Sucessful implementation of onClick Event Handling for MainActivity Posters
         * as well as for Favorite-Posters
         * @param dataClicked for MainPosters
         *                    that are being painted on same recyclerView that was
         *                    initially used for MainActivity posters that is
         *                    starting point for the App
         *
         **/
        default void onMovieItemClickListener(PrimaryMoviesDataHandler dataClicked) {
        }
        default void onFavMovieItemClickListener(String _fprimaryMovies) {
        }
    }
    public MovieAdapter(AdapterClickHandler movieAdapterOnClickHandler, AdapterClickHandler _favMovieAdapterOnClickHandler){
        mClickHandler = movieAdapterOnClickHandler;
        mFavoriteClickHandler = _favMovieAdapterOnClickHandler;
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
            adapterPosition = getAdapterPosition();
            if(mFavMovieReceived == null && mMoviesClickedList == null){
                Snackbar.make(v.getRootView()
                        , MessageFormat.format("No favorite movies", (Object) null)
                        , Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            else if(mFavMovieReceived != null)
            {
                mFavoriteClickHandler.onFavMovieItemClickListener(mFavMovieReceived.get(adapterPosition));
            }
            else
                mClickHandler.onMovieItemClickListener(mMoviesClickedList.get(adapterPosition));
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
            imageUrl = mFavMovieReceived.get(position);
        }
        Picasso.get()                                                           // Successful -implementation, includes offline (cached)data retrieval
                .load(imageUrl)
                .fit()
                .rotate(0)
                .centerCrop(5)
                .placeholder(R.drawable.ic_download_icon)
                .error(R.drawable.ic_launcher_foreground)
                .into(movieViewHolder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesClickedList && (null == mFavMovieReceived)) return 0;
        else if (null == mMoviesClickedList && (null != mFavMovieReceived)) {
            return mFavMovieReceived.size();
        } else
        return mMoviesClickedList.size();
    }

    public void setMoviePosters(ArrayList<PrimaryMoviesDataHandler> movieDataRcvd, String ifSorting){
        mMoviesClickedList = movieDataRcvd;
        sorting = ifSorting;
        notifyDataSetChanged();
    }

    /** Favorite Movies Handler
     * @param favList brings in the List of favorite-movies posters
     **/
    public void setMovieFavPosters(ArrayList<String> favList, String ifSorting) {
        mFavMovieReceived = favList;
        sorting = ifSorting;
        notifyDataSetChanged();
    }

}
