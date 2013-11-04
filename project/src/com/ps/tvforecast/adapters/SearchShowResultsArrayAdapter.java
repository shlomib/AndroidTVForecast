package com.ps.tvforecast.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ps.tvforecast.R;
import com.ps.tvforecast.models.ShowInfo;

public class SearchShowResultsArrayAdapter extends ArrayAdapter<ShowInfo> {
	public SearchShowResultsArrayAdapter(Context context, List<ShowInfo> showResults) {
		super(context, R.layout.show_result, showResults);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShowInfo showInfo = this.getItem(position);
		ViewHolder holder = null;
		if(convertView == null) {
		    LayoutInflater inflater = LayoutInflater.from(getContext());
    		convertView = inflater.inflate(R.layout.show_result, null, false);
    		holder = new ViewHolder();
    		holder.tvShowName = (TextView) convertView.findViewById(R.id.tvShowName);
    		holder.tvShowSubDetails = (TextView) convertView.findViewById(R.id.tvShowSubDetails);
    		convertView.setTag(holder);
		}
		else {
		    holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvShowName.setText(showInfo.getName());
		holder.tvShowSubDetails.setText(showInfo.getCountry() + ", " + showInfo.getStarted());
		
		if(!showInfo.isActive()) {
		    holder.tvShowName.setTextColor(Color.parseColor("#8A8A8A"));
		}
		else {
		    holder.tvShowName.setTextColor(Color.parseColor("#000000"));
		}
		
		Log.d("DEBUG", "--- In SearchShowResultsArrayAdapter: " + showInfo.getName() + "[" + showInfo.getCountry() + ", " + showInfo.getStarted() + "]");
		return convertView;
	}
	
	class ViewHolder {
	    TextView tvShowName = null;
	    TextView tvShowSubDetails = null;
	}
}
