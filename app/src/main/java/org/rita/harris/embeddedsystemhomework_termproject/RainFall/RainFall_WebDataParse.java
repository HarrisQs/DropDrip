package org.rita.harris.embeddedsystemhomework_termproject.RainFall;

/**
 * Created by Harris on 2015/12/13.
 */
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RainFall_WebDataParse {

    Map<String, String> info = new HashMap<String, String>(); // 每個裡面都有一個 key 和一個 value ，而 key 是獨一無二的絕不重複，重複會覆蓋裡面原本的值
    Elements Place ;
    Elements Observatory;

    public Map<String, String> Showinfo() throws Exception
    {
        Thread Subthread = new Thread(mutiThread);//開一個新的線程，去執行網路連線
        Subthread.start();//開始線程
        Thread.sleep(1000L);//因為網路連線需要時間，所以需要等去連線的網路線程回來

        String RainFall_Place;
        String RainFall_Observatory;

        /**
         * 這裡再將每個地區的名稱，跟觀測站的名稱，還有雨量，給結合起來，放到MAP中
         * 總共有467個("td")，所以 (467-3)/29 = 16，3+29*i 是地區名稱 ， 29+29*i 是24小時的雨量
         * 總共有17個("a")，而第一個是不重要的，所以都是i+1
         */
        for(int i = 0 ;i < 16; i++)
        {
            RainFall_Place = Place.get(3+29*i).text();
            RainFall_Observatory = Observatory.get(i+1).text();
            info.put(RainFall_Place+"，"+RainFall_Observatory, Place.get(29+29*i).text());
        }
        return info;
    }

    //用Runnable包起來子線程要做的事情 -- 網路連線
    private Runnable mutiThread = new Runnable(){
        public void run(){
            try {
                URL url = new URL("http://www.cwb.gov.tw/V7/observe/rainfall/Rain_Hr/3.htm");
                Document xmlDoc = Jsoup.parse(url, 3000);
                Place = xmlDoc.select("td");//在網頁原始碼裡面，標籤是td的都會被抓出來
                Observatory = xmlDoc.select("a");//在網頁原始碼裡面，標籤是a的都會被抓出來
            }
            catch (Exception e)
            {
                Log.v("Internet connectionless", e.toString());
            }
        }
    };
}
