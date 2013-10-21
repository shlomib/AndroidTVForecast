package com.ps.tvforecast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ps.tvforecast.models.ShowInfo;
import com.ps.tvforecast.parsers.MyShowParser;


public class RestClient {

	public static final String REST_URL = "http://services.tvrage.com/feeds/";

	public void getAndHandleClientReponse(String apiUrl, AsyncHttpResponseHandler responseHndlr) {
		AsyncHttpClient client = new AsyncHttpClient();
		Log.d("DEBUG", "apiUrl is " + apiUrl);
		client.get( apiUrl, responseHndlr);
	}

	public void getShows(List<String> showIdList) {

		String apiUrl;

		for(String showId: showIdList) {
			apiUrl = REST_URL + "episodeinfo.php?sid=" + showId;
			AsyncHttpResponseHandler respHandlr = new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					Log.d("DEBUG", "In onSuccess" + response);
					InputStream stream = null;
					try {
						MyShowParser showParser = new MyShowParser();
						InputStream in = new ByteArrayInputStream(response.getBytes());
						ShowInfo showInfo = showParser.parse(in);
						showInfo.printProperties();

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

	}
}
