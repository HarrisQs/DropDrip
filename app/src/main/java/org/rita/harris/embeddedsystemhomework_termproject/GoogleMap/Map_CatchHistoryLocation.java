package org.rita.harris.embeddedsystemhomework_termproject.GoogleMap;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Harris on 2015/12/19.
 */
public class Map_CatchHistoryLocation {

    private ParseQuery<ParseObject> query;
    private List<HashMap<String,String>> list;
    private ArrayList<HashMap<String,String>> Detaillist;
    private List<ParseObject> scoreList;
    public Map_CatchHistoryLocation() {
    }
    public void RefreshData() throws ParseException {
        query = ParseQuery.getQuery("Location");
        scoreList = query.find();
        list = new ArrayList<HashMap<String,String>>();
        Detaillist = new ArrayList<HashMap<String,String>>();
        Thread Subthread = new Thread(mutiThread);//開一個新的線程，去執行網路連線
        Subthread.start();
    }
    public List<HashMap<String,String>> CatchData() throws Exception
    {
        return list;
    }
    private Runnable mutiThread = new Runnable(){
        public void run(){
            String Type ,  TrueName , Place , Contact , Description, Latitude, Longitude , UpdateAt;
            for (int i = 0; i < scoreList.size(); i++) {
                HashMap<String, String> info = new HashMap<String, String>(); // 每個裡面都有一個 key 和一個 value ，而 key 是獨一無二的絕不重複，重複會覆蓋裡面原本的值
                HashMap<String, String> DetailData = new HashMap<String, String>();
                Type = scoreList.get(i).getString("Type");
                TrueName = scoreList.get(i).getString("TrueName");
                Place = scoreList.get(i).getString("Place");
                Contact =scoreList.get(i).getString("Contact");
                Description = scoreList.get(i).getString("Description");
                Latitude = scoreList.get(i).getString("Latitude");//緯度
                Longitude = scoreList.get(i).getString("Longitude");
                UpdateAt = scoreList.get(i).getUpdatedAt().toString();

                info.put("型態&發起人", "總類 : " + Type + "\n發起人 : " + TrueName);
                info.put("地點&聯絡方式", "地點 : " + Place + "\t聯絡方式 : " + Contact);
                list.add(info);

                DetailData.put("Type",Type);
                DetailData.put("TrueName",TrueName);
                DetailData.put("Place",Place);
                DetailData.put("Contact",Contact);
                DetailData.put("Description",Description);
                DetailData.put("Latitude",Latitude);//緯度
                DetailData.put("Longitude",Longitude);//經度
                DetailData.put("UpdateAt",UpdateAt);//經度
                Detaillist.add(DetailData);
            }
        }
    };
    public HashMap<String,String> getDetail(int i )
    {
        return Detaillist.get(i);
    }
    public int getDetail_Size( )
    {
        return Detaillist.size();
    }
}
