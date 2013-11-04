package com.ps.tvforecast.fragments;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.ps.tvforecast.R;
import com.ps.tvforecast.RestClient;
import com.ps.tvforecast.models.ShowInfo;
import com.ps.tvforecast.models.ShowsModelSingleton;

public class ShowListFragment extends Fragment {

    List<ShowInfo> showInfoList = new ArrayList<ShowInfo>();
    StickyListHeadersListView lvShowList;
    RestClient restClient = new RestClient();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_show_list, container, false);
      return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        showInfoList.clear();
        ShowsModelSingleton.initInstance(getActivity().getApplicationContext());
        restClient.getShows(ShowsModelSingleton.getInstance().getShowIds());
        lvShowList = (StickyListHeadersListView) getView().findViewById(R.id.lvShowList);
        lvShowList.setAdapter(ShowsModelSingleton.getInstance().getShowsArrayAdapter());
        
        initEventListeners();
    }
    
  private void initEventListeners() {
      lvShowList.setOnItemLongClickListener(new OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long rowId) {
              ShowInfo selectedShow = ShowsModelSingleton.getInstance().getShowInfoList().get(pos);
              ShowsModelSingleton.getInstance().deleteShowInfo(selectedShow);
              return true;
          }
      });
    }

}