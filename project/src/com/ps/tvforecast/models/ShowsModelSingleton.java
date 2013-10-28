package com.ps.tvforecast.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.ps.tvforecast.SearchShowResultsArrayAdapter;
import com.ps.tvforecast.ShowsArrayAdapter;


public class ShowsModelSingleton {
    
  private static ShowsModelSingleton instance;
  private static List<ShowInfo> showInfoList;
  private static ShowsArrayAdapter showsArrayAdapter;
  private static List<ShowInfo> showInfoSearchResults;
  private static SearchShowResultsArrayAdapter searchShowResultsArrayAdapter;
   
  public String customVar;
  
  private static Map<String, Integer> mapFor2xxError = new HashMap<String, Integer>();
   
  public static void initInstance(Context c) {
    if (instance == null) {
      // Create the instance
      instance = new ShowsModelSingleton();
      
      // Set up the my shows adapter
      showInfoList = new ArrayList<ShowInfo>();
      showsArrayAdapter = new ShowsArrayAdapter(c , showInfoList);
      
      // Set up the search shows results adapter
      showInfoSearchResults = new ArrayList<ShowInfo>();
      searchShowResultsArrayAdapter = new SearchShowResultsArrayAdapter(c , showInfoSearchResults);
    }
  }
 
  public static ShowsModelSingleton getInstance() {
    // Return the instance
    return instance;
  }
  
  public void addShowInfo(ShowInfo showInfo) {
	  Log.d("DEBUG", "Adding ShowInfo");
	  //showInfoList.add(showInfo);
	  showsArrayAdapter.add(showInfo);
  }
  
  public void updateShowInfo(ShowInfo showInfo) {
	  showsArrayAdapter.update(showInfo);
  }
  
  public Integer getErrorCountForShowId(String showId) {
	 
	  return  (mapFor2xxError.containsKey(showId)? mapFor2xxError.get(showId): 0);
  }
  
  public void incrementErrorCountForShowId(String showId) {
	  int count = mapFor2xxError.containsKey(showId) ?  mapFor2xxError.get(showId).intValue(): 0;
	  count++;
	  mapFor2xxError.put(showId, count);
  }
  
  public void clearErrorCountForShowId(String showId) {
	  mapFor2xxError.remove(showId);
  }
  
  public List<ShowInfo> getShowInfoList() {
	  return showInfoList;
  }
  
  public List<ShowInfo> getShowInfoSearchResults() {
      return showInfoSearchResults;
  }
  
  public List<String> getShowIds() {
      List<String> ids = new ArrayList<String>();
      
      for(ShowInfo si : showInfoList) {
          if(si.getId() != null) {
              ids.add(si.getId());
          }
      }
      
      return ids;
  }
  
  public ShowsArrayAdapter getShowsArrayAdapter() {
	  return showsArrayAdapter;
  }
  
  public SearchShowResultsArrayAdapter getSearchShowResultsArrayAdapter() {
      return searchShowResultsArrayAdapter;
  }
   
  private ShowsModelSingleton() {
    // Constructor hidden because this is a singleton
  }
  
}