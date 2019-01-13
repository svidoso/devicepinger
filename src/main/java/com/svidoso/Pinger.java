package com.svidoso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pinger {

    Logger log = LoggerFactory.getLogger(Pinger.class.getSimpleName());

    Thread worker;

    private Map<String, List<String>> hostsPerVar;

    private WebUpdater webUpdater;
    private long intervalMS = 10000;

    private volatile boolean quit = false;

    public Pinger(WebUpdater webUpdater, Map<String, List<String>> hostsPerVar, long intervalMS) {
        this.hostsPerVar = hostsPerVar;
        this.intervalMS = intervalMS;
        this.webUpdater = webUpdater;
        worker = new Thread(this::loop);
        worker.start();
    }

    public void stop() {
        this.quit = true;
        worker.interrupt();
    }

    private void loop(){
        while(!quit){
            hostsPerVar.forEach((key, value) -> {
                try {
                    if (value.stream().anyMatch(this::isReachable)) {
                        webUpdater.updateVariable(key, System.currentTimeMillis()/1000L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            try {
                Thread.sleep(intervalMS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isReachable(String host){
        try {
            InetAddress address = InetAddress.getByName(host);
            return address.isReachable(500);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

}
