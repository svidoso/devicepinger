package com.svidoso;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pinger {
    Thread worker;

    private Map<String, List<String>> hostsPerVar;
    private Map<String, Long> lastContact = new HashMap<>();

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
                    lastContact.putIfAbsent(key, 0L);
                    if (value.stream().noneMatch(this::isReachable)) {
                        webUpdater.updateVariable(key, System.currentTimeMillis() / 1000 - lastContact.get(key));
                    } else {
                        webUpdater.updateVariable(key, 0);
                        lastContact.put(key, System.currentTimeMillis() / 1000);
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
            e.printStackTrace();
        }
        return false;
    }

}
