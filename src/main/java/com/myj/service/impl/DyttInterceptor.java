package com.myj.service.impl;

import com.myj.service.UrlInterceptor;

import java.util.regex.Pattern;

/**
 * 电影天堂网站url拦截器
 */
public class DyttInterceptor implements UrlInterceptor {
    public boolean shouldIntercept(String url) {
        if(!url.startsWith("https://www.loldytt.com") && !url.startsWith("http://www.loldytt.com")){
            return true;
        }
        return false;
    }

    public String dealWithUrl(String url){
        //处理分页的url
        String str = "/\\S+/chart/\\S+.html";
        Pattern pattern = Pattern.compile(str);
        if(pattern.matcher(url).matches()){
            url = "https://www.loldytt.com" + url;
        }
        return url;
    }
}
