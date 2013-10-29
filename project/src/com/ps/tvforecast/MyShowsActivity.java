package com.ps.tvforecast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
		//initializeModels();
		restClient.getShows(ShowsModelSingleton.getInstance().getShowIds());
		lvShowList = (ListView) findViewById(R.id.lvShowList);
    	
		lvShowList.setAdapter(ShowsModelSingleton.getInstance().getShowsArrayAdapter());
	}
	
	public void initializeModels() {
		Map<String,String> properties = new HashMap<String, String>();
		properties.put(ShowInfo.SHOW_ID, "8511");
		properties.put(ShowInfo.SHOW_NAME, "Foo Bar The Big Bang Theory");
		properties.put(ShowInfo.SHOW_NEXT_EPISODE_DATE, "1382652000");
		properties.put(ShowInfo.SHOW_NEXT_EPISODE_NUMBER,"07x06");
		properties.put(ShowInfo.SHOW_NEXT_EPISODE_TITLE, "The Romance Resonance");
		ShowInfo bigBang = new ShowInfo(properties);
		ShowsModelSingleton.getInstance().addShowInfo(bigBang);
		
		Map<String,String> properties2 = new HashMap<String, String>();
		properties2.put(ShowInfo.SHOW_ID, "22622");
		properties2.put(ShowInfo.SHOW_NAME, "Foo bar Modern Family");
		properties2.put(ShowInfo.SHOW_NEXT_EPISODE_DATE, "1382569200");
		properties2.put(ShowInfo.SHOW_NEXT_EPISODE_NUMBER,"05x06");
		properties2.put(ShowInfo.SHOW_NEXT_EPISODE_TITLE, "The Help");
		ShowInfo modernF = new ShowInfo(properties2);
		ShowsModelSingleton.getInstance().addShowInfo(modernF);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String showname = pref.getString("showname", "n/a");
		Log.d("DB", showname);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_shows, menu);
		return true;
	}
	
	public void onClickSearchShows(MenuItem mi) {
	    Intent i = new Intent(MyShowsActivity.this, SearchNewShowsActivity.class);
	    startActivity(i);
	}

}
