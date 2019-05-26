package nanodegree.dfw.perm.moviesTwo.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import nanodegree.dfw.perm.moviesTwo.R;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.PostersViewHolder> {

    private static final String TAG = TrailersAdapter.class.getSimpleName();
    private ArrayList<String> mTrailerClickedList;
    final private TrailersOnClickHandler mTrailerClickHandler;

    public interface TrailersOnClickHandler {
        default void onTrailerItemClickListener(String trailerClicked) {
        }
    }

    public TrailersAdapter(TrailersOnClickHandler trailerClickHandler) {
        mTrailerClickHandler = trailerClickHandler;
    }

    public class PostersViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
//        ImageButton b_TrailerPlayer;
        ImageView b_TrailerPlayer;

        public PostersViewHolder(@NonNull View itemView) {
            super(itemView);
            b_TrailerPlayer = itemView.findViewById(R.id.playButton);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) { }

        @Override
        public void onClick(View v) {
            int trailerAdapterPosition = getAdapterPosition();
            mTrailerClickHandler.onTrailerItemClickListener(mTrailerClickedList.get(trailerAdapterPosition));
            Log.d("_tClick", "trailer: " + trailerAdapterPosition + " clicked");
        }
    }

    public PostersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_trailerbutton_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        PostersViewHolder trailersViewHolder = new PostersViewHolder(view);
        return trailersViewHolder;
    }

    @Override
    public void onBindViewHolder(PostersViewHolder pholder, int position) {
        pholder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerClickedList) return 0;
        return mTrailerClickedList.size();
    }

    public void setValidTrailers(ArrayList<String> trailersIn) {
        mTrailerClickedList = trailersIn;
        notifyDataSetChanged();
    }
}
