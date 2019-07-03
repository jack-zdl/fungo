package com.fungo.system.msservice.mall;

import com.fungo.system.service.MSServiceMemberAccountService;
import com.game.common.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 系统用户账户接口
 */
public class MSServiceMemberAccountController {

    @Autowired
    private MSServiceMemberAccountService msServiceMemberAccountService;

    /**
     *  扣减用户积分
     * @return  
     */
    @PostMapping("/ms/service/system/user/account/score/sub")
    public ResultDto<Boolean> subtractMemberScoreAccount(@RequestBody Map<String, Object> accountParamMap) throws Exception {

        if (null == accountParamMap || accountParamMap.isEmpty()) {
            return ResultDto.error("-1", "参数不能为空!");
        }

        String mb_id = (String) accountParamMap.get("mb_id");
        Integer score = (Integer) accountParamMap.get("score");

        boolean accountScore = msServiceMemberAccountService.subtractAccountScore(mb_id, score);

        return ResultDto.success(accountScore);
    }

//-----
}
