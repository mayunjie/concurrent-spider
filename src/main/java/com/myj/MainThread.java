package com.myj;

import com.myj.model.Movie;
import com.myj.model.UrlCollection;
import com.myj.service.PageAnalysis;
import com.myj.service.UrlInterceptor;
import com.myj.service.impl.DyttInterceptor;
import com.myj.service.impl.DyttPageAnalysis;
import com.myj.util.DownloadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class MainThread{

    private static UrlCollection urlCollection = new UrlCollection();

    private UrlInterceptor urlInterceptor;

    private String[] seeds;

    private PageAnalysis pageAnalysis;

    private List<Movie> movieList = new ArrayList<Movie>(1000);

    private ReentrantLock lock = new ReentrantLock();

    private int startIndex = 1;
    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public PageAnalysis getPageAnalysis() {
        return pageAnalysis;
    }

    public void setPageAnalysis(PageAnalysis pageAnalysis) {
        this.pageAnalysis = pageAnalysis;
    }

    public MainThread(UrlInterceptor urlInterceptor, PageAnalysis pageAnalysis, String[] seeds){
        this.urlInterceptor = urlInterceptor;
        this.pageAnalysis = pageAnalysis;
        this.seeds = seeds;
        initUrlCollection();
    }

    public void initUrlCollection(){
        for(String seed : seeds){
            urlCollection.enqueueUnVisitedUrl(seed);
        }
    }
    public void crawl(){
        String url = null;
        while((url = urlCollection.dequeueUnVisitedUrl()) != null){
            urlCollection.addVisitedUrl(url);
            String html = DownloadUtils.getPageDomString(url);
            System.out.println(Thread.currentThread() + url);
            DownloadUtils.extractLinks(html, urlCollection, urlInterceptor);
            if(pageAnalysis.isTargetPage(url)){
                Movie movie = (Movie) pageAnalysis.extractPageInfo(html);
                if(movie != null){
                    //movieList需要同步
                    lock.lock();
                    movieList.add(movie);
                    lock.unlock();
                }
            }
        }
    }

    public void printToFile(List<Movie> movieList){
        pageAnalysis.printToFile(movieList, lock);
    }

    public static void main(String[] args) {
        final MainThread main = new MainThread(new DyttInterceptor(), new DyttPageAnalysis(),
                new String[]{"https://www.loldytt.com/"});
        ExecutorService service = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i++){
            service.submit(new Runnable() {
                public void run() {
                    main.crawl();
                }
            });
        }
        service.shutdown();
        while(!service.isTerminated()){
            if(main.getMovieList().size() >= 1000){
                service.shutdownNow();
            }
        }
        System.out.println("启动写操作！=================================");
        main.printToFile(main.getMovieList());
    }


}
