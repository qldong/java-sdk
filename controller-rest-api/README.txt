 _______________________________________________________________________
|                              LICENSE                                  |
|                                                                       |
|=====================APACHE Common Codec 1.7===========================|
|                                                                       |
| Apache Common Codec 1.7 is licensed under the Apache License 2.0.     |
|  See the files called LICENSE.txt and NOTICE.txt for more information.|
|                                                                       |	
|=================APACHE HttpComponents Core 4.2.3======================|
|                                                                       |
| Apache HttpComponents Core is licensed under the Apache License 2.0.  |
| See the files called LICENSE.txt and NOTICE.txt for more information. |
|                                                                       |
|=================APACHE HttpComponents Core 4.2.2======================|
|                                                                       |
| Apache HttpComponents Core is licensed under the Apache License 2.0   |
| See the files called LICENSE.txt and NOTICE.txt for more information. |
|                                                                       |
 -----------------------------------------------------------------------

=========================================================================


The RESTToolkit provides a high level java interface to accelerate the 
development of the programs that use AppDynamics REST interface.

-------------------------------------------------------------------------
INFO
-------------------------------------------------------------------------

Files/Folders Included:

bin                   - Contains class files
lib                   - Contains Third-party project references
src                   - Contains source code to REST Toolkit
doc                   - Contains Javadocs
dist                  - Contains the distribution package 
                        (jar and license files)	      
build.xml             - Ant build script to package the project
			(only required if changing java code)

NOTE: In order to use functions provided in the toolkit, contents from 
      the dist/jar/lib folder should also be referenced in the project.


Main Java File: src/com/appdynamics/REST/RESTToolkit.java
	-> This file contains the functions that calls the rest API

------------------------------------------------------------------------
HOW TO USE
------------------------------------------------------------------------
1. Import RESTToolkit.jar (dist/jar/RESTToolkit.jar)
2. Import sub-libraries (dist/jar/lib/*)
3. Instantiate RESTToolkit class

RESTToolkit toolkit = new RESTToolkit(username, password, controller url (i.e.
	http://localhost:8090"), extra parameters)


4. Call functions 

Example:
	
	toolkit.getApplications();  -- Returns a list of Applications

	toolkit.getARTForApp(String app, int duration, boolean rollup)
				
				-- Returns a metric with the query result

------------------------------------------------------------------------
REBUILD PROJECT
------------------------------------------------------------------------

1. Go to root directory (where all the files are located) through command line
2. Type "ant" (without the quotes)
3. 'dist' will be updated with the packaged jar and libraries