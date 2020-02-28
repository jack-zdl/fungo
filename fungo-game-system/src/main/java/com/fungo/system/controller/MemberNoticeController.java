package com.fungo.system.controller;

import com.alibaba.fastjson.JSON;
import com.fungo.system.dto.MemberNoticeInput;
import com.fungo.system.service.IMemberNoticeService;
import com.fungo.system.service.IMemberService;
import com.fungo.system.service.impl.CommunityServiceImpl;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.enums.CommonEnum;
import com.game.common.repo.cache.facade.FungoCacheGame;
import com.game.common.vo.DelObjectListVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private static final Logger LOGGER = LoggerFactory.getLogger( CommunityServiceImpl.class);

    @Autowired
    private IMemberNoticeService iMemberNoticeService;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private FungoCacheGame fungoCacheGame;

    /**
     * 客户端轮询获取用户系统消息接口  显示红点
     * @param memberprofile
     * @param noticeInput
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/api/user/notices")
    public ResultDto<List<Map<String, Object>>> bindThirdSNSWithLogged(MemberUserProfile memberprofile,HttpServletRequest request, @Valid @RequestBody MemberNoticeInput noticeInput, BindingResult errors) throws Exception {
        if(errors.hasErrors()){
            return ResultDto.ResultDtoFactory.buildSuccess( AbstractResultEnum.CODE_SYSTEM_FIVE.getKey(),errors.getAllErrors().stream().map( ObjectError::getDefaultMessage).collect( Collectors.joining(",") ));
        }
//        List<Map<String, Object>> noticesList = iMemberNoticeService.queryMbNotices(os,noticeInput);
        String appVersion = "2.5.1";
        if(StringUtils.isNoneBlank(request.getHeader("appversion"))){
            appVersion = request.getHeader("appversion");
        }
        String os = request.getHeader("os");
        if(os == null){
            os = "";
        }
        ResultDto<List<Map<String, Object>>> re = new ResultDto<>();
        Map<String, Object> resultMap = memberService.getNewUnReadNotice(memberprofile.getLoginId(),os, appVersion);
        if (null != resultMap && !resultMap.isEmpty()) {
            re.setData( Arrays.asList(resultMap));
        }
        return re;
    }

    /**
     * 建立用户的ios测试，厂商testflight消息接口
     * @param noticeInput
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api/user/ios/notices", method = RequestMethod.POST)
    public ResultDto<List<Map<String, Object>>> updateUserIosNotice( @Valid @RequestBody MemberNoticeInput noticeInput) throws Exception {
        List<Map<String, Object>> noticesList = iMemberNoticeService.insertMbNotices(noticeInput);
        if (null != noticesList && !noticesList.isEmpty()) {
            return ResultDto.success(noticesList);
        }
        ResultDto<List<Map<String, Object>>> resultDto = ResultDto.success("暂无消息");
        resultDto.setData(Collections.emptyList());
        return resultDto;
    }

    /**
     * 删除个人消息
     * @param memberprofile
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api/user/notices", method = RequestMethod.DELETE)
    public ResultDto<String> updateUserIosNotice( MemberUserProfile memberprofile,@Valid @RequestBody DelObjectListVO delObjectListVO) throws Exception {
        ResultDto<String> resultDto = null;
        try {
            resultDto = iMemberNoticeService.delMbNotices(delObjectListVO);
            if(CommonEnum.SUCCESS.code().equals( String.valueOf(resultDto.getStatus())) ){
                fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_COMMENTS, "", null);
            }
        }catch (Exception e){
            LOGGER.error( "删除个人消息异常，noticeId="+ JSON.toJSONString(delObjectListVO.getCommentIds()),e);
            resultDto = ResultDto.error( "-1", "删除个人消息异常");
        }
        return resultDto;
    }

    /**
     * 新增个人的礼品卡消息
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/api/user/giftcard/notices")
    public ResultDto<String> addUserGiftcardNotice( @Valid @RequestBody DelObjectListVO delObjectListVO) throws Exception {
        ResultDto<String> resultDto = null;
        try {
            resultDto = iMemberNoticeService.addUserGiftcardNotice(delObjectListVO);
        }catch (Exception e){
            LOGGER.error( "新增个人的礼品卡消息，noticeId="+ JSON.toJSONString(delObjectListVO.getCommentIds()),e);
            resultDto = ResultDto.error( "-1", "新增个人的礼品卡消息异常");
        }
        return resultDto;
    }
}
