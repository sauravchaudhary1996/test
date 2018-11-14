package com.example.redundant.webdata;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

public class GettingData {
	
	
	public Map<String, String> getWebData(String url) {
        System.out.println("Inside getWebData for URL: " + url);
        Map<String, String> webData = new HashMap<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Exception for URL: with exception message" + e.getMessage());
            return null;
        }
 
        String title = doc.title();
        webData.put("title", title);
        String keywords = doc.select("meta[name=keywords]").first().attr("content");
        webData.put("keywords", keywords);
        String description = doc.select("meta[name=description]").get(0).attr("content");
        webData.put("description", description);
        return webData;
    }
 

}
