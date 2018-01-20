package com.myj.model;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 资源链接数据结构
 */
public class UrlCollection {
    /**
     * 待爬取得url集合
     */
    private BlockingQueue<String> unVisitedUrl = new LinkedBlockingQueue<String>();

    /**
     * 已爬取得url集合
     */
    private HashSet<String> visitedUrl = new HashSet<String>();

    public void addVisitedUrl(String url){
        visitedUrl.add(url);
    }

    public void removeVisitedUrl(String url){
        visitedUrl.remove(url);
    }

    public int getVisitedNum(){
        return visitedUrl.size();
    }

    public void enqueueUnVisitedUrl(String url){
        try{
            if(!unVisitedUrl.contains(url) && !visitedUrl.contains(url)){
                unVisitedUrl.put(url);
            }
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

    }

    public String dequeueUnVisitedUrl(){
        try{
            return unVisitedUrl.take();
        }
        catch(InterruptedException e){
            e.printStackTrace();
            return null;
        }

    }

    public String dequeueUnVisitedUrl(long mills){
        try{
            return unVisitedUrl.poll(mills, TimeUnit.MILLISECONDS);
        }
        catch(InterruptedException e){
            e.printStackTrace();
            return "";
        }
    }

    public boolean isUnVisitedEmpty(){
        return unVisitedUrl.isEmpty();
    }

    public int getUnVisitedNum(){
        return unVisitedUrl.size();
    }
}
