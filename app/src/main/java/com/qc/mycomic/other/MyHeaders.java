package com.qc.mycomic.other;

import com.bumptech.glide.load.model.Headers;
import com.qc.mycomic.util.Codes;

import java.util.HashMap;
import java.util.Map;

public class MyHeaders implements Headers {

    String referer;

    public MyHeaders(String referer) {
        this.referer = referer;
    }

    public MyHeaders() {
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>();
        if (referer != null) {
            map.put("Referer", referer);
        }
        map.put("User-Agent", Codes.USER_AGENT);
        return map;
    }

}
