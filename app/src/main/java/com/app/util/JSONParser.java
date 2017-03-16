package com.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.util.Log;

/**
 * Created by Kishore on 9/26/2015.
 */
public class JSONParser {

	private static JSONArray jsonArray = null;
	private static String json = "";

	/**
	 * Method sends request to the given url.
	 * 
	 * Note: URL should be complete.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public JSONArray getJSON_Response(String url) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url
					+ "response code: " + responseCode);
			System.out.println("Response Code : " + responseCode);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			json = sb.toString();
		} catch (IOException e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
			e.printStackTrace();
		}

		// try parse the string to a JSON object
		try {
			if (!json.equals("null")) {
				jsonArray = new JSONArray(json);
				Log.d("jsonArray:: ", jsonArray + "");
			} else {
				jsonArray = null;
			}

		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jsonArray;

	}

	/**
	 * Method sends request to the given url.
	 *
	 * Note: URL should be complete.
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public JSONObject getJSON_Response(String url, boolean isResultJSONObj) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url
					+ "response code: " + responseCode);
			System.out.println("Response Code : " + responseCode);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;

			System.out.println("about to execute while loop");
			while ((line = reader.readLine()) != null) {
				System.out.println("Inside while loop line: " + line);
				sb.append(line);
			}

			System.out.println("Result : " + sb.toString());

			return new JSONObject(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ArrayList jsonParser(JSONArray jsonArray, Class myClass,
								ArrayList data) {
		System.out.println("coming here to convert josn");
		Gson g = new Gson();
		JSONArray ja = jsonArray;
		try {

			for (int i = 0; i < ja.length(); i++) {
				System.out.println("coming here to convert josn" + i);
				Log.i(ja.getString(i), myClass.toString());
				data.add(g.fromJson(ja.getString(i), myClass));
				// listener.loading(i,ja.length());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return data;
	}

	public Object jsonParser(JSONObject jsonObject, Class myClass) throws JSONException {
		Gson g = new Gson();
		if(jsonObject != null) return  g.fromJson(jsonObject.toString(), myClass);
		return null;
	}
}