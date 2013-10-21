package com.ps.tvforecast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MyShowsActivity extends Activity {

	RestClient restClient = new RestClient();
	List<String> showsList = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_shows);
		String[] shows = {"22622", "2930", "8511"};
		showsList = Arrays.asList(shows);
		restClient.getShows(showsList);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_shows, menu);
		return true;
	}

}
