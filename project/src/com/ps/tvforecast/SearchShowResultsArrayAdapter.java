package com.ps.tvforecast;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ps.tvforecast.models.ShowInfo;

public class SearchShowResultsArrayAdapter extends ArrayAdapter<ShowInfo> {
	public SearchShowResultsArrayAdapter(Context context, List<ShowInfo> showResults) {
		super(context, R.layout.simple_text_view, showResults);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShowInfo showInfo = this.getItem(position);
		TextView tvShowInfo;
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			tvShowInfo = (TextView) inflater.inflate(R.layout.simple_text_view, parent, false);
		}
		else {
			tvShowInfo = (TextView) convertView;
			
		}
		String showInfoStr = showInfo.getAsString();
		tvShowInfo.setText(showInfoStr);
		Log.d("DEBUG", "--- In SearchShowResultsArrayAdapter: " + showInfoStr);
		return tvShowInfo;
	}
}
