package com.example.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.database.MovieEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.PlaceViewHolder> {

    private Context mContext;
    private ArrayList<String> imageURLs;
    private ArrayList<String> movieTitles;
    private ArrayList<Long> movieIds;
    private final MyAdapterOnClickHandler mClickHandler;
    private long movieId;

    public interface MyAdapterOnClickHandler {
        void onClick(long movieId);
    }

    public MyAdapter(MyAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mPlace;
        public TextView mText;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            mPlace = itemView.findViewById(R.id.image_iv);
            mText = itemView.findViewById(R.id.title_tv);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            movieId = movieIds.get(adapterPosition);
            Log.v("movieId", Long.toString(movieId));
            try {
                mClickHandler.onClick(movieId);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_custom_layout, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Picasso.with(mContext).load(imageURLs.get(position)).into(holder.mPlace);
        holder.mText.setText(movieTitles.get(position));
    }

    @Override
    public int getItemCount() {
        if (imageURLs != null) {
            return imageURLs.size();
        } else return 0;
    }

    void setData(ArrayList<String> images, ArrayList<String> titles, ArrayList<Long> ids) {
        imageURLs = new ArrayList<>();
        movieTitles = new ArrayList<>();
        movieIds = new ArrayList<>();
        imageURLs = images;
        movieTitles = titles;
        movieIds = ids;
        notifyDataSetChanged();
    }

    public void setMovies(List<MovieEntry> movieEntries) {
        imageURLs = new ArrayList<>();
        movieTitles = new ArrayList<>();
        movieIds = new ArrayList<>();
        for (MovieEntry movie: movieEntries) {
            imageURLs.add(movie.getImageURL());
            movieTitles.add(movie.getMovieTitle());
            movieIds.add(movie.getId());
        }
        notifyDataSetChanged();
    }
}


