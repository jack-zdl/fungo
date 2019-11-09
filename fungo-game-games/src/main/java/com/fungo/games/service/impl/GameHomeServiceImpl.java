package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.dao.FindCollectionGroupDao;
import com.fungo.games.dao.NewGameDao;
import com.fungo.games.entity.*;
import com.fungo.games.feign.CommunityFeignClient;
import com.fungo.games.feign.SystemFeignClient;
import com.fungo.games.service.*;
import com.game.common.api.InputPageDto;
import com.game.common.bean.AdminCollectionGroup;
import com.game.common.bean.CollectionItemBean;
import com.game.common.bean.HomePageBean;
import com.game.common.bean.NewGameBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.index.BannerBean;
import com.game.common.dto.mall.MallBannersInput;
import com.game.common.repo.cache.facade.FungoCacheIndex;
import com.game.common.util.PageTools;
import com.game.common.util.exception.BusinessException;
import com.game.common.vo.AdminCollectionVo;
import com.game.common.vo.CircleGamePostVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @Author: Carlos
 * @Date: 2019/7/29
 */
@Service
public class GameHomeServiceImpl implements GameHomeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHomeService.class);

    @Autowired
    private FungoCacheIndex fungoCacheIndex;
    @Autowired
    private HomePageService homePageService;
    @Autowired
    private NewGameDao newGameDao;
    @Autowired
    private FindCollectionGroupService findCollectionGroupService;
    @Autowired
    private FindCollectionItemService findCollectionItemService;
    @Autowired
    private FindCollectionGroupDao findCollectionGroupDao;
    @Autowired
    private GameSurveyRelService surveyRelService;
    @Autowired
    private GameService gameService;
    @Autowired
    private CommunityFeignClient communityFeignClient;
    @Autowired
    private NewGameService newGameService;
    @Autowired
    private SystemFeignClient systemFeignClient;


    /**
     * 首页显示内容
     *
     * @return
     */
    @Override
    public FungoPageResultDto<HomePageBean> queryHomePage(InputPageDto inputPageDto, String memberId, String os) {
        FungoPageResultDto<HomePageBean> re = new FungoPageResultDto<>();
        if (inputPageDto.getPage() < 1) {
            re.setMessage("传入页数必须大于等于1");
            re.setAfter(1);
            re.setBefore(0);
            return re;
        }
        List<HomePage> result = new ArrayList<>();
        //查询制顶和正常的全部数据
        Page<HomePage> totalPage = homePageService.selectPage(new Page<HomePage>(1, 1),
                new EntityWrapper<HomePage>().in("state", "0,3").orderBy("updated_at", false));
        List<HomePage> totalListPage = totalPage.getRecords();
        //查询数据按时间排序
        Page<HomePage> page = homePageService.selectPage(new Page<HomePage>(inputPageDto.getPage(), inputPageDto.getLimit()),
                new EntityWrapper<HomePage>().eq("state", 0).orderBy("updated_at", false));
        //获取前一页后一页数据
        FungoPageResultDto<HomePage> fungoPageResultDto = new FungoPageResultDto<>();
        //查询总数据
        List<HomePage> homePageList = homePageService.selectList(new EntityWrapper<HomePage>().eq("state", 0));
        Page<HomePage> pages = new Page();
        pages.setTotal(homePageList.size());
        pages.setSize(inputPageDto.getLimit());
        pages.setCurrent(inputPageDto.getPage());
        fungoPageResultDto.setData(homePageList);
        PageTools.pageToResultDto(fungoPageResultDto, pages);
        re.setBefore(fungoPageResultDto.getBefore());
        re.setAfter(fungoPageResultDto.getAfter());
        List<HomePage> pageList = page.getRecords();
        if (1 == inputPageDto.getPage()) {
            //置顶标识首页数据
            List<HomePage> topList = homePageService.selectList(new EntityWrapper<HomePage>().eq("state", 3));
            boolean flag = true;
            if (topList.isEmpty()) {
                flag = false;
                result = pageList;
            }
            //第一条即是制定也是最新更新的
            if (flag) {
                if (!totalListPage.get(0).getId().equals(pageList.get(0).getId())) {
                    result.add(totalListPage.get(0));
                    topList = topList.stream().filter(s -> !s.getId().equals(totalListPage.get(0).getId())).collect(Collectors.toList());
                    if (topList.size() == 1) {
                        result.add(topList.get(0));
                    } else if (topList.size() > 1) {
                        //取置顶数据的随机一个
                        result.add(topList.get(new Random().nextInt((topList.size()))));
                    }
                    for (int i = 0; i < pageList.size(); i++) {
                        result.add(pageList.get(i));
                    }
                } else {
                    for (int i = 0; i < pageList.size(); i++) {
                        if (i == 1) {
                            result.add(topList.get(0));
                        }
                        result.add(pageList.get(i));
                    }
                }
            }
        } else {
            result = page.getRecords();
        }
        List<HomePageBean> homePageBeanList = new ArrayList<>();
        HomePageBean homePageBean = null;
        for (HomePage homePage : result) {
            Game game = gameService.selectById(homePage.getGameId());
            homePageBean = new HomePageBean();
            homePageBean.setId(homePage.getId());
            homePageBean.setGameId(homePage.getGameId());
            homePageBean.setName(game.getName());
            homePageBean.setTags(game.getTags());
            homePageBean.setCanFast(game.getCanFast());
            if(StringUtils.isNotBlank(game.getOrigin()) && "中国".equals(game.getOrigin())){
                homePageBean.setOrigin(null);
            }else{
                homePageBean.setOrigin(game.getOrigin());
            }
            homePageBean.setScore(game.getScore());
            homePageBean.setIosState(game.getIosState());
            homePageBean.setAndroidState(game.getAndroidState());
            homePageBean.setRmdLag(homePage.getRmdLag());
            homePageBean.setRmdReason(homePage.getRmdReason());
            homePageBean.setVideo(homePage.getVideo());
            homePageBean.setAppImages(homePage.getAppImages());
            homePageBean.setApk(game.getApk());
            if(null==game.getVersionChild()){
                homePageBean.setVersion(game.getVersionMain());
            }else if(null!=game.getVersionMain() && null!=game.getVersionChild()){
                homePageBean.setVersion(game.getVersionMain()+"."+game.getVersionChild());
            }
            homePageBean.setGameSize(game.getGameSize());
            homePageBean.setAndroidPackageName(game.getAndroidPackageName());
            homePageBean.setIcon(game.getIcon());
            if (StringUtils.isNotBlank(memberId)) {
                GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", homePage.getGameId()).eq("phone_model", os).eq("state", 0));
                if (srel != null) {
                    homePageBean.setMake(true);
                    homePageBean.setClause(1 == srel.getAgree() ? true : false);
                }
            }
            CircleGamePostVo circleGamePostVo = new CircleGamePostVo();
            circleGamePostVo.setGameId(homePage.getGameId());
            circleGamePostVo.setType(1);
            ResultDto<String> dto = communityFeignClient.getCircleByGame(circleGamePostVo);
            if (null == dto) {
                homePageBean.setCircleId(null);
            } else {
                homePageBean.setCircleId(dto.getData());
            }
            homePageBeanList.add(homePageBean);
        }
        re.setData(homePageBeanList);
        return re;
    }


    /**
     * 新游信息查询
     *
     * @return
     */
    @Override
    public FungoPageResultDto<NewGameBean> queryNewGame(InputPageDto inputPageDto, String memberId, String os) {
        //查询选择日期字段在今天开始往后30天内的所有数据，先根据时间排序，在对每天内的排序号(sort)进行排序
        FungoPageResultDto<NewGameBean> re = new FungoPageResultDto<>();
        if (inputPageDto.getPage() < 1) {
            re.setMessage("传入页数必须大于等于1");
            re.setAfter(1);
            re.setBefore(0);
            return re;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(new Date());
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        //查询有制顶标识的数据
        NewGameBean bean = new NewGameBean();
        //开始时间
        bean.setStartTime(startCalendar.getTime());
        //结束时间
        Calendar endCalendar = startCalendar.getInstance();
        endCalendar.add(Calendar.DATE, 30);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);
        bean.setEndTime(endCalendar.getTime());
        //查询分页列表startOffset
        int startOffset = (inputPageDto.getPage() - 1) * inputPageDto.getLimit();
        bean.setStartOffset(startOffset);
        bean.setPageSize(inputPageDto.getLimit());
        List<NewGameBean> list = newGameDao.getNewGameAll(bean);
        //获取前一页后一页标识
        List<NewGame> totalList = newGameService.selectList(new EntityWrapper<NewGame>().ge("choose_date", startCalendar.getTime()).lt("choose_date", endCalendar.getTime()).eq("state", "0"));
        FungoPageResultDto<NewGameBean> fungoPageResultDto = new FungoPageResultDto<>();
        Page<HomePage> pages = new Page();
        pages.setTotal(totalList.size());
        pages.setSize(inputPageDto.getLimit());
        pages.setCurrent(inputPageDto.getPage());
        fungoPageResultDto.setData(list);
        PageTools.pageToResultDto(fungoPageResultDto, pages);
        re.setBefore(fungoPageResultDto.getBefore());
        re.setAfter(fungoPageResultDto.getAfter());
        for (NewGameBean newGameBean : list) {
            if(null==newGameBean.getVersionChild()){
                newGameBean.setVersion(newGameBean.getVersionMain());
            }else if(null!=newGameBean.getVersionMain() && null!=newGameBean.getVersionChild()){
                newGameBean.setVersion(newGameBean.getVersionMain()+"."+newGameBean.getVersionChild());
            }
            if (null == newGameBean.getScore()) {
                newGameBean.setScore(0.0);
            }
            if(StringUtils.isNotBlank(newGameBean.getOrigin()) && "中国".equals(newGameBean.getOrigin())){
                newGameBean.setOrigin(null);
            }
            newGameBean.setMake(false);
            if (StringUtils.isNotBlank(memberId)) {
                GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", newGameBean.getGameId()).eq("phone_model", os).eq("state", 0));
                if (srel != null) {
                    newGameBean.setMake(true);
                    newGameBean.setClause(1 == srel.getAgree() ? true : false);
                }
            }
            if (null != newGameBean.getChooseDate()) {
                newGameBean.setTime(newGameBean.getChooseDate().getTime());
            }
            CircleGamePostVo circleGamePostVo = new CircleGamePostVo();
            circleGamePostVo.setGameId(newGameBean.getGameId());
            circleGamePostVo.setType(1);
            ResultDto<String> dto = communityFeignClient.getCircleByGame(circleGamePostVo);
            if (null == dto) {
                newGameBean.setCircleId(null);
            } else {
                newGameBean.setCircleId(dto.getData());
            }
        }
        re.setData(list);
        return re;
    }


    /**
     * 查看往期新游信息
     *
     * @return
     */
    @Override
    public FungoPageResultDto<NewGameBean> queryOldGame(InputPageDto inputPageDto) {
        //查询选择日期字段在今天开始往后30天内的所有数据，先根据时间排序，在对每天内的排序号(sort)进行排序
        FungoPageResultDto<NewGameBean> re = new FungoPageResultDto<>();
        if (inputPageDto.getPage() < 1) {
            re.setMessage("传入页数必须大于等于1");
            re.setAfter(1);
            re.setBefore(0);
            return re;
        }
        re.setAfter((inputPageDto.getPage() + 1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //查询分页列表startOffset
        int startOffset = (inputPageDto.getPage() - 1) * inputPageDto.getLimit();
        //查询有制顶标识的数据
        NewGameBean bean = new NewGameBean();
        bean.setStartOffset(startOffset);
        bean.setChooseDate(calendar.getTime());
        bean.setPageSize(inputPageDto.getLimit());
        List<NewGameBean> list = newGameDao.queryOldGame(bean);
        //获取前一页后一页标识
        List<NewGame> totalList = newGameService.selectList(new EntityWrapper<NewGame>().lt("choose_date", calendar.getTime()).eq("state", "0"));
        FungoPageResultDto<NewGameBean> fungoPageResultDto = new FungoPageResultDto<>();
        Page<HomePage> pages = new Page();
        pages.setTotal(totalList.size());
        pages.setSize(inputPageDto.getLimit());
        pages.setCurrent(inputPageDto.getPage());
        fungoPageResultDto.setData(list);
        PageTools.pageToResultDto(fungoPageResultDto, pages);
        re.setBefore(fungoPageResultDto.getBefore());
        re.setAfter(fungoPageResultDto.getAfter());
        for (NewGameBean newGameBean : list) {
            if(StringUtils.isNotBlank(newGameBean.getOrigin()) && "中国".equals(newGameBean.getOrigin())){
                newGameBean.setOrigin(null);
            }
            if(null==newGameBean.getVersionChild()){
                newGameBean.setVersion(newGameBean.getVersionMain());
            }else if(null!=newGameBean.getVersionMain() && null!=newGameBean.getVersionChild()){
                newGameBean.setVersion(newGameBean.getVersionMain()+"."+newGameBean.getVersionChild());
            }
            if (null == newGameBean.getScore()) {
                newGameBean.setScore(0.0);
            }
            if (null != newGameBean.getChooseDate()) {
                newGameBean.setTime(newGameBean.getChooseDate().getTime());
            }
        }
        re.setData(list);
        return re;
    }


    /**
     * 合集组信息列表查询
     *
     * @return
     */
    @Override
    @Transactional
    public FungoPageResultDto<AdminCollectionGroup> queryCollectionGroup(AdminCollectionVo inputPageDto) {
        FungoPageResultDto<AdminCollectionGroup> re = new FungoPageResultDto<>();
        if (inputPageDto.getPage() < 1) {
            re.setMessage("传入页数必须大于等于1");
            re.setAfter(1);
            re.setBefore(0);
            return re;
        }
        List<AdminCollectionGroup> colLists = new ArrayList<>();
        Page<FindCollectionGroup> page = findCollectionGroupService.selectPage(new Page<FindCollectionGroup>(inputPageDto.getPage(), inputPageDto.getLimit()),
                new EntityWrapper<FindCollectionGroup>().eq("state", 0).eq("is_online", 0).orderBy("sort desc, updated_at", false));
        //获取前一页后一页标识
        List<FindCollectionGroup> totalList = findCollectionGroupService.selectList(new EntityWrapper<FindCollectionGroup>().eq("state", "0").eq("is_online", 0));
        FungoPageResultDto<FindCollectionGroup> fungoPageResultDto = new FungoPageResultDto<>();
        Page<HomePage> pages = new Page();
        pages.setTotal(totalList.size());
        pages.setSize(inputPageDto.getLimit());
        pages.setCurrent(inputPageDto.getPage());
        fungoPageResultDto.setData(page.getRecords());
        PageTools.pageToResultDto(fungoPageResultDto, pages);
        re.setBefore(fungoPageResultDto.getBefore());
        re.setAfter(fungoPageResultDto.getAfter());
        List<FindCollectionGroup> collectionList = page.getRecords();
        if (!collectionList.isEmpty()) {
            AdminCollectionGroup adminCollectionGroup = null;
            for (FindCollectionGroup findCollectionGroup : collectionList) {
                adminCollectionGroup = new AdminCollectionGroup();
                adminCollectionGroup.setCoverPicture(findCollectionGroup.getCoverPicture());
                adminCollectionGroup.setSort(findCollectionGroup.getSort());
                adminCollectionGroup.setName(findCollectionGroup.getName());
                adminCollectionGroup.setDetail(findCollectionGroup.getDetail());
                adminCollectionGroup.setId(findCollectionGroup.getId());
                adminCollectionGroup.setList(Arrays.asList());
                colLists.add(adminCollectionGroup);
            }
        }
        re.setData(colLists);
        return re;
    }



    /**
     * 合集项信息查询
     *
     * @return
     */
    @Override
    @Transactional
    public ResultDto<AdminCollectionGroup> queryCollectionItem(AdminCollectionVo input, String memberId, String os) {
        ResultDto<AdminCollectionGroup> re = new ResultDto<>();
        AdminCollectionGroup adminCollectionGroup = new AdminCollectionGroup();
        if (StringUtils.isBlank(input.getId())) {
            throw new BusinessException("请输入查询主键");
        }
        FindCollectionGroup findCollectionGroup = findCollectionGroupService.selectById(input.getId());
        adminCollectionGroup.setCoverPicture(findCollectionGroup.getCoverPicture());
        adminCollectionGroup.setSort(findCollectionGroup.getSort());
        adminCollectionGroup.setName(findCollectionGroup.getName());
        adminCollectionGroup.setDetail(findCollectionGroup.getDetail());
        adminCollectionGroup.setId(findCollectionGroup.getId());
        List<CollectionItemBean> itemList = findCollectionGroupDao.getCollectionItemAll(findCollectionGroup.getId());
        for (CollectionItemBean collectionItemBean : itemList) {
            if(null==collectionItemBean.getVersionChild()){
                collectionItemBean.setVersion(collectionItemBean.getVersionMain());
            }else if(null!=collectionItemBean.getVersionMain() && null!=collectionItemBean.getVersionChild()){
                collectionItemBean.setVersion(collectionItemBean.getVersionMain()+"."+collectionItemBean.getVersionChild());
            }
            if (null == collectionItemBean.getScore()) {
                collectionItemBean.setScore(0.0);
            }
            collectionItemBean.setMake(false);
            if (StringUtils.isNotBlank(memberId)) {
                GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", collectionItemBean.getGameId()).eq("phone_model", os).eq("state", 0));
                if (srel != null) {
                    collectionItemBean.setMake(true);
                    collectionItemBean.setClause(1 == srel.getAgree() ? true : false);
                }
            }
            CircleGamePostVo circleGamePostVo = new CircleGamePostVo();
            circleGamePostVo.setGameId(collectionItemBean.getGameId());
            circleGamePostVo.setType(1);
            ResultDto<String> dto = communityFeignClient.getCircleByGame(circleGamePostVo);
            if (null == dto) {
                collectionItemBean.setCircleId(null);
            } else {
                collectionItemBean.setCircleId(dto.getData());
            }
        }
        adminCollectionGroup.setList(itemList);
        MallBannersInput mallBannersInput = new MallBannersInput();
        mallBannersInput.setTarget_id(input.getId());
        mallBannersInput.setLogin_id(memberId);
        ResultDto<BannerBean> reBanner = systemFeignClient.queryCollection(mallBannersInput);
        if(null!=reBanner && null!=reBanner.getData()){
            BannerBean bannerBean = reBanner.getData();
            adminCollectionGroup.setCount(bannerBean.getCount());
        }
        re.setData(adminCollectionGroup);
        return re;
    }


}
