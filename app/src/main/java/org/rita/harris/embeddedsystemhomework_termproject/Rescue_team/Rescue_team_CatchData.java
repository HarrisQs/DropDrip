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
    private ParseQuery<ParseObject> Replyquery;
    private List<HashMap<String,String>> list;
    private ArrayList<HashMap<String,String>> Detaillist;
    private ArrayList<HashMap<String,String>> ReplyDetaillist;
    private List<ParseObject> scoreList;
    private List<ParseObject> ReplyscoreList;
    public Rescue_team_CatchData() throws ParseException {
        RefreshData();
    }
    public void RefreshData() throws ParseException {
        query = ParseQuery.getQuery("RescueTeam");
        scoreList = query.find();
        list = new ArrayList<HashMap<String,String>>();
        Detaillist = new ArrayList<HashMap<String,String>>();
        ReplyDetaillist = new ArrayList<HashMap<String,String>>();
        Thread Subthread = new Thread(mutiThread);//開一個新的線程，去執行網路連線
        Subthread.start();
        Replyquery = ParseQuery.getQuery("ReplyRescureTeam");
        ReplyscoreList = Replyquery.find();
        Thread ReplySubthread = new Thread(ReplymutiThread);//開一個新的線程，去執行網路連線
        ReplySubthread.start();
    }
    private Runnable mutiThread = new Runnable() {
        public void run() {
            String Title, TrueName, Place, Cellphone, Description, UpdateAt;
            int IsTitle;
            for (int i = 0; i < scoreList.size(); i++) {
                HashMap<String, String> info = new HashMap<String, String>(); // 每個裡面都有一個 key 和一個 value ，而 key 是獨一無二的絕不重複，重複會覆蓋裡面原本的值
                HashMap<String, String> DetailData = new HashMap<String, String>();
                Title = scoreList.get(i).getString("Title");
                TrueName = scoreList.get(i).getString("TrueName");
                Place = scoreList.get(i).getString("Place");
                Cellphone = scoreList.get(i).getString("CellPhone");
                IsTitle = scoreList.get(i).getInt("IsTitle");
                Description = scoreList.get(i).getString("Description");
                UpdateAt = scoreList.get(i).getUpdatedAt().toString();
                info.put("型態&發起人", "總類 : " + Title + "\n發起人 : " + TrueName);
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
        }
    };
    private Runnable ReplymutiThread = new Runnable() {
        public void run() {
            String TrueName, IsParticipate, Cellphone, WhichOne, UpdateAt;
            for (int i = 0; i < ReplyscoreList.size(); i++) {
                HashMap<String, String> DetailData = new HashMap<String, String>();
                TrueName = ReplyscoreList.get(i).getString("TrueName");
                Cellphone = ReplyscoreList.get(i).getString("CellPhone");
                WhichOne = ReplyscoreList.get(i).getString("WhichOne");
                IsParticipate = ReplyscoreList.get(i).getString("IsParticipate");
                UpdateAt = ReplyscoreList.get(i).getUpdatedAt().toString();
                DetailData.put("TrueName", TrueName);
                DetailData.put("Cellphone", Cellphone);
                DetailData.put("IsParticipate", IsParticipate);
                DetailData.put("WhichOne", WhichOne);
                DetailData.put("UpdateAt", UpdateAt);
                ReplyDetaillist.add(DetailData);
            }
        }
    };
    public List<HashMap<String,String>> CatchData() throws Exception
    {
        return list;
    }
    public HashMap<String,String> getDetail(int i )
    {
        return Detaillist.get(i);
    }
    public HashMap<String,String> getReplyDetail(int i )
    {
        return ReplyDetaillist.get(i);
    }
    public int getDetail_Size( )
    {
        return Detaillist.size();
    }
    public int getReplyDetaillist_Size( )
    {
        return ReplyDetaillist.size();
    }
}
