Valuereporter-Agent
===================

This agent will monitor all calls to public methods. When a method is called, start-time and end-time will be forwarded
to the Valuereporter service.

Installation
===================

Start your appserver/program with an additional command -javaagent:<path to Valuereporter-Agent>=<properties>
Example:
java -javaagent:../valuereporter-agent/valuereporter-agent-jar-with-dependencies.jar=base.package:com.example,valuereporter.host:http://localhost:4901,prefix:<serviceId> -jar <your jar file>

Example Tomcat:
In catalina.bat, add to top of the file.
```
SET JAVA_OPTS=%JAVA_OPTS% -javaagent:/full/path/to/newrelic.jar
```

Configuration
===================

* base.package com.example
* valuereporter.host:http://localhost:4901
* prefix:<serviceId>  - unique identifier for this service, and node. Used to identify the input from multiple services
and nodes, in Valuereporter
