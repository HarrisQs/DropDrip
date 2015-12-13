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

    Map<String, String> info = new HashMap<String, String>();

    public Map<String, String> Showinfo() throws Exception
    {
        Thread Subthread = new Thread(mutiThread);//開一個新的線程，去執行網路連線
        Subthread.start();//開始線程
        Thread.sleep(1000L);//因為網路連線需要時間，所以需要等去連線的網路線程回來
        Log.v("info",info.get("link"));
        return info;
    }
    //用Runnable包起來子線程要做的事情 -- 網路連線
    private Runnable mutiThread = new Runnable(){
        public void run(){
            // 運行網路連線的程式
            try {
                URL url = new URL("http://www.cwb.gov.tw/V7/observe/rainfall/Rain_Hr/3.htm");
                Document xmlDoc = Jsoup.parse(url, 3000);
                Elements link = xmlDoc.select("span");
                info.put("link", link.get(8).text());
            }
            catch (Exception e)
            {
                Log.v("Internet connectionless", e.toString());
            }
        }
    };
}
