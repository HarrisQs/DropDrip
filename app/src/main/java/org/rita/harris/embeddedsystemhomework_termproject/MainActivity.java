package org.rita.harris.embeddedsystemhomework_termproject;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.ParseException;

import org.rita.harris.embeddedsystemhomework_termproject.AccountData.LoginActivity;
import org.rita.harris.embeddedsystemhomework_termproject.AccountData.RegesterActivity;
import org.rita.harris.embeddedsystemhomework_termproject.AddNewItem.Add_Asylum_PointActivity;
import org.rita.harris.embeddedsystemhomework_termproject.GoogleMap.MapsActivity;
import org.rita.harris.embeddedsystemhomework_termproject.RainFall.RainFall_DetailDataWebSite;
import org.rita.harris.embeddedsystemhomework_termproject.RainFall.RainFall_WebDataParse;
import org.rita.harris.embeddedsystemhomework_termproject.Rescue_team.Add_Rescue_Team;
import org.rita.harris.embeddedsystemhomework_termproject.Rescue_team.RescueTeamActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //<共同>變數
    private Toolbar toolbar;
    private static Context mContext;
    private NavigationView navigationView;
    private static StarterApplication mUser_BasicData;
    private static StarterApplication globalMap;
    public static View rootView;
    private boolean IN = false;
    public static int who = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Common_initialize();
        Navigation_initialize();
        ActionBar_initialize();
        setTitle("");
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        ChangeItem();
        if(IN)
        {
            try {
                globalMap.GlobalMapData.RefreshData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                globalMap.mRescue_team_Data.RefreshData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            PlaceholderFragment.onxCreateView();
        }
        else
        {
            IN = true;
        }

    }

            //<MainActivity> Context get
    public static Context MainActivity_Context()
    {
        return mContext;
    }

            //<共同> 初始化
    public void Common_initialize()
    {
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mUser_BasicData = (StarterApplication) MainActivity.MainActivity_Context().getApplicationContext();
        globalMap = (StarterApplication) MainActivity.MainActivity_Context().getApplicationContext();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    globalMap.GlobalMapData.RefreshData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    globalMap.mRescue_team_Data.RefreshData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Snackbar.make(view, "更新了所有資料 ... ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override // <共同>將MENU初始化的
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override // <共同>選擇 MENU中的哪一個
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
            //<Navigation> 初始化
    public void Navigation_initialize()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);//右上三條線 也是另一種觸發NAVIGATION的方法
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);//設置這個CLASS是navigation ITEM的監聽者
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override // <Navigation> 處裡返回鍵的
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void ChangeItem()
    {
        final Menu subMenu = navigationView.getMenu();
        if(mUser_BasicData.mUser_BasicData.IsChangeButtonText())//reference 有東西 所以要改成 ("登出") ("更改帳號")
        {
            subMenu.findItem(R.id.Login_Logout).setTitle("登出");
            subMenu.findItem(R.id.New_Change_account).setTitle("更換帳號");
        }
        else // reference 沒有東西 所以要改成 ("登入") ("註冊")
        {
            subMenu.findItem(R.id.Login_Logout).setTitle("登入");
            subMenu.findItem(R.id.New_Change_account).setTitle("註冊");
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override// <Navigation> Item監聽者
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.New)//庇護點
        {
            if(mUser_BasicData.mUser_BasicData.IsChangeButtonText()) {//登入狀態
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Add_Asylum_PointActivity.class);
                startActivity(intent);
                who = 1;
            }
            else
                Toast.makeText(MainActivity_Context(), "Please Log In, Thank you. " , Toast.LENGTH_LONG).show();//沒有登入
        }
        else if (id == R.id.Rescue_Team)//救難隊
        {
            if(mUser_BasicData.mUser_BasicData.IsChangeButtonText()) {//登入狀態
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Add_Rescue_Team.class);
                startActivity(intent);
                who = 3;
            }
            else
                Toast.makeText(MainActivity_Context(), "Please Log In, Thank you. " , Toast.LENGTH_LONG).show();//沒有登入
        }
        else if (item.getTitle().toString().equals("登入"))//因為沒有ID所以只能比標題
        {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            // TODO: 2015/12/17 Set NickName for Log in successful user
        }
        else if (item.getTitle().toString().equals("註冊"))//因為沒有ID所以只能比標題
        {
            //  2015/12/16 Register New account
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, RegesterActivity.class);
            startActivity(intent);
        }
        else if (item.getTitle().toString().equals("登出"))//因為沒有ID所以只能比標題
        {
            //  2015/12/16 Clear Reference
            SharedPreferences settings = getSharedPreferences("AccountData", 0);
            settings.edit().putString("Account", "").commit();
            settings.edit().putString("Password", "").commit();
            settings.edit().putString("Nickname", "").commit();
            ChangeItem();
        }
        else if (item.getTitle().toString().equals("更換帳號"))//因為沒有ID所以只能比標題
        {
            SharedPreferences settings = getSharedPreferences("AccountData", 0);
            settings.edit().putString("Account", "").commit();
            settings.edit().putString("Password", "").commit();
            settings.edit().putString("Nickname", "").commit();
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
            //<ActionBar> 初始化
    public void ActionBar_initialize()
    {
        SectionsPagerAdapter mSectionsPagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager());//處理要顯示幾頁

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container); //處理內容顯示的
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs); //處理TOOLBAR的下一行
        tabLayout.setupWithViewPager(mViewPager);
    }
            //<ActionBar> 處理分業的CLASS
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position+1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "避難點&緊急事件";
                case 1:
                    return "累積雨量";
                case 2:
                    return "救難隊";
            }
            return null;
        }
    }

            //<ActionBar> 處理內容的CLASS
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        //回傳一個新的入口為了這個fragment 給他一個section 數字
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);//為了判斷現在是哪一個FRAGMENT
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            switch(getArguments().getInt(ARG_SECTION_NUMBER, 0)) {
                case 1:
                    Map_CatchData(rootView);
                    break;
                case 2:
                    RainFall_CatchData(rootView);
                    break;
                case 3:
                    Founded_Rescue_team(rootView);
                break;
            }
            return rootView;
        }
        public static void onxCreateView() {
            Log.e("whi",Integer.toString(who));
            switch (who) {
                case 1:
                    Founded_Rescue_team(rootView);
                    //RainFall_CatchData(rootView);
                    Map_CatchData(rootView);
                    break;
                case 3:

                    Map_CatchData(rootView);
                    //RainFall_CatchData(rootView);
                    Founded_Rescue_team(rootView);
                    break;
            }
        }
    }
            // <RainFall> 去呼叫抓取雨量的資料
    public static void RainFall_CatchData(View rootView)
    {
        List<HashMap<String,String>> descript = new ArrayList<HashMap<String,String>>();
        final List<HashMap<String,String>> descript_Temp ;
        SimpleAdapter adapter;
        RainFall_WebDataParse parse = new RainFall_WebDataParse();

        try {
            descript = parse.Showinfo();// 去別的class 中抓取資料
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(MainActivity_Context(), descript, android.R.layout.simple_list_item_2,
                new String[] { "所在鄉鎮&觀測站","二十四小時累積雨量" }, new int[] { android.R.id.text1,android.R.id.text2 } );//將抓到的資料放list中
        ListView Main_ListView= (ListView)rootView.findViewById(R.id.MainlistView);
            Main_ListView.setAdapter(adapter);
        //TODO 更新時間 Toast.makeText(MainActivity_Context(), parse.getUpdateTime() , Toast.LENGTH_LONG).show();//顯示更新時間

        descript_Temp = descript; // 因為下面一定要用final
        final RainFall_DetailDataWebSite ShowWebSite = new RainFall_DetailDataWebSite();
        Main_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//判斷USER點擊哪個Item，點後會跳到對應的網站
                Uri uri=Uri.parse(ShowWebSite.get_WebSite_of_Some_Place(descript_Temp.get(position).toString()));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity_Context().startActivity(intent);
            }
        });
    }
    public static void Map_CatchData(View rootView)
    {
        List<HashMap<String,String>> descript = new ArrayList<HashMap<String,String>>();
        SimpleAdapter adapter;

        try {
            descript = globalMap.GlobalMapData.CatchData();// 去別的class 中抓取資料
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(MainActivity_Context(), descript, android.R.layout.simple_list_item_2,
                new String[] { "型態&發起人","地點&聯絡方式" }, new int[] { android.R.id.text1,android.R.id.text2 } );//將抓到的資料放list中
        ListView Main_ListView= (ListView)rootView.findViewById(R.id.MainlistView);
        Main_ListView.setAdapter(adapter);

        Main_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//判斷USER點擊哪個Item，點後會跳到對應的網站
                Bundle mBundle = new Bundle();
                mBundle.putInt("Show", position);
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(MainActivity.MainActivity_Context(), MapsActivity.class);
                intent.putExtras(mBundle);
                MainActivity_Context().startActivity(intent);
            }
        });
    }
    public static void Founded_Rescue_team(View rootView)
    {
        List<HashMap<String,String>> descript = new ArrayList<HashMap<String,String>>();
        SimpleAdapter adapter;

        try {
            descript = globalMap.mRescue_team_Data.CatchData();// 去別的class 中抓取資料
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(MainActivity_Context(), descript, android.R.layout.simple_list_item_2,
                new String[] { "型態&發起人","地點&聯絡方式" }, new int[] { android.R.id.text1,android.R.id.text2 } );//將抓到的資料放list中
        ListView Main_ListView= (ListView)rootView.findViewById(R.id.MainlistView);
        Main_ListView.setAdapter(adapter);

        Main_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//判斷USER點擊哪個Item，點後會跳到對應的網站
                Bundle mBundle = new Bundle();
                mBundle.putInt("Show", position);
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(MainActivity.MainActivity_Context(), RescueTeamActivity.class);
                intent.putExtras(mBundle);
                MainActivity_Context().startActivity(intent);
            }
        });
    }
}

