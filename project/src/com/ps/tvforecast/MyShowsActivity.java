package com.ps.tvforecast;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.ps.tvforecast.models.ShowInfo;
import com.ps.tvforecast.models.ShowsModelSingleton;

public class MyShowsActivity extends Activity {

	RestClient restClient = new RestClient();
	List<ShowInfo> showInfoList = new ArrayList<ShowInfo>();
	ListView lvShowList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_shows);
		showInfoList.clear();
		ShowsModelSingleton.initInstance(getApplicationContext());
		restClient.getShows(ShowsModelSingleton.getInstance().getShowIds());
		lvShowList = (ListView) findViewById(R.id.lvShowList);
		lvShowList.setAdapter(ShowsModelSingleton.getInstance().getShowsArrayAdapter());
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
