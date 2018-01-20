package com.myj.service.impl;

import com.myj.model.Movie;
import com.myj.service.PageAnalysis;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class DyttPageAnalysis implements PageAnalysis{
    public Movie extractPageInfo(String html){
        if(null == html || "".equals(html)){
            return null;
        }
        Movie movie = new Movie();
        Document doc = Jsoup.parse(html);
        Element downUrlElement = doc.getElementsByClass("downUrl").first();
        if(downUrlElement == null){
            return null;
        }
        Elements aElements = downUrlElement.getElementsByTag("a");
        List<String> downloadUrlList = new ArrayList<String>();
        for(Element element : aElements){
            downloadUrlList.add(element.attr("href"));
        }
        movie.setDownloadUrlList(downloadUrlList);

        //栏目
        String lm = doc.getElementsByClass("lm").first().text();
        String[] lmArr = lm.split(">");
        //电影分类
        movie.setType(lmArr[0]);
        //电影标题
        movie.setTitle(lmArr[1]);
        return movie;
    }

    public boolean isTargetPage(String url){
        String regex = "^https?://www.loldytt.com/\\S+/\\S+$";
        String pageRegex = "\\S+/\\S+/\\S+.html";
        Pattern pattern = Pattern.compile(regex);
        Pattern pagePattern = Pattern.compile(pageRegex);
        if(pattern.matcher(url).matches() && !pagePattern.matcher(url).matches()){
            return true;
        }
        return false;
    }

    public void printToFile(List<Movie> movieList, ReentrantLock lock){
        int startIndex = 1;
        StringBuilder sb = new StringBuilder();
        List<Movie> removeList = new ArrayList<Movie>();
        lock.lock();
        for(Movie movie : movieList){
            sb.append(startIndex++).append(":").append(movie.getTitle()).append("\t").append(movie.getDownloadUrlList().get(0)).append("\r\n");
            removeList.add(movie);
        }
        movieList.removeAll(removeList);
        lock.unlock();
        File file = new File("dytt.txt");
        OutputStream out = null;
        try{
            out = new FileOutputStream(file, true);
            out.write(sb.toString().getBytes("utf-8"));
            out.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if(out != null){
                try{
                    out.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
