Valuereporter-Agent
===================

This agent will monitor all calls to public methods. When a method is called, start-time and end-time will be forwarded
to the Valuereporter service.

Installation
===================

Start your appserver/program with an additional command -javaagent:<path to Valuereporter-Agent>=<properties>

__Example:__
```
java -javaagent:../valuereporter-agent/valuereporter-agent-jar-with-dependencies.jar= \
base.package:com.example,valuereporter.host:localhost,valuereporter.port:4901,prefix:myService \
 -jar <your jar file>
```

__Example Tomcat:__

In catalina.bat, add to top of the file.
```
SET JAVA_OPTS=%JAVA_OPTS% -javaagent:../valuereporter-agent/valuereporter-agent-jar-with-dependencies.jar= \
base.package:com.example,valuereporter.host:localhost,valuereporter.port:4901,prefix:myService \
```

Configuration
===================

* __base.package__ - The package you want to scan. Example: com.example
* __valuereporter.host__ - Where ValueReporter is running. (optional) Example: localhost
* __valuereporter.port__ - Port of  ValueReporter (optional) Example: 8080
* __prefix__  - unique identifier for this service, and node. Used to identify the input from multiple services
and nodes, in Valuereporter

Future improvements
===================

- When multiple methods has the same name, and different parameters, they will now be reported as one method.
For the time beeing, this is not a crucial factor, thoug. When the GUI of Valuereporter are able show these variations
with the same method name, in a propper way. Then we can start reporting on individual variations from the same method-
name.

- Time-based reporting. Ensure that data are sent from Valuereporter-agent at given intervals. Current implementation
will, when activity on the monitored application is low, hold the data for a long period of time.

Manual Testing
===================

Use class CommandActivitySenderTest
