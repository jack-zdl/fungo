package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.controller.MemberIncentHonorController;
import com.fungo.system.dao.BasActionDao;
import com.fungo.system.dao.MemberFollowerDao;
import com.fungo.system.entity.BasNotice;
import com.fungo.system.entity.IncentRanked;
import com.fungo.system.entity.Member;
import com.fungo.system.entity.MemberFollower;
import com.fungo.system.service.IncentRankedService;
import com.fungo.system.service.MemberFollowerService;
import com.fungo.system.service.MemberService;
import com.fungo.system.service.SystemService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.vo.MemberFollowerVo;
import com.sun.istack.NotNull;
import org.apache.commons.beanutils.BeanUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>系统微服务对外服务实现类</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/10
 */
@Service
public class SystemServiceImpl implements SystemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemServiceImpl.class);

    @Autowired
    private BasActionDao basActionDao;

    @Autowired
    private MemberFollowerService memberFollowerServiceImap;

    @Autowired
    private MemberService memberServiceImap;

    @Autowired
    private IncentRankedService incentRankedServiceImap;
    /**
     * 功能描述: 根据用户id查询被关注人的id集合
     * @param: [memberId]
     * @return: com.game.common.dto.FungoPageResultDto<java.lang.String>
     * @auther: dl.zhang
     * @date: 2019/5/10 14:22
     */
    @Override
    public FungoPageResultDto<String> getFollowerUserId(@NotNull String memberId) {
        FungoPageResultDto<String> re = null;
        List<String> followerIdList = new ArrayList<>();
        try {
            followerIdList  =  basActionDao.getFollowerUserId(memberId);
            re = new FungoPageResultDto<String>();
            re.setData(followerIdList);
//            PageTools.pageToResultDto(re, plist);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemServiceImpl.getFollowerUserId",e);
            re =  FungoPageResultDto.error("-1", "找不到目标");
        }finally {
            return re;
        }
    }

    /**
     * 功能描述: 
     * @param: [vo] 根据对象获取对象集合
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberFollowerDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 16:48
     */
    @Override
    public FungoPageResultDto<MemberFollowerDto> getMemberFollowerList(MemberFollowerVo vo) {
        FungoPageResultDto<MemberFollowerDto> re = null;
        try {
            Page<MemberFollower> basNoticePage = new Page<>(vo.getPage(), vo.getLimit());
            re = new FungoPageResultDto<MemberFollowerDto>();
            MemberFollower memberFollower = new MemberFollower();
            BeanUtils.copyProperties(memberFollower,vo );
            EntityWrapper<MemberFollower> memberFollowerWrapper = new EntityWrapper<>(memberFollower);
            Page<MemberFollower> page = memberFollowerServiceImap.selectPage(basNoticePage,memberFollowerWrapper);
            PageTools.pageToResultDto(re, page);
            List<MemberFollower> memberFollowers = page.getRecords();
            List<MemberFollowerDto> memberFollowerDtos = CommonUtils.deepCopy(memberFollowers,MemberFollowerDto.class);
            re.setData(memberFollowerDtos);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemServiceImpl.getMemberFollowerList",e);
            re =  FungoPageResultDto.error("-1", "找不到目标");
        }
        return re;
    }

    /**
     * 功能描述: 根据用户会员DTO对象分页查询用户会员
     * @param: [memberDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 17:40
     */
    @Override
    public FungoPageResultDto<MemberDto> getMemberDtoList(MemberDto memberDto) {
        FungoPageResultDto<MemberDto> re = null;
        try {
            Page<Member> basNoticePage = new Page<>(memberDto.getPage(), memberDto.getLimit());
            re = new FungoPageResultDto<MemberDto>();
            Member memberFollower = new Member();
            BeanUtils.copyProperties(memberFollower,memberDto );
            EntityWrapper<Member> memberFollowerWrapper = new EntityWrapper<>(memberFollower);
            Page<Member> page = memberServiceImap.selectPage(basNoticePage,memberFollowerWrapper);
            PageTools.pageToResultDto(re, page);
            List<Member> memberFollowers = page.getRecords();
            List<MemberDto> memberFollowerDtos = CommonUtils.deepCopy(memberFollowers,MemberDto.class);
            re.setData(memberFollowerDtos);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemServiceImpl.getMemberFollowerList",e);
            re =  FungoPageResultDto.error("-1", "找不到目标");
        }
        return re;
    }

    @Override
    public FungoPageResultDto<IncentRankedDto> getIncentRankedList(IncentRankedDto incentRankedDto) {
        FungoPageResultDto<IncentRankedDto> re = null;
        try {
            Page<IncentRanked> basNoticePage = new Page<>(incentRankedDto.getPage(), incentRankedDto.getLimit());
            re = new FungoPageResultDto<>();
            IncentRanked  incentRanked = new IncentRanked();
            BeanUtils.copyProperties(incentRanked,incentRankedDto );
            EntityWrapper<IncentRanked> wrapper = new EntityWrapper<>(incentRanked);
            Page<IncentRanked> page = incentRankedServiceImap.selectPage(basNoticePage,wrapper);
            PageTools.pageToResultDto(re, page);
            List<IncentRanked> memberFollowers = page.getRecords();
            List<IncentRankedDto> memberFollowerDtos = CommonUtils.deepCopy(memberFollowers,IncentRankedDto.class);
            re.setData(memberFollowerDtos);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemServiceImpl.getMemberFollowerList",e);
            re =  FungoPageResultDto.error("-1", "找不到目标");
        }
        return re;
    }


}
