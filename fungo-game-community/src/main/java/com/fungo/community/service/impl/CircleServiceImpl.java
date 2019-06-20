package com.fungo.community.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.config.FungoCircleParameter;
import com.fungo.community.dao.mapper.BasTagDao;
import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.dao.mapper.CmmPostCircleMapper;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.feign.SystemFeignClient;
import com.fungo.community.service.CircleService;
import com.fungo.community.service.CmmCircleService;
import com.game.common.bean.TagBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CircleTypeDto;
import com.game.common.dto.community.CmmCircleDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.community.PostOutBean;
import com.game.common.dto.system.CircleFollow;
import com.game.common.dto.system.CircleFollowVo;
import com.game.common.vo.CmmCirclePostVo;
import com.game.common.vo.CmmCircleVo;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;

/**
 * <p>圈子接口实现类</p>
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
@Service
public class CircleServiceImpl implements CircleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CircleServiceImpl.class);

    @Autowired
    private CmmCircleService cmmCircleServiceImap;
    @Autowired
    private CmmCircleMapper cmmCircleMapper;
    @Autowired
    private SystemFeignClient systemFeignClient;
    @Autowired
    private CmmPostCircleMapper cmmPostCircleMapper;
    @Autowired
    private CmmPostDao cmmPostDao;
    @Autowired
    private BasTagDao basTagDao;
    @Autowired
    private FungoCircleParameter fungoCircleParameter;

    @Override
    public FungoPageResultDto<CmmCircleDto> selectCircle(String memberId, CmmCircleVo cmmCircleVo) {
        FungoPageResultDto<CmmCircleDto> re ;
        int pageNum = cmmCircleVo.getPage();
        int limitNum  =cmmCircleVo.getLimit();
        try {
            List<CmmCircleDto> cmmCircleDtoList =  new ArrayList<>();
            Page<CmmCircle> page = new Page<>(pageNum, limitNum);
            List<CmmCircle> list = new ArrayList<>();
            if(CmmCircleVo.SorttypeEnum.ALL.getKey().equals(cmmCircleVo.getQuerytype())){
                list = cmmCircleMapper.selectPageByKeyword(page,cmmCircleVo);
                list.stream().forEach(r -> {
                    CmmCircleDto s = new CmmCircleDto();
                    try {
                        BeanUtils.copyProperties(s, r);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cmmCircleDtoList.add(s);
                });
//            BeanUtils.copyProperties(list,cmmCircleDtoList);
                List<CircleFollow>  circleFollows = new ArrayList<>();
                list.stream().forEach(x -> {
                    CircleFollow circleFollow = new CircleFollow();
                    circleFollow.setCircleId(x.getId());
                    circleFollows.add(circleFollow);
                });
                CircleFollowVo circleFollowVo = new CircleFollowVo();
                circleFollowVo.setMemberId(memberId);
                circleFollowVo.setCircleFollows(circleFollows);
                ResultDto<CircleFollowVo> resultDto = systemFeignClient.circleListFollow(circleFollowVo);
                if(resultDto.isSuccess()){
                    cmmCircleDtoList.stream().forEach(s -> {
                        List<CircleFollow> circleFollow =  resultDto.getData().getCircleFollows().stream().filter(e -> e.getCircleId().equals(s.getId())).collect(Collectors.toList());
                        s.setFollow((circleFollow == null || circleFollow.size() ==0 )? false:circleFollow.get(0).isFollow());
                    });
                }
            }else if(CmmCircleVo.SorttypeEnum.BROWSE.getKey().equals(cmmCircleVo.getQuerytype())){

            }else if(CmmCircleVo.SorttypeEnum.FOLLOW.getKey().equals(cmmCircleVo.getQuerytype())){
                CircleFollowVo param = new CircleFollowVo();
                param.setMemberId(memberId);
//                param.setPage(pageNum);
//                param.setLimit(limitNum); //
                FungoPageResultDto<String> circleFollowVos = systemFeignClient.circleListMineFollow(param);
                if(circleFollowVos.getData().size() > 0){
                    List<String> ids =circleFollowVos.getData();
                    String sortType = cmmCircleVo.getSorttype();
                    List<CmmCircle> cmmCircles = cmmCircleMapper.selectPageByIds(page,sortType,ids);
                    cmmCircles.stream().forEach(r -> {
                        CmmCircleDto s = new CmmCircleDto();
                        try {
                            BeanUtils.copyProperties(s, r);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        s.setFollow(true);
                        cmmCircleDtoList.add(s);
                    });
                }
            }
            re = FungoPageResultDto.FungoPageResultDtoFactory.buildSuccess(cmmCircleDtoList,cmmCircleVo.getPage()-1,page);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("获取圈子集合",e);
            re = FungoPageResultDto.error("-1","获取圈子集合异常");
        }
        return re;
    }

    @Override
    public ResultDto<CmmCircleDto> selectCircleById(String memberId, String circleId) throws InvocationTargetException, IllegalAccessException {
        ResultDto<CmmCircleDto> re = null;
        CmmCircleDto cmmCircleDto = new CmmCircleDto();
        try {
            CmmCircle cmmCircle = cmmCircleServiceImap.selectById(circleId);
            BeanUtils.copyProperties(cmmCircleDto,cmmCircle);
            CircleFollowVo circleFollowVo = new CircleFollowVo();
            circleFollowVo.setMemberId(memberId);
            ResultDto<CircleFollowVo> resultDto = systemFeignClient.circleListFollow(circleFollowVo);
            if(resultDto != null && resultDto.getData() != null && resultDto.getData().getCircleFollows() != null){
                List<CircleFollow> circleFollows = resultDto.getData().getCircleFollows().stream().filter( r -> r.getCircleId().equals(circleId)).collect(Collectors.toList());
                cmmCircleDto.setFollow(circleFollows.size()>0 ? true:false );
            }
            //获取文章
//            List<CmmPost> cmmPosts = cmmPostCircleMapper.getCmmCircleListByPostId(cmmCircleDto.getId());
//            List<TagBean> tagBeans = basTagDao.getPostTags();
//            cmmPosts.stream().forEach(s -> {
//                CmmCircleDto.TagPost tagPost = new CmmCircleDto.TagPost();
//                List<TagBean> tag =  tagBeans.stream().filter(x -> x.getId().equals(s.getTags())).collect(Collectors.toList());
//                tagPost.setTag(tag.get(0).getName());

//            });
            re  = ResultDto.success(cmmCircleDto);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("获取圈子详情",e);
            re  = ResultDto.error("-1","获取圈子详情异常");
        }
        return re;
    }

    @Override
    public FungoPageResultDto<PostOutBean> selectCirclePost(String memberId, CmmCirclePostVo cmmCirclePostVo) {

        try {
            if(CmmCirclePostVo.QueryTypeEnum.ALL.getKey().equals(cmmCirclePostVo.getQuerytype())){

            }else if(CmmCirclePostVo.QueryTypeEnum.TESTPLAY.getKey().equals(cmmCirclePostVo.getQuerytype())){

            }else if(CmmCirclePostVo.QueryTypeEnum.STRATEGY.getKey().equals(cmmCirclePostVo.getQuerytype())){

            }else if(CmmCirclePostVo.QueryTypeEnum.TITTLETATTLE.getKey().equals(cmmCirclePostVo.getQuerytype())){

            }else if(CmmCirclePostVo.QueryTypeEnum.GOSSIP.getKey().equals(cmmCirclePostVo.getQuerytype())){

            }else if(CmmCirclePostVo.QueryTypeEnum.OTHER.getKey().equals(cmmCirclePostVo.getQuerytype())){

            }

            if(CmmCirclePostVo.SortTypeEnum.PUBDATE.getKey().equals(cmmCirclePostVo.getSorttype())){

            }else if(CmmCirclePostVo.SortTypeEnum.PUBREPLY.getKey().equals(cmmCirclePostVo.getSorttype())){

            }else if(CmmCirclePostVo.SortTypeEnum.ESSENCE.getKey().equals(cmmCirclePostVo.getSorttype())){

            }else if(CmmCirclePostVo.SortTypeEnum.DISCUSS.getKey().equals(cmmCirclePostVo.getSorttype())){

            }
            //获取文章
            List<CmmPost> cmmPosts = cmmPostDao.getCmmCircleListByPostId(cmmCirclePostVo.getCircleId());
            List<TagBean> tagBeans = basTagDao.getPostTags();
            cmmPosts.stream().forEach(s -> {
                CmmCircleDto.TagPost tagPost = new CmmCircleDto.TagPost();
                List<TagBean> tag =  tagBeans.stream().filter(x -> x.getId().equals(s.getTags())).collect(Collectors.toList());
                tagPost.setTag(tag.get(0).getName());
            });
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("圈子获取下属文章",e);
        }
        return null;
    }

    /**
     * 功能描述: 查询圈子的文章类型
     * @param: [memberId, cmmCirclePostVo]
     * @return: com.game.common.dto.ResultDto<com.game.common.dto.community.CircleTypeDto>
     * @auther: dl.zhang
     * @date: 2019/6/20 15:38
     */
    @Override
    public ResultDto<List<CircleTypeDto>> selectCirclePostType(String memberId, CmmCirclePostVo cmmCirclePostVo) {
        List<CircleTypeDto> circleTypeDtos = new ArrayList<>();
        ResultDto<List<CircleTypeDto>> re = new ResultDto<>();
        try {
            List<CmmPost> cmmPosts = cmmPostDao.getCmmCircleListByPostId(cmmCirclePostVo.getCircleId());
            List<TagBean> tagBeans = basTagDao.getPostTags();
            Map<String, List<CmmPost>> cmmPostMap  = cmmPosts.stream().collect(groupingBy(CmmPost::getTags));
            Iterator<String> iter = cmmPostMap.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                List<CmmPost> valueList = cmmPostMap.get(key);
                if (valueList.size() < fungoCircleParameter.getPostnumber())
                    cmmPostMap.remove(key);
            }
            for (String key : cmmPostMap.keySet()) {
                CircleTypeDto circleTypeDto = new CircleTypeDto();
                Optional<TagBean> cmmPost = tagBeans.stream().filter(r -> r.getId().equals(key)).findFirst();
                circleTypeDto.setCirclePostType(cmmPost.get().getId());
                circleTypeDto.setCirclePostName(cmmPost.get().getName());
                circleTypeDtos.add(circleTypeDto);
            }
            re.setData(circleTypeDtos);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查询圈子的文章类型",e);
        }
        return re;
    }

}
