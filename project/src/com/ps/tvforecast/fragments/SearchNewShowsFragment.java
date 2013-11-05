package com.ps.tvforecast.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ps.tvforecast.R;
import com.ps.tvforecast.RestClient;
import com.ps.tvforecast.models.ShowInfo;
import com.ps.tvforecast.models.ShowsModelSingleton;

public class SearchNewShowsFragment extends Fragment {

    RestClient restClient = new RestClient();
    List<ShowInfo> showInfoResults = new ArrayList<ShowInfo>();
    EditText etShowName;
    Button btnSearch;
    ListView lvShowResults;
    public ProgressBar pbSearchProgress;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_search_new_shows, container, false);
      return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        etShowName = (EditText) getView().findViewById(R.id.etShowName);
        btnSearch = (Button) getView().findViewById(R.id.btnSearch);
        lvShowResults = (ListView) getView().findViewById(R.id.lvShowResults);
        pbSearchProgress = (ProgressBar) getView().findViewById(R.id.pbSearchProgress);
        
        ShowsModelSingleton.getInstance().getSearchShowResultsArrayAdapter().clear();
        lvShowResults.setAdapter(ShowsModelSingleton.getInstance().getSearchShowResultsArrayAdapter());
        
        pbSearchProgress.setVisibility(View.INVISIBLE);
        
        initEventListeners();
    }
    
    private void initEventListeners() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String showName = etShowName.getText().toString();
                
                if(!showName.equals("")) {
                    pbSearchProgress.setVisibility(View.VISIBLE);
                    ShowsModelSingleton.getInstance().getSearchShowResultsArrayAdapter().clear();
                    restClient.searchForShow(showName, pbSearchProgress);
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
    
    public void onShowSelected(View item, int pos) {
        ShowInfo showToAdd = ShowsModelSingleton.getInstance().getShowInfoSearchResults().get(pos);
        StringBuilder sb = new StringBuilder();
        
        if(showToAdd != null && showToAdd.getId() != null) {
            //if show is not already in my shows list, then add it and go back to main view
            if(!ShowsModelSingleton.getInstance().getShowIds().contains(showToAdd.getId())) {
                if(showToAdd.isActive()) {
                    ShowsModelSingleton.getInstance().addShowInfo(showToAdd);
                    ShowsModelSingleton.getInstance().getSearchShowResultsArrayAdapter().clear();
                    restClient.getLatestShowInfoWithEpisodeDetails(showToAdd.getId());
                    restClient.getLatestShowInfoWithImage(showToAdd.getId());
                    Toast.makeText(getActivity().getApplicationContext(), "Show " +showToAdd.getId() +"  was added", 
                            Toast.LENGTH_SHORT).show();
                    
                    // closes the activity, returns to parent
                    getActivity().finish();
                }
                else {
                    sb.append("\"").append(showToAdd.getName()).append("\" has ended. Please select another show.");
                    Toast.makeText(getActivity().getApplicationContext(), sb.toString(), 
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                sb.append("\"").append(showToAdd.getName()).append("\" is already in your list. Please select another show.");
                Toast.makeText(getActivity().getApplicationContext(), sb.toString(), 
                        Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Unable to add show...", 
                    Toast.LENGTH_SHORT).show();
        }
        
    }
}
