package com.ps.tvforecast.models;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.ps.tvforecast.ShowsArrayAdapter;


public class ShowsModelSingleton 
{
  private static ShowsModelSingleton instance;
  private static List<ShowInfo> showInfoList;
  private static ShowsArrayAdapter showsArrayAdapter;
   
  public String customVar;
   
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