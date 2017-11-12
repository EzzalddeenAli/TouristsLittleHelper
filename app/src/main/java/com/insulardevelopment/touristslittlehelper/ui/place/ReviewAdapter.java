package com.insulardevelopment.touristslittlehelper.ui.place;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Review;

import java.util.List;

/**
 * Адаптер отзывов
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews;

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
        holder.setupHolder(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView authorTextView, reviewTextView, timeTextView;
        public ImageView[] stars = new ImageView[5];

        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.review_author_text_view);
            reviewTextView = itemView.findViewById(R.id.review_text_view);
            timeTextView = itemView.findViewById(R.id.review_time_text_view);
            stars[0] = itemView.findViewById(R.id.review_image_view_star1);
            stars[1] = itemView.findViewById(R.id.review_image_view_star2);
            stars[2] = itemView.findViewById(R.id.review_image_view_star3);
            stars[3] = itemView.findViewById(R.id.review_image_view_star4);
            stars[4] = itemView.findViewById(R.id.review_image_view_star5);
        }

        public void setupHolder(Review review) {
            authorTextView.setText(review.getAuthorName());
            timeTextView.setText(review.getTime());
            reviewTextView.setText(review.getText());
            for (int i = 0; i < review.getRating(); i++) {
                stars[i].setVisibility(View.VISIBLE);
            }
            for (int i = review.getRating(); i < 5; i++) {
                stars[i].setVisibility(View.INVISIBLE);
            }
        }
    }
}
