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

public class ShowsArrayAdapter extends ArrayAdapter<ShowInfo> {
	public ShowsArrayAdapter(Context context, List<ShowInfo> showResults) {
		super(context, R.layout.simple_text_view, showResults);
	}
	
	public void update(ShowInfo showInfo) {
		ShowInfo match = getShowInfo(showInfo.getId());
		Log.d("DEBUG", "After getShowInfo in ShowsArrayAdapter");
		if(match !=null) {
			match.updateShowInfo(showInfo);
			Log.d("DEBUG", "After match in update "+ match.getAsString());
			this.notifyDataSetChanged();
		}
	}
	
	public ShowInfo getShowInfo(String id) {
		Log.d("DEBUG", "ShowsArrayAdapter getCount" + this.getCount());
		for(int i=0; i < this.getCount(); i++) {
			ShowInfo curItem = getItem(i);
			Log.d("DEBUG", "ShowsArrayAdapter curItemId" + curItem.getId() + "looking for " + id);
			if(curItem.getId().equals(id)) {
				return curItem;
			}
		}
		return null;
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
		Log.d("DEBUG", "--- In ShowsArrayAdapter " + showInfoStr );
		return tvShowInfo;
	}
}
