package com.svidoso;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App{

    private static final String DEVICEVARS_FILE = "src/main/resources/devicevars.properties";
    private static long interval_s=60;

        public static void main(String[] args) throws IOException, InterruptedException {
            VarHostReader varHostReader = new VarHostReader();
            varHostReader.read(DEVICEVARS_FILE);
            WebUpdater webUpdater = new WebUpdater();
            Pinger pinger = new Pinger(webUpdater, varHostReader.getHostsPerVar(), interval_s*1000);
            synchronized (pinger) {
                pinger.wait();
            }
        }
}