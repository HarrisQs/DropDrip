package org.rita.harris.embeddedsystemhomework_termproject.UserData;

import android.content.SharedPreferences;
import android.util.Log;

import org.rita.harris.embeddedsystemhomework_termproject.MainActivity;

/**
 * Created by Harris on 2015/12/17.
 */
public class User_BasicData {
    private String Account = "";
    private String Password = "";
    private String NickName = "";

    public void setAccount(String Account)
    {
        this.Account = Account;
    }
    public void setPassword(String Password)
    {
        this.Password = Password;
    }
    public void setNickName(String Nickname)
    {
        this.NickName = Nickname;
    }
    public String getAccount()
    {
        return Account;
    }
    public String getPassword()
    {
        return Password;
    }
    public String getNickName()
    {
        return NickName;
    }
    //   <Navigation> 判斷reference裡面是否有存帳號資訊
    public boolean IsChangeButtonText()
    {
        SharedPreferences settings = MainActivity.MainActivity_Context().getSharedPreferences("AccountData", 0);
        String Account = settings.getString("Account", "");
        String Password = settings.getString("Password","");
        String Nickname = settings.getString("Nickname","");
        Log.v("Mail",Account);
        Log.v("Password",Password);
        Log.v("Nickname",Nickname);
        if(Account == "" && Password == "")//裡面是空的代表沒有帳號的資訊
            return false;
        else {
            this.Account = Account;
            this.Password = Password;
            this.NickName = Nickname;
            return true;
        }
    }
}

