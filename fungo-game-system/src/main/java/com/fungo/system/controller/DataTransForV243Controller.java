package com.fungo.system.controller;


import com.fungo.system.function.DataTransForUserLevelService;
import com.fungo.system.function.FungoGrantHandworkService;
import com.fungo.system.service.IMemberIncentAccountService;
import com.fungo.system.service.IMemberIncentRuleRankService;
import com.fungo.system.service.IMemberService;
import com.game.common.dto.ResultDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *  V2.4.3 之前的版本数据迁移接口
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@RestController
public class DataTransForV243Controller {


    @Autowired
    private IMemberIncentAccountService incentAccountService;

    @Autowired
    private IMemberIncentRuleRankService iMemberIncentRuleRankService;

    @Autowired
    private IMemberService memberService;

    /**
     * 数据迁移接口授权秘钥
     */
    @Value("${fgoTransfAuth}")
    private String fgoTransfAuth;

    @Autowired
    private FungoGrantHandworkService fungoExcelParser;

    @Autowired
    private DataTransForUserLevelService dataTransForUserLevelService;


    /**
     * 手动给用户添加fungo币
     * @return
     */
    @RequestMapping(value = "/api/user/coin/grant", method = RequestMethod.POST)
    public ResultDto<String> grantCoinToMember(@RequestBody Map<String, String> authMap) throws Exception {

        if (isAuthOk(authMap)) {
            fungoExcelParser.excuteParserToFungoCoin();
            return ResultDto.success();
        }
        return ResultDto.error("-1", "您需要授权操作!");
    }


    /**
     * 用户历史第三方任务任务值和fungo币数据迁移
     * @return
     */
    @RequestMapping(value = "/api/user/incents/transf/third", method = RequestMethod.POST)
    public ResultDto<String> transfMemberThird(@RequestBody Map<String, String> authMap) throws Exception {

        if (isAuthOk(authMap)) {

           // iMemberIncentRuleRankService.transfMemberThird(authMap);

            return ResultDto.success();
        }
        return ResultDto.error("-1", "您需要授权操作!");
    }


    /**
     * 积分账户数据迁移
     * @return
     */
    @RequestMapping(value = "/api/user/incents/transf/exp", method = RequestMethod.POST)
    public ResultDto<String> transfMemberExp(@RequestBody Map<String, String> authMap) throws Exception {

        if (isAuthOk(authMap)) {
            incentAccountService.transfMemberExp();
            return ResultDto.success();
        }
        return ResultDto.error("-1", "您需要授权操作!");
    }


    /**
     * 用户等级成就数据迁移
     * @return
     */
    @RequestMapping(value = "/api/user/incents/transf/level", method = RequestMethod.POST)
    public ResultDto<String> transfMemberLevel(@RequestBody Map<String, String> authMap) throws Exception {

        if (isAuthOk(authMap)) {
            String mbId = authMap.get("mb_id");

            if (StringUtils.isNotBlank(mbId)) {
                //iMemberIncentRuleRankService.transfMemberLevel(mbId);
            } else {
               //iMemberIncentRuleRankService.transfMemberLevel();
            }

            //迁移用户的等级和fungo身份证数据
            //dataTransForUserLevelService.excuteParserToUserLevel();
            // 查询没有等级和fungo身份证图片的用户
            // 且给这些用户添加上图片数据
            dataTransForUserLevelService.queryMbWithoutLevelFungoImgs();

            return ResultDto.success();
        }

        return ResultDto.error("-1", "您需要授权操作!");
    }


    /**
     * 迁移Fun身份证数据
     * @throws Exception
     */
    @RequestMapping(value = "/api/mine/initrank", method = RequestMethod.POST)
    public ResultDto<String> initRank(@RequestBody Map<String, String> authMap) throws Exception {
        if (isAuthOk(authMap)) {
            memberService.initRank();
            return ResultDto.success();
        }
        return ResultDto.error("-1", "您需要授权操作!");
    }


    /**
     * 验证调用该接口是否被授权
     * @param authMap
     * @return
     */
    private Boolean isAuthOk(Map<String, String> authMap) {
        if (null == authMap || authMap.isEmpty()) {
            return false;
        }
        String authValuee = authMap.get("authKey");
        if (StringUtils.equalsIgnoreCase(authValuee, fgoTransfAuth)) {
            return true;
        }
        return false;
    }

//---------
}
