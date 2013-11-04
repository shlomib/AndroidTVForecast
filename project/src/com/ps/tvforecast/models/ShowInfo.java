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
    
    public static enum Schedule {
        TODAY(1, "Today"),
        TOMORROW(2, "Tomorrow"),
        THIS_WEEK(3, "This Week"),
        NEXT_WEEK(4, "Next Week"),
        THIS_MONTH(5, "This Month"),
        UPCOMING(6, "Upcoming"),
        TBD(7, "To Be Announced");
        
        private final int id;
        private final String label;

        private Schedule(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public int getId(){
            return id;
         }
        
        public String toString(){
           return label;
        }
    }
    
    Map<String, String> properties = new HashMap<String, String>();
    
    public Boolean isActive() {
        if(this.getStatus().toUpperCase(Locale.US).contains("ENDED") ||
            this.getStatus().toUpperCase(Locale.US).contains("CANCELED") ||
            this.getStatus().toUpperCase(Locale.US).contains("REJECTED")) {
            return false;
        }
        
        return true;
    }
    
    public Boolean isTBD() {
        return this.getStatus().toUpperCase(Locale.US).contains("TBD");
    }
    
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
    
    public String getNextEpisodeTimeMillis() {
        return getPropertyByName(SHOW_NEXT_EPISODE_TIME_GMT);
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
    
    public String getNextEpisodeDateAsLabel() {
        String nextEpisodeDateStr = "";
        
        Date nextEpisodeDate = getActualNextEpisodeDate();
        if(nextEpisodeDate != null) {
            nextEpisodeDateStr = new SimpleDateFormat("EEEE, MMMM d", Locale.US).format(nextEpisodeDate);
        }
        
        return nextEpisodeDateStr;
    }
    
    public String getNextEpisodeTimeAsLabel() {
        String nextEpisodeTimeStr = "";
        
        Date nextEpisodeDate = getActualNextEpisodeDate();
        if(nextEpisodeDate != null) {
            nextEpisodeTimeStr = new SimpleDateFormat("h:mm a", Locale.US).format(nextEpisodeDate);
        }
        
        return nextEpisodeTimeStr;
    }
    
    public long getNextEpisodeDaysLeft() {
        if(this.getNextEpisodeTime() == null || this.getNextEpisodeTime().equalsIgnoreCase("")) {
            return -1;
        }
        
        Date now = new Date();
        Date nextEpisodeDate = getActualNextEpisodeDate();
        
        if(nextEpisodeDate != null) {
            return getDateDiff(now, nextEpisodeDate);
        }
        
        return -1;
    }
    
    public Date getActualNextEpisodeDate() {
        Date nextEpisodeDate = null;
        try {
            nextEpisodeDate = parseRFC3339Date(this.getNextEpisodeTime());
        } catch (IndexOutOfBoundsException obe) {
            obe.printStackTrace();
            Log.d("ERROR", obe.getMessage());
        } catch (ParseException pe) {
            pe.printStackTrace();
            Log.d("ERROR", pe.getMessage());
        }
        
        return nextEpisodeDate;
    }
    
    public Schedule getSchedule() {
        Date now = new Date();
        Date nextEpisodeDate = getActualNextEpisodeDate();
        
        if(nextEpisodeDate == null) {
            return Schedule.TBD;
        }
        else if(getNextEpisodeDaysLeft() == 0) {
            return Schedule.TODAY;
        }
        else if(getNextEpisodeDaysLeft() == 1) {
            return Schedule.TOMORROW;
        }
        else if((this.getYearForDate(now) == this.getYearForDate(nextEpisodeDate)) &&
                (this.getWeekOfYearForDate(now) == this.getWeekOfYearForDate(nextEpisodeDate))) {
            return Schedule.THIS_WEEK;
        }
        else if(((this.getYearForDate(now) == this.getYearForDate(nextEpisodeDate)) &&
                ((this.getWeekOfYearForDate(now)+1) == this.getWeekOfYearForDate(nextEpisodeDate))) ||
                ((this.getYearForDate(now)+1 == this.getYearForDate(nextEpisodeDate)) &&
                ((this.getWeekOfYearForDate(now)-52) == this.getWeekOfYearForDate(nextEpisodeDate)))) {
            return Schedule.NEXT_WEEK;
        }
        else if(this.getMonthForDate(now) == this.getMonthForDate(nextEpisodeDate)) {
            return Schedule.THIS_MONTH;
        }
        else if(!this.isTBD()) {
            return Schedule.UPCOMING;
        }
        
        return Schedule.TBD;
    }
    
    public String getAsString() {
        StringBuilder ret = new StringBuilder();
    	String showName = this.getName();
    	String nextExpisodeNum =  this.getNextEpisodeNumber();
    	String nextExpisodeTitle = this.getNextEpisodeTitle();
    	
    	String nextEpisodeDate = this.getNextEpisodeDateAsLabel();
    	String nextEpisodeTime = "";
    	
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
    
    private static Date parseRFC3339Date(String datestring) throws java.text.ParseException, IndexOutOfBoundsException {
        if(datestring == null) {
            return null;
        }
        
        Date d = new Date();

        //if there is no time zone, we don't need to do any special parsing.
        if(datestring.endsWith("Z")){
            try{
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);//spec for RFC3339                    
                d = s.parse(datestring);
            }
            catch(java.text.ParseException pe){//try again with optional decimals
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US);//spec for RFC3339 (with fractional seconds)
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
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);//spec for RFC3339      
        try {
            d = s.parse(datestring);
        }
        catch(java.text.ParseException pe){//try again with optional decimals
            s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", Locale.US);//spec for RFC3339 (with fractional seconds)
            s.setLenient(true);
            d = s.parse(datestring);
        }
        return d;
    }
    
    private long getDateDiff(Date dateOne, Date dateTwo) {
        
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;

        return delta;
    }
    
    private int getMonthForDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }
    
    private int getYearForDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    
    private int getWeekOfYearForDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    public int compareTo(ShowInfo si) {
        String myNextEpisodeTime = getNextEpisodeTimeMillis();
        String otherNextEpisodeTime = si.getNextEpisodeTimeMillis();
        
        if(myNextEpisodeTime == null || myNextEpisodeTime.equalsIgnoreCase("")) {
            return 1;
        }
        if(otherNextEpisodeTime == null || otherNextEpisodeTime.equalsIgnoreCase("")) {
            return -1;
        }
         return(Integer.parseInt(myNextEpisodeTime) - Integer.parseInt(otherNextEpisodeTime));
    }
}
