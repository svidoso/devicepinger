package com.svidoso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private void loop() {
        while (!quit) {
            hostsPerVar.forEach((key, value) -> {
                try {
                    Optional<String> first = value.stream().filter(this::isReachable).findFirst();
                    if (first.isPresent()) {
                        log.info("{} online.", first.get());
                        webUpdater.updateVariable(key, System.currentTimeMillis() / 1000L);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            });
            try {
                Thread.sleep(intervalMS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    private boolean isReachable(String host){
//        try {
//            InetAddress address = InetAddress.getByName(host);
//            return address.isReachable(500);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return false;
//    }

    private boolean isReachable(String host) {
        // in case of Linux change the 'n' to 'c'
        Process p1 = null;
        int returnVal = 1;
        try {
            // change to -n for windows
            p1 = Runtime.getRuntime().exec("ping -c 1 " + host);
            returnVal = p1.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
        return (returnVal == 0);
    }

}
