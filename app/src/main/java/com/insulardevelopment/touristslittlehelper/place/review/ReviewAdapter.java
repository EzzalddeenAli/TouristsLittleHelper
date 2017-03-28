package com.insulardevelopment.touristslittlehelper.place.review;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.insulardevelopment.touristslittlehelper.R;

import java.util.List;

/**
 * Created by Маргарита on 22.02.2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews;


    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        public TextView authorTextView, reviewTextView, timeTextView;
        public ImageView [] stars = new ImageView[5];

        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorTextView = (TextView) itemView.findViewById(R.id.review_author_text_view);
            reviewTextView = (TextView) itemView.findViewById(R.id.review_text_view);
            timeTextView = (TextView) itemView.findViewById(R.id.review_time_text_view);
            stars[0] = (ImageView) itemView.findViewById(R.id.review_image_view_star1);
            stars[1] = (ImageView) itemView.findViewById(R.id.review_image_view_star2);
            stars[2] = (ImageView) itemView.findViewById(R.id.review_image_view_star3);
            stars[3] = (ImageView) itemView.findViewById(R.id.review_image_view_star4);
            stars[4] = (ImageView) itemView.findViewById(R.id.review_image_view_star5);
        }
    }

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.authorTextView.setText(review.getAuthorName());
        holder.timeTextView.setText(review.getTime());
        holder.reviewTextView.setText(review.getText());
        for (int i = 0; i < review.getRating(); i++){
            holder.stars[i].setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

}
