package com.tempos21.market.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tempos21.mymarket.sdk.model.AppRate;
import com.worldline.alfredo.R;

import java.util.List;


public class RatingsAdapter extends ArrayAdapter<AppRate> {

    private static final String HILDE = "HILDE";
    private static final String OTTO = "OTTO";
    private static final String OLIVIA = "OLIVIA";

    private List<AppRate> ratings;
	private Activity context;
	
	public RatingsAdapter(Activity context, List<AppRate> ratings) {
		super(context, R.layout.item_ratings, ratings);
		this.ratings = ratings;
		this.context = context;
	}

	
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder vh;
		
		if(v == null) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			v = mInflater.inflate(R.layout.item_ratings, null);
		}
		if (v.getTag()==null) {
		
			vh = new ViewHolder();
			vh.ratingText = (TextView) v.findViewById(R.id.ratingText);
			vh.ratingDate = (TextView) v.findViewById(R.id.ratingDate);
			vh.userFullName = (TextView) v.findViewById(R.id.userFullName);
		    vh.userImage = (ImageView) v.findViewById(R.id.userImage);
			v.setTag(vh);
			
		}else{
			vh = (ViewHolder) v.getTag();
		}

		vh.ratingText.setText(ratings.get(position).text);
		vh.ratingDate.setText(ratings.get(position).dateCreation);
		vh.userFullName.setText(ratings.get(position).userFullName + ". ");
		vh.userImage.setImageResource(chooseImage(ratings.get(position).userFullName));

		return v;
	}


    private int chooseImage(String user){
        if(user.equalsIgnoreCase(HILDE))
            return R.drawable.hilde_profile;
        if(user.equalsIgnoreCase(OLIVIA))
            return R.drawable.olivia_profile;
        if(user.equalsIgnoreCase(OTTO))
            return R.drawable.otto_profile;

        return R.drawable.no_image;
    }


	class ViewHolder {
		TextView ratingText;
		TextView ratingDate;
		TextView userFullName;
        ImageView userImage;
	}
	
}
