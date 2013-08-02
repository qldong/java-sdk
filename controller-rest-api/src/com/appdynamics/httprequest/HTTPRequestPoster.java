/**
 * Copyright 2013 AppDynamics
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */




package com.appdynamics.httprequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HTTPRequestPoster
{
	/**
	 * Sends an HTTP GET request to a url
	 * 
	 * @param endpoint
	 *			- The URL of the server. (Example:
	 *			" http://www.yahoo.com/search")
	 * @param requestParameters
	 *			- all the request parameters (Example:
	 *			"param1=val1&param2=val2"). Note: This method will add the
	 *			question mark (?) to the request - DO NOT add it yourself
	 * @return - The response from the end point
	 */
	public static String sendGetRequest(String endpoint, String requestParameters)
	{
		String result = null;
		if (endpoint.startsWith("http://"))
		{
			// Send a GET request to the servlet
			try
			{
				// Send data
				String urlStr = endpoint;
				if (requestParameters != null && requestParameters.length() > 0)
				{
					urlStr += "?" + requestParameters;
				}
				URL url = new URL(urlStr);
				URLConnection conn = url.openConnection();

				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = rd.readLine()) != null)
				{
					sb.append(line);
				}
				rd.close();
				result = sb.toString();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String sendGetRequestWithAuthorization(String endpoint,
			String requestParameters, final String Login, final String Password) 
	{
		String result = null;
		
		if (endpoint.startsWith("http://"))
		{
			// Send a GET request to the servlet
			try
			{
				// Send data
				String urlStr = endpoint;
				if (requestParameters != null && requestParameters.length() > 0)
				{
					urlStr += "?" + requestParameters;
				}
				
				// Set default cookie manager
				CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
				
				// Change Default Authentication
				Authenticator.setDefault(new Authenticator()
				{
					@Override
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(Login, Password.toCharArray());
					}
				});
				
				URLConnection conn = new URL(urlStr).openConnection();
				
				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = rd.readLine()) != null)
				{
					sb.append(line);
				}
				rd.close();
				result = sb.toString();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Reads data from the data reader and posts it to a server via POST
	 * request. data - The data you want to send endpoint - The server's address
	 * output - writes the server's response to output
	 * 
	 * @throws Exception
	 */
	public static void postData(Reader data, URL endpoint, Writer output) throws Exception
	{
		HttpURLConnection urlc = null;
		try
		{
			urlc = (HttpURLConnection) endpoint.openConnection();
			try
			{
				urlc.setRequestMethod("POST");
			}
			catch (ProtocolException e)
			{
				throw new Exception("Shouldn't happen: HttpURLConnection doesn't support POST??", e);
			}
			urlc.setDoOutput(true);
			urlc.setDoInput(true);
			urlc.setUseCaches(false);
			urlc.setAllowUserInteraction(false);
			urlc.setRequestProperty("Content-type", "text/xml; charset=UTF-8");

			OutputStream out = urlc.getOutputStream();

			try
			{
				Writer writer = new OutputStreamWriter(out, "UTF-8");
				pipe(data, writer);
				writer.close();
			}
			catch (IOException e)
			{
				throw new Exception("IOException while posting data", e);
			}
			finally
			{
				if (out != null)
				{
					out.close();
				}
			}

			InputStream in = urlc.getInputStream();
			try
			{
				Reader reader = new InputStreamReader(in);
				pipe(reader, output);
				reader.close();
			}
			catch (IOException e)
			{
				throw new Exception("IOException while reading response", e);
			}
			finally
			{
				if (in != null)
				{
					in.close();
				}
			}
		}
		catch (IOException e)
		{
			throw new Exception("Connection error (is server running at "
					+ endpoint + " ?): " + e);
		}
		finally
		{
			if (urlc != null)
			{
				urlc.disconnect();
			}
		}
	}

	/**
	 * Sends an HTTP GET request to a url with authentication
	 * @param user - username for authentication
	 * @param pass - password for authentication
	 * @param url
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static String postJSONwithAuth(String user, String pass, String url, String json) throws Exception
	{
		HttpClient httpClient = new DefaultHttpClient();
		String resp ="";
		try
		{
			String loginPassword = user + ":" + pass;

			//encoding  byte array into base 64
			byte[] encoded = Base64.encodeBase64(loginPassword.getBytes());

			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(json);
			request.setHeader("content-type", "application/json");
			request.setHeader("Authorization", "Basic " + new String(encoded));
		
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			resp = response.toString();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new Exception();
		}
		finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		return resp;
	}

	/**
	 * Pipes everything from the reader to the writer via a buffer
	 */
	private static void pipe(Reader reader, Writer writer) throws IOException
	{
		char[] buf = new char[1024];
		int read = 0;
		while ((read = reader.read(buf)) >= 0)
		{
			writer.write(buf, 0, read);
		}
		writer.flush();
	}

}
