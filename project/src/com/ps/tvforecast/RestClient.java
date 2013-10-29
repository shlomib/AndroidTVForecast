package com.ps.tvforecast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.net.Uri;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ps.tvforecast.models.ShowInfo;
import com.ps.tvforecast.models.ShowsModelSingleton;
import com.ps.tvforecast.parsers.MyShowParser;

public class RestClient {
    
	private final String TvRageError = "max_user_connections";
	private final int TvRageErrorNumRetries = 3;
	public static final String REST_URL = "http://services.tvrage.com/feeds/";

	public void getAndHandleClientReponse(String apiUrl, AsyncHttpResponseHandler responseHndlr) {
		AsyncHttpClient client = new AsyncHttpClient();
		Log.d("DEBUG", "apiUrl is " + apiUrl);
		client.get( apiUrl, responseHndlr);
		
	}


	public void getShows(List<String> showIdList) {
		for(String showId : showIdList) {
		    getLatestShowInfoWithEpisodeDetails(showId);
		}
	}
	
	/*
	 * EX: http://services.tvrage.com/feeds/episodeinfo.php?sid=8052
	 
    	<show id="8511">
            <name>The Big Bang Theory</name>
            <link>http://www.tvrage.com/The_Big_Bang_Theory</link>
            <started>2007-09-24</started>
            <ended/>
            <country>USA</country>
            <status>Returning Series</status>
            <classification>Scripted</classification>
            <genres>
                <genre>Comedy</genre>
            </genres>
            <airtime>Thursday at 08:00 pm</airtime>
            <runtime>30</runtime>
            <latestepisode>
                <number>07x05</number>
                <title>The Workplace Proximity</title>
                <airdate>2013-10-17</airdate>
            </latestepisode>
            <nextepisode>
                <number>07x06</number>
                <title>The Romance Resonance</title>
                <airdate>2013-10-24</airdate>
                <airtime format="RFC3339">2013-10-24T20:00:00-4:00</airtime>
                <airtime format="GMT+0 NODST">1382652000</airtime>
            </nextepisode>
        </show>
    */
	
	public void getLatestShowInfoWithEpisodeDetails(final String showId) {
		String apiUrl = REST_URL + "episodeinfo.php?sid=" + showId;		
		AsyncHttpResponseHandler respHandlr = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response)  {
				Log.d("DEBUG", "getLatestShowInfoWithEpisodeDetails::onSuccess() - " + response);

				if(response.contains(TvRageError)) {
					if(ShowsModelSingleton.getInstance().getErrorCountForShowId(showId) < TvRageErrorNumRetries) {
						ShowsModelSingleton.getInstance().incrementErrorCountForShowId(showId);
						Log.d("DEBUG", "Show api request for showId: " + showId + " got error " + ShowsModelSingleton.getInstance().getErrorCountForShowId(showId));
						getLatestShowInfoWithEpisodeDetails(showId);
					}
					else {
						Log.d("DEBUG", "ERROR: Exceeding retried for showId: " + showId);
						return;
						
					}
				}
				else {
					Log.d("DEBUG", "Show api request succeeded, clearing map for showId: " + showId);
					ShowsModelSingleton.getInstance().clearErrorCountForShowId(showId);
				}

				InputStream stream = null;
				try {
					MyShowParser showParser = new MyShowParser();
					InputStream in = new ByteArrayInputStream(response.getBytes());
					ShowInfo newShowInfo = showParser.parseShow(in);
					newShowInfo.setId(showId);
					newShowInfo.printProperties();
					ShowsModelSingleton.getInstance().updateShowInfo(newShowInfo);

				} catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					if (stream != null) {
						try {
							stream.close();
						} catch(IOException ie) {
						    ie.printStackTrace();
						}
					}
				}
			}

			@Override
			public void onFailure(Throwable error) {
				Log.d("DEBUG", "getLatestShowInfoWithEpisodeDetails::onFailure() - " + error.getMessage());
				error.printStackTrace();
				ShowsModelSingleton.getInstance().clearErrorCountForShowId(showId);
			}
		};
		getAndHandleClientReponse(apiUrl, respHandlr);

	}
	
	/*
     * EX: http://services.tvrage.com/feeds/episodeinfo.php?sid=8052
     
        <Results>
            <show>
                <showid>25056</showid>
                <name>The Walking Dead</name>
                <link>http://www.tvrage.com/The_Walking_Dead</link>
                <country>US</country>
                <started>2010</started>
                <ended>0</ended>
                <seasons>4</seasons>
                <status>Returning Series</status>
                <genres>
                    <genre>Action</genre>
                    <genre>Drama</genre>
                    <genre>Horror/Supernatural</genre>
                    <genre>Thriller</genre>
                </genres>
            </show>
            <show>
                <showid>8052</showid>
                <name>Walking The Bible</name>
                <link>http://www.tvrage.com/shows/id-8052</link>
                <country>US</country>
                <started>2006</started>
                <ended>2006</ended>
                <seasons>1</seasons>
                <status>Canceled/Ended</status>
                <classification>Documentary</classification>
                <genres>
                    <genre>Mystery</genre>
                </genres>
            </show>
        </results>
    */
    
    public void searchForShow(final String queryStr) {
        String apiUrl = REST_URL + "search.php?show=" + Uri.encode(queryStr);
        AsyncHttpResponseHandler respHandlr = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response)  {
                Log.d("DEBUG", "searchForShow::onSuccess()" + response);

                if(response.contains(TvRageError)) {
                    if(ShowsModelSingleton.getInstance().getErrorCountForShowId(queryStr) < TvRageErrorNumRetries) {
                        ShowsModelSingleton.getInstance().incrementErrorCountForShowId(queryStr);
                        Log.d("DEBUG", "Show api request search for show [" + queryStr + "] got error " + ShowsModelSingleton.getInstance().getErrorCountForShowId(queryStr));
                        searchForShow(queryStr);
                    }
                    else {
                        Log.d("DEBUG", "ERROR: Exceeding retried for search show: " + queryStr);
                        return;
                    }
                }
                else {
                    Log.d("DEBUG", "Show api request succeeded, clearing map for search show" + queryStr);
                    ShowsModelSingleton.getInstance().clearErrorCountForShowId(queryStr);
                }

                InputStream stream = null;
                try {
                    MyShowParser showParser = new MyShowParser();
                    InputStream in = new ByteArrayInputStream(response.getBytes());
                    List<ShowInfo> foundShowInfos = showParser.parseResults(in);
                    Log.d("DEBUG", "searchForShow::onSuccess() " + String.valueOf(foundShowInfos.size()));
                    ShowsModelSingleton.getInstance().getSearchShowResultsArrayAdapter().clear();
                    ShowsModelSingleton.getInstance().getSearchShowResultsArrayAdapter().addAll(foundShowInfos);

                }catch(Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        }catch(IOException ie) { ie.printStackTrace();}
                    }
                }
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d("DEBUG", "searchForShow::onFailure() " + error.getMessage());
                error.printStackTrace();
                ShowsModelSingleton.getInstance().clearErrorCountForShowId(queryStr);
            }
        };
        getAndHandleClientReponse(apiUrl, respHandlr);

    }

	public void updateShowInfo(ShowInfo showInfo) {
		Log.d("DEBUG", "In updateShowInfo!!!");
		showInfo.printProperties();
	}

}
