package com.ps.tvforecast.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.ps.tvforecast.adapters.SearchShowResultsArrayAdapter;
import com.ps.tvforecast.adapters.ShowsArrayAdapter;
import com.ps.tvforecast.parsers.MyShowParser;


public class ShowsModelSingleton {
    
  private static ShowsModelSingleton instance;
  private static List<ShowInfo> showInfoList;
  private static ShowsArrayAdapter showsArrayAdapter;
  private static List<ShowInfo> showInfoSearchResults;
  private static SearchShowResultsArrayAdapter searchShowResultsArrayAdapter;
  private static Context myContext;
  
  public String customVar;
  
  private static Map<String, Integer> mapFor2xxError = new HashMap<String, Integer>();
   
  public static void initInstance(Context c) {
    if (instance == null) {
        myContext = c;
        
      // Create the instance
      instance = new ShowsModelSingleton();
      
      // Set up the my shows adapter
      initShowInfoList();
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
	  showsArrayAdapter.add(showInfo);
	  sortShowsBySchedule();
	  storeLocalDB("shows", getSerializedShowInfoList());
	  showsArrayAdapter.notifyDataSetChanged();
  }
  
  public void updateShowInfo(ShowInfo showInfo) {
	  showsArrayAdapter.update(showInfo);
	  sortShowsBySchedule();
	  storeLocalDB("shows", getSerializedShowInfoList());
	  showsArrayAdapter.notifyDataSetChanged();
  }
  
  public void updateShowImage(String showId, String image) {
	  ShowInfo myShowInfo = showsArrayAdapter.getShowInfo(showId);
	  myShowInfo.setPropertyByName(MyShowParser.SHOW_IMAGE_TAG,  image);
	  Log.d("DEBUG", "Updating showId " + showId + " with image " + image);
	  showsArrayAdapter.update(myShowInfo);
	  sortShowsBySchedule();
	  storeLocalDB("shows", getSerializedShowInfoList());
	  showsArrayAdapter.notifyDataSetChanged();
}
  
  public void deleteShowInfo(ShowInfo showInfo) {
      Log.d("DEBUG", "Adding ShowInfo");
      showsArrayAdapter.remove(showInfo);
      sortShowsBySchedule();
      storeLocalDB("shows", getSerializedShowInfoList());
      showsArrayAdapter.notifyDataSetChanged();
  }
  
  private void storeLocalDB(String key, String value) {
      SharedPreferences pref =   
              PreferenceManager.getDefaultSharedPreferences(myContext);
          Editor edit = pref.edit();
          edit.putString(key, value);
          edit.commit(); 
  }
  
  private static String readLocalDB(String key) {
      SharedPreferences pref =   
              PreferenceManager.getDefaultSharedPreferences(myContext);
         
      return pref.getString(key, "N/A");
  }
  
  private String getSerializedShowInfoList() {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = null;
    try {
        oos = new ObjectOutputStream(baos);
        oos.writeObject(showInfoList);
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (oos != null) {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (baos != null) {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String result = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
    Log.d("DEBUG", "our string is: " + result);
    return result;
  }
  
@SuppressWarnings("unchecked")
private static void initShowInfoList() {
      showInfoList = new ArrayList<ShowInfo>();
      
      String data = readLocalDB("shows");
      
      byte [] ba = Base64.decode(data, Base64.DEFAULT);
      
      if(!data.equalsIgnoreCase("N/A")) {
          ByteArrayInputStream bais = new ByteArrayInputStream(ba);
          ObjectInputStream ois = null;
          
          Log.d("DEBUG", "shows found in local DB");
          
        try {
          ois = new ObjectInputStream(bais);

          showInfoList = (ArrayList<ShowInfo>)ois.readObject();
        } catch (OptionalDataException e) {
            Log.d("DEBUG", "ERR1: ", e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.d("DEBUG", "ERR2: ", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("DEBUG", "ERR3: ", e);
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
      }
      else {
          Log.d("DEBUG", "no shows found in local DB");
      }
      
      sortShowsBySchedule();
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
   
  private static void sortShowsBySchedule() {
      class ShowInfoComparator implements Comparator<ShowInfo> {
          @Override
          public int compare(ShowInfo si1, ShowInfo si2) {
              return si1.compareTo(si2);
          }
      }
      
      Collections.sort(showInfoList, new ShowInfoComparator());
  }
  
  private ShowsModelSingleton() {
    // Constructor hidden because this is a singleton
  }
  
}