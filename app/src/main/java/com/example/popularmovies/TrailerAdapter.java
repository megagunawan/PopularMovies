package com.example.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Context mContext;
    private ArrayList<String> trailerKeys;
    private ArrayList<String> trailerTitles;
    private long movieId;
    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(String key);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        public ImageButton mImageButton;
        public TextView mTitle;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mImageButton = itemView.findViewById(R.id.iv_trailer);
            mTitle = itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String key;
            int adapterPosition = getAdapterPosition();
            key = trailerKeys.get(adapterPosition);
            try {
                mClickHandler.onClick(key);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_trailer_layout, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.mTitle.setText(trailerTitles.get(position));
    }

    @Override
    public int getItemCount() {
        if (trailerKeys != null) {
            return trailerKeys.size();
        } else return 0;
    }

    void setData(ArrayList<String> keys, ArrayList<String> titles, Long id) {
        trailerKeys = new ArrayList<>();
        trailerTitles = new ArrayList<>();
        trailerKeys = keys;
        trailerTitles = titles;
        movieId = id;
        notifyDataSetChanged();
    }
}
