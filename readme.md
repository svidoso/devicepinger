#### Usage

Will update "Systemvariablen" in homematic ccu3 based on last successful 
ping of specified devices in "src/main/resources/devicevars.properties".
Might need to adjust url in WebUpdater.java.

#### build

	mvn clean install assembly:single
