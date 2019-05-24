package nanodegree.dfw.perm.moviesTwo.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.ArrayList;
import nanodegree.dfw.perm.moviesTwo.R;

    public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.PostersViewHolder> {

        private static final String TAG = TrailersAdapter.class.getSimpleName();

        private ArrayList<String> mTrailerClickedList;
        final private TrailersAdapterOnClickHandler mTrailerClickHandler;

        public interface TrailersAdapterOnClickHandler {
            default void onTrailerClickListener(String trailerClicked) {
            }
        }

        public TrailersAdapter(TrailersAdapterOnClickHandler trailerClickHandler) {
                mTrailerClickHandler = trailerClickHandler;

            }

        public class PostersViewHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener{
            ImageButton b_TrailerPlayer;

            public PostersViewHolder(@NonNull View itemView) {
                super(itemView);
                b_TrailerPlayer = itemView.findViewById(R.id.playButton);
                itemView.setOnClickListener(this);
            }

            public void bind(int position) {
                b_TrailerPlayer.getResources().getDrawable(R.drawable.ic_btn_24dp, null);
            }

            @Override
            public void onClick(View v) {
                int trailerAdapterPosition = getAdapterPosition();
                mTrailerClickHandler.onTrailerClickListener(mTrailerClickedList.get(trailerAdapterPosition));
                Log.d("_tClick", "trailer clicked");
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
            Log.d(TAG, "#" + position);
            if(getItemCount() != 0){
                Toast.makeText(pholder.b_TrailerPlayer.getContext(), String.format("%d Trailers available", getItemCount()), Toast.LENGTH_SHORT).show();
                for (int count = 0; count < getItemCount(); count++)
                      {
                          pholder.bind(position);                                // to be replaced with actual
                      }
            }
        }

        @Override
        public int getItemCount() {
            if(null == mTrailerClickedList) return 0;
            return mTrailerClickedList.size();
        }

        public void setValidTrailers(ArrayList<String> trailers) {
            mTrailerClickedList = trailers;
            notifyDataSetChanged();
        }
    }
