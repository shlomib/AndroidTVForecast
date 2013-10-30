package com.ps.tvforecast.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.util.Log;

// This class represents a single entry (post) in the XML feed.
// Model for show XML : http://services.tvrage.com/feeds/episodeinfo.php?sid=8511
// It includes the data members for show
public  class ShowInfo implements Serializable {
	
    private static final long serialVersionUID = 8149147469720945079L;
    
    //properties of show
    public static final String SHOW_ID = "id";
    public static final String SHOW_NAME = "name";
    public static final String SHOW_STATUS = "status";
    public static final String SHOW_COUNTRY = "country";
    public static final String SHOW_STARTED = "started";
    public static final String SHOW_LINK = "link";
    public static final String SHOW_ENDED = "ended";
    
    public static final String SHOW_LATEST_EPISODE_DATE = "latestepisode_airdate";
    public static final String SHOW_LATEST_EPISODE_TITLE = "latestepisode_title";
    public static final String SHOW_LATEST_EPISODE_NUMBER = "latestepisode_number";
    
    public static final String SHOW_NEXT_EPISODE_DATE = "nextepisode_airdate";
    public static final String SHOW_NEXT_EPISODE_TIME_RFC = "nextepisode_airtime_RFC";
    public static final String SHOW_NEXT_EPISODE_TIME_GMT = "nextepisode_airtime_GMT";
    public static final String SHOW_NEXT_EPISODE_TITLE = "nextepisode_title";
    public static final String SHOW_NEXT_EPISODE_NUMBER = "nextepisode_number";
    
    //TODO @ Pri extract <airtime format="GMT+0 NODST">1382652000</airtime> to get time
    
    Map<String, String> properties = new HashMap<String, String>();
    
    public String getId() {
    	return getPropertyByName(SHOW_ID);
    }
    
    public void setId(String id) {
    	setPropertyByName(SHOW_ID, id);
    }
    
    public String getName() {
        return getPropertyByName(SHOW_NAME);
    }
    
    public String getStatus() {
        return getPropertyByName(SHOW_STATUS);
    }
    
    public String getCountry() {
        return getPropertyByName(SHOW_COUNTRY);
    }
    
    public String getStarted() {
        return getPropertyByName(SHOW_STARTED);
    }
    
    public String getNextEpisodeTitle() {
        return getPropertyByName(SHOW_NEXT_EPISODE_TITLE);
    }
    
    public String getNextEpisodeNumber() {
        return getPropertyByName(SHOW_NEXT_EPISODE_NUMBER);
    }
    
    public String getNextEpisodeDate() {
        return getPropertyByName(SHOW_NEXT_EPISODE_DATE);
    }
    
    public String getNextEpisodeTime() {
        return getPropertyByName(SHOW_NEXT_EPISODE_TIME_RFC);
    }
    
    public ShowInfo(Map<String, String> properties) {
    	this.properties = properties;
    }
    
    public String getPropertyByName(String propertyName) {
    	if( properties!=null && properties.containsKey(propertyName)) {
    		return properties.get(propertyName);
    	}
    	else return null;
    }
    
    public void setPropertyByName(String property, String value) {
    	properties.put(property, value);
    }
    
    public void setProperties(Map<String, String> properties) {
    	this.properties = properties;
    }
    
    public boolean hasProperty(String property) {
    	return (properties.containsKey(property) && properties.get(property)!=null);
    }
    
    public void printProperties() {
    	Log.d("DEBUG", "Printing properties");
    	for( String property : properties.keySet()) {
    		Log.d("DEBUG", "key : " + property + " value: " + properties.get(property));
    	}
    }
    
