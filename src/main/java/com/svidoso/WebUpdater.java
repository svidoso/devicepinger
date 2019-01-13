package com.svidoso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUpdater {

    Logger log = LoggerFactory.getLogger(VarHostReader.class.getSimpleName());

    void updateVariable(String ise_id, long seconds) throws Exception {
        String url = "http://ccu3-webui/addons/xmlapi/statechange.cgi?ise_id=" + ise_id + "&new_value=" + seconds;
        log.info("Sending: " + url);
        getHTML(url);
    }

    public String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}
