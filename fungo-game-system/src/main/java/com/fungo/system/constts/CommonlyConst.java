package com.fungo.system.constts;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lyc
 * @create 2019/5/7 11:15
 */
public class CommonlyConst {
    private static List communityList = new ArrayList<Integer>();
    private static List gameList = new ArrayList<Integer>();
    private static List systemList = new ArrayList<Integer>();
    static{
        //        社区
        communityList.add(1);
        communityList.add(2);
        communityList.add(4);
        communityList.add(5);
        communityList.add(7);
        communityList.add(8);
        communityList.add(11);

        //        游戏
        gameList.add(3);
        gameList.add(6);

        //        系统
        systemList.add(0);
        systemList.add(9);
    }
    private CommonlyConst(){
    }

    public static List getCommunityList() {
        return communityList;
    }

    public static List getSystemList() {
        return systemList;
    }

    public static List getGameList() {
        return gameList;
    }



}
