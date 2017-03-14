package com.app.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceHandler {

	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;

	public ServiceHandler() {

	}

	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * */
	public String makeServiceCall(String url, int method) {
		return this.makeServiceCall(url, method, null);
	}

	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * @params - http request params
	 * */
	public String makeServiceCall(String url, int method,
			String params) {

		if (method == POST) {
			try {
				//	String url1="http://cleanmycar.okcygenio.com/cleanmycar/index.php/getWashRequests";

				URL object = new URL(url);

				HttpURLConnection conn = (HttpURLConnection) object.openConnection();

				conn.setDoOutput(true);

				conn.setDoInput(true);

				conn.setRequestProperty("Content-Type", "application/json");

				conn.setRequestProperty("Accept", "application/json");

				conn.setRequestMethod("POST");

				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

				wr.write(params);

				wr.flush();

				//display what returns the POST request

				StringBuilder sb = new StringBuilder();

				int HttpResult = conn.getResponseCode();

				if (HttpResult == HttpURLConnection.HTTP_OK) {

					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

					String line = null;

					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}

					br.close();

					System.out.println("response" + sb.toString());

				} else {
					System.out.println(conn.getResponseMessage());
				}
				response = sb.toString();


			} catch (Exception e) {
				e.printStackTrace();
			}

			return response;
		}else if(method==GET){

				try {
					sendGet(url);
				}catch(Exception e){
					e.printStackTrace();

				}
			}
		
		
		
		return response;
		
	}


	private void sendGet(String myurl) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpEntity httpEntity = null;
		HttpResponse httpResponse = null;


		String url = myurl;



		HttpGet httpGet = new HttpGet(url);

		httpResponse = httpClient.execute(httpGet);


		httpEntity = httpResponse.getEntity();
		response = EntityUtils.toString(httpEntity);



	}
}
