# AppDynamics Controller REST API SDK

##Use Case

The Controller REST API SDK extension provides a high-level Java interface to accelerate your development of programs that use the AppDynamics REST interface.

##Directory Structure

| File/Folder | Description |
| --- | --- |
| Main Java File | src/com/appdynamics/REST/RESTToolkit.java - This file contains the functions that calls the rest API. |
| bin | Contains class files |
| lib | Contains Third-party project references |
| src | Contains source code to REST Toolkit |
| doc | Contains Javadocs |
| dist | Contains the distribution package (jar and license files) |
| build.xml | Ant build script to package the project (only required if changing Java code)

**Note**: In order to use the functions provided in the toolkit, the contents from the dist/lib folder should also be referenced in the project.

##Installation

1. Import RESTToolkit.jar (dist/jar/RESTToolkit.jar).

2. Import the sublibraries (dist/jar/lib/\*).

3. Instantiate the RESTToolkit class as follows:

        RESTToolkit toolkit = new RESTToolkit(username, password, controller url, extra parameters)

    where the 'username' specification is similar to "user@customer1"
    
    and the 'controller ulr' specification resembles "http://localhost:8090".

4. Call the functions such as in the following examples:

        toolkit.getApplications();
    Returns a list of applications.

        toolkit.getARTForApp(String app, int duration, boolean rollup);
    Returns a metric with the query results.

##Rebuilding the Project

1. From the command line, go to root directory of the REST Toolkit. 

2. Type "ant".

    The 'dist' directory will be updated with the packaged jar and libraries.


##Contributing

Always feel free to fork and contribute any changes directly via [GitHub](https://github.com/Appdynamics/java-sdk).

##Community

Find out more in the [AppSphere](http://appsphere.appdynamics.com/t5/Extensions/Java-SDK-for-AppDynamics/idi-p/899) community.

##Support

For any questions or feature request, please contact [AppDynamics Center of Excellence](mailto:ace-request@appdynamics.com).
