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
        query = ParseQuery.getQuery("RescueTeam");
        query.whereGreaterThan("IsTitle",0);//GET 是為了能夠找到資料
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    try {
                        String Title, TrueName, Place, Cellphone, Description, UpdateAt;
                        int IsTitle = 0;
                        for (int i = 0; i < scoreList.size(); i++) {
                            HashMap<String, String> info = new HashMap<String, String>(); // 每個裡面都有一個 key 和一個 value ，而 key 是獨一無二的絕不重複，重複會覆蓋裡面原本的值
                            HashMap<String, String> DetailData = new HashMap<String, String>();
                            Title = query.get(scoreList.get(i).getObjectId().toString()).getString("Title");
                            TrueName = query.get(scoreList.get(i).getObjectId().toString()).getString("TrueName");
                            Place = query.get(scoreList.get(i).getObjectId().toString()).getString("Place");
                            Cellphone = query.get(scoreList.get(i).getObjectId().toString()).getString("CellPhone");
                            IsTitle = query.get(scoreList.get(i).getObjectId().toString()).getInt("IsTitle");
                            Description = query.get(scoreList.get(i).getObjectId().toString()).getString("Description");
                            UpdateAt = query.get(scoreList.get(i).getObjectId().toString()).getUpdatedAt().toString();
                            info.put("型態&發起人", "總類 : " + Title+ "\n發起人 : " + TrueName);
                            info.put("地點&聯絡方式", "地點 : " + Place + "\t聯絡方式 : " + Cellphone);
                            DetailData.put("Title", Title);
                            DetailData.put("TrueName", TrueName);
                            DetailData.put("Place", Place);
                            DetailData.put("Cellphone", Cellphone);
                            DetailData.put("Description", Description);
                            DetailData.put("IsTitle", Integer.toString(IsTitle));
                            DetailData.put("UpdateAt", UpdateAt);
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
