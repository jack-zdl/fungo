package com.fungo.games.utils;

import com.game.common.util.CommonUtil;
import java.util.List;

/**
 * <p>put on this GameCommonUtil from anywhere of used function</p>
 * @Author: dl.zhang
 * @Date: 2020/3/3
 */
public class GameCommonUtil {

    public static String getTagString(List<String> tags){
        if(tags == null||tags.isEmpty()){
            return null;
        }
        StringBuilder builder = new StringBuilder();
        int size = tags.size();
        for(int i = 0;i< size;i++){
            String tag = tags.get(i);
            if(i == (size -1)){
                builder.append("'").append(tag).append("'");
            }else {
                builder.append("'").append(tag).append("'").append(",");
            }
        }
        String tagIds = builder.toString();
        if(CommonUtil.isNull(tagIds)){
            return null;
        }
        return tagIds;
    }
}
