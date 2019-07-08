package com.game.common.vo;

import com.game.common.api.InputPageDto;
import com.game.common.enums.BaseEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
@Getter
@Setter
@ToString
public class CmmCircleVo extends InputPageDto {
    
    /**
     * 功能描述: <p>查询类型</p>
     * <p>
     *     0 全部查询
     *     1 最近浏览的圈子
     *     2 我关注的圈子
     * </p>
     * @auther: dl.zhang
     * @date: 2019/6/11 16:14
     */
    private String queryType;

    /**
     * 功能描述:  follow cirlce list sort type
     * <p>
     *     1  follow date sort
     *     2  hotvalue sort
     * </p>
     * @auther: dl.zhang
     * @date: 2019/6/19 16:45
     */
    private String sortType;

    /**
     * 功能描述:关键字查询
     * @auther: dl.zhang
     * @date: 2019/6/11 16:16
     */
    private String keyword;

    private String id;

    private String circleName;

    private String circleIcon;

    private String intro;

    private Integer type;

    private Integer state;

    private Integer memberNum;

    private Integer hotValue;

    private Integer sort;

    public enum SorttypeEnum implements BaseEnum<SorttypeEnum,String> {
        ALL("0","全部查询"),
        BROWSE("1","最近浏览的圈子"),
        FOLLOW("2","关注圈子");
        String key;
        String value;

        SorttypeEnum(String s, String s1) {
            this.key = s;
            this.value = s1;
        }
        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public enum TypeEnum implements BaseEnum<SorttypeEnum,String> {
        OFFICIAL("1","官方"),
        GAMES("2","游戏"),
        INTEREST("3","兴趣");

        String key;
        String value;

        TypeEnum(String s, String s1) {
            this.key = s;
            this.value = s1;
        }
        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        public static String getValueByKey(String key){
            if(key.equals(TypeEnum.OFFICIAL.getKey())){
                return TypeEnum.OFFICIAL.getValue();
            }else if(key.equals(TypeEnum.GAMES.getKey())){
                return TypeEnum.GAMES.getValue();
            }else if(key.equals(TypeEnum.INTEREST.getKey())){
                return TypeEnum.INTEREST.getValue();
            }
            return "";
        }
    }
}
