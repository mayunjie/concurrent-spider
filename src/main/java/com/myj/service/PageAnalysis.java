package com.myj.service;

import com.myj.model.Movie;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  分析页面，爬取有用信息
 */
public interface PageAnalysis {

    Object extractPageInfo(String html);

    /**
     * 定义页面url规则
     * @param url
     * @return
     */
    boolean isTargetPage(String url);

    /**
     * 导出结果
     * @param movieList
     * @param lock
     */
    void printToFile(List<Movie> movieList, ReentrantLock lock);
}
