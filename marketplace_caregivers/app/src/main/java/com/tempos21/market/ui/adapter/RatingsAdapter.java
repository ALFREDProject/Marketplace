package com.tempos21.market.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tempos21.market.client.bean.Rating;
import com.tempos21.market.client.bean.Ratings;
import com.worldline.alfredo.R;


public class RatingsAdapter extends ArrayAdapter<Rating> {

    private Ratings ratings;
    private Activity context;

    public RatingsAdapter(Activity context, Ratings ratings) {
        super(context, R.layout.item_ratings, ratings);
        this.ratings = ratings;
        this.context = context;
    }


    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder vh;

        if (v == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            v = mInflater.inflate(R.layout.item_ratings, null);
        }
        if (v.getTag() == null) {

            vh = new ViewHolder();
            vh.ratingText = (TextView) v.findViewById(R.id.ratingText);
            vh.ratingTitle = (TextView) v.findViewById(R.id.ratingTitle);
            vh.ratingRate = (RatingBar) v.findViewById(R.id.ratingRate);
            vh.ratingUser = (TextView) v.findViewById(R.id.ratingUser);
            vh.ratingDate = (TextView) v.findViewById(R.id.ratingDate);
            vh.ratingVersion = (TextView) v.findViewById(R.id.ratingVersion);
            vh.userFullName = (TextView) v.findViewById(R.id.userFullName);


            v.setTag(vh);

        } else {
            vh = (ViewHolder) v.getTag();
        }


        vh.ratingText.setText(ratings.get(position).getText());
        vh.ratingTitle.setText(ratings.get(position).getTitle());
        vh.ratingRate.setRating(ratings.get(position).getRate());
        vh.ratingUser.setText(ratings.get(position).getUserName());
        vh.ratingDate.setText(ratings.get(position).getDateCreation());
        vh.ratingVersion.setText("(v." + ratings.get(position).getVersionString() + ")");
        vh.userFullName.setText(ratings.get(position).getUserFullName());

        return v;

    }

    class ViewHolder {
        TextView ratingTitle;
        TextView ratingText;
        RatingBar ratingRate;
        TextView ratingUser;
        TextView ratingVersion;
        TextView ratingDate;
        TextView userFullName;
    }

}
