package com.ps.tvforecast.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ps.tvforecast.R;

public class MyShowsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_shows);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_shows, menu);
		return true;
	}
	
	public void onClickSearchShows(MenuItem mi) {
        Intent i = new Intent(MyShowsActivity.this, SearchNewShowsActivity.class);
	    startActivity(i);
	}

}
