package com.myj.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储电影信息的数据结构
 */
public class Movie {
    /**
     * 电影标题
     */
    private String title;

    /**
     * 电影页面的url
     */
    private String url;

    /**
     * 下载链接
     */
    private List<String> downloadUrlList = new ArrayList<String>();

    /**
     * 电影分类
     */
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getDownloadUrlList() {
        return downloadUrlList;
    }

    public void setDownloadUrlList(List<String> downloadUrlList) {
        this.downloadUrlList = downloadUrlList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
