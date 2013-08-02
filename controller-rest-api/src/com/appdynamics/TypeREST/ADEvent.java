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

import java.util.ArrayList;

public class ADEvent
{
	public ArrayList<ADEntity> affectedEntities;
	public boolean archived;
	public String deepLinkUrl;
	public long eventTime;
	public int id;
	public boolean markedAsRead;
	public boolean markedAsResolved;
	public String severity;
	public String subType;
	public String summary;
	public ADEntity triggeredEntity;
	public String type;
}
