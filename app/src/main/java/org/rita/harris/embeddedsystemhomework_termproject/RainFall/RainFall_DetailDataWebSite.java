package org.rita.harris.embeddedsystemhomework_termproject.RainFall;

import android.util.Log;

/**
 * Created by Harris on 2015/12/13.
 */
public class RainFall_DetailDataWebSite
{
    private String PalceName [] = {"龜山","平鎮","龍潭","蘆竹","桃園","大園","中壢","楊梅"
                                    ,"八德","大溪","觀音","水尾","復興","拉山","蘇樂","新屋"};
    private String WebSite [] = {"http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C64&xid=3",//龜山
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C65&xid=3",//平鎮
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C67&xid=3",//龍潭
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C62&xid=3",//蘆竹
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C48&xid=3",//桃園
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C54&xid=3",//大園
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C52&xid=3",//中壢
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C66&xid=3",//楊梅
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C49&xid=3",//八德
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C63&xid=3",//大溪
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C59&xid=3",//觀音
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C1C51&xid=3",//水尾
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=C0C46&xid=3",//復興
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=A0C54&xid=3",//拉拉山
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=88C69&xid=3",//蘇樂
                                 "http://www.cwb.gov.tw/V7/observe/rainfall/Plot/plotChart_All2.php?stid=46705&xid=3"//新屋
    };

    public String get_WebSite_of_Some_Place(String PlaceName)
    {
        int Comma = PlaceName.indexOf(",");
        String Observatory = PlaceName.substring (Comma-2,Comma);
        for(int i = 0 ; i < 16; i++)
            if(PalceName [i].equals(Observatory))
                return WebSite [i];

        return "http://www.cwb.gov.tw/V7/observe/rainfall/Rain_Hr/3.htm";
    }

}
