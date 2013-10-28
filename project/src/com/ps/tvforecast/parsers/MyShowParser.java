package com.ps.tvforecast.parsers;

/*
 * This code is taken from the example code at http://developer.android.com/training/basics/network-ops/xml.html
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.ps.tvforecast.models.ShowInfo;

/**
 * This class parses XML feeds from stackoverflow.com.
 * Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (post) in the XML feed.
 */
public class MyShowParser {
    private static final String ns = null;
   
    private static final String SHOW_ID_TAG = "showid";
    public static final String RESULTS_TAG = "Results";
    public static final String SHOW_TAG = "show";
    
    private static final String SHOW_LATEST_EPISODE_TAG = "latestepisode";
    private static final String SHOW_NEXT_EPISODE_TAG = "nextepisode";
    private static final String SHOW_EPISODE_DATE_TAG = "airdate";
    // TODO Pri extract based on attribute
    //private static final String SHOW_EPISODE_TIME_TAG = "airtime";
    private static final String SHOW_EPISODE_TITLE_TAG = "title";
    private static final String SHOW_EPISODE_NUMBER_TAG = "number";
    
    //all the tags in Show XML that are simple elements
    // Ref http://services.tvrage.com/feeds/episodeinfo.php?sid=8511
    private Set<String> simpleShowElementTags = new HashSet<String>( 
    		Arrays.asList(new String[] {
    				ShowInfo.SHOW_NAME,ShowInfo.SHOW_STATUS,ShowInfo.SHOW_COUNTRY, ShowInfo.SHOW_STARTED, ShowInfo.SHOW_LINK, ShowInfo.SHOW_ENDED
    		})
    		);
    
    private Set<String> episodeElementTags = new HashSet<String>( 
    		Arrays.asList(new String[] {
    				SHOW_EPISODE_DATE_TAG,
    				//SHOW_EPISODE_TIME_TAG, // TODO @ Pri uncomment later to get time
    				SHOW_EPISODE_TITLE_TAG, 
    				SHOW_EPISODE_NUMBER_TAG
    		})
    		);
    

    // We don't use namespaces
    public ShowInfo parseShow(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readShow(parser);
        } finally {
            in.close();
        }
    }
    
 // We don't use namespaces
    public List<ShowInfo> parseResults(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readResultsShowList(parser);
        } finally {
            in.close();
        }
    }

    // Parses the contents of a results list of show entities. If it encounters a show hands it
    // off
    // to its respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private List<ShowInfo> readResultsShowList(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, RESULTS_TAG);
        List<ShowInfo> showInfos = new ArrayList<ShowInfo>();
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
    
            // read the show info
            if(elementName.equals(SHOW_TAG)) {
                showInfos.add(readShow(parser));
            }
            else {
                skip(parser);
            }
        }
        
        return showInfos;
    }
    
    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private ShowInfo readShow(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, SHOW_TAG);
		Map<String, String> properties = new HashMap<String, String>();
		
		while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
        
            String elementName = parser.getName();
            
            //this will read the show id and store it appropriately
            if(parser.getName().equalsIgnoreCase(SHOW_TAG)) {
                properties.put(ShowInfo.SHOW_ID, parser.getAttributeValue(ns, ShowInfo.SHOW_ID));
            }
            //This will read and put all the simple tags for Show info
            else if (  simpleShowElementTags.contains(elementName) ) {
                String value = readString(parser, elementName);
                properties.put(elementName, value);
            }
            // read all the properties related to  latest and next episodes separately since they are complex XML nodes
            else if(elementName.equals(SHOW_LATEST_EPISODE_TAG) || elementName.equals(SHOW_NEXT_EPISODE_TAG) ) {
            	Map<String, String> episodeProperties = readEpisodeProperties(elementName, episodeElementTags, parser);
            	properties.putAll(episodeProperties);
            }
            else {
                skip(parser);
            }
        }
        return new ShowInfo(properties);
    }
    
    // Processes title tags in the feed.
    private String readString(XmlPullParser parser, String elName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, elName);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, elName);
        return title;
    }
    
    private Map<String, String> readEpisodeProperties(String episodeType,
    		Set<String> episodeProperties, XmlPullParser parser) 
    				throws IOException, XmlPullParserException  {
    	Map<String, String> retProperties = new HashMap<String, String> ();
    	
		
        parser.require(XmlPullParser.START_TAG, ns, episodeType);
		Map<String, String> properties = new HashMap<String, String>();
		
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elementName = parser.getName();
            //This will read and put all the simple tags for Show info
            if (  episodeProperties.contains(elementName) ) {
                String value = readString(parser, elementName);
                //key is episodeType + "_" + elementName
                String keyName = new StringBuilder().append(episodeType).append("_").append(elementName).toString();
                retProperties.put(keyName, value);
            }
            else {
                skip(parser);
            }
        }
    	return retProperties;
    }
/*
    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")) {
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

*/
    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                    depth--;
                    break;
            case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
