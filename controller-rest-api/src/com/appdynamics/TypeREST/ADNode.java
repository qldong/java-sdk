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




package com.appdynamics.TypeREST;

/* Example object
 *
 * 		"appAgentPresent": true,
        "appAgentVersion": "Server Agent v3.2.0.0 RC Build Date 2011-02-28 23:21:37",
        "id": 4,
        "machineAgentPresent": false,
        "machineAgentVersion": "",
        "machineId": 2,
        "machineName": "vinod-eligetis-macbook-pro-2.local",
        "machineOSType": "Mac OSX",
        "name": "Node_8002",
        "tierId": 3,
        "tierName": "Inventory Server",
        "type": "Tomcat 5.x"
 */

public class ADNode {
	public String appAgentPresent;
	public String appAgentVersion;
	public String id;
	public String machineAgentPresent;
	public String machineAgentVersion;
	public String machineId;
	public String machineName;
	public String machineOSType;
	public String name;
	public String nodeUniqueLocalId;
	public String tierId;
	public String tierName;
	public String type;
}
