package com.ps.tvforecast.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.ps.tvforecast.ShowsArrayAdapter;


public class ShowsModelSingleton 
{
  private static ShowsModelSingleton instance;
  private static List<ShowInfo> showInfoList;
  private static ShowsArrayAdapter showsArrayAdapter;
   
  public String customVar;
  
  private static Map<String, Integer> mapFor2xxError = new HashMap<String, Integer>();
   
  public static void initInstance(Context c)
  {
    if (instance == null)
    {
      // Create the instance
      instance = new ShowsModelSingleton();
      showInfoList = new ArrayList<ShowInfo>();
      showsArrayAdapter = new ShowsArrayAdapter(c , showInfoList);
    }
  }
 
  public static ShowsModelSingleton getInstance()
  {
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
  
  public ShowsArrayAdapter getShowsArrayAdapter() {
	  return showsArrayAdapter;
  }
   
  private ShowsModelSingleton()
  {
    // Constructor hidden because this is a singleton
  }
  
}