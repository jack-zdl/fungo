package com.fungo.games.consts;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  判断mq的业务流向
 *  消息大类型 社区:3,4,7 系统 1,2
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/20 13:30
 */
public class MQMessageTypeConstant {
    private static List communityList = new ArrayList<Integer>();
    private static List systemList = new ArrayList<Integer>();
    static{
        //        社区
        communityList.add(3);
        communityList.add(4);
        communityList.add(7);


//        系统
        systemList.add(1);
        systemList.add(2);
    }
    private MQMessageTypeConstant(){
    }

    public static List getCommunityList() {
        return communityList;
    }

    public static List getSystemList() {
        return systemList;
    }

}
