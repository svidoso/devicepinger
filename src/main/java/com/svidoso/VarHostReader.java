package com.svidoso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class VarHostReader {

    private Logger log = LoggerFactory.getLogger(VarHostReader.class.getSimpleName());
    private Map<String, List<String>> hostsPerVar = new HashMap<>();

    public Map<String, List<String>> getHostsPerVar() {
        return hostsPerVar;
    }

    public void read(String propertiesFile) throws IOException {
        hostsPerVar.clear();

        Properties props = new Properties();
        props.load(VarHostReader.class.getResourceAsStream(propertiesFile));

        props.entrySet().forEach(objectObjectEntry -> {
            List<String> hosts = Arrays.asList(objectObjectEntry.getValue().toString().split(","));
            hostsPerVar.put((String) objectObjectEntry.getKey(), hosts.stream().map(String::trim).collect(Collectors.toList()));
        });

        log.info(hostsPerVar.toString());
    }
}
