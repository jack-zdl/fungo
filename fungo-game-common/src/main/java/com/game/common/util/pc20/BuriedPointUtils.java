package com.game.common.util.pc20;

import com.game.common.util.pc20.analysysjavasdk.AnalysysException;
import com.game.common.util.pc20.analysysjavasdk.AnalysysJavaSdk;
import com.game.common.util.pc20.analysysjavasdk.DEBUG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/6/13 13:08
 */
public class BuriedPointUtils {

    private static Logger logger = LoggerFactory.getLogger(BuriedPointUtils.class);

    @Autowired
    private AnalysysJavaSdk analysysJavaSdk;

    public final static String APP_KEY = "571cf37a38870ec7";
    public final static String ANALYSYS_SERVICE_URL = "https://arkcloud-0529.analysys.cn:4089";
//    private final static AnalysysJavaSdk analysys = new AnalysysJavaSdk(new SyncCollecter(ANALYSYS_SERVICE_URL), APP_KEY);

    /**
     * 登录
     * @param map
     * @throws AnalysysException
     */
    public static void login05(Map<String,String> map,AnalysysJavaSdk analysys){
        String distinctId = map.get("distinctId");
        String platForm = map.get("platForm"); //Android平台
        analysys.setDebugMode(DEBUG.CLOSE); //设置debug模式
        Map<String, Object> trackPropertie = new HashMap<String, Object>();
        trackPropertie.put("login001", map.get("login001")); //登录方式
        trackPropertie.put("login002", map.get("login002")); //IP地址
        try {
            analysys.track(distinctId, false, "login05", trackPropertie, platForm);
        } catch (AnalysysException e) {
            e.printStackTrace();
        }
//        analysys.flush();
    }

    /**
     * 了解游戏的详情
     * @param map
     * @param analysys
     */
    public static void gamepage(Map<String,String> map,AnalysysJavaSdk analysys){
        String distinctId = map.get("distinctId");
        String platForm = map.get("platForm"); //Android平台
        analysys.setDebugMode(DEBUG.CLOSE); //设置debug模式
        Map<String, Object> trackPropertie = new HashMap<String, Object>();
        trackPropertie.put("gamename", map.get("gamename")); //游戏名称
        trackPropertie.put("gameid", map.get("gameid")); //游戏ID
        trackPropertie.put("loadnum", map.get("loadnum")); //下载量
        try {
            analysys.track(distinctId, false, "gamepage", trackPropertie, platForm);
        } catch (AnalysysException e) {
            logger.error("游戏的详情analysystrack异常",e);
            e.printStackTrace();
        }
//        analysys.flush();
    }
}