    public String getAsString() {
        StringBuilder ret = new StringBuilder();
    	String showName = this.getName();
    	String nextExpisodeNum =  this.getNextEpisodeNumber();
    	String nextExpisodeTitle = this.getNextEpisodeTitle();
    	//String nextEpisodeTime = this.getNextEpisodeTime();
    	
    	String nextEpisodeDate;;
    	String nextEpisodeTime;
    	
    	try {
    	    //nextEpisodeTime = new SimpleDateFormat("dd/MM/yyyy h:mm:ss a", Locale.US).format(new java.util.Date(Long.parseLong(this.getNextEpisodeTime()))); 
    	    //nextEpisodeTime = parseRFC3339Date(this.getNextEpisodeTime()).toString();
    	    Date parsedDate = parseRFC3339Date(this.getNextEpisodeTime());
    	    if(parsedDate != null) {
    	        nextEpisodeDate = new SimpleDateFormat("EEEE, MMMM d h:mm a", Locale.US).format(parsedDate);
    	    }
    	    else {
    	        nextEpisodeDate = "";
    	    }
    	} catch (IndexOutOfBoundsException obe) {
    	    obe.printStackTrace();
    	    Log.d("ERROR", obe.getMessage());
    	    nextEpisodeDate = "";
        } catch (ParseException pe) {
            pe.printStackTrace();
            Log.d("ERROR", pe.getMessage());
            nextEpisodeDate = "";
        }
    	
    	//nextEpisodeTime = getTimeLabel(this.getNextEpisodeTime());
    	//nextEpisodeTime = getTimeLabel(getPropertyByName(SHOW_NEXT_EPISODE_TIME_RFC));
    	nextEpisodeTime = "";
    	
    	if(showName!=null) {
    	    ret.append(showName + "\n");
    	}
    	if(nextExpisodeNum!=null) {
    	    ret.append(nextExpisodeNum + "\n");
    	}
    	if(nextExpisodeTitle!=null) {
    	    ret.append(nextExpisodeTitle + "\n");
    	}
    	if(nextEpisodeDate!=null) {
    	    ret.append(nextEpisodeDate + "\n");
    	}
        if(nextEpisodeTime!=null) {
            ret.append(nextEpisodeTime + "\n");
        }
    	return ret.toString();
    	
    }
    
   
    public void updateShowInfo(ShowInfo showInfo) {
    	Map<String, String> showInfoProperties = showInfo.properties;
    	Log.d("DEBUG", "Trying to update to id " + showInfo.getId());
    	for(String key:showInfoProperties.keySet()) {
    		if(!key.equalsIgnoreCase("SHOW_ID")) {
    			this.properties.put(key, showInfoProperties.get(key));
    			Log.d("DEBUG", "Copy " +  showInfoProperties.get(key));
    		}
    	}
    }
    
    private String getTimeLabel(String datestring) {
        Calendar c0 = Calendar.getInstance(); // today
        
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date(datestring)); // your date

        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
          && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
            return "YESTERDAY";
        }
        else if (c0.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c0.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                  return "TODAY";
              }
        else {
            return "N/A";
        }
    }
    
    private static Date parseRFC3339Date(String datestring) throws java.text.ParseException, IndexOutOfBoundsException {
        if(datestring == null) {
            return null;
        }
        
        Date d = new Date();

        //if there is no time zone, we don't need to do any special parsing.
        if(datestring.endsWith("Z")){
          try{
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");//spec for RFC3339                    
            d = s.parse(datestring);          
          }
          catch(java.text.ParseException pe){//try again with optional decimals
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");//spec for RFC3339 (with fractional seconds)
            s.setLenient(true);
            d = s.parse(datestring);          
          }
          return d;
        }

             //step one, split off the timezone. 
        String firstpart = datestring.substring(0,datestring.lastIndexOf('-'));
        String secondpart = datestring.substring(datestring.lastIndexOf('-'));
            
              //step two, remove the colon from the timezone offset
        secondpart = secondpart.substring(0,secondpart.indexOf(':')) + secondpart.substring(secondpart.indexOf(':')+1);
        datestring  = firstpart + secondpart;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");//spec for RFC3339      
        try{
          d = s.parse(datestring);        
        }
        catch(java.text.ParseException pe){//try again with optional decimals
          s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");//spec for RFC3339 (with fractional seconds)
          s.setLenient(true);
          d = s.parse(datestring);        
        }
        return d;
      }
    
}
