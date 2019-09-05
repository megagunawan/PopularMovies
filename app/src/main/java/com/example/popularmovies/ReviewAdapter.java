package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context mContext;
    private ArrayList<String> reviewAuthors;
    private ArrayList<String> reviewContents;
    private long movieId;

    public ReviewAdapter() {
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthor;
        public TextView mContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.tv_author);
            mContent = itemView.findViewById(R.id.tv_content);
        }
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_review_layout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        String textAuthor = "Reviewer: " + reviewAuthors.get(position);
        holder.mAuthor.setText(textAuthor);
        holder.mContent.setText(reviewContents.get(position));
    }

    @Override
    public int getItemCount() {
        if (reviewAuthors != null) {
            return reviewAuthors.size();
        } else return 0;
    }

    void setData(ArrayList<String> images, ArrayList<String> titles, Long id) {
        reviewAuthors = new ArrayList<>();
        reviewContents = new ArrayList<>();
        reviewAuthors = images;
        reviewContents = titles;
        movieId = id;
        notifyDataSetChanged();
    }
}
