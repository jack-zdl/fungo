package com.game.common.vo;

import com.game.common.api.InputPageDto;
import com.game.common.enums.BaseEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
@Getter
@Setter
@ToString
public class CmmCirclePostVo extends InputPageDto {

    private String circleId;

    /**
     * 功能描述: <p>查询类型</p>
     * <p>
     *
     * </p>
     * @auther: dl.zhang
     * @date: 2019/6/11 16:14
     */
    private String querytype;

    /**
     * 功能描述:  follow cirlce list sort type
     * <p>
     *      1","最新发布"
     *      *       "2","最新评论"
     *      *       "3","只看精选"
     *      *       "4","最热讨论"
     * </p>
     * @auther: dl.zhang
     * @date: 2019/6/19 16:45
     */
    private String sorttype;



    public enum SortTypeEnum implements BaseEnum<SortTypeEnum,String> {

        PUBDATE("1","最新发布"),
        PUBREPLY("2","最新评论"),
        ESSENCE("3","只看精选"),
        DISCUSS("4","最热讨论");
        String key;
        String value;

        SortTypeEnum(String s, String s1) {
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

    public enum QueryTypeEnum implements BaseEnum<QueryTypeEnum,String> {

        ALL("1","全部查询"),
        TESTPLAY("2","评测试玩"),
        STRATEGY("3","攻略心得"),
        TITTLETATTLE("4","同人杂谈"),
        GOSSIP("5","咨询八卦"),
        OTHER("6","其他");

        String key;
        String value;

        QueryTypeEnum(String s, String s1) {
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

}
