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




package com.appdynamics.REST;

import java.util.ArrayList;

import com.appdynamics.JSON.JSONArray;
import com.appdynamics.JSON.JSONException;
import com.appdynamics.JSON.JSONObject;
import com.appdynamics.TypeREST.ADApplication;
import com.appdynamics.TypeREST.ADBusinessTransaction;
import com.appdynamics.TypeREST.ADEntity;
import com.appdynamics.TypeREST.ADEvent;
import com.appdynamics.TypeREST.ADMetric;
import com.appdynamics.TypeREST.ADNode;
import com.appdynamics.TypeREST.ADTier;
import com.appdynamics.httprequest.HTTPRequestPoster;

public class RESTToolkit
{
	private ArrayList<ADApplication> applications;
	private ArrayList<ADBusinessTransaction> BTs;
	private ArrayList<ADMetric> metrics;
	private ArrayList<ADNode> nodes;
	private ArrayList<ADTier> tiers;
	private ArrayList<ADEvent> events;

	private String rawJSON;
	private String controllerURL;
	private String params;
	private String username;
	private String password;

	/**
	 * AppDynamics Toolkit constructor to initialize user and controller information
	 *
	 * (EXAMPLE)
	 * username: username@customer1 
	 * password: xxxx
	 * controllerURL: http://localhost:8090
	 * params: &output=JSON
	 *
	 * If you are using the SaaS Controller, your username is your AppDynamics account name
	 *
	 * @param	username 		Username of the user making the request
	 * @param	password 		Password of the user making the request
	 * @param	controllerURL 	URL of the server from where the request is being made 
	 * 							(i.e. http://localhost:8090)
	 * @param	params 			Further parameters to specify type of return data
	 */
	public RESTToolkit(String username, String password, String controllerURL, String params)
	{
		this.username = username;
		this.password = password;
		this.controllerURL = controllerURL;
		this.params = params;
	}

	/**
	 * Get all applications
	 * @return 			ArrayList{@literal <}ADApplication{@literal >} - List of applications
	 * @throws 			JSONException
	 */
	public ArrayList<ADApplication> getApplications() throws JSONException
	{
		applications = new ArrayList<ADApplication>();

		rawJSON = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL +
				"/controller/rest/applications", "output=JSON&" + params, username, password);

