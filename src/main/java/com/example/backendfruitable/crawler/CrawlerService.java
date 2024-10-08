package com.example.backendfruitable.crawler;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrawlerService {
    private final WebDriver webDriver;
    private final WebDriverWait wait;

    public void CrawlerZara(String url){
        
    }
}
