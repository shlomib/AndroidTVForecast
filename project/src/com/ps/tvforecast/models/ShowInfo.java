package com.ps.tvforecast.models;

import java.io.Serializable;
import java.util.HashMap;
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
    
    public String getNextEpisodeNumber() {
        return getPropertyByName(SHOW_NEXT_EPISODE_NUMBER);
    }
    
    public String getNextEpisodeDate() {
        return getPropertyByName(SHOW_NEXT_EPISODE_DATE);
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
    	String ret = "";
    	String showName = this.getPropertyByName(SHOW_NAME);
    	String nextExpisodeNum =  this.getPropertyByName(SHOW_NEXT_EPISODE_NUMBER);
    	String nextExpisodeTitle = this.getPropertyByName(SHOW_NEXT_EPISODE_TITLE);
    	String nextEpisodeDate = this.getPropertyByName(SHOW_NEXT_EPISODE_DATE);
    	if(showName!=null) {
    	ret += showName + "\n";
    	}
    	if(nextExpisodeNum!=null) {
    	ret += nextExpisodeNum + "\n";
    	}
    	if(nextExpisodeTitle!=null) {
    		ret += nextExpisodeTitle + "\n";
    	}
    	if(nextEpisodeDate!=null) {
    		ret += SHOW_NEXT_EPISODE_DATE;
    	}
    	return ret;
    	
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
    
    
}
