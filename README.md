#Mobiilitiedekerhon backend#

Master contains the state of Mobiilitiedekerho project's back-end server code at the of the course.

##How to compile the project?##

- mvn compile

How to run the project?

- mvn exec:java -Dexec.mainClass="fi.helsinki.cs.mobiilitiedekerho.backend.App"

or

- run the shell script: "run_backend.sh"

##All dependencies can be found in pom.xml##
- Maven will download them for you if needed.
- If a new version of a dependency is released (and is desired to be used) then change the old dependency-declaration in pom.xml for the new one.
