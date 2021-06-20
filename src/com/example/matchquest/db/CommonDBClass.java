package com.example.matchquest.db;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.example.matchquest.common.TeamQuestConstants;

public class CommonDBClass {
	
//	private static String url = "http://192.168.0.104:3000";// net202
	private static String url = "http://192.168.1.10:3000";//sathish
//	private static String url = "http://192.168.1.2:3000";//netgear
//	private static String url = "http://192.168.137.229:3000";
	
	
	public static HttpURLConnection getDBConnection(String urlPattern,JSONObject json, String param,String callMethod) 
	{
		HttpURLConnection urlConnection = null;
		try{
		URL urlType = new URL(url + urlPattern);
		urlConnection = (HttpURLConnection) urlType.openConnection();
		
		urlConnection.setConnectTimeout(15000);

		//below lines not neccessary for GET method upto(DoOutput())
		
		if(callMethod.equals(CommonDBConstants.postMethod_key))
		{
		urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); 
		urlConnection.setReadTimeout(10000);
		
		urlConnection.setRequestMethod(CommonDBConstants.postMethod_key);
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(true);
		if(json != null)
		{
		OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
		writer.write(json.toString());

		  // Close streams and disconnect.
		  writer.close();
		  out.close();
		}
			
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		    return urlConnection;
	}
	
	public static String readStream(InputStream in) {
		  BufferedReader reader = null;
		  StringBuffer response = new StringBuffer();
		  try {
		    reader = new BufferedReader(new InputStreamReader(in));
		    String line = "";
		    while ((line = reader.readLine()) != null) {
		      response.append(line);
		    }
		  } catch (IOException e) {
		    e.printStackTrace();
		  } finally {
		    if (reader != null) {
		      try {
		        reader.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		      }
		    }
		  }
		  return response.toString();
		} 
}
