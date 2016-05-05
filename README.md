#Mobiilitiedekerhon backend#

Master contains the state of Mobiilitiedekerho project's back-end server code at the of the course.

##Project compilation and deploying to production##
All mvn-commands must be done in the directory where the pom.xml is (or you can point to it "from outside")
###How to compile the project?###
- mvn compile
- use mvn compile -e in case of errors to check what they were.

###How to run the project?###
You can simply run the shell script: "run_backend.sh" for this.
-  mvn exec:java -Dexec.mainClass="fi.helsinki.cs.mobiilitiedekerho.backend.App"

###How to deploy to production###
You can use the shell script "deploy_to_production.sh" in misc-mt for this (you need the ssh key in your computer for this)  
Or manually (no need to store keys, more fexibility)
- mvn package
- scp [point to the packaged jar, the one with the dependencies] mobiili@[Server's IP] [where to store the jar]
- ssh to the server (in your preffered way)
- Finally start the back-end server from the jar using the mobiilitiedekerho.sh -script:  
  Run mobiilitiedekerho.sh stop if needed  
  Run mobiilitiedekerho.sh start  
  (In misc-mt you can see the contents of mobiilitiedekerho.sh)

##All dependencies can be found in pom.xml##
- Maven will download them for you if needed.
- If a new version of a dependency is released (and is desired to be used) then change the old dependency-declaration in pom.xml for the new one.
