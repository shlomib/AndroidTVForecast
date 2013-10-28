package com.ps.tvforecast;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ps.tvforecast.models.ShowInfo;
import com.ps.tvforecast.models.ShowsModelSingleton;

public class SearchNewShowsActivity extends Activity {

    RestClient restClient = new RestClient();
    List<ShowInfo> showInfoResults = new ArrayList<ShowInfo>();
    EditText etShowName;
    Button btnSearch;
    ListView lvShowResults;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new_shows);
        
        etShowName = (EditText) findViewById(R.id.etShowName);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        lvShowResults = (ListView) findViewById(R.id.lvShowResults);
        
        ShowsModelSingleton.getInstance().getSearchShowResultsArrayAdapter().clear();
        lvShowResults.setAdapter(ShowsModelSingleton.getInstance().getSearchShowResultsArrayAdapter());
        
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String showName = etShowName.getText().toString();
                
                if(!showName.equals("")) {
                    ShowsModelSingleton.getInstance().getSearchShowResultsArrayAdapter().clear();
                    restClient.searchForShow(showName);
                }
            }
        });
        
        lvShowResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long rowId) {
                onShowSelected(item, pos);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_new_shows, menu);
        return true;
    }
    
    public void onShowSelected(View item, int pos) {
        ShowInfo showToAdd = ShowsModelSingleton.getInstance().getShowInfoSearchResults().get(pos);
        
        //if show is not already in my shows list, then add it and go back to main view
        if(showToAdd != null && !ShowsModelSingleton.getInstance().getShowIds().contains(showToAdd.getId())) {
            ShowsModelSingleton.getInstance().addShowInfo(showToAdd);
            
            // closes the activity, returns to parent
            this.finish();
        }
    }

}
