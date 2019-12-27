package com.fungo.community.service.portal.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.dao.mapper.CmmCommunityDao;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.entity.CmmCommunity;
import com.fungo.community.entity.portal.CmmCommunityIndex;
import com.fungo.community.feign.SystemFeignClient;
import com.fungo.community.service.portal.IPortalCommunityService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.*;
import com.game.common.dto.community.portal.CmmCommunityIndexDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.repo.cache.facade.FungoCacheCommunity;
import com.game.common.util.CommonUtil;
import com.game.common.util.PageTools;
import com.game.common.util.TempPageUtils;
import com.game.common.util.date.DateTools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@SuppressWarnings("all")
@Service
public class PortalCommunityServiceImpl implements IPortalCommunityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalCommunityServiceImpl.class);

    @Autowired
    private CmmCommunityDaoService communityService;

    @Autowired
    private CmmCommunityDao communityDao;

    @Autowired
    private FungoCacheCommunity fungoCacheCommunity;

    //依赖系统和用户微服务
    @Autowired(required = false)
    private SystemFeignClient systemFeignClient;
    @Autowired
    private CmmCommunityDao cmmCommunityDao;
    @Autowired
    private CmmCircleMapper cmmCircleMapper;



    @Override
    public FungoPageResultDto<CommunityOutPageDto> getCmmCommunityList(String userId, CommunityInputPageDto communityInputPageDto) {
        FungoPageResultDto<CommunityOutPageDto> re = null;
        //from redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_LIST;
        String keySuffix = userId + JSON.toJSONString(communityInputPageDto);

        re = (FungoPageResultDto<CommunityOutPageDto>) fungoCacheCommunity.getIndexCache(keyPrefix, keySuffix);
        if (null != re) {
            return re;
        }
        String filter = communityInputPageDto.getFilter();
        String keyword = communityInputPageDto.getKey_word();
        int limit = communityInputPageDto.getLimit();
        int page = communityInputPageDto.getPage();
        int sort = communityInputPageDto.getSort();
        List<CmmCommunity> communityList = new ArrayList<>();
        Wrapper<CmmCommunity> entityWrapper = new EntityWrapper<CmmCommunity>();
        entityWrapper.and("state = {0}", 1);
        // 关键字
        if (keyword != null && !keyword.equals("")) {
            keyword = keyword.replace(" ", "");
            entityWrapper = entityWrapper.like("name", keyword);
        }
        re = new FungoPageResultDto<CommunityOutPageDto>();
        List<CommunityOutPageDto> dataList = new ArrayList<>();
        re.setData(dataList);
        //!fixme 获取用户详情
        // Member user = menberService.selectById(userId);
        List<String> idsList = new ArrayList<String>();
        idsList.add(userId);
        ResultDto<List<MemberDto>> listMembersByids = null;
        try {
            listMembersByids = systemFeignClient.listMembersByids(idsList, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        MemberDto memberDto = null;
        if (null != listMembersByids) {
            List<MemberDto> memberDtoList = listMembersByids.getData();
            if (null != memberDtoList && !memberDtoList.isEmpty()) {
                memberDto = memberDtoList.get(0);
            }
        }
        if (filter.equals("official")) {
            //官方社区
            entityWrapper = entityWrapper.eq("type", 1);
        } else if (filter.equals("game")) {
            //普通社区
            entityWrapper = entityWrapper.eq("type", 0);
        } else if (filter.equals("mine")) {//我关注的社区
            if (memberDto == null) {
                return FungoPageResultDto.error("1", "找不到用户");
            }
            //!fixme 根据用户id，动作类型，目前类型，状态获取目前id集合
            /*
            List<BasAction> actionList = actionService.selectList(Condition.create().setSqlSelect("target_id")
                    .eq("member_id", userId).eq("type", 5).eq("target_type", 4).and("state = 0"));
            */
//			if(actionList == null || actionList.size() == 0) {
//				return FungoPageResultDto.error("241","没有找到用户关注的社区");
//			}
            List<BasActionDto> actionList = null;
            BasActionDto basActionDto = new BasActionDto();
            basActionDto.setMemberId(userId);
            basActionDto.setType(5);
            basActionDto.setTargetType(4);
            basActionDto.setState(0);
            try {
                ResultDto<List<BasActionDto>> actionByConditionRs = systemFeignClient.listActionByCondition(basActionDto);
                if (null != actionByConditionRs) {
                    actionList = actionByConditionRs.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            List<String> communityIdList = new ArrayList<>();
            Page<CmmCommunity> cmmPage = new Page<CmmCommunity>();
            if (actionList != null && actionList.size() > 0) {
                for (BasActionDto action : actionList) {
                    if (!CommonUtil.isNull(action.getTargetId())) {
                        communityIdList.add(action.getTargetId());
                    }
                }
                cmmPage = communityService
                        .selectPage(new Page<>(page, limit), Condition.create()
                                .where("state != {0}", -1).in("id", communityIdList).orderBy("created_at", false));
                communityList = cmmPage.getRecords();
            }
//			if(communityList == null || communityList.size() == 0) {
//				return FungoPageResultDto.error("241","没有找到用户关注的社区");
//			}
            for (CmmCommunity community : communityList) {
                CommunityOutPageDto out = new CommunityOutPageDto();
                out.setObjectId(community.getId());
                out.setName(community.getName());
                out.setIntro(community.getIntro());
                out.setIcon(community.getIcon());
                out.setIs_followed(true);
                //社区文章评论数
                int commentNum = communityDao.getCommentNumOfCommunity(community.getId());
                //社区热度
                out.setHot_value(community.getFolloweeNum() + community.getPostNum() + commentNum);
                out.setCreatedAt(DateTools.fmtDate(community.getCreatedAt()));
                out.setUpdatedAt(DateTools.fmtDate(community.getUpdatedAt()));
                out.setType(community.getType());
                dataList.add(out);
            }

            PageTools.pageToResultDto(re, cmmPage);

            //redis cache
            fungoCacheCommunity.excIndexCache(true, keyPrefix, keySuffix, re);

            return re;
        }
        // sort
        if (sort == 4) {//排序 热度值 关注数+帖子数
            entityWrapper = entityWrapper.orderBy("(followee_num+post_num)", false);
        } else if (sort == 20) {//排序 优先按照标记数排序，标记越大排名越前，标记数相同按照热度排序，热度越高排序越靠前
            entityWrapper = entityWrapper.orderBy("sort", false);
            entityWrapper = entityWrapper.orderBy("hotValue", false);
        } else {
            entityWrapper.orderBy("(followee_num+post_num)", false);
        }
        Page<CmmCommunity> cmmPage = communityService
                .selectPage(new Page<>(page, limit), entityWrapper);
        communityList = cmmPage.getRecords();

        //封装返回数据
        for (CmmCommunity community : communityList) {
            CommunityOutPageDto out = new CommunityOutPageDto();
            out.setObjectId(community.getId());
            out.setName(community.getName());
            out.setIcon(community.getIcon());
            out.setIntro(community.getIntro());
            out.setCreatedAt(DateTools.fmtDate(community.getCreatedAt()));
            out.setUpdatedAt(DateTools.fmtDate(community.getUpdatedAt()));

            int commentNum = communityDao.getCommentNumOfCommunity(community.getId());

            out.setHot_value(community.getFolloweeNum() + community.getPostNum() + commentNum);
            out.setType(community.getType());

            if (memberDto != null) {

                //!fixme  是否关注
                /*
                int selectCount = actionService.selectCount(new EntityWrapper<BasAction>().eq("target_id", community.getId())
                        .eq("target_type", 4).eq("type", 5).eq("member_id", userId).and("state = 0"));
                */


                BasActionDto basActionDtoLike = new BasActionDto();

                basActionDtoLike.setMemberId(userId);
                basActionDtoLike.setType(5);
                basActionDtoLike.setState(0);
                basActionDtoLike.setTargetId(community.getId());
                basActionDtoLike.setTargetType(4);

                int selectCount = 0;
                try {
                    ResultDto<Integer> resultDtoLike = systemFeignClient.countActionNum(basActionDtoLike);

                    if (null != resultDtoLike) {
                        selectCount = resultDtoLike.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (selectCount != 0) {
                    out.setIs_followed(true);
                } else {
                    out.setIs_followed(false);
                }
            }
            dataList.add(out);
        }
        PageTools.pageToResultDto(re, cmmPage);

        //redis cache
        fungoCacheCommunity.excIndexCache(true, keyPrefix, keySuffix, re);

        return re;
    }

    /**
     * PC2.0圈子首页列表
     * 优先显示官方社区
     * 其次游戏社区
     * @param userId
     * @param communityInputPageDto
     * @return
     */
    @Override
    public FungoPageResultDto<CmmCommunityIndexDto> getCommunityListPC2_0(String userId, CommunityInputPageDto communityInputPageDto) {
        FungoPageResultDto<CmmCommunityIndexDto> resultDto = null;
        //from redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_GETCOMMUNITYLISTPC2_0;
        String keySuffix = userId + JSON.toJSONString(communityInputPageDto);

        resultDto = (FungoPageResultDto<CmmCommunityIndexDto>) fungoCacheCommunity.getIndexCache(keyPrefix, keySuffix);
        if (null != resultDto) {
            return resultDto;
        }
//      分页计算起始行,行数
        Map<String, Integer> pageLimiter = TempPageUtils.getPageLimiter(communityInputPageDto.getPage(), communityInputPageDto.getLimit());
//        分页PC2.0圈子首页列表
        List<CmmCommunityIndex> communityListPC2_0 = communityService.getCommunityListPC2_0(pageLimiter);
        List<CmmCommunityIndexDto> dtoList = new ArrayList<>();
        for (CmmCommunityIndex cmmCommunityIndex: communityListPC2_0) {
            CmmCommunityIndexDto cmmCommunityIndexDto = new CmmCommunityIndexDto();
            BeanUtils.copyProperties(cmmCommunityIndex, cmmCommunityIndexDto);
            dtoList.add(cmmCommunityIndexDto);
        }
//        分页PC2.0圈子首页列表总数
        int count = communityService.getCommunityListPC2_0Count();
        resultDto = new FungoPageResultDto<CmmCommunityIndexDto>();
        resultDto.setData(dtoList);
        resultDto.setCount(count);
        resultDto.setAfter(communityInputPageDto.getPage() > Math.ceil(count / communityInputPageDto.getLimit()) ? -1 : communityInputPageDto.getPage() + 1);
        resultDto.setBefore(-1);

        //redis cache
        fungoCacheCommunity.excIndexCache(true, keyPrefix, keySuffix, resultDto);
        return resultDto;
    }

    /**
     * PC2.0圈子首页最近浏览圈子
     * @param userId
     * @param communityInputPageDto
     * @return
     */
    @Override
    public FungoPageResultDto<CmmCommunityDto> getRecentBrowseCommunity(String userId, CommunityInputPageDto communityInputPageDto) {
        FungoPageResultDto<CmmCommunityDto> resultDto = null;
        List<CmmCommunityDto> cmmCommunityIndexDtos = new ArrayList<>();
        //from redis cache
        /*String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_GETRECENTBROWSECOMMUNITY;
        String keySuffix = userId + JSON.toJSONString(communityInputPageDto);

        resultDto = (FungoPageResultDto<CmmCommunityIndexDto>) fungoCacheCommunity.getIndexCache(keyPrefix, keySuffix);
        if (null != resultDto) {
            return resultDto;
        }*/
        if (StringUtils.isEmpty(userId)){
            return new FungoPageResultDto<CmmCommunityDto>();
        }
//        根据用户Id获取最近浏览圈子行为 8个
        ResultDto<List<String>> recentBrowseCommunityByUserId = systemFeignClient.getRecentBrowseCommunityByUserId(userId);

        if (recentBrowseCommunityByUserId != null){
            List<String> data = recentBrowseCommunityByUserId.getData();
            if (data != null && data.size() > 0){
//                首页最近浏览圈子
//                List<CmmCommunity> cmmCommunities =  cmmCommunityDao.selectCmmCommunityByBrowse(data);
                List<CmmCircle> cmmCircles =  cmmCircleMapper.selectCmmCommunityByBrowse(data);
                for (CmmCircle cmmCircle:cmmCircles) {
                    CmmCommunityDto cmmCommunityDto = new CmmCommunityDto();
                    cmmCommunityDto.setId(cmmCircle.getId());
                    cmmCommunityDto.setName(cmmCircle.getCircleName());
                    cmmCommunityDto.setIcon(cmmCircle.getCircleIcon());
                    cmmCommunityIndexDtos.add(cmmCommunityDto);
                }
//                for (CmmCommunity cmmCommunity:cmmCommunities) {
//                    CmmCommunityDto cmmCommunityDto = new CmmCommunityDto();
//                    BeanUtils.copyProperties(cmmCommunity, cmmCommunityDto);
//                    cmmCommunityIndexDtos.add(cmmCommunityDto);
//                }
            }
        }

        resultDto = new FungoPageResultDto<CmmCommunityDto>();
        resultDto.setData(cmmCommunityIndexDtos);
        resultDto.setCount(cmmCommunityIndexDtos.size());
        resultDto.setAfter(-1);
        resultDto.setBefore(-1);
        //redis cache
//        fungoCacheCommunity.excIndexCache(true, keyPrefix, keySuffix, resultDto);
        return resultDto;
    }


}
