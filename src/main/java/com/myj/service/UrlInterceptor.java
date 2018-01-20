package com.myj.service;

/**
 * url拦截器，定义拦截url的规则
 */
public interface UrlInterceptor {
    /**
     * 是否该拦截输入的url
     * @param url
     * @return 如果url不符合爬取标准，则返回true
     */
    boolean shouldIntercept(String url);

    /**
     * 处理特殊的url
     * @param url
     * @return
     */
    String dealWithUrl(String url);
}
