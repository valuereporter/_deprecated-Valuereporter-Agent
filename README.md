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
