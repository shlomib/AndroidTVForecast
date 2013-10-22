package com.ps.tvforecast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ps.tvforecast.models.ShowInfo;
import com.ps.tvforecast.models.ShowsModelSingleton;
import com.ps.tvforecast.parsers.MyShowParser;
/*
private class RestClient extends AsyncTask<String, Integer, ArrayList<ShowInfo>> {
    protected Long doInBackground(URL... urls) {
        int count = urls.length;
        long totalSize = 0;
        for () {
           
            // Escape early if cancel() is called
            if (isCancelled()) break;
        }
        return totalSize;
    }

    protected void onProgressUpdate(Integer... progress) {
        setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        showDialog("Downloaded " + result + " bytes");
    }
}
*/


public class RestClient 
{
	public static final String REST_URL = "http://services.tvrage.com/feeds/";
	//List<ShowInfo> showInfoList = new ArrayList<ShowInfo>();

	public void getAndHandleClientReponse(String apiUrl, AsyncHttpResponseHandler responseHndlr) {
		AsyncHttpClient client = new AsyncHttpClient();
		Log.d("DEBUG", "apiUrl is " + apiUrl);
		client.get( apiUrl, responseHndlr);
	}


	public void getShows(List<String> showIdList) {

		String apiUrl;

		for(String showId: showIdList) {
			apiUrl = REST_URL + "episodeinfo.php?sid=" + showId;
/*
			ResponseHandler<String> respHandlr = new BasicResponseHandler();
			HttpGet getRequest = new HttpGet(apiUrl);
			HttpClient client = new DefaultHttpClient();

			String response;
			try {
				response = client.execute(getRequest, respHandlr);

				Log.d("DEBUG", "In onSuccess" + response);
				InputStream stream = null;
				try {
					MyShowParser showParser = new MyShowParser();
					InputStream in = new ByteArrayInputStream(response.toString().getBytes());
					ShowInfo showInfo = showParser.parse(in);
					showInfo.printProperties();
					showInfoList.add(showInfo);

				}catch(Exception e) {
					Log.d("DEBUG", "Exception in inner loop");
					
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

			catch(Exception e) {
				Log.d("DEBUG", "Exception in getting show" + e);
				e.printStackTrace();
			}
*/



			
			AsyncHttpResponseHandler respHandlr = new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					Log.d("DEBUG", "In onSuccess" + response);
					InputStream stream = null;
					try {
						MyShowParser showParser = new MyShowParser();
						InputStream in = new ByteArrayInputStream(response.getBytes());
						ShowInfo newShowInfo = showParser.parse(in);
						newShowInfo.printProperties();
						ShowsModelSingleton singleton = ShowsModelSingleton.getInstance();
						singleton.addShowInfo(newShowInfo);
						//updateShowInfo(newShowInfo);

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
					Log.d("DEBUG", "In onFailure" + error.getMessage());
					error.printStackTrace();
				}
			};
			getAndHandleClientReponse(apiUrl, respHandlr);
			
	
		}
		//Log.d("DEBUG", "Return the length of showInfoList" + showInfoList.size());
		//return showInfoList;
	}

	public void updateShowInfo(ShowInfo showInfo) {
		Log.d("DEBUG", "In updateShowInfo!!!");
		showInfo.printProperties();
	}
	
}

