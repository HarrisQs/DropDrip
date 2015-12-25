package org.rita.harris.embeddedsystemhomework_termproject.Rescue_team;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.ParseObject;

import org.rita.harris.embeddedsystemhomework_termproject.MainActivity;
import org.rita.harris.embeddedsystemhomework_termproject.R;
import org.rita.harris.embeddedsystemhomework_termproject.StarterApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RescueTeamActivity extends AppCompatActivity {

    private int Match;
    private StarterApplication RescueData;
    private List<HashMap<String,String>> descript = new ArrayList<HashMap<String,String>>();
    private EditText Name;
    private EditText Cellphone;

    public RescueTeamActivity() {
        Cellphone = null;
        Name = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                AlertDialog  dialog = new AlertDialog.Builder(RescueTeamActivity.this)
                        .setIcon(android.R.drawable.btn_star_big_on)
                        .setTitle("是否參加此次救難行動：")
                        .setPositiveButton("參加", onclick)
                        .setNeutralButton("取消", onclick)
                        .setNegativeButton("不參加", onclick).create();
                final View inputView = inflater.inflate(R.layout.customer_dialog, null);
                dialog.setView(inputView);
                Name = (EditText)inputView.findViewById(R.id.edit2Text);
                Cellphone = (EditText)inputView.findViewById(R.id.editText);
                dialog.show();
            }
        });
        RescueData = (StarterApplication) MainActivity.MainActivity_Context().getApplicationContext();
        Bundle mBundle =this.getIntent().getExtras();
        Match = mBundle.getInt("Show");
        setTitle(RescueData.mRescue_team_Data.getDetail(Match).get("Title"));
        List();
    }
    DialogInterface.OnClickListener onclick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_NEGATIVE:
                    ParseObject NEGATIVESaveObject = new ParseObject("Location");
                    NEGATIVESaveObject.put("TrueName", Name.getText().toString());
                    NEGATIVESaveObject.put("CellPhone", Cellphone.getText().toString());
                    NEGATIVESaveObject.put("WhichOne",RescueData.mRescue_team_Data.getDetail(Match).get("IsTitle"));
                    NEGATIVESaveObject.put("IsParticipate", "不參加");
                    NEGATIVESaveObject.saveInBackground();
                    break;
                case Dialog.BUTTON_POSITIVE:
                    ParseObject POSITIVESaveObject = new ParseObject("ReplyRescureTeam");
                    POSITIVESaveObject.put("TrueName", Name.getText().toString());
                    POSITIVESaveObject.put("CellPhone", Cellphone.getText().toString());
                    POSITIVESaveObject.put("WhichOne", RescueData.mRescue_team_Data.getDetail(Match).get("IsTitle"));
                    POSITIVESaveObject.put("IsParticipate", "參加");
                    POSITIVESaveObject.saveInBackground();
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    dialog.dismiss();
                    break;
            }

        }
    };

    private void List()
    {
        /**
         * 這裡分成三部分
         * 第一部分:發佈人的一些消息
         * 第二部分:其他人回覆參加或不參加的
         * 第三部分將上方收集到的資訊都寫入
         */
        HashMap<String, String> info = new HashMap<String, String>();
        info.put("型態&發起人", "發起人 : " + RescueData.mRescue_team_Data.getDetail(Match).get("TrueName")
                + "\t聯絡方式 : " + RescueData.mRescue_team_Data.getDetail(Match).get("Cellphone"));
        info.put("地點&聯絡方式", "地點 : " + RescueData.mRescue_team_Data.getDetail(Match).get("Place")
                + "\n發布時間 : " + RescueData.mRescue_team_Data.getDetail(Match).get("UpdateAt")
                + "\n詳細敘述 : " + RescueData.mRescue_team_Data.getDetail(Match).get("Description"));
        descript.add(info);
        for(int i = 0 ;i < RescueData.mRescue_team_Data.getReplyDetaillist_Size(); i++) {
            info = new HashMap<String, String>();
            if (RescueData.mRescue_team_Data.getReplyDetail(i).get("WhichOne").equals(RescueData.mRescue_team_Data.getDetail(Match).get("IsTitle"))) {
                info.put("型態&發起人", "留言人 : " + RescueData.mRescue_team_Data.getReplyDetail(i).get("TrueName")
                        + "\t聯絡方式 : " + RescueData.mRescue_team_Data.getReplyDetail(i).get("Cellphone"));
                info.put("地點&聯絡方式", "是否參加 : " + RescueData.mRescue_team_Data.getReplyDetail(i).get("IsParticipate")
                        + "\n發布時間 : " + RescueData.mRescue_team_Data.getReplyDetail(i).get("UpdateAt"));
                descript.add(info);
            }
        }
        SimpleAdapter adapter;
        adapter = new SimpleAdapter(MainActivity.MainActivity_Context(), descript, android.R.layout.simple_list_item_2,
                new String[] { "型態&發起人","地點&聯絡方式" }, new int[] { android.R.id.text1,android.R.id.text2 } );//將抓到的資料放list中
        ListView Main_ListView= (ListView) findViewById(R.id.Mainlist);
        Main_ListView.setAdapter(adapter);

    }
}
