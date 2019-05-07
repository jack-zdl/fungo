package com.fungo.system.controller;

import com.fungo.system.dto.MemberNoticeInput;
import com.fungo.system.service.IMemberNoticeService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *      用户消息通知controller
 * </p>
 * since:V2.4.6
 * @author mxf
 * @since 2018-12-04
 */
@RestController
public class MemberNoticeController {


    @Autowired
    private IMemberNoticeService iMemberNoticeService;


    /**
     * 客户端轮询获取用户系统消息接口
     * @param memberprofile
     * @param request
     * @param noticeInput
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api/user/notices", method = RequestMethod.POST)
    public ResultDto<List<Map<String, Object>>> bindThirdSNSWithLogged(MemberUserProfile memberprofile, HttpServletRequest request,
                                                                       @Valid @RequestBody MemberNoticeInput noticeInput) throws Exception {

        noticeInput.setMb_id(memberprofile.getLoginId());

        List<Map<String, Object>> noticesList = iMemberNoticeService.queryMbNotices(noticeInput);

        if (null != noticesList && !noticesList.isEmpty()) {
            return ResultDto.success(noticesList);
        }
        ResultDto<List<Map<String, Object>>> resultDto = ResultDto.success("暂无消息");
        resultDto.setData(Collections.emptyList());
        return resultDto;
    }

}