		JSONArray parsedJSON = new JSONArray(rawJSON);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			addApplication(parsedJSON.getJSONObject(jArrayPos));	
		}

		return applications;
	}


	/**
	 * Gets the events from a specific time range
	 * 
	 * (INFO)
	 * To get current time in milliseconds: System.currentTimeMillis();
	 * 
	 * To get time from past use SimpleDateFormat 
	 * (EXAMPLE)
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * format.setTimezone(Timezone.getTimezone("GMT"));
	 * Date date = format.parse("2013-01-01 23:59:59");
	 * Time in milliseconds: date.getTime();
	 * 
	 * @param 	app			Application Name or Application ID
	 * @param 	startTime	(Milliseconds) Start Time of the Range from which data will be pulled in UNIX epoch time.
	 * @param 	endTime		(Milliseconds) End Time of the Range from which the data will be pulled in UNIX epoch time.
	 * @param 	types		A string of either one event-type OR a comma-separated string of event-types
	 * 						Example: "STALL" or "APPLICATION_ERROR,STALL,DEADLOCK"
	 * @param	severities	A string of either one severity OR a comma-separated string of severities
	 * 						Example: "ERROR" or "INFO,WARN,ERROR"
	 * @return				ArrayList{@literal <}ADEvent{@literal >} 
	 * 							- List of Events
	 * @throws 				JSONException
	 */
	public ArrayList<ADEvent> getEvents(String app, long startTime, long endTime, String types, String severities) throws JSONException
	{
		getApplications();
		
		events = new ArrayList<ADEvent>();
		
		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/events", "time-range-type=BETWEEN_TIMES&start-time=" + startTime + "&end-time=" + endTime
				+ "&event-types=" + types + "&severities=" + severities
				+ "&output=JSON&" + params, username, password);
		
		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			addEvent(parsedJSON.getJSONObject(jArrayPos));	
		}

		return events;
	}

	/**
	 * Gets the events for a duration of minutes before the current time
	 * 
	 * (INFO)
	 * To get current time in milliseconds: System.currentTimeMillis();
	 * 
	 * To get time from past use SimpleDateFormat 
	 * (EXAMPLE)
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * format.setTimezone(Timezone.getTimezone("GMT"));
	 * Date date = format.parse("2013-01-01 23:59:59");
	 * Time in milliseconds: date.getTime();
	 * 
	 * @param 	app			Application Name or Application ID
	 * @param 	startTime	(Milliseconds) Start Time of the Range from which data will be pulled in UNIX epoch time.
	 * @param 	endTime		(Milliseconds) End Time of the Range from which the data will be pulled in UNIX epoch time.
	 * @param 	types		A string of either one event-type OR a comma-separated string of event-types
	 * @param	severities	A string of either one severity OR a comma-separated string of severities
	 * @return				ArrayList{@literal <}ADEvent{@literal >} 
	 * 							- List of Events
	 * @throws 				JSONException
	 */
	public ArrayList<ADEvent> getEvents(String app, int time, String types, String severities) throws JSONException
	{
		getApplications();
		
		events = new ArrayList<ADEvent>();
		
		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/events", "time-range-type=BEFORE_NOW&duration-in-mins=" + time
				+ "&event-types=" + types + "&severities=" + severities
				+ "&output=JSON&" + params, username, password);
		
		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			addEvent(parsedJSON.getJSONObject(jArrayPos));	
		}

		return events;
	}

	/**
	 * Gets all the Business Transactions
	 * @return 			ArrayList{@literal <}ADBusinessTransaction{@literal >} 
	 * 						- List of Business Transactions
	 * @throws 			JSONException
	 */
	public ArrayList<ADBusinessTransaction> getBTs() throws JSONException
	{
		getApplications();

		ArrayList<ADBusinessTransaction> tmpBTs = new ArrayList<ADBusinessTransaction>();

		for (ADApplication curApp : applications)
		{
			tmpBTs.addAll(getBTsFromApp(curApp.name));
		}

		BTs = tmpBTs;

		return BTs;
	}

	/**
	 * Gets all Business Transaction specific to an appName
	 * @param 	app	Name of Application
	 * @return 			ArrayList{@literal <}ADBusinessTransaction{@literal >} - List of Business Transactions
	 * @throws 			JSONException
	 */
	public ArrayList<ADBusinessTransaction> getBTsFromApp(String app) throws JSONException
	{
		BTs = new ArrayList<ADBusinessTransaction>();

		rawJSON = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/business-transactions", "output=JSON&" + params, username, password);

		JSONArray parsedJSON = new JSONArray(rawJSON);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			addBT(parsedJSON.getJSONObject(jArrayPos));		
		}
		
		return BTs;
	}

	/**
	 * Get All Business Transactions of a specific Tier
	 * @param 	tierName	Tier Name
	 * @return 				ArrayList{@literal <}ADBusinessTransaction{@literal >} 
	 * 							- List of Business Transactions
	 * @throws 				JSONException
	 */
	public ArrayList<ADBusinessTransaction> getBTsFromTier(String tierName) throws JSONException
	{
		getBTs();

		ArrayList<ADBusinessTransaction> filteredBTs = new ArrayList<ADBusinessTransaction>();

		for (ADBusinessTransaction bt : BTs)
		{
			if (bt.tierName.equals(tierName))
			{
				filteredBTs.add(bt);
			}
		}

		return filteredBTs;
	}

	/**
	 * Find Business transaction from its ID
	 * @param 	BTId		ID of the BusinessTransaction
	 * @return				ADBusinessTransaction 
	 * 							- Business Transaction Object
	 * @throws 				JSONException
	 */
	public ADBusinessTransaction getBTbyId(String BTId) throws JSONException
	{
		getBTs();

		for (ADBusinessTransaction BT : BTs)
		{
			if (BT.id.equals(BTId))
			{
				return BT;
			}
		}

		return null;
	}

	/**
	 * Get All Tiers
	 * @return		ArrayList{@literal <}ADTier{@literal >} 
	 * 					- List of Tiers
	 * @throws 		JSONException
	 */
	public ArrayList<ADTier> getTiers() throws JSONException
	{
		getApplications();

		ArrayList<ADTier> tmpTier = new ArrayList<ADTier>();

		for (ADApplication curApp : applications)
		{
			tmpTier.addAll(getTiersFromApp(curApp.name));
		}

		tiers = tmpTier;

		return tiers;
	}

	/**
	 * Get all tiers specific to an application
	 * @param 	app	Application Name or Application ID
	 * @return		ArrayList{@literal <}ADTier{@literal >} 
	 * 					- List of Tiers
	 * @throws 		JSONException
	 */
	public ArrayList<ADTier> getTiersFromApp(String app) throws JSONException
	{
		tiers = new ArrayList<ADTier>();

		rawJSON = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/tiers", "output=JSON&" + params, username, password);

		JSONArray parsedJSON = new JSONArray(rawJSON);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			addTier(parsedJSON.getJSONObject(jArrayPos));
		}
		
		return tiers;
	}

	/**
	 * Get a tier object from its ID
	 * @param 	tierId		Tier ID
	 * @return				ADTier
	 * 							- Tier Object
	 * @throws 				JSONException
	 */
	public ADTier getTierById(String tierId) throws JSONException
	{
		getTiers();
		
		for (ADTier tier : tiers)
		{
			if (tier.id.equals(tierId))
			{
				return tier;
			}
		}
		
		return null;
	}

	/**
	 * Get All Nodes
	 * @return 	ArrayList{@literal <}ADNode{@literal >} 
	 * 				- List of Nodes
	 * @throws 	JSONException
	 */
	public ArrayList<ADNode> getNodes() throws JSONException
	{
		getApplications();

		ArrayList<ADNode> tmpNodes = new ArrayList<ADNode>();

		for (ADApplication curApp : applications)
		{
			tmpNodes.addAll(getNodesFromApp(curApp.name));
		}
		
		nodes = tmpNodes;
		
		return nodes;
	}

	/**
	 * Get all nodes specific to an appName
	 * @param	app	Application Name or Application ID
	 * @return		ArrayList{@literal <}ADNode{@literal >} 
	 * 					- List of Nodes 
	 * @throws 		JSONException
	 */
	public ArrayList<ADNode> getNodesFromApp(String app) throws JSONException
	{
		nodes = new ArrayList<ADNode>();
		rawJSON = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/nodes", "output=JSON&" + params, username, password);

		JSONArray parsedJSON = new JSONArray(rawJSON);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			addNode(parsedJSON.getJSONObject(jArrayPos));
		}
		
		return nodes;
	}

	/**
	 * Get all nodes specific to a tier name
	 * @param	tierName	Tier Name
	 * @return				ArrayList{@literal <}ADNode{@literal >} 
	 * 							- List of Nodes
	 * @throws 				JSONException
	 */
	public ArrayList<ADNode> getNodesFromTier(String tierName) throws JSONException
	{
		getNodes();

		ArrayList<ADNode> filteredNodes = new ArrayList<ADNode>();

		for (ADNode node : nodes)
		{
			if (node.tierName.equals(tierName))
			{
				filteredNodes.add(node);
			}
		}

		return filteredNodes;
	}

	/**
	 * Get a specific node by name
	 * @param	nodeName	Name of node
	 * @return				ArrayList{@literal <}ADNode{@literal >} 
	 * 							- List of Nodes
	 * @throws				JSONException
	 */
	public ADNode getNodeByName(String nodeName) throws JSONException
	{
		getNodes();

		for (ADNode node : nodes)
		{
			if (node.name.equals(nodeName))
			{
				return node;
			}
		}
		
		return null;
	}

	/**
	 * Gets a specific node by node id.
	 * @param 	nodeId		Node Id
	 * @return				If found returns (ADMetric) Node. Else returns null;
	 * @throws 				JSONException
	 */
	public ADNode getNodeById(String nodeId) throws JSONException
	{
		getNodes();

		for (ADNode node : nodes)
		{
			if (node.id.equals(nodeId))
			{
				return node;
			}
		}
		
		return null;
	}

	/**
	 * Gets the average response time for a business transaction from a specific time range.
	 * 
	 * (INFO)
	 * To get current time in milliseconds: System.currentTimeMillis();
	 * 
	 * To get time from past use SimpleDateFormat 
	 * (EXAMPLE)
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * format.setTimezone(Timezone.getTimezone("GMT"));
	 * Date date = format.parse("2013-01-01 23:59:59");
	 * Time in milliseconds: date.getTime();
	 * 
	 * @param 	app			Application Name or Application ID
	 * @param 	BT			Business Transaction Name
	 * @param 	startTime	(Milliseconds) Start Time of the Range from which data will be pulled in UNIX epoch time.
	 * @param 	endTime		(Milliseconds) End Time of the Range from which the data will be pulled in UNIX epoch time.
	 * @param 	rollup		If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned.
	 * @return				ArrayList{@literal <}ADMetric{@literal >} 
	 * 							- List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getARTForBT (String app, String BT, long startTime, long endTime, boolean rollup) 
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		getBTs();

		String tierName = "";

		for (ADBusinessTransaction bt : BTs)
		{
			if (bt.name.equals(BT))
			{
				tierName = bt.tierName;
			}
		}
		
		String metricPath = "Business Transaction Performance|Business Transactions|"
				+ tierName + "|" + BT + "|Normal Average Response Time (ms)";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BETWEEN_TIMES&start-time="+ startTime + "&end-time=" + endTime
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Get average response time for a business transaction for a duration of the current time.
	 * @param 	app 		Application Name or Application ID
	 * @param 	BT 			Name of Business Transaction 
	 * @param 	duration 	Time duration to which the data has to be pulled from
	 * @param 	rollup 		If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned.
	 * @return 				ArrayList{@literal <}ADMetric{@literal >} 
	 * 							- List of Metrics.
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getARTForBT(String app, String BT, int duration, boolean rollup)
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		getBTs();

		String tierName = "";

		for (ADBusinessTransaction bt : BTs)
		{
			if (bt.name.equals(BT))
			{
				tierName = bt.tierName;
			}
		}

		String metricPath = "Business Transaction Performance|Business Transactions|"
				+ tierName + "|" + BT + "|Normal Average Response Time (ms)";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BEFORE_NOW&duration-in-mins="+ duration + "&rollup="
				+ rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the calls per minute metrics for a business transaction from a specific time range.
	 * 
	 * (INFO)
	 * To get current time in milliseconds: System.currentTimeMillis();
	 * 
	 * To get time from past use SimpleDateFormat 
	 * (EXAMPLE)
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * format.setTimezone(Timezone.getTimezone("GMT"));
	 * Date date = format.parse("2013-01-01 23:59:59");
	 * Time in milliseconds: date.getTime();
	 * 
	 * @param 	app			Application Name or Application ID
	 * @param 	BT			Business Transaction Name
	 * @param 	startTime	(Milliseconds) Start Time of the Range from which data will be pulled in UNIX epoch time.
	 * @param 	endTime		(Milliseconds) End Time of the Range from which the data will be pulled in UNIX epoch time.
	 * @param 	rollup		If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned.
	 * @return				ArrayList{@literal <}ADMetric{@literal >}
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getLoadForBT(String app, String BT, long startTime, long endTime, boolean rollup)
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		getBTs();

		String tierName = "";

		for (ADBusinessTransaction bt : BTs)
		{
			if (bt.name.equals(BT))
			{
				tierName = bt.tierName;
			}
		}

		String metricPath = "Business Transaction Performance|Business Transactions|" + tierName + "|" + BT
				+ "|Calls per Minute";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/"
				+ app.trim().replaceAll(" ", "%20") + "/metric-data", "output=JSON&metric-path="
				+ metricPath.trim().replaceAll(" ", "%20") + "&time-range-type=BETWEEN_TIMES&start-time="
				+ startTime + "&end-time=" + endTime + "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the calls per minute metrics for a business transaction for a duration from the current time.
	 * 
	 * @param 	app 		Application Name or Application ID
	 * @param 	BT 			Name of Business Transaction 
	 * @param 	duration 	(Minutes) Duration to return the metric data.
	 * @param 	rollup 		If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned. 
	 * @return 				ArrayList{@literal <}ADMetric{@literal >} 
	 * 							- List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getLoadForBT(String appName, String BT, int duration, boolean rollup)
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		getBTs();

		String tierName = "";

		for (ADBusinessTransaction bt : BTs)
		{
			if (bt.name.equals(BT))
			{
				tierName = bt.tierName;
			}
		}

		String metricPath = "Business Transaction Performance|Business Transactions|" + tierName + "|" + BT
				+ "|Calls per Minute";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/"
				+ appName.trim().replaceAll(" ", "%20") + "/metric-data", "output=JSON&metric-path="
				+ metricPath.trim().replaceAll(" ", "%20") + "&time-range-type=BEFORE_NOW&duration-in-mins="
				+ duration + "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the error metrics for a business transaction from a specific time range.
	 * 
	 * (INFO)
	 * To get current time in milliseconds: System.currentTimeMillis();
	 * 
	 * To get time from past use SimpleDateFormat 
	 * (EXAMPLE)
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * format.setTimezone(Timezone.getTimezone("GMT"));
	 * Date date = format.parse("2013-01-01 23:59:59");
	 * Time in milliseconds: date.getTime();
	 * 
	 * @param 	app			Application Name or Application ID
	 * @param 	BT			Business Transaction Name
	 * @param 	startTime	(Milliseconds) Start time from which the metric data is returned in UNIX epoch time.
	 * @param 	endTime		(Milliseconds) End time until which the metric data is returned in UNIX epoch time.
	 * @param 	rollup		If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned.
	 * @return				ArrayList{@literal <}ADMetric{@literal >} - List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getErrorsForBT(String app, String BT, long startTime, long endTime, boolean rollup)
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		getBTs();

		String tierName = "";

		for (ADBusinessTransaction bt : BTs)
		{
			if (bt.name.equals(BT))
			{
				tierName = bt.tierName;
			}
		}

		String metricPath = "Business Transaction Performance|Business Transactions|"
				+ tierName +"|" + BT + "|Errors per Minute";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BETWEEN_TIMES&start-time="+ startTime + "&end-time=" + endTime 
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the error metrics for a specific business transaction for a duration from the current time.
	 * @param 	app 		Application Name or Application ID
	 * @param 	BT 			Business Transaction Name
	 * @param 	duration 	(Minutes) Duration to return the metric data.
	 * @param 	rollup  	If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned.
	 * @return 				ArrayList{@literal <}ADMetric{@literal >} 
	 * 							- List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getErrorsForBT(String app, String BT, int duration, boolean rollup) 
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		getBTs();

		String tierName = "";

		for (ADBusinessTransaction bt : BTs)
		{
			if (bt.name.equals(BT))
			{
				tierName = bt.tierName;
			}
		}

		String metricPath = "Business Transaction Performance|Business Transactions|"
				+ tierName +"|" + BT + "|Errors per Minute";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BEFORE_NOW&duration-in-mins="+ duration 
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the average response time for an application from a specific time range.
	 * 
	 * (INFO)
	 * To get current time in milliseconds: System.currentTimeMillis();
	 * 
	 * To get time from past use SimpleDateFormat 
	 * (EXAMPLE)
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * format.setTimezone(Timezone.getTimezone("GMT"));
	 * Date date = format.parse("2013-01-01 23:59:59");
	 * Time in milliseconds: date.getTime();
	 * 
	 * @param 	app			Application Name or Application ID
	 * @param 	startTime	(Milliseconds) Start time from which the metric data is returned in UNIX epoch time.
	 * @param 	endTime		(Milliseconds) End time until which the metric data is returned in UNIX epoch time.
	 * @param 	rollup		If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned.
	 * @return				ArrayList{@literal <}ADMetric{@literal >}
	 * 							- List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getARTForApp(String app, long startTime, long endTime, boolean rollup)
			throws JSONException
	{	
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|Average Response Time (ms)";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BETWEEN_TIMES&start-time="+ startTime + "&end-time=" + endTime 
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the average response time for a specific application for a duration from the current time.
	 * @param 	app			Application Name or Application ID
	 * @param 	duration 	Length of Time to which the data will be pulled from
	 * @param 	rollup  	If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned. 
	 * @return 				ArrayList{@literal <}ADMetric{@literal >} 
	 * 							- List of metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getARTForApp(String app, int duration, boolean rollup) throws JSONException
	{	
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|Average Response Time (ms)";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BEFORE_NOW&duration-in-mins="+ duration 
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the calls per minute metrics for an application for a specific time range.
	 * 
	 * (INFO)
	 * To get current time in milliseconds: System.currentTimeMillis();
	 * 
	 * To get time from past use SimpleDateFormat 
	 * (EXAMPLE)
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * format.setTimezone(Timezone.getTimezone("GMT"));
	 * Date date = format.parse("2013-01-01 23:59:59");
	 * Time in milliseconds: date.getTime();
	 * 
	 * @param 	app			Application Name or Application ID
	 * @param 	startTime	(Milliseconds) Start time from which the metric data is returned in UNIX epoch time.
	 * @param 	endTime		(Milliseconds) End time until which the metric data is returned in UNIX epoch time.
	 * @param 	rollup		If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned.
	 * @return				ArrayList{@literal <}ADMetric{@literal >}
	 * 							- List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getLoadForApp(String app, long startTime, long endTime, boolean rollup)
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|Calls per Minute";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BETWEEN_TIMES&start-time="+ startTime + "&end-time=" + endTime 
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Get calls per minute metrics for an application for a duration of the current time.
	 * @param 	app 		Application Name or Application ID
	 * @param 	duration 	(Minutes) Length of Time from which the data has to be pulled
	 * @param 	rollup   	If true, value as single data point is returned. 
	 * 						If false, all the values within the specified time range are returned.
	 * @return 				ArrayList{@literal <}ADMetric{@literal >} 
	 * 							- List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getLoadForApp(String app, int duration, boolean rollup) 
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|Calls per Minute";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BEFORE_NOW&duration-in-mins="+ duration 
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the error metrics for a specific application for a specific time range.
	 * 
	 * (INFO)
	 * To get current time in milliseconds: System.currentTimeMillis();
	 * 
	 * To get time from past use SimpleDateFormat 
	 * (EXAMPLE)
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * format.setTimezone(Timezone.getTimezone("GMT"));
	 * Date date = format.parse("2013-01-01 23:59:59");
	 * Time in milliseconds: date.getTime();
	 * 
	 * @param 	app			Application Name or Application ID
	 * @param 	startTime	(Milliseconds) Start time from which the metric data is returned in UNIX epoch time.
	 * @param 	endTime		(Milliseconds) End time until which the metric data is returned in UNIX epoch time.
	 * @param 	rollup		If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned.
	 * @return				ArrayList{@literal <}ADMetric{@literal >}
	 * 							- List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getErrorsForApp(String app, long startTime, long endTime, boolean rollup)
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|Errors per Minute";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&output=JSON&time-range-type=BETWEEN_TIMES&start-time="+ startTime 
				+ "&end-time=" + endTime + "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the error metrics for a specific application from a duration from the current time
	 * @param 	app			Application Name or Application ID
	 * @param 	duration	(Minutes) Duration to return the metric data.
	 * @param 	rollup  	If true, value as single data point is returned. 
	 * 						If false, all the values within the specified time range are returned.
	 * @return 				ArrayList{@literal <}ADMetric{@literal >} - List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getErrorsForApp(String app, int duration, boolean rollup) 
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|Errors per Minute";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&output=JSON&time-range-type=BEFORE_NOW&duration-in-mins="+ duration 
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the average response time for a specific tier from a specific time range
	 * 
	 * (INFO)
	 * To get current time in milliseconds: System.currentTimeMillis();
	 * 
	 * To get time from past use SimpleDateFormat 
	 * (EXAMPLE)
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * format.setTimezone(Timezone.getTimezone("GMT"));
	 * Date date = format.parse("2013-01-01 23:59:59");
	 * Time in milliseconds: date.getTime();
	 * 
	 * @param 	app			Application Name or Application ID
	 * @param 	tierName	Tier Name
	 * @param 	startTime	(Milliseconds) Start time from which the metric data is returned in UNIX epoch time.
	 * @param 	endTime		(Milliseconds) End time until which the metric data is returned in UNIX epoch time.
	 * @param 	rollup		If true, value as single data point is returned. 
	 * 						If false, all the values within the specified time range are returned.
	 * @return				ArrayList{@literal <}ADMetric{@literal >} 
	 * 							- List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getARTForTier(String app, String tierName, long startTime, long endTime, boolean rollup)
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|" + tierName + "|Average Response Time (ms)";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BETWEEN_TIMES&start-time="+ startTime + "&end-time=" + endTime 
				+ "&rollup=" + rollup, username, password);
	
		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the average response time metrics for a specific tier for a duration from the current time
	 * @param 	app 		Application Name or Application ID
	 * @param 	tierName 	Name of the Tier
	 * @param 	duration 	(Minutes) Duration to return the metric data.
	 * @param 	rollup  	If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned.
	 * @return 				ArrayList{@literal <}ADMetric{@literal >} 
	 * 							- List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getARTForTier(String appName, String tierName, int duration, boolean rollup) 
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|" + tierName + "|Average Response Time (ms)";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + appName.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BEFORE_NOW&duration-in-mins="+ duration 
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);
		
		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the call per minute metrics for a specific application for a specific time range
	 * 
	 * (INFO)
	 * To get current time in milliseconds: System.currentTimeMillis();
	 * 
	 * To get time from past use SimpleDateFormat 
	 * (EXAMPLE)
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * format.setTimezone(Timezone.getTimezone("GMT"));
	 * Date date = format.parse("2013-01-01 23:59:59");
	 * Time in milliseconds: date.getTime();
	 * 
	 * @param 	app			Application Name or Application ID
	 * @param 	tierName	Tier Name
	 * @param 	startTime	(Milliseconds) Start time from which the metric data is returned in UNIX epoch time.
	 * @param 	endTime		(Milliseconds) End time until which the metric data is returned in UNIX epoch time.
	 * @param 	rollup		If true, value as single data point is returned. 
	 * 						If false, all the values within the specified time range are returned.
	 * @return				ArrayList{@literal <}ADMetric{@literal >}
	 * 							- List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getLoadForTier(String app, String tierName, long startTime, long endTime, boolean rollup)
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|" + tierName + "|Calls per Minute";
		
		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BETWEEN_TIMES&start-time="+ startTime + "&end-time=" + endTime
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);
		
		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}
		
		return metrics;
	}

	/**
	 * Gets the call per minute metrics for a specific application for a duration from the current time
	 * @param 	app 		Application Name or Application ID
	 * @param 	tierName 	Tier Name
	 * @param 	duration 	(Minutes) Duration from current time to which the data has to be pulled from
	 * @param 	rollup		If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned.
	 * @return 				ArrayList{@literal <}ADMetric{@literal >} 
	 * 							- List of metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getLoadForTier(String app, String tierName, int duration, boolean rollup) throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|" + tierName + "|Calls per Minute";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replaceAll(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BEFORE_NOW&duration-in-mins="+ duration 
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the Error Metrics for a specific Tier for a specific time range
	 * 
	 * (INFO)
	 * To get current time in milliseconds: System.currentTimeMillis();
	 * 
	 * To get time from past use SimpleDateFormat 
	 * (EXAMPLE)
	 * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 * format.setTimezone(Timezone.getTimezone("GMT"));
	 * Date date = format.parse("2013-01-01 23:59:59");
	 * Time in milliseconds: date.getTime();
	 * 
	 * @param 	app			Application Name or Application ID
	 * @param 	tierName	Tier Name
	 * @param 	startTime	(Milliseconds) Start time from which the metric data is returned in UNIX epoch time.
	 * @param 	endTime		(Milliseconds) End time until which the metric data is returned in UNIX epoch time.
	 * @param 	rollup		If true, value as single data point is returned. 
	 * 						If false, all the values within the specified time range are returned.
	 * @return				ArrayList{@literal <}ADMetric{@literal >}
	 * 							- List of Metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getErrorsForTier(String app, String tierName, long startTime, long endTime, boolean rollup) throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|" + tierName + "|Errors per Minute";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + app.trim().replace(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BETWEEN_TIMES&start-time="+ startTime + "&end-time=" + endTime 
				+ "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Gets the Error Metrics for a specific Tier for a duration from the current time
	 * @param 	app 		Application Name or Application ID
	 * @param 	tierName 	Tier Name
	 * @param 	duration 	(Minutes) Duration to return the metric data.
	 * @param 	rollup  	If true, value as single data point is returned. 
	 * 				 		If false, all the values within the specified time range are returned.
	 * @return 				ArrayList{@literal <}ADMetric{@literal >}
	 * 							- List of metrics
	 * @throws 				JSONException
	 */
	public ArrayList<ADMetric> getErrorsForTier(String appName, String tierName, int duration, boolean rollup)
			throws JSONException
	{
		metrics = new ArrayList<ADMetric>();

		String metricPath = "Overall Application Performance|" + tierName + "|Errors per Minute";

		String result = HTTPRequestPoster.sendGetRequestWithAuthorization(controllerURL
				+ "/controller/rest/applications/" + appName.trim().replace(" ", "%20")
				+ "/metric-data", "output=JSON&metric-path=" + metricPath.trim().replaceAll(" ", "%20")
				+ "&time-range-type=BEFORE_NOW&duration-in-mins="+ duration + "&rollup=" + rollup, username, password);

		JSONArray parsedJSON = new JSONArray(result);

		for (int jArrayPos = 0; jArrayPos < parsedJSON.length(); jArrayPos++)
		{
			if (parsedJSON.getJSONObject(jArrayPos).has("metricValues"))
			{
				JSONArray metricArray = parsedJSON.getJSONObject(jArrayPos).getJSONArray("metricValues");
				for (int jMetricPos = 0; jMetricPos < metricArray.length(); jMetricPos++)
				{
					addMetric(metricArray.getJSONObject(jMetricPos));
				}
			}
		}

		return metrics;
	}

	/**
	 * Adds a single application instance into the application array
	 * @param 	applicationJSON 	JSON object captured from the REST request
	 * @throws 	JSONException
	 */
	public void addApplication(JSONObject applicationJSON) throws JSONException
	{
		ADApplication application = new ADApplication();
		application.id = (applicationJSON.get("id")).toString();
		application.description = (applicationJSON.get("description")).toString();
		application.name = (applicationJSON.get("name")).toString();

		applications.add(application);
	}

	/**
	 * Adds a single business transaction instance into the business transaction array
	 * @param 	btJSON 	JSON object captured from the REST request
	 * @throws 			JSONException
	 */
	public void addBT(JSONObject btJSON) throws JSONException
	{
		ADBusinessTransaction BT= new ADBusinessTransaction();
		BT.id = (btJSON.get("id")).toString();
		BT.background = (btJSON.get("background")).toString();
		BT.entryPointType = (btJSON.get("entryPointType")).toString();
		BT.internalName = (btJSON.get("internalName")).toString();
		BT.name = (btJSON.get("name")).toString();
		BT.tierId = (btJSON.get("tierId")).toString();
		BT.tierName = (btJSON.get("tierName")).toString();

		BTs.add(BT);
	}

	/**
	 * Adds a single node instance into the node array
	 * @param 	nodeJSON 	JSON object captured from the REST request
	 * @throws 				JSONException
	 */
	public void addNode(JSONObject nodeJSON) throws JSONException
	{
		ADNode node = new ADNode();
		node.appAgentPresent = (nodeJSON.get("appAgentPresent")).toString();
		node.appAgentVersion = (nodeJSON.get("appAgentVersion")).toString();
		node.id = (nodeJSON.get("id")).toString();
		node.machineAgentPresent = (nodeJSON.get("machineAgentPresent")).toString();
		node.machineAgentVersion = (nodeJSON.get("machineAgentVersion")).toString();
		node.machineId = (nodeJSON.get("machineId")).toString();
		node.machineName = (nodeJSON.getString("machineName")).toString();
		node.machineOSType = (nodeJSON.getString("machineOSType")).toString();
		node.name = (nodeJSON.get("name")).toString();
		node.nodeUniqueLocalId = (nodeJSON.getString("nodeUniqueLocalId")).toString();
		node.tierId = (nodeJSON.get("tierId")).toString();
		node.tierName = (nodeJSON.get("tierName")).toString();
		node.type = (nodeJSON.get("type")).toString();

		nodes.add(node);
	}

	/**
	 * Adds a single tier instance into the tier array
	 * @param 	tierJSON 	JSON object captured from the REST request
	 * @throws 				JSONException
	 */
	public void addTier(JSONObject tierJSON) throws JSONException
	{
		ADTier tier = new ADTier();
		tier.agentType = (tierJSON.get("agentType")).toString();
		tier.id = (tierJSON.get("id")).toString();
		tier.description = (tierJSON.get("description")).toString();
		tier.name = (tierJSON.get("name")).toString();
		tier.numberOfNodes = (tierJSON.get("numberOfNodes")).toString();
		tier.type = (tierJSON.get("type")).toString();

		tiers.add(tier);
	}

	/**
	 * Adds a single metric Instance into the metric array
	 * @param 	metricJSON 	JSON captured from the REST request
	 * @throws 				JSONException
	 */
	public void addMetric(JSONObject metricJSON) throws JSONException
	{
		ADMetric metric = new ADMetric();
		metric.current = metricJSON.getLong("current");
		metric.max = metricJSON.getLong("max");
		metric.min = metricJSON.getLong("min");
		metric.startTimeInMillis = metricJSON.getLong("startTimeInMillis");
		metric.value = metricJSON.getLong("value");

		metrics.add(metric);
	}

	/**
	 * Adds a single event instance into the events array
	 * @param 	eventJSON	JSON object captured from REST request
	 * @throws 				JSONException
	 */
	public void addEvent(JSONObject eventJSON) throws JSONException 
	{
		ADEvent event = new ADEvent();

		JSONArray entitiesJSON = eventJSON.getJSONArray("affectedEntities");

		event.affectedEntities = new ArrayList<ADEntity>();

		for (int jMetricPos = 0; jMetricPos < entitiesJSON.length(); jMetricPos++)
		{
			addEntity(entitiesJSON.getJSONObject(jMetricPos), event);
		}

		event.archived = eventJSON.getBoolean("archived");
		event.deepLinkUrl = eventJSON.getString("deepLinkUrl");
		event.eventTime = eventJSON.getLong("eventTime");
		event.id = eventJSON.getInt("id");
		event.markedAsRead = eventJSON.getBoolean("markedAsRead");
		event.markedAsResolved = eventJSON.getBoolean("markedAsResolved");
		event.severity = eventJSON.getString("severity");
		event.subType = eventJSON.getString("subType");
		event.summary = eventJSON.getString("summary");

		if (eventJSON.get("triggeredEntity").toString() == "null")
		{
			event.triggeredEntity = null;
		}
		else
		{
			event.triggeredEntity.entityId = eventJSON.getJSONObject("triggeredEntity").getInt("entityId");
			event.triggeredEntity.entityType = eventJSON.getJSONObject("triggeredEntity").getString("entityType");
		}

		event.type = eventJSON.getString("type");

		events.add(event);
	}

	/**
	 * Adds a single instance of entity to the event's affected entities list
	 * @param 	entityJSON	JSON object captured from REST request
	 * @param 	event		Event to which the entity is to be added to
	 * @throws 				JSONException
	 */
	public void addEntity(JSONObject entityJSON, ADEvent event) throws JSONException
	{
		ADEntity entity = new ADEntity();
		entity.entityId = entityJSON.getInt("entityId");
		entity.entityType = entityJSON.getString("entityType");

		event.affectedEntities.add(entity);
	}
}
