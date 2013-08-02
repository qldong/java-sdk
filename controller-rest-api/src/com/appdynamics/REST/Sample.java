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

import java.util.Date;
import java.util.List;

import com.appdynamics.JSON.JSONException;
import com.appdynamics.TypeREST.ADApplication;
import com.appdynamics.TypeREST.ADBusinessTransaction;
import com.appdynamics.TypeREST.ADEvent;
import com.appdynamics.TypeREST.ADMetric;
import com.appdynamics.TypeREST.ADNode;
import com.appdynamics.TypeREST.ADTier;

/**
 * 
 * @author victor.gavrielov
 *
 * This is a Test class which calls every method in the RESTToolkit class.
 * It prints out a sample of:
 * - all non-metrics (applications, business transactions, tiers, and nodes),
 * - all events in the past 15 minutes from the current time, and between the past 10 and 5 minutes if --events
 *   is an argument
 * - all metrics in the past 15 minutes from the current time, and between the past 10 and 5 minutes if --metrics
 * 	 is an argument
 */
public class Sample {

	public static void main(String[] args) {

		System.out.println("========================");
		System.out.println("      NON-METRICS:");
		System.out.println("========================");

		// set showMetrics to true if the flag --metrics was provided as an argument
		// set showEvents to true if the flag --events was provided as an argument
		boolean showMetrics = (args.length > 0 && args[0].equals("--metrics") || args.length > 1 && args[1].equals("--metrics"));
		boolean showEvents = (args.length > 0 && args[0].equals("--events") || args.length > 1 && args[1].equals("--events"));
		String username = "**username**";
		String password = "**password**";
		String controllerURL = "http://localhost:8080";
		String extraParams = null;
		RESTToolkit rtk = new RESTToolkit(username, password, controllerURL, extraParams);

		try { // catching the JSONException

			// getApplications
			List<ADApplication> applications = rtk.getApplications();
			System.out.println("------------------------");
			System.out.println("All Applications:\n");
			for(ADApplication app : applications){
				System.out.println(app.name);
			}

			// getBTs
			List<ADBusinessTransaction> bts = rtk.getBTs();
			System.out.println("------------------------");
			System.out.println("All Business Transactions:\n");
			for(ADBusinessTransaction bt : bts){
				System.out.println(bt.name);
			}

			//getTiers
			List<ADTier> tiers = rtk.getTiers();
			System.out.println("------------------------");
			System.out.println("All Tiers:\n");
			for(ADTier tier : tiers){
				System.out.println(tier.name);
			}

			//getNodes
			List<ADNode> nodes = rtk.getNodes();
			System.out.println("------------------------");
			System.out.println("All Nodes:\n");
			for(ADNode node : nodes){
				System.out.println(node.name);
			}

			//getBTbyId
			System.out.println("------------------------");
			System.out.println("All BT IDs and their names:\n");
			for(ADBusinessTransaction bt : bts){
				System.out.println("BT ID:   " + bt.id);
				System.out.println("BT name: " + rtk.getBTbyId(bt.id).name + "\n");
			}

			//getBTsFromApp (run through all Apps)
			System.out.println("------------------------");
			System.out.println("All BTs from all Apps:\n");
			for(ADApplication app : applications){
				System.out.println("App name:\n" + app.name);
				System.out.println("BTs:");
				for(ADBusinessTransaction bt : rtk.getBTsFromApp(app.id)){
					System.out.println(bt.name);
				}
				System.out.println();
			}

			//getBTsFromTier (run through all Tiers)
			System.out.println("------------------------");
			System.out.println("All BTs from all Tiers:\n");
			for(ADTier tier : tiers){
				System.out.println("Tier name:\n" + tier.name);
				System.out.println("BTs:");
				for(ADBusinessTransaction bt : rtk.getBTsFromTier(tier.name)){
					System.out.println(bt.name);
				}
				System.out.println();
			}

			// rtk.getNodeById(nodeId)
			System.out.println("------------------------");
			System.out.println("All Node IDs and their names:\n");
			for(ADNode node : nodes){
				System.out.println("Node ID:   " + node.id);
				System.out.println("Node name: " + rtk.getNodeById(node.id).name + "\n");
			}


			//			rtk.getNodeByName(nodeName)
			System.out.println("------------------------");
			System.out.println("Check that Nodes can be found by names:\n");
			for(ADNode node : nodes){
				System.out.println("Node name: " + node.name + (rtk.getNodeByName(node.name).name.equals(node.name) ? " OK" : 
					" Error: wrong search result: " + rtk.getNodeById(node.id).name));
			}

			//	rtk.getNodesFromApp(app) (run through all apps)
			System.out.println("------------------------");
			System.out.println("All Nodes from all Apps:\n");
			for(ADApplication app : applications){
				System.out.println("App name:\n" + app.name);
				System.out.println("Nodes:");
				for(ADNode node : rtk.getNodesFromApp(app.id)){
					System.out.println(node.name);
				}
				System.out.println();
			}

			// rtk.getNodesFromTier(tierName)
			System.out.println("------------------------");
			System.out.println("All Nodes from all Tiers:\n");
			for(ADTier tier : tiers){
				System.out.println("Tier name:\n" + tier.name);
				System.out.println("Nodes:");
				for(ADNode node : rtk.getNodesFromTier(tier.name)){
					System.out.println(node.name);
				}
				System.out.println();
			}

			// rtk.getTierById(tierId)
			System.out.println("------------------------");
			System.out.println("All Tier IDs and their names:\n");
			for(ADTier tier : tiers){
				System.out.println("Tier ID:   " + tier.id);
				System.out.println("Tier name: " + rtk.getTierById(tier.id).name + "\n");
			}

			// rtk.getTiersFromApp() (run through all apps)
			System.out.println("------------------------");
			System.out.println("All Tiers from all Apps:\n");
			for(ADApplication app : applications){
				System.out.println("App name:\n" + app.name);
				System.out.println("Tiers:");
				for(ADTier tier : rtk.getTiersFromApp(app.id)){
					System.out.println(tier.name);
				}
				System.out.println();
			}


			System.out.println("------------------------");


			if(showEvents){
				showEvents(rtk, applications);
			}
			
			if(showMetrics){
				printMetrics(rtk, applications, bts, tiers);
			}


		} catch (JSONException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void showEvents(RESTToolkit rtk,	List<ADApplication> applications) throws JSONException{
		System.out.println("========================");
		System.out.println("        EVENTS:");
		System.out.println("========================");

		List<ADEvent> events;

		System.out.println("\nEVENTS:\n");

		System.out.println("All event IDs from all Apps btwn 10 and 5 minutes ago:\n");
		for(ADApplication app : applications){
			System.out.println("App name:\n" + app.name);
			System.out.println("Event IDs:");
			events = rtk.getEvents(app.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, "APPLICATION_ERROR", "INFO,WARN,ERROR");
			for(ADEvent event : events){				
				System.out.println(event.id);
			}
		}

		System.out.println("------------------------");
		System.out.println("All event IDs from all Apps in the last 15 minutes:\n");
		for(ADApplication app : applications){
			System.out.println("App name:\n" + app.name);
			System.out.println("Event IDs:");
			events = rtk.getEvents(app.name, 15, "APPLICATION_ERROR,STALL,DEADLOCK", "INFO,WARN,ERROR");
			for(ADEvent event : events){				
				System.out.println(event.id);
			}
		}
		
		System.out.println("------------------------");
	}

	private static void printMetrics(RESTToolkit rtk, List<ADApplication> apps, List<ADBusinessTransaction> bts, List<ADTier> tiers) throws JSONException{

		System.out.println("========================");
		System.out.println("        METRICS:");
		System.out.println("========================");

		List<ADMetric> metrics;

		// METRICS
		System.out.println("------------------------");
		System.out.println("------------------------");
		System.out.println("\nAVERAGE RESPONSE TIMES (ARTs):\n");

		// rtk.getARTForApp(app, duration, rollup)
		System.out.println("All ARTs from all Apps in the last 15 minutes:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			System.out.print("ART rollup: ");
			metrics = rtk.getARTForApp(app.name, 15, true);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
			}

			System.out.println("Each ART:");
			metrics = rtk.getARTForApp(app.name, 15, false);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
			}
		}

		//	rtk.getARTForApp(app, startTime, endTime, rollup)	
		System.out.println("------------------------");
		System.out.println("All ARTs from all Apps btwn 10 and 5 minutes ago:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			System.out.print("ART rollup: ");
			metrics = rtk.getARTForApp(app.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, true);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
			}

			System.out.println("Each ART:");
			metrics = rtk.getARTForApp(app.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, false);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
			}
		}

		//			rtk.getARTForBT(app, BT, duration, rollup)
		System.out.println("------------------------");
		System.out.println("All ARTs from all Apps and all BTs in the last 15 minutes:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			for(ADBusinessTransaction bt : rtk.getBTsFromApp(app.id)){
				System.out.println("- BT name: " + bt.name);
				
				
				System.out.print("ART rollup: ");
				metrics = rtk.getARTForBT(app.name, bt.name, 15, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
				}

				System.out.println("Each ART:");
				metrics = rtk.getARTForBT(app.name, bt.name, 15, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
				}
			}
		}


		//			rtk.getARTForBT(app, BT, startTime, endTime, rollup)
		System.out.println("------------------------");
		System.out.println("All ARTs from all Apps and all BTs btwn 10 and 5 minutes ago:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			for(ADBusinessTransaction bt : rtk.getBTsFromApp(app.id)){
				System.out.println("- BT name: " + bt.name);

				System.out.print("ART rollup: ");
				metrics = rtk.getARTForBT(app.name, bt.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
				}

				System.out.println("Each ART:");
				metrics = rtk.getARTForBT(app.name, bt.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
				}
			}
		}
		
		
		//			rtk.getARTForTier(appName, tierName, duration, rollup)
		System.out.println("------------------------");
		System.out.println("All ARTs from all Apps and all Tiers in the last 15 minutes:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			for(ADTier tier : rtk.getTiersFromApp(app.id)){
				System.out.println("- Tier name: " + tier.name);
				
				
				System.out.print("ART rollup: ");
				metrics = rtk.getARTForTier(app.name, tier.name, 15, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
				}

				System.out.println("Each ART:");
				metrics = rtk.getARTForTier(app.name, tier.name, 15, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
				}
			}
		}

		//			rtk.getARTForTier(app, tierName, startTime, endTime, rollup)
		System.out.println("------------------------");
		System.out.println("All ARTs from all Apps and all TIERs btwn 10 and 5 minutes ago:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			for(ADTier tier : rtk.getTiersFromApp(app.id)){
				System.out.println("- Tier name: " + tier.name);

				System.out.print("ART rollup: ");
				metrics = rtk.getARTForTier(app.name, tier.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
				}

				System.out.println("Each ART:");
				metrics = rtk.getARTForTier(app.name, tier.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", value: " + metric.value + "ms");
				}
			}
		}


		System.out.println("------------------------");
		System.out.println("------------------------");
		System.out.println("\nERRORS:\n");

		//METRICS
		//			rtk.getErrorsForApp(app, duration, rollup)
		System.out.println("All Errors from all Apps in the last 15 minutes:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			System.out.print("Error rollup: ");
			metrics = rtk.getErrorsForApp(app.name, 15, true);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
			}

			System.out.println("Each Error:");
			metrics = rtk.getErrorsForApp(app.name, 15, false);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
			}
		}
		
		//			rtk.getErrorsForApp(app, startTime, endTime, rollup)
		System.out.println("------------------------");
		System.out.println("All Errors from all Apps btwn 10 and 5 minutes ago:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);

			System.out.print("Error rollup: ");
			metrics = rtk.getErrorsForApp(app.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, true);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
			}

			System.out.println("Each Error:");
			metrics = rtk.getErrorsForApp(app.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, false);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
			}

		}


		//			rtk.getErrorsForBT(app, BT, duration, rollup)
		System.out.println("------------------------");
		System.out.println("All Errors from all Apps and all BTs in the last 15 minutes:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			for(ADBusinessTransaction bt : rtk.getBTsFromApp(app.id)){
				System.out.println("- BT name: " + bt.name);
				
				System.out.print("Error rollup: ");
				metrics = rtk.getErrorsForBT(app.name, bt.name, 15, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}

				System.out.println("Each Error:");
				metrics = rtk.getErrorsForBT(app.name, bt.name, 15, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}
			}
		}
		
		//			rtk.getErrorsForBT(app, BT, startTime, endTime, rollup)
		System.out.println("------------------------");
		System.out.println("All Errors from all Apps and all BTs btwn 10 and 5 minutes ago:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			for(ADBusinessTransaction bt : rtk.getBTsFromApp(app.id)){
				System.out.println("- BT name: " + bt.name);

				System.out.print("Error rollup: ");
				metrics = rtk.getErrorsForBT(app.name, bt.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}

				System.out.println("Each Error:");
				metrics = rtk.getErrorsForBT(app.name, bt.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}
			}
		}

		//			rtk.getErrorsForTier(appName, tierName, duration, rollup)
		System.out.println("------------------------");
		System.out.println("All Errors from all Apps and all Tiers in the last 15 minutes:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			for(ADTier tier : rtk.getTiersFromApp(app.id)){
				System.out.println("- Tier name: " + tier.name);
				
				
				System.out.print("Error rollup: ");
				metrics = rtk.getErrorsForTier(app.name, tier.name, 15, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}

				System.out.println("Each Error:");
				metrics = rtk.getErrorsForTier(app.name, tier.name, 15, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}
			}
		}

		//			rtk.getErrorsForTier(app, tierName, startTime, endTime, rollup)
		System.out.println("------------------------");
		System.out.println("All Errors from all Apps and all TIERs btwn 10 and 5 minutes ago:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			for(ADTier tier : rtk.getTiersFromApp(app.id)){
				System.out.println("- Tier name: " + tier.name);

				System.out.print("Error rollup: ");
				metrics = rtk.getErrorsForTier(app.name, tier.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}

				System.out.println("Each Error:");
				metrics = rtk.getErrorsForTier(app.name, tier.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}
			}
		}

		System.out.println("------------------------");
		System.out.println("------------------------");
		System.out.println("\nLOAD DATA:\n");


		// METRICS
		//			rtk.getLoadForApp(app, duration, rollup)
		System.out.println("All Load Data from all Apps in the last 15 minutes:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			System.out.print("Load Data rollup: ");
			metrics = rtk.getLoadForApp(app.name, 15, true);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
			}

			System.out.println("Each Load Data:");
			metrics = rtk.getLoadForApp(app.name, 15, false);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
			}
		}
		
		//			rtk.getLoadForApp(app, startTime, endTime, rollup)
		System.out.println("------------------------");
		System.out.println("All Load Data from all Apps btwn 10 and 5 minutes ago:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);

			System.out.print("Load Data rollup: ");
			metrics = rtk.getLoadForApp(app.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, true);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
			}

			System.out.println("Each Load Data:");
			metrics = rtk.getLoadForApp(app.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, false);
			for(ADMetric metric : metrics){				
				System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
			}

		}
		
		//			rtk.getLoadForBT(appName, BT, duration, rollup)
		System.out.println("------------------------");
		System.out.println("All Load Data from all Apps and all BTs in the last 15 minutes:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			for(ADBusinessTransaction bt : rtk.getBTsFromApp(app.id)){
				System.out.println("- BT name: " + bt.name);
				
				System.out.print("Load Data rollup: ");
				metrics = rtk.getLoadForBT(app.name, bt.name, 15, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}

				System.out.println("Each Load Data:");
				metrics = rtk.getLoadForBT(app.name, bt.name, 15, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}
			}
		}		
		
		//			rtk.getLoadForBT(app, BT, startTime, endTime, rollup)		
		System.out.println("------------------------");
		System.out.println("All Load Data from all Apps and all BTs btwn 10 and 5 minutes ago:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			for(ADBusinessTransaction bt : rtk.getBTsFromApp(app.id)){
				System.out.println("- BT name: " + bt.name);

				System.out.print("Load Data rollup: ");
				metrics = rtk.getLoadForBT(app.name, bt.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}

				System.out.println("Each Load Data:");
				metrics = rtk.getLoadForBT(app.name, bt.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}
			}
		}

		//			rtk.getLoadForTier(app, tierName, duration, rollup)
		System.out.println("------------------------");
		System.out.println("All Load Data from all Apps and all Tiers in the last 15 minutes:\n");
		for(ADApplication app : apps){
			System.out.println("App name:\n" + app.name);
			for(ADTier tier : rtk.getTiersFromApp(app.id)){
				System.out.println("- Tier name: " + tier.name);
				
				
				System.out.print("Load Data rollup: ");
				metrics = rtk.getLoadForTier(app.name, tier.name, 15, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}

				System.out.println("Each Load Data:");
				metrics = rtk.getLoadForTier(app.name, tier.name, 15, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}
			}
		}
		
		//			rtk.getLoadForTier(app, tierName, startTime, endTime, rollup)
		System.out.println("------------------------");
		System.out.println("All Load Data from all Apps and all TIERs btwn 10 and 5 minutes ago:\n");
		for(ADApplication app : apps){
			System.out.println("App name: " + app.name);
			for(ADTier tier : rtk.getTiersFromApp(app.id)){
				System.out.println("- Tier name: " + tier.name);

				System.out.print("Load Data rollup: ");
				metrics = rtk.getLoadForTier(app.name, tier.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, true);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}

				System.out.println("Each Load Data:");
				metrics = rtk.getLoadForTier(app.name, tier.name, System.currentTimeMillis()-600000, System.currentTimeMillis()-300000, false);
				for(ADMetric metric : metrics){				
					System.out.println("time: " + new Date(metric.startTimeInMillis) + ", requested value: " + metric.value);
				}
			}
		}

		System.out.println("------------------------");
	}

}
