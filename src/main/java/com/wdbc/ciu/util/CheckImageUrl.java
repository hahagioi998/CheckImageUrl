package com.wdbc.ciu.util;

import java.net.HttpURLConnection;
import java.net.URL;

public class CheckImageUrl {
    public static boolean exist(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod ("HEAD");
            //超时时间
            huc.setConnectTimeout(5000);
            huc.connect();
            return huc.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }
}
