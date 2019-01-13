#### Usage

Will update "Systemvariablen" in homematic ccu3 based on last successful 
ping of specified devices in "src/main/resources/devicevars.properties".
Might need to adjust url in WebUpdater.java.

#### CCU3 Dependancies

	"XML-API"
	"HQ WebUI" (to find ise_id of systemvariable)
	https://www.homematic-inside.de/addons

#### Build

	mvn clean install assembly:single
