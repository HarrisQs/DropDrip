package org.rita.harris.embeddedsystemhomework_termproject.Rescue_team;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HarrisQs on 2015/12/23.
 */
public class Rescue_team_CatchData {
    private ParseQuery<ParseObject> query;
    private ArrayList<HashMap<String,String>> list;
    private ArrayList<HashMap<String,String>> Detaillist;
    public Rescue_team_CatchData()
    {
        list = new ArrayList<HashMap<String,String>>();
        Detaillist = new ArrayList<HashMap<String,String>>();
        query = ParseQuery.getQuery("Location");
        query.whereEqualTo("Get", true);//GET 是為了能夠找到資料
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    try {
                        String Type ,  TrueName , Place , Contact , Description, Latitude, Longitude , UpdateAt;
                        for (int i = 0; i < scoreList.size(); i++) {
                            HashMap<String, String> info = new HashMap<String, String>(); // 每個裡面都有一個 key 和一個 value ，而 key 是獨一無二的絕不重複，重複會覆蓋裡面原本的值
                            HashMap<String, String> DetailData = new HashMap<String, String>();
                            Type = query.get(scoreList.get(i).getObjectId().toString()).getString("Type");
                            TrueName = query.get(scoreList.get(i).getObjectId().toString()).getString("TrueName");
                            Place = query.get(scoreList.get(i).getObjectId().toString()).getString("Place");
                            Contact = query.get(scoreList.get(i).getObjectId().toString()).getString("Contact");
                            Description = query.get(scoreList.get(i).getObjectId().toString()).getString("Description");
                            Latitude = query.get(scoreList.get(i).getObjectId().toString()).getString("Latitude");//緯度
                            Longitude = query.get(scoreList.get(i).getObjectId().toString()).getString("Longitude");
                            UpdateAt = query.get(scoreList.get(i).getObjectId().toString()).getUpdatedAt().toString();
                            info.put("型態&發起人", "總類 : " + Type + "\n發起人 : " + TrueName);
                            info.put("地點&聯絡方式", "地點 : " + Place + "\t聯絡方式 : " + Contact);
                            DetailData.put("Type",Type);
                            DetailData.put("TrueName",TrueName);
                            DetailData.put("Place",Place);
                            DetailData.put("Contact",Contact);
                            DetailData.put("Description",Description);
                            DetailData.put("Latitude",Latitude);//緯度
                            DetailData.put("Longitude",Longitude);//經度
                            DetailData.put("UpdateAt",UpdateAt);//經度
                            list.add(info);//把每筆資料分成三部分，放到Arraylist裡面
                            Detaillist.add(DetailData);
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Log.d("Location", "Error: " + e.getMessage());
                }
            }
        });
    }
    public ArrayList<HashMap<String,String>> CatchData() throws Exception
    {
        return list;
    }
    public HashMap<String,String> getDetail(int i )
    {
        return Detaillist.get(i);
    }
    public int getDetail_Size( )
    {
        return Detaillist.size();
    }
}
