package com.fungo.games.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.common.utils.StringUtils;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.games.config.NacosFungoCircleConfig;
import com.fungo.games.dao.BasTagDao;
import com.fungo.games.dao.GameCollectionGroupDao;
import com.fungo.games.dao.GameDao;
import com.fungo.games.dao.GameTagDao;
import com.fungo.games.entity.*;
import com.fungo.games.facede.IEvaluateProxyService;
import com.fungo.games.feign.CommunityFeignClient;
import com.fungo.games.feign.SystemFeignClient;
import com.fungo.games.helper.es.ESDAOServiceImpl;
import com.fungo.games.service.*;
import com.game.common.api.InputPageDto;
import com.game.common.buriedpoint.BuriedPointUtils;
import com.game.common.common.MemberIncentCommonUtils;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.Setting;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.*;
import com.game.common.dto.mall.MallGoodsInput;
import com.game.common.dto.search.GameSearchOut;
import com.game.common.dto.user.MemberDto;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.enums.CommonEnum;
import com.game.common.repo.cache.facade.FungoCacheGame;
import com.game.common.repo.cache.facade.FungoCacheMember;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.StringUtil;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;
import com.game.common.vo.CircleGamePostVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@Service("gameService")
public class GameServiceImpl implements IGameService {

    private static Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired
    private GameService gameService;
    @Autowired
    private GameReleaseLogService logService;
    @Autowired
    private GameEvaluationService gameEvaluationService;
    @Autowired
    private GameTagService gameTagService;
    @Autowired
    private GameSurveyRelService surveyRelService;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private GameCollectionItemService gameCollectionItemService;
    @Autowired
    private FungoCacheGame fungoCacheGame;
    @Autowired
    private IEvaluateProxyService iEvaluateProxyService;
    @Autowired
    private FungoCacheMember fungoCacheMember;
    @Autowired
    private GameSurveyRelService gameSurveyRelService;
    @Autowired
    private GameTagAttitudeService gameTagAttitudeService;
    @Autowired
    private BasTagService basTagService;
    @Autowired
    private BasTagGroupService basTagGroupService;
    @Autowired
    private CommunityFeignClient communityFeignClient;
    @Autowired
    private GameCollectionGroupDao collectionGroupDao;
    @Autowired
    private BasTagDao dao;
    @Autowired
    private NacosFungoCircleConfig nacosFungoCircleConfig;
    @Autowired
    private ESDAOServiceImpl esdaoServiceImpl;
    @Autowired
    private GameTagDao gameTagDao;
    @Autowired
    private AsyncTaskService asyncTaskService;
    @Autowired
    private SystemFeignClient systemFeignClient;

    @Override
    public FungoPageResultDto<GameOutPage> getGameList(GameInputPageDto gameInputDto, String memberId, String os) {
        String keySuffix = JSON.toJSONString(gameInputDto) + os;
        FungoPageResultDto<GameOutPage> re = (FungoPageResultDto<GameOutPage>) fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_LIST + memberId, keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }

        Wrapper wrapper = new EntityWrapper<Game>().where("state = {0}", 0);
        int limit = gameInputDto.getLimit();
        int page = gameInputDto.getPage();
        int sort = gameInputDto.getSort();
        String tag = gameInputDto.getTag();
        if (tag != null && !"".equals( tag.replace(" ", "")  ) ) {
            wrapper = wrapper.like("tags", tag);
        }

        @SuppressWarnings("unchecked")
//		Page<Game> gamePage = gameService.selectPage(new Page<>(page, limit),wrapper);
//		List<Game> gameList = gamePage.getRecords();
//         Page page1 = new Page(page,limit);
                List<Game> gameList = gameService.selectList(wrapper);
        if (gameList.size() == 0) {
            return new FungoPageResultDto<>();
        }
        Comparator<Game> hotFun = new HotFun();// 排序接口
        Comparator<Game> rateFun = new RateFun();// 排序接口
        Comparator<Game> dateFun = new DateFun();// 排序接口

        if (sort == 1) {// 时间正序
            Collections.sort(gameList, dateFun);
        } else if (sort == 2) {// 时间倒序
            Collections.sort(gameList, Collections.reverseOrder(dateFun));
        } else if (sort == 3) {// 热力值正序
            Collections.sort(gameList, hotFun);
        } else if (sort == 4) {// 热力值倒序
            Collections.sort(gameList, Collections.reverseOrder(hotFun));
        } else if (sort == 5) {// 评分正序
            Collections.sort(gameList, rateFun);
        } else if (sort == 6) {// 评分倒序
            Collections.sort(gameList, Collections.reverseOrder(rateFun));
        } else {//默认排序 评分倒序
            Collections.sort(gameList, Collections.reverseOrder(rateFun));
        }

        boolean m = false;
        if (!CommonUtil.isNull(memberId)) {
            m = true;
        }

        Page<Game> p = pageFormat(gameList, page, limit);
        List<Game> records = p.getRecords();
        List<GameOutPage> dataList = new ArrayList<>();
        for (Game game : records) {
            GameOutPage out = new GameOutPage();
            out.setObjectId(game.getId());
            out.setName(game.getName());
            out.setIcon(game.getIcon());


            //游戏编号
            out.setGameIdtSn(game.getGameIdtSn());

            //2.4.3
            out.setAndroidState(game.getAndroidState());
            out.setIosState(game.getIosState());
            out.setRating(getGameRating(game.getId()));
            if (game.getAndroidPackageName() == null) {
                game.setAndroidPackageName("");
            }
            if (game.getApk() == null) {
                game.setApk("");
            }
            out.setApkUrl(game.getApk());
            out.setTags(game.getTags());
            out.setAndroidStatusDesc(gameStatusDesc(game.getAndroidStatusDesc()));
            out.setIosStatusDesc(gameStatusDesc(game.getIosStatusDesc()));
            out.setItunesId(game.getItunesId());
            out.setAndroidPackageName(game.getAndroidPackageName());
            out.setGame_size((long) game.getGameSize());
            //推荐数据 已弃用
            Integer recommend_total_count = game.getRecommendNum() + game.getUnrecommendNum();
            DecimalFormat df = new DecimalFormat("#.00");
            out.setRecommend_total_rate(recommend_total_count == 0 ? 0 : Double.parseDouble(df.format((double) game.getRecommendNum() / recommend_total_count * 100)));
            out.setRecommend_total_count(gameEvaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", game.getId()).and("state != -1")));
            //

            out.setCreatedAt(DateTools.fmtDate(game.getCreatedAt()));
            out.setUpdatedAt(DateTools.fmtDate(game.getUpdatedAt()));

            if (m) {
                GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", game.getId()).eq("phone_model", os).eq("state", 0));
                if (srel != null) {
                    out.setBinding(!StringUtils.isNullOrEmpty(srel.getAppleId()));
                    out.setClause(1 == srel.getAgree() ? true : false);
                    out.setMake(true);
                }
            }

            dataList.add(out);
        }
        re = new FungoPageResultDto<GameOutPage>();
        re.setData(dataList);
        PageTools.pageToResultDto(re, p);

        //redis cache
        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_LIST + memberId, keySuffix, re,60);
        return re;
    }


    // 转换游戏大小的格式
    public String formatGameSize(long size) {
        Double dSize = (double) size;
        dSize = dSize / 1024 / 1024;
        if (dSize == 0) {
            return "未知";
        } else if (dSize >= 1024) {
            dSize = dSize / 1024;
            return String.format("%.2f", dSize) + "G";
        } else {
            return String.format("%.2f", dSize) + "M";
        }
    }


    //手动分页
    public Page<Game> pageFormat(List<Game> gameList, int page, int limit) {
        int totalCount = gameList.size();//总条数

        int totalPage = (int) Math.ceil((double) totalCount / limit);//总页数
        if (page == totalPage) {
            gameList = gameList.subList(limit * (page - 1), totalCount);
        } else {
            gameList = gameList.subList(limit * (page - 1), limit * page);
        }

        Page<Game> gamePage = new Page<>(page, limit);
        gamePage.setRecords(gameList);
        gamePage.setCurrent(page);
        gamePage.setTotal(totalCount);

        return gamePage;
    }


    public List<TraitBean> traitFormat(HashMap<String, BigDecimal> rateData) {
        List<TraitBean> traitList = new ArrayList<>();

        if (traitList.size() == 0) {
            for (int i = 1; i <= 5; i++) {
                TraitBean tb = new TraitBean();
                tb.setKey("avgTrait" + i);
                tb.setKeyName(transKey(tb.getKey()));
                tb.setValue(rateData.get(tb.getKey()) == null ? BigDecimal.valueOf(0) : rateData.get(tb.getKey()));
                traitList.add(tb);
            }
        }
        return traitList;
    }

    /**
     *  2019-11-17 修回原来的顺序
     *  trait 1 画面
     *  trait 5  玩法
     * @param key
     * @return
     */
    public String transKey(String key) {
            if (key.contains("1")) {
                return "画面";
            } else if (key.contains("2")) {
                return "音乐";
            } else if (key.contains("3")) {
                return "氪金";
            } else if (key.contains("4")) {
                return "剧情";
            } else if (key.contains("5")) {
                return "玩法";
            }
//        if (key.contains("1")) {
//            return "玩法";
//        } else if (key.contains("2")) {
//            return "氪金";
//        } else if (key.contains("3")) {
//            return "音乐";
//        } else if (key.contains("4")) {
//            return "画面";
//        } else if (key.contains("5")) {
//            return "剧情";
//        }
        return "";
    }

    public HashMap<String, Double> percentFormat(HashMap<String, Double> perMap) {
        double i = perMap.get("1") + perMap.get("2") + perMap.get("3") + perMap.get("4") + perMap.get("5");
        if (i != 100 && i != 0) {
            DecimalFormat fm = new DecimalFormat("#.00");
            if (i > 100) {
                double rest = i - 100;
                if (perMap.get("1") > rest) {
                    perMap.put("1", Double.parseDouble(fm.format(perMap.get("1") - rest)));
                } else if (perMap.get("2") > rest) {
                    perMap.put("2", Double.parseDouble(fm.format(perMap.get("2") - rest)));
                } else if (perMap.get("3") > rest) {
                    perMap.put("3", Double.parseDouble(fm.format(perMap.get("3") - rest)));
                } else if (perMap.get("4") > rest) {
                    perMap.put("4", Double.parseDouble(fm.format(perMap.get("4") - rest)));
                } else if (perMap.get("5") > rest) {
                    perMap.put("5", Double.parseDouble(fm.format(perMap.get("5") - rest)));
                }
            } else if (i < 100) {
                double rest = 100 - i;
                if (perMap.get("1") > 0) {
                    perMap.put("1", Double.parseDouble(fm.format(perMap.get("1") + rest)));
                } else if (perMap.get("2") > 0) {
                    perMap.put("2", Double.parseDouble(fm.format(perMap.get("2") + rest)));
                } else if (perMap.get("3") > 0) {
                    perMap.put("3", Double.parseDouble(fm.format(perMap.get("3") + rest)));
                } else if (perMap.get("4") > 0) {
                    perMap.put("4", Double.parseDouble(fm.format(perMap.get("4") + rest)));
                } else if (perMap.get("5") > 0) {
                    perMap.put("5", Double.parseDouble(fm.format(perMap.get("5") + rest)));
                }
            }
        }
        return perMap;

    }

    @Override
    public ResultDto<GameOut> getGameDetail(String gameId, String memberId, String ptype) throws Exception {
        GameOut out = new GameOut();
        try {
            GameOut outResult = (GameOut) fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + gameId, memberId + ptype);
            if (null != outResult) {
                return ResultDto.success(outResult);
            }
            Game game = null;
            if(MemberIncentCommonUtils.checkNumber(gameId)){
                game = gameService.selectOne(new EntityWrapper<Game>().eq("game_idt_sn", gameId).eq("state", "0"));
            }else {
                game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameId).eq("state", "0"));
            }
//            Game game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameId).eq("state", "0"));
            if (game == null) {
                return ResultDto.error( AbstractResultEnum.CODE_GAME_THREE.getKey(), "找不到目标游戏");
            }
            // 根据图片比例数据生成相应的返回字段
            int width = 16, height = 9;
            if (game.getImageRatio() == 1) {
                width = 9;
                height = 16;
            }

            out.setName(game.getName());
            out.setGame_size(formatGameSize(game.getGameSize()));
            out.setImage_height(height);
            out.setImage_width(width);
            String versionChild = game.getVersionChild();
            if(CommonUtil.isNull(versionChild)){
                out.setVersion(game.getVersionMain() );
            }else {
                out.setVersion(game.getVersionMain()+"."+versionChild);
            }


            out.setFungoTalk(game.getFungoTalk());

            //游戏编号
            out.setGameIdtSn(game.getGameIdtSn());

            //根据当前运行环境生成链接
            String env = Setting.RUN_ENVIRONMENT;
            if (env.equals("dev")) {
                out.setLink_url(Setting.DEFAULT_SHARE_URL_DEV + "/game/" + gameId);
            } else if (env.equals("pro")) {
                out.setLink_url(Setting.DEFAULT_SHARE_URL_PRO + "/game/" + gameId);
            } else if (env.equals("uat")) {
                out.setLink_url(Setting.DEFAULT_SHARE_URL_DEV + "/game/" + gameId);
            }

            // 上架计划增加下载按钮的开关（关）
            out.setIs_download(false);
            try {
                if (game.getCompatibility() != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    out.setCompatibility((ArrayList<String>) mapper.readValue(game.getCompatibility(), ArrayList.class));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            out.setPackageName(game.getAndroidPackageName());

            out.setVideo(game.getVideo());
            out.setApk(game.getApk());
            out.setCover_image(game.getCoverImage());
            out.setDetail(game.getDetail());
            out.setDeveloper(game.getDeveloper());
            out.setIcon(game.getIcon());
            out.setImage_ratio(game.getImageRatio());
            out.setIsbn_id(game.getIsbnId());
            out.setIntro(game.getIntro());
            out.setItunes_id(game.getItunesId());
            out.setObjectId(game.getId());
            out.setLink_community(game.getCommunityId());
            String origin1 = game.getOrigin();
            out.setOrigin(origin1.equals("中国")?null:origin1);
            out.setGoogleDeputyName(game.getGoogleDeputyName());
            out.setRelease_image(game.getReleaseImage());
            out.setState(game.getState());
            out.setUpdate_log(game.getUpdateLog());
            out.setVersion_child(game.getVersionChild());
            out.setVersion_main(game.getVersionMain());
            out.setCompany(game.getCompany());
            out.setPublisher(game.getPublisher());

            if (!StringUtils.isNullOrEmpty(memberId)) {
                ResultDto<Map<String,Object>> resultDto = systemFeignClient.getCollectByGameId(gameId,memberId);
                if(resultDto!= null && resultDto.getData() != null){
                    Map<String,Object> resultMap = resultDto.getData();
                    out.setCollect( (boolean)resultMap.get( "collect" ));
                }
            }
            // 处理加速
            String origin = origin1;
            Boolean canFast = game.getCanFast();
            if(canFast == null){
                canFast = Boolean.TRUE;
            }
            out.setCanFast(origin.equals("中国")?Boolean.FALSE:canFast);
            // 处理描述
            out.setAndroidStatusDesc(gameStatusDesc(game.getAndroidStatusDesc()));

            // 生成推荐相关数据
            //fix bug: 修改排序规则 按时间升序 [by mxf 2019-03-01]
            List<GameEvaluation> recoList = gameEvaluationService.selectList(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("state", 0).orderBy("created_at", true));
            //end
//		List<BasAction> recoList = actionService.selectList(new EntityWrapper<BasAction>().eq("target_id", gameId).in("type", new Integer[] { 8, 9 }).and("state != -1").orderBy("created_at", false));
            // List<String> userIdList =
            // actionList.stream().map(BasAction::getMemberId).collect(Collectors.toList());
//		int likeCountRecent = 0;
//		int unlikeCountRecent = 0;
//		int recentDayCount = 7;
//		int recentTime = recentDayCount * 24 * 60 * 60 * 1000;
//		boolean isRecent = true;
            List<Map<String, Object>> recommendList = new ArrayList<>();
            if (recoList != null && recoList.size() != 0) {
                for (GameEvaluation g : recoList) {
                    if (recommendList.size() < 8) {
                        Map<String, Object> map = new HashMap<>();
//                    迁移 微服务 根据用户id获取memberDto feign执行
//                    2019-05-11
//                    lyc
//                    Member user = memberService.selectById(g.getMemberId());
                        MemberDto memberDto = new MemberDto();
                        memberDto.setId(g.getMemberId());
                        MemberDto user = iEvaluateProxyService.getMemberDtoBySelectOne(memberDto);
                        if (null != user) {
                            map.put("username", user.getUserName());
                            map.put("avatar", user.getAvatar());
//                        迁移 微服务 根据用户id获取用户身份图标
//                        2019-05-11
//                        lyc
//                        map.put("statusImg", iuserService.getStatusImage(memberId));
                            if (!StringUtils.isNullOrEmpty(memberId)) {
                                List<HashMap<String, Object>> list = iEvaluateProxyService.getStatusImageByMemberId(memberId);
                                map.put("statusImg", list);
                            }
                            recommendList.add(map);
                        }
                    }
//				if (isRecent && new Date().getTime() - g.getCreatedAt().getTime() <= recentTime) {
//					if ("1".equals(g.getIsRecommend())) {
//						likeCountRecent = likeCountRecent + 1;
//					} else {
//						unlikeCountRecent = unlikeCountRecent + 1;
//					}
//				} else {
//					isRecent = false;
//				}
                }
                out.setRecommend_list(recommendList);
            }
//		int recommend_recent_count = likeCountRecent + unlikeCountRecent;
//		int recommend_total_count = game.getRecommendNum() + game.getUnrecommendNum();
//		//gameMap.put("recommend_list", recommendList);
//		out.setRecent_day_count(recentDayCount);
//		out.setRecommend_recent_count(recommend_recent_count);
//		DecimalFormat df = new DecimalFormat("#.00");
//		out.setRecommend_recent_rate(recommend_recent_count == 0 ? 0 : Double.parseDouble(df.format((double)likeCountRecent / recommend_recent_count * 100)));
//		out.setRecommend_total_rate(recommend_total_count == 0 ? 0 :  Double.parseDouble(df.format((double)game.getRecommendNum() / recommend_total_count * 100)));
//		out.setRecommend_total_count(recommend_total_count);

            //fix:游戏下载量使用虚假字段 [by mxf 2019-05-07]
            int downloadNum = 0;
            if (null == game.getBoomDownloadNum() || 0 == game.getBoomDownloadNum()) {
                downloadNum = game.getDownloadNum();
            } else {
                downloadNum = game.getBoomDownloadNum().intValue();
            }
            out.setDownload_num(downloadNum);
            //ends

            // 查询评论数量
            int evaCount = gameEvaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).and("state != -1")); //.ne( "type","2" )
            out.setEvaluation_num(evaCount);

            //2.4  平均分 每颗星占比 雷达图
            //没人评分 有人评分
            int count = gameEvaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId));
            if (count > 0) {
                HashMap<String, BigDecimal> rateData = gameDao.getRateData(gameId);
                if (rateData != null) {
                    if (rateData.get("avgRating") != null) {
                        out.setRating(Double.parseDouble(rateData.get("avgRating").toString()));
                    }
                    rateData.remove("gameId");
                    rateData.remove("avgRating");
                } else {
                    rateData = new HashMap<String, BigDecimal>();
                    rateData.put("avgTrait1", BigDecimal.valueOf(0));
                    rateData.put("avgTrait2", BigDecimal.valueOf(0));
                    rateData.put("avgTrait3", BigDecimal.valueOf(0));
                    rateData.put("avgTrait4", BigDecimal.valueOf(0));
                    rateData.put("avgTrait5", BigDecimal.valueOf(0));
                }
                out.setTraitList(traitFormat(rateData));

                //获取每个游戏评分所占百分比 key:评分  value:百分比
                HashMap<String, BigDecimal> percentData = gameDao.getPercentData(gameId);
                HashMap<String, Double> perMap = new HashMap<>();

                if (percentData != null) {//评分两两分组,统计占比 1：1-2分， 2：3-4分， 3：5-6分， 4：7-8分， 5：9-10分
                    perMap.put("1", percentData.get("1").doubleValue() + percentData.get("2").doubleValue());
                    perMap.put("2", percentData.get("3").doubleValue() + percentData.get("4").doubleValue());
                    perMap.put("3", percentData.get("5").doubleValue() + percentData.get("6").doubleValue());
                    perMap.put("4", percentData.get("7").doubleValue() + percentData.get("8").doubleValue());
                    perMap.put("5", percentData.get("9").doubleValue() + percentData.get("10").doubleValue());
                } else {
                    perMap.put("1", 0.0);
                    perMap.put("2", 0.0);
                    perMap.put("3", 0.0);
                    perMap.put("4", 0.0);
                    perMap.put("5", 0.0);
                }
                perMap = percentFormat(perMap);

                out.setRatingList(rateFormat(perMap));
                //无评分时，返回默认值数据
            } else {

                List<TraitBean> traitList = new ArrayList<>();
                for (int i = 1; i <= 5; i++) {
                    TraitBean tb = new TraitBean();
                    tb.setKey("avgTrait" + i);
                    tb.setKeyName(transKey(tb.getKey()));
                    tb.setValue(BigDecimal.valueOf(0));
                    traitList.add(tb);
                }
                out.setTraitList(traitList);


                HashMap<String, Double> perMap = new HashMap<>();
                perMap.put("1", 0.0);
                perMap.put("2", 0.0);
                perMap.put("3", 0.0);
                perMap.put("4", 0.0);
                perMap.put("5", 0.0);
                perMap = percentFormat(perMap);
                out.setRatingList(rateFormat(perMap));

            }
            String tags = game.getTags();
            if(CommonUtil.isNull(tags)){
                out.setTags(new ArrayList<>());
            }else{
                out.setTags(Arrays.asList(tags.split(",")));
            }

           /* List<TagBean> tags = dao.getGameTags(gameId);
            // 查询游戏标签
            List<GameTag> gameTagList = gameTagService.selectList(new EntityWrapper<GameTag>().eq("game_id", gameId).eq("type", 2));
            //andNew("type = {0}",1).or("like_num > {0}",5).orderBy("like_num", false).last("LIMIT 5"));
            if (gameTagList != null && gameTagList.size() > 0) {
//            迁移微服务 根据判断集合id获取BasTagList集合
                List<BasTag> tagList = basTagService.selectList(new EntityWrapper<BasTag>().in("id", gameTagList.stream().map(GameTag::getTagId).collect(Collectors.toList())));

                if (tagList != null && tagList.size() > 0) {
                    List<String> tagNames = new ArrayList<>();
                    tagList.forEach(tag -> tagNames.add(tag.getName()));
                    //gameMap.put("tags", tagNames);
                    out.setTags(tagNames);
                }
            }*/

            out.setCreatedAt(DateTools.fmtDate(game.getCreatedAt()));
            out.setUpdatedAt(DateTools.fmtDate(game.getUpdatedAt()));
            try {
                if (game.getImages() != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    out.setImages((ArrayList<String>) mapper.readValue(game.getImages(), ArrayList.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.setAuthor(game.getMemberId());

            if (game.getAndroidState() != null) {
                out.setAndroidState(game.getAndroidState());
            }
            if (game.getIosState() != null) {
                out.setIosState(game.getIosState());
            }

            if (org.apache.commons.lang3.StringUtils.isNoneBlank(memberId) &&
                    org.apache.commons.lang3.StringUtils.isNoneBlank(ptype)) {//游戏预约测试信息
                GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", game.getId()).eq("phone_model", ptype).eq("state", 0));
                if (srel != null) {
                    out.setBinding(!StringUtils.isNullOrEmpty(srel.getAppleId()));
                    out.setClause(1 == srel.getAgree() ? true : false);
                    out.setMake(true);
                }
            }

            //从系统微服务获取该游戏的礼包数量
            MallGoodsInput mallGoodsInput = new MallGoodsInput();
            mallGoodsInput.setGameId(gameId);
            Integer goodsCountWithGame = iEvaluateProxyService.queryGoodsCountWithGame(mallGoodsInput);
            out.setGoodsCount(goodsCountWithGame);

            /**
             * 功能描述: 添加游戏关联圈子
             * @auther: dl.zhang
             * @date: 2019/6/24 13:50
             */
            try {
                CircleGamePostVo circleGamePostVo = new CircleGamePostVo(CircleGamePostVo.CircleGamePostTypeEnum.GAMESID.getKey(),game.getId(),"");
                ResultDto<String> re = communityFeignClient.getCircleByGame(circleGamePostVo);
                if (CommonEnum.SUCCESS.code().equals(String.valueOf(re.getStatus())) && !re.getData().equals("")) {
                    out.setLink_circle(re.getData());
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("根据游戏id询圈子id异常,游戏id：" + game.getId(), e);
            }
            List<GameCollectionGroup> gameCollectionGroupList = collectionGroupDao.selectGameCollectionGroupByGameId( gameId);
            List<GameOut.GameGroup> gameGroups = new ArrayList<>(  );
            if(gameCollectionGroupList != null && gameCollectionGroupList.size() > 0){
                gameCollectionGroupList.stream().forEach( x ->{
                    GameOut.GameGroup gameGroup = new GameOut.GameGroup();
                    gameGroup.setGameCollectionId( x.getId() );
                    gameGroup.setGameCollectionName( x.getName() );
                    gameGroups.add( gameGroup );
                } );
                out.setGameGroups( gameGroups );
            }
            //
            out.setUserAndroidState("0");
            out.setUserIosState("0");
            List<GameSurveyRel> surs = surveyRelService.selectList(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", gameId).eq( "state","0" ));
            surs.stream().forEach( s ->{
                if("Android".equals(s.getPhoneModel())){
                    out.setUserAndroidState("1");
                }else if("iOS".equals( s.getPhoneModel() )){
                    out.setUserIosState("1");
                }
            } );
        }catch (Exception e){
            logger.error("游戏详情异常,游戏id="+gameId,e);
        }
        //redis cache
        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + gameId,
                memberId + ptype, out, 60 * 5);

        // vpc2.1 改版 记录用户浏览游戏详情
        if(StringUtil.isNotNull(gameId)&&StringUtil.isNotNull(memberId)){
            asyncTaskService.recordGameView(memberId,gameId);
        }
        return ResultDto.success(out);
    }

    @Override
    public ResultDto<GameOut> getGameDetailByNumber(String gameNumber, String memberId, String ptype) {
        GameOut out = new GameOut();
        GameOut outResult = (GameOut) fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + gameNumber, memberId + ptype);
        if (null != outResult) {
            return ResultDto.success(outResult);
        }
//            Game game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameId).eq("state", "0"));
        Game game = null;
        if(MemberIncentCommonUtils.checkNumber(gameNumber)){
            game = gameService.selectOne(new EntityWrapper<Game>().eq("game_idt_sn", gameNumber).eq("state", "0"));
        }else {
            game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameNumber).eq("state", "0"));
        }

        if (game == null) {
            return ResultDto.error( AbstractResultEnum.CODE_GAME_THREE.getKey(), AbstractResultEnum.CODE_GAME_THREE.getFailevalue());
        }
        String gameId = game.getId();
        try {
            // 根据图片比例数据生成相应的返回字段
            int width = 16, height = 9;
            if (game.getImageRatio() == 1) {
                width = 9;
                height = 16;
            }

            out.setName(game.getName());
            out.setGame_size(formatGameSize(game.getGameSize()));
            out.setImage_height(height);
            out.setImage_width(width);
            String versionChild = game.getVersionChild();
            if(CommonUtil.isNull(versionChild)){
                out.setVersion(game.getVersionMain() );
            }else {
                out.setVersion(game.getVersionMain()+"."+versionChild);
            }


            out.setFungoTalk(game.getFungoTalk());

            //游戏编号
            out.setGameIdtSn(game.getGameIdtSn());

            //根据当前运行环境生成链接
            String env = Setting.RUN_ENVIRONMENT;
            if (env.equals("dev")) {
                out.setLink_url(Setting.DEFAULT_SHARE_URL_DEV + "/game/" + gameId);
            } else if (env.equals("pro")) {
                out.setLink_url(Setting.DEFAULT_SHARE_URL_PRO + "/game/" + gameId);
            } else if (env.equals("uat")) {
                out.setLink_url(Setting.DEFAULT_SHARE_URL_DEV + "/game/" + gameId);
            }

            // 上架计划增加下载按钮的开关（关）
            out.setIs_download(false);
            try {
                if (game.getCompatibility() != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    out.setCompatibility((ArrayList<String>) mapper.readValue(game.getCompatibility(), ArrayList.class));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            out.setPackageName(game.getAndroidPackageName());

            out.setVideo(game.getVideo());
//            out.setApk(game.getApk());
            out.setCover_image(game.getCoverImage());
            out.setDetail(game.getDetail());
            out.setDeveloper(game.getDeveloper());
            out.setIcon(game.getIcon());
            out.setImage_ratio(game.getImageRatio());
            out.setIsbn_id(game.getIsbnId());
            out.setIntro(game.getIntro());
            out.setItunes_id(game.getItunesId());
            out.setObjectId(game.getId());
            out.setLink_community(game.getCommunityId());
            String origin1 = game.getOrigin();
            out.setOrigin(origin1.equals("中国")?null:origin1);
            out.setGoogleDeputyName(game.getGoogleDeputyName());
            out.setRelease_image(game.getReleaseImage());
            out.setState(game.getState());
            out.setUpdate_log(game.getUpdateLog());
            out.setVersion_child(game.getVersionChild());
            out.setVersion_main(game.getVersionMain());
            out.setCompany(game.getCompany());
            out.setPublisher(game.getPublisher());

            // 处理加速
            String origin = origin1;
            Boolean canFast = game.getCanFast();
            if(canFast == null){
                canFast = Boolean.TRUE;
            }
            out.setCanFast(origin.equals("中国")?Boolean.FALSE:canFast);
            // 处理描述
            out.setAndroidStatusDesc(gameStatusDesc(game.getAndroidStatusDesc()));

            // 生成推荐相关数据
            //fix bug: 修改排序规则 按时间升序 [by mxf 2019-03-01]
            List<GameEvaluation> recoList = gameEvaluationService.selectList(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("state", 0).orderBy("created_at", true));
            //end
//		List<BasAction> recoList = actionService.selectList(new EntityWrapper<BasAction>().eq("target_id", gameId).in("type", new Integer[] { 8, 9 }).and("state != -1").orderBy("created_at", false));
            // List<String> userIdList =
            // actionList.stream().map(BasAction::getMemberId).collect(Collectors.toList());
//		int likeCountRecent = 0;
//		int unlikeCountRecent = 0;
//		int recentDayCount = 7;
//		int recentTime = recentDayCount * 24 * 60 * 60 * 1000;
//		boolean isRecent = true;
            List<Map<String, Object>> recommendList = new ArrayList<>();
            if (recoList != null && recoList.size() != 0) {
                for (GameEvaluation g : recoList) {
                    if (recommendList.size() < 8) {
                        Map<String, Object> map = new HashMap<>();
//                    迁移 微服务 根据用户id获取memberDto feign执行
//                    2019-05-11
//                    lyc
//                    Member user = memberService.selectById(g.getMemberId());
                        MemberDto memberDto = new MemberDto();
                        memberDto.setId(g.getMemberId());
                        MemberDto user = iEvaluateProxyService.getMemberDtoBySelectOne(memberDto);
                        if (null != user) {
                            map.put("username", user.getUserName());
                            map.put("avatar", user.getAvatar());
//                        迁移 微服务 根据用户id获取用户身份图标
//                        2019-05-11
//                        lyc
//                        map.put("statusImg", iuserService.getStatusImage(memberId));
                            if (!StringUtils.isNullOrEmpty(memberId)) {
                                List<HashMap<String, Object>> list = iEvaluateProxyService.getStatusImageByMemberId(memberId);
                                map.put("statusImg", list);
                            }
                            recommendList.add(map);
                        }
                    }
//				if (isRecent && new Date().getTime() - g.getCreatedAt().getTime() <= recentTime) {
//					if ("1".equals(g.getIsRecommend())) {
//						likeCountRecent = likeCountRecent + 1;
//					} else {
//						unlikeCountRecent = unlikeCountRecent + 1;
//					}
//				} else {
//					isRecent = false;
//				}
                }
                out.setRecommend_list(recommendList);
            }
//		int recommend_recent_count = likeCountRecent + unlikeCountRecent;
//		int recommend_total_count = game.getRecommendNum() + game.getUnrecommendNum();
//		//gameMap.put("recommend_list", recommendList);
//		out.setRecent_day_count(recentDayCount);
//		out.setRecommend_recent_count(recommend_recent_count);
//		DecimalFormat df = new DecimalFormat("#.00");
//		out.setRecommend_recent_rate(recommend_recent_count == 0 ? 0 : Double.parseDouble(df.format((double)likeCountRecent / recommend_recent_count * 100)));
//		out.setRecommend_total_rate(recommend_total_count == 0 ? 0 :  Double.parseDouble(df.format((double)game.getRecommendNum() / recommend_total_count * 100)));
//		out.setRecommend_total_count(recommend_total_count);

            //fix:游戏下载量使用虚假字段 [by mxf 2019-05-07]
            int downloadNum = 0;
            if (null == game.getBoomDownloadNum() || 0 == game.getBoomDownloadNum()) {
                downloadNum = game.getDownloadNum();
            } else {
                downloadNum = game.getBoomDownloadNum().intValue();
            }
            out.setDownload_num(downloadNum);
            //ends

            // 查询评论数量
            int evaCount = gameEvaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).and("state != -1")); //.ne( "type","2" )
            out.setEvaluation_num(evaCount);

            //2.4  平均分 每颗星占比 雷达图
            //没人评分 有人评分
            int count = gameEvaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId));
            if (count > 0) {
                HashMap<String, BigDecimal> rateData = gameDao.getRateData(gameId);
                if (rateData != null) {
                    if (rateData.get("avgRating") != null) {
                        out.setRating(Double.parseDouble(rateData.get("avgRating").toString()));
                    }
                    rateData.remove("gameId");
                    rateData.remove("avgRating");
                } else {
                    rateData = new HashMap<String, BigDecimal>();
                    rateData.put("avgTrait1", BigDecimal.valueOf(0));
                    rateData.put("avgTrait2", BigDecimal.valueOf(0));
                    rateData.put("avgTrait3", BigDecimal.valueOf(0));
                    rateData.put("avgTrait4", BigDecimal.valueOf(0));
                    rateData.put("avgTrait5", BigDecimal.valueOf(0));
                }
                out.setTraitList(traitFormat(rateData));

                //获取每个游戏评分所占百分比 key:评分  value:百分比
                HashMap<String, BigDecimal> percentData = gameDao.getPercentData(gameId);
                HashMap<String, Double> perMap = new HashMap<>();

                if (percentData != null) {//评分两两分组,统计占比 1：1-2分， 2：3-4分， 3：5-6分， 4：7-8分， 5：9-10分
                    perMap.put("1", percentData.get("1").doubleValue() + percentData.get("2").doubleValue());
                    perMap.put("2", percentData.get("3").doubleValue() + percentData.get("4").doubleValue());
                    perMap.put("3", percentData.get("5").doubleValue() + percentData.get("6").doubleValue());
                    perMap.put("4", percentData.get("7").doubleValue() + percentData.get("8").doubleValue());
                    perMap.put("5", percentData.get("9").doubleValue() + percentData.get("10").doubleValue());
                } else {
                    perMap.put("1", 0.0);
                    perMap.put("2", 0.0);
                    perMap.put("3", 0.0);
                    perMap.put("4", 0.0);
                    perMap.put("5", 0.0);
                }
                perMap = percentFormat(perMap);

                out.setRatingList(rateFormat(perMap));
                //无评分时，返回默认值数据
            } else {

                List<TraitBean> traitList = new ArrayList<>();
                for (int i = 1; i <= 5; i++) {
                    TraitBean tb = new TraitBean();
                    tb.setKey("avgTrait" + i);
                    tb.setKeyName(transKey(tb.getKey()));
                    tb.setValue(BigDecimal.valueOf(0));
                    traitList.add(tb);
                }
                out.setTraitList(traitList);


                HashMap<String, Double> perMap = new HashMap<>();
                perMap.put("1", 0.0);
                perMap.put("2", 0.0);
                perMap.put("3", 0.0);
                perMap.put("4", 0.0);
                perMap.put("5", 0.0);
                perMap = percentFormat(perMap);
                out.setRatingList(rateFormat(perMap));

            }
            String tags = game.getTags();
            if(CommonUtil.isNull(tags)){
                out.setTags(new ArrayList<>());
            }else{
                out.setTags(Arrays.asList(tags.split(",")));
            }

           /* List<TagBean> tags = dao.getGameTags(gameId);
            // 查询游戏标签
            List<GameTag> gameTagList = gameTagService.selectList(new EntityWrapper<GameTag>().eq("game_id", gameId).eq("type", 2));
            //andNew("type = {0}",1).or("like_num > {0}",5).orderBy("like_num", false).last("LIMIT 5"));
            if (gameTagList != null && gameTagList.size() > 0) {
//            迁移微服务 根据判断集合id获取BasTagList集合
                List<BasTag> tagList = basTagService.selectList(new EntityWrapper<BasTag>().in("id", gameTagList.stream().map(GameTag::getTagId).collect(Collectors.toList())));

                if (tagList != null && tagList.size() > 0) {
                    List<String> tagNames = new ArrayList<>();
                    tagList.forEach(tag -> tagNames.add(tag.getName()));
                    //gameMap.put("tags", tagNames);
                    out.setTags(tagNames);
                }
            }*/

            out.setCreatedAt(DateTools.fmtDate(game.getCreatedAt()));
            out.setUpdatedAt(DateTools.fmtDate(game.getUpdatedAt()));
            try {
                if (game.getImages() != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    out.setImages((ArrayList<String>) mapper.readValue(game.getImages(), ArrayList.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.setAuthor(game.getMemberId());

            if (game.getAndroidState() != null) {
                out.setAndroidState(game.getAndroidState());
            }
            if (game.getIosState() != null) {
                out.setIosState(game.getIosState());
            }

            if (org.apache.commons.lang3.StringUtils.isNoneBlank(memberId) &&
                    org.apache.commons.lang3.StringUtils.isNoneBlank(ptype)) {//游戏预约测试信息
                GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", game.getId()).eq("phone_model", ptype).eq("state", 0));
                if (srel != null) {
                    out.setBinding(!StringUtils.isNullOrEmpty(srel.getAppleId()));
                    out.setClause(1 == srel.getAgree() ? true : false);
                    out.setMake(true);
                }
            }

            //从系统微服务获取该游戏的礼包数量
            MallGoodsInput mallGoodsInput = new MallGoodsInput();
            mallGoodsInput.setGameId(gameId);
            Integer goodsCountWithGame = iEvaluateProxyService.queryGoodsCountWithGame(mallGoodsInput);
            out.setGoodsCount(goodsCountWithGame);

            /**
             * 功能描述: 添加游戏关联圈子
             * @auther: dl.zhang
             * @date: 2019/6/24 13:50
             */
            try {
                CircleGamePostVo circleGamePostVo = new CircleGamePostVo(CircleGamePostVo.CircleGamePostTypeEnum.GAMESID.getKey(),game.getId(),"");
                ResultDto<String> re = communityFeignClient.getCircleByGame(circleGamePostVo);
                if (CommonEnum.SUCCESS.code().equals(String.valueOf(re.getStatus())) && !re.getData().equals("")) {
                    out.setLink_circle(re.getData());
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("根据游戏id询圈子id异常,游戏id：" + game.getId(), e);
            }
            List<GameCollectionGroup> gameCollectionGroupList = collectionGroupDao.selectGameCollectionGroupByGameId( gameId);
            List<GameOut.GameGroup> gameGroups = new ArrayList<>(  );
            if(gameCollectionGroupList != null && gameCollectionGroupList.size() > 0){
                gameCollectionGroupList.stream().forEach( x ->{
                    GameOut.GameGroup gameGroup = new GameOut.GameGroup();
                    gameGroup.setGameCollectionId( x.getId() );
                    gameGroup.setGameCollectionName( x.getName() );
                    gameGroups.add( gameGroup );
                } );
                out.setGameGroups( gameGroups );
            }
            //
            out.setUserAndroidState("0");
            out.setUserIosState("0");
            List<GameSurveyRel> surs = surveyRelService.selectList(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", gameId).eq( "state","0" ));
            surs.stream().forEach( s ->{
                if("Android".equals(s.getPhoneModel())){
                    out.setUserAndroidState("1");
                }else if("iOS".equals( s.getPhoneModel() )){
                    out.setUserIosState("1");
                }
            } );
        }catch (Exception e){
            logger.error("游戏详情异常,游戏id="+gameId,e);
        }
        //redis cache
        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + gameNumber,
                memberId + ptype, out, 60 * 5);

        // vpc2.1 改版 记录用户浏览游戏详情
        if(StringUtil.isNotNull(gameId)&&StringUtil.isNotNull(memberId)){
            asyncTaskService.recordGameView(memberId,gameId);
        }
        return ResultDto.success(out);
    }

//    @Override
//    public FungoPageResultDto<GameOutPage> getGameList(GameInputPageDto gameInputDto, String memberId, String os) {
//        return null;
//    }

    @Override
    public ResultDto<List<TagOutPage>> getGameTags() {
        List<TagOutPage> outList = (List<TagOutPage>) fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_TAG, "");
        if (null != outList && !outList.isEmpty()) {
            return ResultDto.success(outList);
        }
        //官方标签
//        迁移微服务 根据group_id获取BasTag集合
//        2019-05-13
//        lyc
        List<BasTag> tagList = basTagService.selectList(new EntityWrapper<BasTag>().eq("group_id", "5ad55eb4ac502e0042ae29d9"));
       /* BasTagDto basTagDto = new BasTagDto();
        basTagDto.setGroupId("5ad55eb4ac502e0042ae29d9");
        List<BasTagDto> tagList = iEvaluateProxyService.getBasTagBySelectListGroupId(basTagDto);*/
        outList = new ArrayList<>();
        for (BasTag tag : tagList) {
            TagOutPage out = new TagOutPage();
            out.setObjectId(tag.getId());
            out.setName(tag.getName());
            out.setGroupId(tag.getGroupId());
            out.setGameNum(tag.getGameNum());
            out.setSort(tag.getSort());
            out.setCreatedAt(DateTools.fmtDate(tag.getCreatedAt()));
            out.setUpdatedAt(DateTools.fmtDate(tag.getUpdatedAt()));
            outList.add(out);
        }
        //redis cache
        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_TAG, "", outList);
        return ResultDto.success(outList);
    }

    @Override
    public FungoPageResultDto<GameOutPage> recentEvaluatedGamesByMember(String userId, InputPageDto input) {
        String keySuffix = JSON.toJSONString(input);
        FungoPageResultDto<GameOutPage> re = (FungoPageResultDto<GameOutPage>) fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + userId,
                keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }

        Page page = new Page<>(input.getPage(), input.getLimit());
        List<HashMap<String, Object>> list = gameDao.getRecentCommentedGames(page, userId);
        List<GameOutPage> olist = new ArrayList<>();

        for (HashMap<String, Object> map : list) {
            GameOutPage out = new GameOutPage();
            out.setIcon((String) map.get("icon"));
            out.setName((String) map.get("name"));
            out.setObjectId((String) map.get("game_id"));
            HashMap<String, BigDecimal> rateData = gameDao.getRateData((String) map.get("game_id"));
            if (rateData != null) {
                if (rateData.get("avgRating") != null) {
                    out.setRating(Double.parseDouble(rateData.get("avgRating").toString()));
                } else {
                    out.setRating(0.0);
                }
            } else {
                out.setRating(0.0);
            }
            out.setComment_num((int) map.get("comment_num"));
            out.setLink_community((String) map.get("community_id"));
            out.setCategory((String) map.get("tags"));
            if (map.get("apk") != null) {
                out.setApkUrl((String) map.get("apk"));
            } else {
                out.setApkUrl("");
            }
            if (map.get("android_package_name") != null) {
                out.setAndroidPackageName((String) map.get("android_package_name"));
            } else {
                out.setAndroidPackageName("");
            }

            olist.add(out);
        }
        re = new FungoPageResultDto<GameOutPage>();
        olist = olist.stream().distinct().collect( Collectors.toList());
        re.setData(olist);
        PageTools.pageToResultDto(re, page);


        //redis cache
        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + userId, keySuffix, re);
        return re;
    }

    @Override
    public FungoPageResultDto<GameItem> getGameItems(String memberId, GameItemInput input, String os) {
        //通过id找出合集项
        //找出游戏 一个一个配
        FungoPageResultDto<GameItem> re = null;
//        String keySuffix = memberId + JSON.toJSONString(input) + os;
//        FungoPageResultDto<GameItem> re = (FungoPageResultDto<GameItem>) fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_ITEMS,
//                keySuffix);
//
//        if (null != re && null != re.getData() && re.getData().size() > 0) {
//            return re;
//        }

        boolean m = false;
        if (!CommonUtil.isNull(memberId)) {//是否为登录用户
            m = true;
        }

        re = new FungoPageResultDto<GameItem>();
        List<GameItem> ilist = new ArrayList<>();
        Page<GameCollectionItem> itemPage =
                gameCollectionItemService.selectPage(new Page<>(input.getPage(), input.getLimit()), new EntityWrapper<GameCollectionItem>().eq("group_id", input.getGroup_id()).eq("show_state", "1")
                        .orderBy("sort", false));
        List<GameCollectionItem> glist = itemPage.getRecords();
        List<String> gameIdList = new ArrayList<>();
        if (glist.size() > 0) {
            gameIdList = glist.stream().map(GameCollectionItem::getGameId).collect(Collectors.toList());
        }
        if (gameIdList.size() > 0) {
            List<Game> gamel = gameDao.getGameList(gameIdList);  //gameService.selectList(new EntityWrapper<Game>().in("id", gameIdList));
            for (Game game : gamel) {
                if(game.getState() != 0){
                    continue;
                }
                GameItem it = new GameItem();
                if (game.getAndroidPackageName() == null) {
                    game.setAndroidPackageName("");
                }
                if (game.getApk() == null) {
                    game.setApk("");
                }
                it.setAndroidPackageName(game.getAndroidPackageName());
                it.setAndroidState(game.getAndroidState() == null ? -1 : game.getAndroidState() );
                it.setApkUrl(game.getApk());
                if (m) {
                    GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", game.getId())
                            .eq("phone_model", os).eq("state", 0));
                    if (srel != null) {
                        it.setBinding(!StringUtils.isNullOrEmpty(srel.getAppleId()));
                        it.setClause(1 == srel.getAgree() ? true : false);
                        it.setMake(true);
                    }
                }
                it.setIcon(game.getIcon());
                it.setImages(game.getImages());
                it.setCoverImage(game.getCoverImage());
                it.setObjectId(game.getId());
                it.setName(game.getName());
                it.setOrigin(game.getOrigin());
                it.setIosState(game.getIosState() == null ? -1 : game.getIosState());
                it.setItunesId(game.getItunesId());
                it.setTags(game.getTags());
                it.setAndroidStatusDesc(gameStatusDesc(game.getAndroidStatusDesc()));
                it.setIosStatusDesc(gameStatusDesc(game.getIosStatusDesc()));
                HashMap<String, BigDecimal> rateData = gameDao.getRateData(game.getId());
                if (rateData != null) {
                    if (rateData.get("avgRating") != null) {
                        it.setRating(Double.parseDouble(rateData.get("avgRating").toString()));
                    } else {
                        it.setRating(0.0);
                    }
                } else {
                    it.setRating(0.0);
                }
                it.setCategory(game.getTags());
                it.setOrigin(game.getOrigin());
                ilist.add(it);
            }
            re.setData(ilist);
            PageTools.pageToResultDto(re, itemPage);

        }
        //redis cache
//        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_ITEMS, keySuffix, re);
        return re;
    }

    @Override
    public double getGameRating(String gameId) {
        HashMap<String, BigDecimal> rateData = gameDao.getRateData(gameId);
        if (rateData != null) {
            if (rateData.get("avgRating") != null) {
                return Double.parseDouble(rateData.get("avgRating").toString());
            } else {
                return 0.0;
            }
        } else {
            return 0.0;
        }
    }

    @Override
    public FungoPageResultDto<GameOutBean> getGameList(GameListVO input, String memberId) {
        List<Game> gameList = null;
        FungoPageResultDto<GameOutBean> re = new FungoPageResultDto<GameOutBean>();
        try {
            Wrapper<Game> wrapper = new EntityWrapper<Game>().in("id", input.getGameids());
            if (!CommonUtil.isNull(input.getKey())) {
                wrapper.like("name", input.getKey());
            }
            Page<Game> page = gameService.selectPage(new Page<>(input.getPage(), input.getLimit()), wrapper);
            gameList = page.getRecords();

            List<GameOutBean> relist = new ArrayList<>();
            for (Game game : gameList) {
                GameOutBean out = new GameOutBean();
                out.setAndroidState(game.getAndroidState() == null ? 0 : game.getAndroidState());
//			out.setCheckState(3);
                GameReleaseLog log = logService.selectOne(Condition.create().setSqlSelect("id,approve_state as approveState").eq("game_id", game.getId()).orderBy("created_at", false));
                if (log != null) {
                    out.setCheckState(log.getApproveState());
                    out.setiOState(game.getIosState() == null ? 0 : game.getIosState());
                    out.setCoverImage(game.getCoverImage());
                    out.setEditedAt(DateTools.fmtDate(game.getEditedAt()));
                    out.setGameId(game.getId());
                    out.setIcon(game.getIcon());
                    out.setName(game.getName());
                    relist.add(out);
                }
            }
            re.setData(relist);
            PageTools.pageToResultDto(re, page);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("根据id集合查询游戏合集项列表失败", e);
        }
        return re;
    }


    //分数区间
    public List<RatingBean> rateFormat(HashMap<String, Double> perMap) {
        List<RatingBean> blist = new ArrayList<>();
        RatingBean bean1 = new RatingBean();
        bean1.setKey("1-2分");
        bean1.setValue(perMap.get("1"));


        RatingBean bean2 = new RatingBean();
        bean2.setKey("3-4分");
        bean2.setValue(perMap.get("2"));


        RatingBean bean3 = new RatingBean();
        bean3.setKey("5-6分");
        bean3.setValue(perMap.get("3"));


        RatingBean bean4 = new RatingBean();
        bean4.setKey("7-8分");
        bean4.setValue(perMap.get("4"));


        RatingBean bean5 = new RatingBean();
        bean5.setKey("9-10分");
        bean5.setValue(perMap.get("5"));
        blist.add(bean5);
        blist.add(bean4);
        blist.add(bean3);
        blist.add(bean2);
        blist.add(bean1);

        return blist;

    }

    /**
     * 我的游戏列表
     *
     * @param memberId
     * @param inputPage
     * @param os
     * @return
     */
    @Override
    public FungoPageResultDto<MyGameBean> getMyGameList(String memberId, MyGameInputPageDto inputPage, String os) {
        FungoPageResultDto<MyGameBean> re = null;

        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_GAMELIST + memberId;
        String keySuffix = JSON.toJSONString(inputPage + os);

        re = (FungoPageResultDto<MyGameBean>) fungoCacheMember.getIndexCache(keyPrefix, keySuffix);
        if (null != re) {
            return re;
        }

        re = new FungoPageResultDto<MyGameBean>();
        List<MyGameBean> list = new ArrayList<>();

        if (2 == inputPage.getType()) {
            Page<GameSurveyRel> page = gameSurveyRelService.selectPage(new Page<GameSurveyRel>(inputPage.getPage(),
                    inputPage.getLimit()), new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("state", 0));
            List<GameSurveyRel> plist = page.getRecords();
            for (GameSurveyRel gameSurveyRel : plist) {
                Game game = gameService.selectById(gameSurveyRel.getGameId());
                if(game.getState() != 0){
                    continue;
                }
                MyGameBean bean = new MyGameBean();
                bean.setAndroidState(game.getAndroidState());
                bean.setGameContent(game.getDetail());
                bean.setGameIcon(game.getIcon());
                bean.setGameId(gameSurveyRel.getGameId());
                bean.setGameName(game.getName());
                bean.setIosState(game.getIosState());
                bean.setMsgCount(0);
                bean.setPhoneModel(gameSurveyRel.getPhoneModel());
                if (os.equalsIgnoreCase(bean.getPhoneModel())) {
                    list.add(bean);
                }
            }
            re.setData(list);
            PageTools.pageToResultDto(re, page);
        }

        //redis cache
        fungoCacheMember.excIndexCache(true, keyPrefix, keySuffix, re);
        return re;
    }

    /**
     * 搜索游戏
     *
     * @param page
     * @param limit
     * @param keyword
     * @param tag
     * @param sort
     * @param os
     * @param memberId
     * @return
     */
    @Override
    public FungoPageResultDto<GameSearchOut>    searchGames(int page, int limit, String keyword, String tag, String sort, String os, String memberId)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (keyword == null || "".equals(keyword.replace(" ", "")) || keyword.contains("%")) {
            return FungoPageResultDto.error("13", "请输入正确的关键字格式");
        }
        FungoPageResultDto<GameSearchOut> re = new FungoPageResultDto<>();
        try {
            Page<Game> gamePage = new Page<>(page, limit);
            List<Game> gameList = null;
            // 判断是否选择es
            if(nacosFungoCircleConfig.isGameSearch()){
//                @SuppressWarnings("rawtypes")
//                Wrapper<Game> wrapper = Condition.create().setSqlSelect(
//                        "id,icon,name,recommend_num as recommendNum,cover_image as coverImage,unrecommend_num as unrecommendNum,game_size as gameSize,intro,community_id as communityId,created_at as createdAt,updated_at as updatedAt,developer,tags,android_state as androidState,ios_state as iosState,android_package_name as androidPackageName,itunes_id as itunesId,apk")
//                        .eq("state", 0);
//                wrapper.and().like("name", keyword);//.like("name", ).or().like("google_deputy_name", keyword).or().like( "intro",keyword );
                gameList = gameDao.getGmaePage( gamePage,keyword );
//                if (tag != null && !"".equals(tag.replace(" ", ""))) {
//                    wrapper.like("tags", tag);
//                }

//                if (sort != null && !"".equals(sort.replace(" ", ""))) {
//                    gamePage = gameService.selectPage(new Page<>(page, limit), wrapper.orderBy(sort));
//                } else {
//                    gameList = gameService.selectList(wrapper);
//                }
            }else {
                if (sort != null && !"".equals(sort.replace(" ", ""))) {
                    gamePage = esdaoServiceImpl.getGameByES( page,  limit, keyword,  tag,  sort );
                } else {
                    gamePage = esdaoServiceImpl.getGameByES( page,  limit, keyword,  tag,  sort );
                }
            }

            if (gameList == null) {
//                gameList = gamePage.getRecords();
            } else { // 如果sort不存在,默认排序
                if (gameList.size() == 0) {
                    return new FungoPageResultDto<GameSearchOut>();
                }

                if (gameList.size() > 1) {
                    Collections.sort(gameList, new Comparator<Game>() {
                        @Override
                        public int compare(Game g1, Game g2) {
                            try {
                                return (int) (calculate(g2) - calculate(g1));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                throw new RuntimeException("操作失败");
                            }
                        }

                        public Double calculate(Game g) throws ParseException {
                            SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
                            Date t = format.parse("2017-01-01");
                            Double A = 1.8;
                            Integer R = g.getRecommendNum();
                            Date at = g.getEditedAt();
                            Date T = at != null ? at : g.getCreatedAt();

                            return R + (T.getTime() - t.getTime()) * A;
                        }
                    });
                }
                if(gamePage.getTotal() == 0){
                    gamePage = pageFormat(gameList, page, limit);
                }else {
                    gamePage.setRecords(gameList);
                }
            }

            boolean m = false;
            if (!CommonUtil.isNull(memberId)) {
                m = true;
            }

            List<GameSearchOut> dataList = new ArrayList<>();
            for (Game game : gamePage.getRecords()) {
                //
                GameSearchOut out = new GameSearchOut();
                HashMap<String, BigDecimal> rateData = gameDao.getRateData(game.getId());
                if (rateData != null) {
                    if (rateData.get("avgRating") != null) {
                        out.setRating(Double.parseDouble(rateData.get("avgRating").toString()));
                    } else {
                        out.setRating(0.0);
                    }
                } else {
                    out.setRating(0.0);
                }
                out.setObjectId(game.getId());
                out.setName(game.getName());
                List<String> gameTags = gameTagDao.selectGameTag( game.getId());
                String gameTagStrs = String.join(",", gameTags);
                out.setTag(gameTagStrs);
                out.setIcon(game.getIcon());
                out.setCover_image(game.getCoverImage());
                out.setCoverImage(game.getCoverImage());
                out.setIntro(game.getIntro());
                out.setDeveloper(game.getDeveloper());
                out.setLink_community(game.getCommunityId());
                Integer reNum = game.getRecommendNum();
                Integer unredNum = game.getUnrecommendNum();

//			out.setRecommend_num(reNum);
//			out.setUnrecommend_num(unredNum);
//			out.setEvaluation_num(reNum + unredNum);
                int evaCount = gameEvaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", game.getId()).and("state != -1"));
                out.setEvaluation_num(evaCount);
                out.setGame_size(game.getGameSize());

                out.setAndroidPackageName(game.getAndroidPackageName() == null ? "" : game.getAndroidPackageName());
                out.setAndroidState(game.getAndroidState());
                out.setIosState(game.getIosState());
                out.setItunesId(game.getItunesId());
                out.setApkUrl(game.getApk() == null ? "" : game.getApk());

                if (unredNum != 0) {
                    DecimalFormat df = new DecimalFormat("#.00");
                    out.setScore((reNum != null ? (int) Double.parseDouble(df.format((double) reNum / (reNum + unredNum) * 100)) : 0));
                }
                out.setCreatedAt(DateTools.fmtDate(game.getCreatedAt()));
                out.setUpdatedAt(DateTools.fmtDate(game.getUpdatedAt()));
                out.setGameIdtSn( game.getGameIdtSn());
                out.setCategory(gameTagStrs);
                /**
                 * 功能描述: 添加游戏关联圈子
                 * @auther: dl.zhang
                 * @date: 2019/6/24 13:50
                 */
                try {
                    CircleGamePostVo circleGamePostVo = new CircleGamePostVo(CircleGamePostVo.CircleGamePostTypeEnum.GAMESID.getKey(),game.getId(),"");
                    ResultDto<String> reString = communityFeignClient.getCircleByGame(circleGamePostVo);
                    if (CommonEnum.SUCCESS.code().equals(String.valueOf(reString.getStatus())) && !reString.getData().equals("")) {
                        out.setLink_circle(reString.getData());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("根据游戏id询圈子id异常,游戏id：" + game.getId(), e);
                }
                // 琪琪说  有厂商展示厂商，没有厂商展示发行商
                out.setCompany( org.apache.commons.lang3.StringUtils.isNoneBlank(game.getCompany()) ? game.getCompany() : "" );
                out.setPublisher( org.apache.commons.lang3.StringUtils.isNoneBlank(game.getPublisher()) ? game.getPublisher() : "" );
                out.setAddress( game.getOrigin());
                out.setCanFast(  "中国".equals( game.getOrigin())? false: game.getCanFast() != null ? game.getCanFast() : false );
                String androidStatusDesc = game.getAndroidStatusDesc();
                /**
                 * 功能描述:
                 *  {
                 *      dsc:””,
                 *      starDate:””,
                 *      endDate:””
                 *  }
                 */
                Long starDate = null;
                Long endDate = null;
                JSONObject jsonObject = JSON.parseObject( androidStatusDesc);
                if(jsonObject != null){
                    String dsc = jsonObject.getString("dsc");
                    out.setGameStatus( (String) jsonObject.get( "dsc" ) );
                    try {
                        if(!CommonUtil.isNull(dsc)){
                            starDate = (Long) jsonObject.get( "starDate" );
                            endDate = (Long) jsonObject.get( "endDate" );
                            if( starDate == null || endDate == null ){
                                out.setGameStatus( (String) jsonObject.get( "dsc" ) );
                            }else {
                                out.setGameStatus(  DateTools.betweenDate(new Date( starDate ),new Date( endDate ),new Date( )  ) ? (String) jsonObject.get( "dsc" ) : "" );
                            }
                        }
                    }catch (Exception e){
                        logger.error( "androidStatusDesc转换失败",e );
                    }

                }

                out.setTags(gameTagStrs);

                if(!CommonUtil.isNull(memberId)){
                    List<GameSurveyRel> gameSurveyRels = gameSurveyRelService.selectList( new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("state", 0).eq( "game_id",game.getId()));
                    if(gameSurveyRels != null && gameSurveyRels.size() >0){
                        out.setMake(true);
                    }
                }
                out.setVersion( CommonUtil.isNull(game.getVersionChild()) ? game.getVersionMain() : game.getVersionMain()+"."+game.getVersionChild() );
                try {
                    if (game.getImages() != null) {
                        ObjectMapper mapper = new ObjectMapper();
                        out.setImages((ArrayList<String>) mapper.readValue(game.getImages(), ArrayList.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dataList.add(out);
            }
            re.setData(dataList);
            PageTools.pageToResultDto(re, gamePage.getTotal(),limit,page);
        }catch (Exception e){
            logger.error( "搜索游戏异常,参数keyword"+keyword+"tag="+tag+"sort="+sort,e );
            re= FungoPageResultDto.FungoPageResultDtoFactory.buildError( "搜索游戏异常" );
        }
        return re;
    }

    /**
     * 搜索游戏
     *
     * @param page
     * @param limit
     * @param keyword
     * @param tag
     * @param sort
     * @param os
     * @param memberId
     * @return
     */
    @Override
    public FungoPageResultDto<GameSearchOut> searchGamesCount( String keyword)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (keyword == null || "".equals(keyword.replace(" ", "")) || keyword.contains("%")) {
            return FungoPageResultDto.error("13", "请输入正确的关键字格式");
        }
        FungoPageResultDto<GameSearchOut> re = new FungoPageResultDto<GameSearchOut>();
        try {
            Page<Game> gamePage = new Page( 1,10 );
            List<Game> gameList = new ArrayList<>();
            // 判断是否选择es
            if(nacosFungoCircleConfig.isGameSearch()){
//                @SuppressWarnings("rawtypes")
//                Wrapper wrapper = Condition.create().setSqlSelect(
//                        "id,icon,name,recommend_num as recommendNum,cover_image as coverImage,unrecommend_num as unrecommendNum,game_size as gameSize,intro,community_id as communityId,created_at as createdAt,updated_at as updatedAt,developer,tags,android_state as androidState,ios_state as iosState,android_package_name as androidPackageName,itunes_id as itunesId,apk")
//                        .eq("state", 0).and().or().like("name", keyword).or().like( "google_deputy_name  ", keyword ).or().like( "intro",keyword );
//                    gamePage = gameService.selectPage(new Page<>(1, 10), wrapper);
                gameList = gameDao.getGmaePage(gamePage,keyword);
            }else {
                    gamePage = esdaoServiceImpl.getGameByES( 1,  10, keyword,  "",  "" );
            }
            if (gamePage != null) {
//                gameList = gamePage.getRecords();
                re.setCount( gamePage.getTotal());
            } else {
                re.setCount( gameList.size());
            }
        }catch (Exception e){
            logger.error( "搜索游戏异常,参数keyword"+keyword,e );
            re= FungoPageResultDto.FungoPageResultDtoFactory.buildError( "搜索游戏异常" );
        }
        return re;
    }

    /**
     * 功能描述: 搜索时联想关键字
     * @param:
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.search.GameSearchOut>
     * @auther: dl.zhang
     * @date: 2019/10/15 11:44
     */
    @Override
    public FungoPageResultDto<String> searchGamesKeyword(int page, int limit, String keyword, String tag, String sort, String os, String memberId) {
        if (keyword == null || "".equals(keyword.replace(" ", "")) || keyword.contains("%")) {
            return FungoPageResultDto.error("13", "请输入正确的关键字格式");
        }
        FungoPageResultDto<String> re = new FungoPageResultDto<>();
        try {
            Page<Game> gamePage = new Page( 1,10 );;
            List<String> gameNameList = new ArrayList<>();
            List<Game> gameList = null;
            // 判断是否选择es
            if(nacosFungoCircleConfig.isKeywordGameSearch()){
//                @SuppressWarnings("rawtypes")
//                Wrapper wrapper = Condition.create().setSqlSelect(
//                        "id,icon,name,recommend_num as recommendNum,cover_image as coverImage,unrecommend_num as unrecommendNum,game_size as gameSize,intro,community_id as communityId,created_at as createdAt,updated_at as updatedAt,developer,tags,android_state as androidState,ios_state as iosState,android_package_name as androidPackageName,itunes_id as itunesId,apk")
//                        .eq("state", 0).like("name", keyword).or().like( "google_deputy_name like ", keyword ); //.or().like( "intro",keyword )
//
//                if (sort != null && !"".equals(sort.replace(" ", ""))) {
//                    gamePage = gameService.selectPage(new Page<>(page, limit), wrapper.orderBy(sort));
//                } else {
//                    gamePage = gameService.selectPage(new Page<>(page, limit), wrapper);
////                    gameList = gameService.selectList(wrapper);
//                }
                gameList = gameDao.getGmaePage( gamePage,keyword );
            }else {
                if (sort != null && !"".equals(sort.replace(" ", ""))) {
                    gamePage = esdaoServiceImpl.getGameByES( page,  limit, keyword,  tag,  sort );
                } else {
                    gamePage = esdaoServiceImpl.getGameByES( page,  limit, keyword,  tag,  sort );
                }
            }
            gameList = gameList != null ? gameList : gamePage.getRecords();
            gameList.stream().forEach(s -> { String gameName = s.getName();gameNameList.add( gameName); });
            re.setData(gameNameList);
            PageTools.pageToResultDto(re, gamePage);
        }catch (Exception e){
            logger.error( "搜索游戏异常,参数keyword"+keyword+"tag="+tag+"sort="+sort,e );
            re= FungoPageResultDto.FungoPageResultDtoFactory.buildError( "搜索游戏异常" );
        }
        return re;
    }

    /**
     * 根据游戏ID获取游戏标签列表
     *
     * @param gameId
     * @param userId
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Override
    /*
     * { "name": "称原解王",游戏名称 //标签名称 "tagRelId": "5ad55eb4ac502e0042ae29d9",游戏标签id
     * "attitude": 1,态度 "likeNum": 81,支持数 "disLikeNum": 66,反对数 "supportNum": 15 }
     */
    public ResultDto<List> getGameTagList(String gameId, String userId) {
        if (gameId == null) {
            return ResultDto.error("231", "找不到目标游戏");
        }
        // 返回数据
        List<Map> gameTagMapList = new ArrayList<>();


        // 查询
        List<GameTag> gameTagList = new ArrayList<>();
        // 目标游戏的所有标签
        @SuppressWarnings("unchecked")
        Wrapper<GameTag> wrapper = Condition.create().setSqlSelect("id,like_num as likeNum,dislike_num as dislikeNum,tag_id as tagId,type").eq("game_id",
                gameId);
//		if(gameTagService.selectList(wrapper) == null || (gameTagService.selectList(wrapper).size() == 0)) {
//			return ResultDto.error("251", "找不到目标游戏标签");
//		}
        // String gameName = gameService.selectById(gameId).getName();//游戏名称
        if (userId == null || "".equals(userId)) {// 如果用户未登录 那么他的态度都设为默认0,并且过滤掉小于支持数小于5的tag
            gameTagList = gameTagService.selectList(wrapper.andNew("like_num > {0}", 5).or("type = {0}", 1));
//			gameTagList = gameTagService.selectList(wrapper.where("type = {0}",1));
            if (gameTagList == null) {
//				return ResultDto.error("252", "该游戏还没有标签");
                gameTagList = new ArrayList<>();
            }
            for (GameTag gameTag : gameTagList) {
                Map<String, Object> gameTagMap = new HashMap<String, Object>();
//                迁移微服务 根据id获取BasTag对象
//                2019-05-15
//                lyc
                BasTag tagName = basTagService.selectById(gameTag.getTagId());// 标签名称
               /* BasTagDto basTagDto = new BasTagDto();
                basTagDto.setId(gameTag.getTagId());
                BasTagDto tagName = iEvaluateProxyService.getBasTagBySelectById(basTagDto);*/
//				if(tagName == null) {
//					return ResultDto.error("251", "找不到符合要求的标签");
//				}
                if (tagName != null) {
                    Integer likeNum = gameTag.getLikeNum();
                    Integer dislikeNum = gameTag.getDislikeNum();
                    gameTagMap.put("name", tagName.getName());
                    gameTagMap.put("tagRelId", gameTag.getId());
                    gameTagMap.put("attitude", 0);
                    gameTagMap.put("likeNum", likeNum);
                    gameTagMap.put("dislikeNum", dislikeNum);
                    gameTagMap.put("supportNum", likeNum - dislikeNum);
                    gameTagMapList.add(gameTagMap);
                }
            }
        } else {// 查询用户的观点 >=5 或点赞的
//            迁移微服务 根据id获取member对象
//            2019-05-15
//            lyc
//            if (memberService.selectById(userId) == null) {
            MemberDto memberDto = new MemberDto();
            memberDto.setId(userId);
            MemberDto memberDtoBySelectOne = iEvaluateProxyService.getMemberDtoBySelectOne(memberDto);
            if (memberDtoBySelectOne == null) {
                return ResultDto.error("126", "用户不存在");
            }
            gameTagList = gameTagService.selectList(wrapper);
//			gameTagList = gameTagService.selectList(wrapper.where("type = {0}", 1));
            if (gameTagList == null) {
//				return ResultDto.error("252", "该游戏还没有标签");
                gameTagList = new ArrayList<>();
            }
            List<String> tagIdList = new ArrayList<String>();
            for (int i = 0; i < gameTagList.size(); i++) {
                tagIdList.add(gameTagList.get(i).getId());
            } // 标签id组

            // 目标游戏的所有标签中被用户点赞的
            List<GameTagAttitude> gameTagAttitudeList = gameTagAttitudeService.selectList(
                    new EntityWrapper<GameTagAttitude>().eq("member_id", userId).eq("attitude", 1).in("game_tag_id", tagIdList));
            List<String> userTagIdList = new ArrayList<String>();
            if (gameTagAttitudeList != null) {
                for (int i = 0; i < gameTagAttitudeList.size(); i++) {
                    userTagIdList.add(gameTagAttitudeList.get(i).getGameTagId());
                } //
            }

            for (GameTag gameTag : gameTagList) {
                String gameTagId = gameTag.getId();
                if (gameTag.getLikeNum() >= 5 || userTagIdList.contains(gameTagId) || gameTag.getType() == 1) {
                    Map<String, Object> gameTagMap = new HashMap<String, Object>();
//                    迁移微服务 根据id获取BasTag对象
//                    2019-05-15
//                    lyc
                    BasTag tagName = basTagService.selectById(gameTag.getTagId());// 标签名称
                   /* BasTagDto basTagDto = new BasTagDto();
                    basTagDto.setId(gameTag.getTagId());
                    BasTagDto tagName = iEvaluateProxyService.getBasTagBySelectById(basTagDto);*/// 标签名称
                    if (tagName == null) {
                        return ResultDto.error("251", "找不到符合要求的标签");
                    }else {
                        Integer likeNum = gameTag.getLikeNum();
                        Integer dislikeNum = gameTag.getDislikeNum();
                        gameTagMap.put("name", tagName.getName());
                        gameTagMap.put("tagRelId", gameTag.getId());
                        GameTagAttitude gameTagAttitude = gameTagAttitudeService
                                .selectOne(new EntityWrapper<GameTagAttitude>().eq("game_tag_id", gameTagId).eq("member_id", userId));//gameTagId
                        if (gameTagAttitude != null) {
                            gameTagMap.put("attitude", gameTagAttitude.getAttitude());
                        } else {
                            gameTagMap.put("attitude", 0);
                        }
                        gameTagMap.put("likeNum", likeNum);
                        gameTagMap.put("dislikeNum", dislikeNum);
                        gameTagMap.put("supportNum", likeNum - dislikeNum);
                        gameTagMapList.add(gameTagMap);
                    }
                }
            }
        }

        return ResultDto.success(gameTagMapList);
    }

    /**
     * 新增游戏标签
     *
     * @param idList
     * @param userId
     * @param gameId
     * @return
     */
    @Override
    @Transactional
    public ResultDto<String> addGameTag(String[] idList, String userId, String gameId) {
//        迁移微服务 根据id获取member对象
//        2019-05-15
//        lyc
//        Member member = memberService.selectById(userId);
        MemberDto memberDto = new MemberDto();
        memberDto.setId(userId);
        MemberDto member = iEvaluateProxyService.getMemberDtoBySelectOne(memberDto);
        if (member == null) {
            return ResultDto.error("-1", "未查到该用户信息~");
        }
        if (member.getLevel() < 6) {
            return ResultDto.error("-1", "6级才能打标签哦~");
        }
        // 传入的游戏标签
        if ( idList == null || idList.length == 0 ) {
            return ResultDto.error("13", "请选择1到3个标签");
        }
        if (idList.length > 3) {
            return ResultDto.error("13", "最多添加3个标签");
        }
        // 获取游戏本身的游戏标签评价
        @SuppressWarnings("unchecked")
        List<GameTag> gameTagList = gameTagService
                .selectList(Condition.create().setSqlSelect("id").eq("game_id", gameId));
        List<String> gameTagIdList = new ArrayList<String>();
        List<String> preTagIdList = new ArrayList<String>();
        if (gameTagList != null && gameTagList.size() != 0) {
            for (int i = 0; i < gameTagList.size(); i++) {
                gameTagIdList.add(gameTagList.get(i).getId());
            }
            // 获取用户对这些标签的态度
            // 态度为支持
            List<GameTagAttitude> likeAttitudeList = gameTagAttitudeService.selectList(new EntityWrapper<GameTagAttitude>()
                    .eq("member_id", userId).eq("attitude", 1).in("game_tag_id", gameTagIdList));

            if (likeAttitudeList != null && likeAttitudeList.size() != 0) {
                // 获取用户对此游戏支持的标签
                for (int i = 0; i < likeAttitudeList.size(); i++) {
                    preTagIdList.add(likeAttitudeList.get(i).getTagId());
                }
            }
        }
        // 对比新旧，进行删除 // 一个游戏,一个用户最多有三个标签，对比前后三个标签，进行增删
        List<String> newTagIdList = Arrays.asList(idList);
        newTagIdList = new ArrayList<String>(newTagIdList);
        List<String> tempIdList = new ArrayList<String>();
        preTagIdList.forEach(s -> tempIdList.add(s));

        // 删除id
        preTagIdList.removeAll(newTagIdList);
        // 添加id
        newTagIdList.removeAll(tempIdList);
        if (newTagIdList.size() == 0) {
            return ResultDto.success();
        }
        boolean delSuccess = true;
        boolean upSuccess = true;

//        Game game = gameService.selectById(gameId);
//		List<String> tagsFormGame = new ArrayList<>();
//		if(game.getTags() != null);{
//			tagsFormGame = new ArrayList<String>(Arrays.asList(game.getTags().split(",")));
//		}
        for (String delTagId : preTagIdList) {// 删除
            if (CommonUtil.isNull(delTagId)) {
                throw new BusinessException("-1", "标签id不能为空");
            }
            GameTag gameTag = gameTagService
                    .selectOne(new EntityWrapper<GameTag>().eq("tag_id", delTagId).eq("game_id", gameId));
            if (gameTag == null) {
//				return ResultDto.error("251", "对应id的游戏标签不存在");
                continue;
            }
//			BasTag tag = basTagService.selectById(delTagId);
//			String tagName = tag.getName();
//			if(tagsFormGame.contains(tagName) && gameTag.getLikeNum() == 1 && gameTag.getType() == 0) {//游戏表单，删除
//				tagsFormGame.remove(tagName);
//			};
            GameTagAttitude tagAttitude = gameTagAttitudeService.selectOne(new EntityWrapper<GameTagAttitude>()
                    .eq("tag_id", delTagId).eq("member_id", userId).eq("game_tag_id", gameTag.getId()));
            if (tagAttitude == null) {
//				return ResultDto.error("271", "对应id的态度不存在");
                continue;
            }
            tagAttitude.setAttitude(0);
            gameTagAttitudeService.updateById(tagAttitude);
            delSuccess = updateGameTagCount(gameTag, 1, 0);
        }
        for (String updateId : newTagIdList) {// 更新
            if (CommonUtil.isNull(updateId)) {
                throw new BusinessException("-1", "标签id不能为空");
            }
            GameTag gameTag = gameTagService
                    .selectOne(new EntityWrapper<GameTag>().eq("tag_id", updateId).eq("game_id", gameId));
            if (gameTag == null) {
                GameTag newGameTag = new GameTag();
                newGameTag.setTagId(updateId);
                Date date = new Date();
                newGameTag.setGameId(gameId);
                newGameTag.setCreatedAt(date);
                newGameTag.setUpdatedAt(date);
                gameTagService.insert(newGameTag);
                gameTag = newGameTag;
                //return ResultDto.error("211", "游戏标签不存在");
            }

//			BasTag tag = basTagService.selectById(updateId);
//			String tagName = tag.getName();
//			if(!tagsFormGame.contains(tagName)) {
//				tagsFormGame.add(tagName);
//			};
            GameTagAttitude tagAttitude = gameTagAttitudeService.selectOne(new EntityWrapper<GameTagAttitude>()
                    .eq("game_tag_id", gameTag.getId()).eq("member_id", userId));
            if (tagAttitude == null) {//如果态度不存在，新建
                addGameTagAttitude(userId, gameTag.getId(), 1);
                continue;
            }
            Integer attitude = tagAttitude.getAttitude();
            tagAttitude.setAttitude(1);
            gameTagAttitudeService.updateById(tagAttitude);
            if (attitude == 0) {
                upSuccess = updateGameTagCount(gameTag, 0, 1);
            } else if (attitude == 2) {
                upSuccess = updateGameTagCount(gameTag, 2, 1);
            }
        }
//		game.setTags(tagsFormGame.toString().replace("[", "").replace("]", "").replace("\"", ""));
//		boolean updateGame = game.updateById();
//		boolean updateGame = gameDao.updateTags(gameId, tagsFormGame.toString().replace("[", "").replace("]", "").replace("\"", "").replace(" ", ""));
        if (upSuccess && delSuccess) {
            return ResultDto.success();
        } else {
            throw new BusinessException("-1", "标签id不能为空");
        }
    }

//    @Override
//    public FungoPageResultDto<GameOutPage> recentEvaluatedGamesByMember(String userId, InputPageDto input) {
//
//        String keySuffix = JSON.toJSONString(input);
//        FungoPageResultDto<GameOutPage> re = ( FungoPageResultDto<GameOutPage>)fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + userId,
//                keySuffix);
//        if (null != re  && null != re.getData() && re.getData().size() > 0){
//            return re;
//        }
//
//        Page page = new Page<>(input.getPage(), input.getLimit());
//        List<HashMap<String, Object>> list = gameDao.getRecentCommentedGames(page, userId);
//        List<GameOutPage> olist = new ArrayList<>();
//
//        for (HashMap<String, Object> map : list) {
//            GameOutPage out = new GameOutPage();
//            out.setIcon((String) map.get("icon"));
//            out.setName((String) map.get("name"));
//            out.setObjectId((String) map.get("game_id"));
//            HashMap<String, BigDecimal> rateData = gameDao.getRateData((String) map.get("game_id"));
//            if (rateData != null) {
//                if (rateData.get("avgRating") != null) {
//                    out.setRating(Double.parseDouble(rateData.get("avgRating").toString()));
//                } else {
//                    out.setRating(0.0);
//                }
//            } else {
//                out.setRating(0.0);
//            }
//            out.setComment_num((int) map.get("comment_num"));
//            out.setLink_community((String) map.get("community_id"));
//            out.setCategory((String) map.get("tags"));
//            olist.add(out);
//        }
//        re = new FungoPageResultDto<GameOutPage>();
//        PageTools.pageToResultDto(re, page);
//        re.setData(olist);
//
//        //redis cache
//        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + userId, keySuffix, re);
//        return re;
//    }

//    @Override
//    //获取游戏平均分
//    public double getGameRating(String gameId) {
//        HashMap<String, BigDecimal> rateData = gameDao.getRateData(gameId);
//        if (rateData != null) {
//            if (rateData.get("avgRating") != null) {
//                return Double.parseDouble(rateData.get("avgRating").toString());
//            } else {
//                return 0.0;
//            }
//        } else {
//            return 0.0;
//        }
//    }

//    @Override
//    public FungoPageResultDto<GameItem> getGameItems(String memberId, GameItemInput input, String os) {
//        //通过id找出合集项
//        //找出游戏 一个一个配
//        String keySuffix = memberId + JSON.toJSONString(input) + os;
//        FungoPageResultDto<GameItem> re = (FungoPageResultDto<GameItem>) fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_ITEMS,
//                keySuffix);
//
//        if (null != re  && null != re.getData() && re.getData().size() > 0) {
//            return re;
//        }
//
//        boolean m = false;
//        if (!CommonUtil.isNull(memberId)) {//是否为登录用户
//            m = true;
//        }
//
//        re = new FungoPageResultDto<GameItem>();
//        List<GameItem> ilist = new ArrayList<>();
//        Page<GameCollectionItem> itemPage =
//                gameCollectionItemService.selectPage(new Page<>(input.getPage(), input.getLimit()), new EntityWrapper<GameCollectionItem>().eq("group_id", input.getGroup_id()));
//
//        List<GameCollectionItem> glist = itemPage.getRecords();
//        List<String> gameIdList = new ArrayList<>();
//        if (glist.size() > 0) {
//            gameIdList = glist.stream().map(GameCollectionItem::getGameId).collect(Collectors.toList());
//        }
//        if (gameIdList.size() > 0) {
//            List<Game> gamel = gameService.selectList(new EntityWrapper<Game>().in("id", gameIdList));
//            for (Game game : gamel) {
//                GameItem it = new GameItem();
//                it.setAndroidPackageName(game.getAndroidPackageName());
//                it.setAndroidState(game.getAndroidState());
//                it.setApkUrl(game.getApk());
//                if (m) {
//                    GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", game.getId()).eq("phone_model", os).eq("state", 0));
//                    if (srel != null) {
//                        it.setBinding(!StringUtils.isNullOrEmpty(srel.getAppleId()));
//                        it.setClause(1 == srel.getAgree() ? true : false);
//                        it.setMake(true);
//                    }
//                }
//                it.setIcon(game.getIcon());
//                it.setObjectId(game.getId());
//                it.setName(game.getName());
//                it.setIosState(game.getIosState());
//                it.setItunesId(game.getItunesId());
//
//                HashMap<String, BigDecimal> rateData = gameDao.getRateData(game.getId());
//                if (rateData != null) {
//                    if (rateData.get("avgRating") != null) {
//                        it.setRating(Double.parseDouble(rateData.get("avgRating").toString()));
//                    } else {
//                        it.setRating(0.0);
//                    }
//                } else {
//                    it.setRating(0.0);
//                }
//                it.setCategory(game.getTags());
//
//                ilist.add(it);
//            }
//            PageTools.pageToResultDto(re, itemPage);
//            re.setData(ilist);
//        }
//        //redis cache
//        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_ITEMS, keySuffix, re);
//        return re;
//    }

    public boolean updateGameTagCount(GameTag gameTag, int preNum, int curNum) {
        Integer likeNum = gameTag.getLikeNum();
        Integer dislikeNum = gameTag.getDislikeNum();
        if (preNum == 0 && curNum == 1) { // null => 支持
            gameTag.setLikeNum(likeNum + 1);
        } else if (preNum == 0 && curNum == 2) { // null => 反对
            gameTag.setDislikeNum(dislikeNum + 1);
        } else if (preNum == 1 && curNum == 0) { // 支持 => null
            gameTag.setLikeNum(likeNum - 1);
        } else if (preNum == 1 && curNum == 2) { // 支持 => 反对
            gameTag.setLikeNum(likeNum - 1);
            gameTag.setDislikeNum(dislikeNum + 1);
        } else if (preNum == 2 && curNum == 0) { // 反对 => null
            gameTag.setDislikeNum(dislikeNum - 1);
        } else if (preNum == 2 && curNum == 1) { // 反对 => 支持
            gameTag.setLikeNum(likeNum + 1);
            gameTag.setDislikeNum(dislikeNum - 1);
        } else {
            return false;
        }
        gameTag.setUpdatedAt(new Date());
        return gameTagService.updateById(gameTag);
    }

    @Override
    @Transactional
    public ResultDto<String> addGameTagAttitude(String userId, String gameTagId, Integer attitude) {
        if (attitude != 0 && attitude != 1 && attitude != 2) {
            return ResultDto.error("13", "错误的态度");
        }
//        迁移微服务 根据id获取member对象
//        2019-05-15
//        lyc
//        if ("".equals(userId) || memberService.selectById(userId) == null) {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(userId);
        if ("".equals(userId) || iEvaluateProxyService.getMemberDtoBySelectOne(memberDto) == null) {
            return ResultDto.error("126", "找不到用户");
        }
        //获取目标游戏
        GameTag gameTag = gameTagService.selectById(gameTagId);
        if (gameTag == null) {
            return ResultDto.error("251", "游戏标签不存在");
        }
        String gameId = gameTag.getGameId();
        Game game = gameService.selectById(gameId);
        if (game == null) {
            return ResultDto.error("231", "目标游戏不存在");
        }
        int count = gameTagAttitudeService.selectCount(new EntityWrapper<GameTagAttitude>().eq("game_tag_id", gameTagId)
                .eq("member_id", userId).eq("attitude", 1));
        int decount = gameTagAttitudeService.selectCount(new EntityWrapper<GameTagAttitude>().eq("game_tag_id", gameTagId)
                .eq("member_id", userId).eq("attitude", 2));
        if (count >= 3 && attitude == 1) {
            return ResultDto.error("99", "同一游戏只能支持三个标签");
        }
        if (decount >= 3 && attitude == 2) {
            return ResultDto.error("99", "同一游戏只能反对三个标签");
        }
        GameTagAttitude gameAttitude = gameTagAttitudeService.selectOne(new EntityWrapper<GameTagAttitude>()
                .eq("game_tag_id", gameTagId).eq("member_id", userId));
        boolean success = false;
        if (gameAttitude != null) {//修改
            int preAttitude = gameAttitude.getAttitude();
            if (preAttitude == attitude) {
                return ResultDto.success();
            } else {
                gameAttitude.setAttitude(attitude);
                gameAttitude.setUpdatedAt(new Date());
                gameTagAttitudeService.updateById(gameAttitude);
                success = updateGameTagCount(gameTag, preAttitude, attitude);
            }
        } else {//新增
            GameTagAttitude newTagAttitude = new GameTagAttitude();
            newTagAttitude.setMemberId(userId);
            newTagAttitude.setAttitude(attitude);
            newTagAttitude.setGameTagId(gameTagId);
            newTagAttitude.setTagId(gameTag.getTagId());
            Date date = new Date();
            newTagAttitude.setCreatedAt(date);
            newTagAttitude.setUpdatedAt(date);
            success = gameTagAttitudeService.insert(newTagAttitude);
            if (success) {
                success = updateGameTagCount(gameTag, 0, attitude);
            }
        }
        if (success) {
            return ResultDto.success();
        } else {
            throw new BusinessException("-1", "操作失败");
        }

    }

    /**
     * 获取热门游戏标签
     *
     * @return
     */
    @Override
    public ResultDto<List> getHotTagList() {
        @SuppressWarnings("unchecked")
        List<GameTag> hotGameTagList = gameTagService
                .selectList(Condition.create().setSqlSelect("tag_id as tagId").orderBy("like_num", false).last("LIMIT 10"));
        List<String> hotIdList = new ArrayList<String>();
        for (int i = 0; i < hotGameTagList.size(); i++) {
            hotIdList.add(hotGameTagList.get(i).getTagId());
        }
//        迁移微服务
//        2019-05-15
//        lyc
        List<BasTag> hotTagList = basTagService.selectList(new EntityWrapper<BasTag>().in("id", hotIdList));
        // List<BasTagDto> hotTagList = iEvaluateProxyService.getBasTagBySelectListInId(hotIdList);
        List<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
        for (BasTag hotTag : hotTagList) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", hotTag.getName());
            map.put("objectId", hotTag.getId());
            resultList.add(map);
        }
        return ResultDto.success(resultList);
    }

    /**
     * 获取游戏预选标签
     *
     * @param memberUserPrefile
     * @param tagInput
     * @return
     */
    @Override
    public ResultDto<TagSelectOut> getSelectTagList(MemberUserProfile memberUserPrefile, TagInput tagInput) {
        String game_id = tagInput.getGameId();
        if (game_id == null || "".endsWith(game_id.replace(" ", ""))) {
            return ResultDto.error("231", "找不到游戏id");
        }
        Game game = gameService.selectById(game_id);
        if (game == null) {
            return ResultDto.error("231", "找不到目标游戏");
        }
        TagSelectOut out = new TagSelectOut();
        //获取用户对此游戏的支持标签
        @SuppressWarnings("unchecked")
        List<GameTag> gameTagList = gameTagService.selectList(Condition.create().setSqlSelect("id,tag_id as tagId").eq("game_id", game_id));
        String userId = memberUserPrefile.getLoginId();
        // 目标游戏的所有标签中被用户点赞的
        List<GameTagAttitude> gameTagAttitudeList = gameTagAttitudeService.selectList(
                new EntityWrapper<GameTagAttitude>().eq("member_id", userId).eq("attitude", 1)
                        .in("game_tag_id", gameTagList.stream().map(GameTag::getId).collect(Collectors.toList())));
        List<String> TagIdList = new ArrayList<String>();
        if (gameTagAttitudeList != null) {
            for (int i = 0; i < gameTagAttitudeList.size(); i++) {
                TagIdList.add(gameTagAttitudeList.get(i).getTagId());
            }
        }
        //用户支持的标签
//        迁移微服务 根据id集合获取bastag集合
//        2019-05-15
//        lyc
//        List<BasTag> tagList = basTagService.selectList(new EntityWrapper<BasTag>().in("id", TagIdList));
        //List<BasTagDto> tagList = iEvaluateProxyService.getBasTagBySelectListInId(TagIdList);
        List<Map<String, Object>> tagByGameId = getTagByGameId(game_id, TagIdList).getData();
        List<Map<String, String>> selectedList = new ArrayList<>();
//		for(BasTag tag : tagList) {
//			Map<String,String> map = new HashMap<>();
//			map.put("name", tag.getName());
//			map.put("tagId", tag.getId());
//			selectedList.add(map);
//		}
        for (Map<String, Object> map : tagByGameId) {
            List<Map<String, Object>> l = (List) map.get("tagList");
            for (Map<String, Object> m : l) {
                if ((boolean) m.get("isSupported") == true) {
                    Map<String, String> s = new HashMap<>();
                    s.put("name", (String) m.get("name"));
                    s.put("tagId", (String) m.get("tagId"));
                    selectedList.add(s);
                }
            }
        }
        out.setTagList(tagByGameId);
        out.setSelectedList(selectedList);
        return ResultDto.success(out);
    }

    /**
     * 根据游戏id集合获取FungoPageResultDto<GameOutBean>
     *
     * @param input
     * @return
     */
    @Override
    public FungoPageResultDto<GameOutBean> getGameList1(GameItemInput input) {
        String[] split = input.getGroup_id().split(",");
        List<String> mlist = Arrays.asList(split);
        List<Game> gameList = null;
        FungoPageResultDto<GameOutBean> re = new FungoPageResultDto<GameOutBean>();
        if (mlist != null && mlist.size() > 0) {
            Wrapper<Game> wrapper = new EntityWrapper<Game>().in("id", mlist);
            if (!CommonUtil.isNull(input.getName())) {
                wrapper.like("name", input.getName());
            }
            Page<Game> page = gameService.selectPage(new Page<>(input.getPage(), input.getLimit()), wrapper);
            gameList = page.getRecords();

            List<GameOutBean> relist = new ArrayList<>();
            for (Game game : gameList) {
                GameOutBean out = new GameOutBean();
                out.setAndroidState(game.getAndroidState() == null ? 0 : game.getAndroidState());
//			out.setCheckState(3);
                GameReleaseLog log = logService.selectOne(Condition.create().setSqlSelect("id,approve_state as approveState").eq("game_id", game.getId()).orderBy("created_at", false));
                if (log != null) {
                    out.setCheckState(log.getApproveState());
                }
                out.setiOState(game.getIosState() == null ? 0 : game.getIosState());
                out.setCoverImage(game.getCoverImage());
                out.setEditedAt(DateTools.fmtDate(game.getEditedAt()));
                out.setGameId(game.getId());
                out.setIcon(game.getIcon());
                out.setName(game.getName());
                relist.add(out);
            }
            re.setData(relist);
            PageTools.pageToResultDto(re, page);
        }
        return re;
    }


    @Override
    public ResultDto<List<GameOutPage>> viewGames(String memberId) {

        List<String> ids = iEvaluateProxyService.listGameHisIds(memberId);
        List<GameOutPage> gameOutPages = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return ResultDto.success(gameOutPages);
        }
        StringBuilder orderby = new StringBuilder("FIELD(id");
        for (String id : ids) {
            orderby.append(",'" + id + "'");
        }
        orderby.append(")");


        EntityWrapper<Game> wrapper = new EntityWrapper<>();
        wrapper.in("id", ids);

        wrapper.orderBy(orderby.toString());

        List<Game> games = gameService.selectList(wrapper);
        for (Game game : games) {
            {
                GameOutPage out = new GameOutPage();
                out.setObjectId(game.getId());
                out.setName(game.getName());
                out.setIcon(game.getIcon());
                //2.4.3
                out.setAndroidState(game.getAndroidState());
                out.setIosState(game.getIosState());
                out.setRating(getGameRating(game.getId()));
                if (game.getAndroidPackageName() == null) {
                    game.setAndroidPackageName("");
                }
                if (game.getApk() == null) {
                    game.setApk("");
                }
                out.setApkUrl(game.getApk());
                out.setItunesId(game.getItunesId());
                out.setAndroidPackageName(game.getAndroidPackageName());
                out.setGame_size((long) game.getGameSize());
                //推荐数据 已弃用
                Integer recommend_total_count = game.getRecommendNum() + game.getUnrecommendNum();
                DecimalFormat df = new DecimalFormat("#.00");
                out.setRecommend_total_rate(recommend_total_count == 0 ? 0 : Double.parseDouble(df.format((double) game.getRecommendNum() / recommend_total_count * 100)));
                out.setRecommend_total_count(gameEvaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", game.getId()).and("state != -1")));
                //

                out.setCreatedAt(DateTools.fmtDate(game.getCreatedAt()));
                out.setUpdatedAt(DateTools.fmtDate(game.getUpdatedAt()));

            /*    GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", game.getId()).eq("phone_model", os).eq("state", 0));
                    if (srel != null) {
                        out.setBinding(!StringUtils.isNullOrEmpty(srel.getAppleId()));
                        out.setClause(1 == srel.getAgree() ? true : false);
                        out.setMake(true);
                    }*/

                gameOutPages.add(out);
            }
        }
        return ResultDto.success(gameOutPages);
    }

    @Override
    public ResultDto<List<GameOut>> listGameByids(String gameIds) {
        Wrapper<Game> wrapper = new EntityWrapper<Game>();
        wrapper.in("id", gameIds);
        List<Game> games = gameService.selectList(wrapper);
        List<GameOut> gameOuts = new ArrayList<>();
        for (Game game : games) {
            GameOut out = new GameOut();
            out.setName(game.getName());
            out.setIcon(game.getIcon());
            out.setObjectId(game.getId());
            gameOuts.add(out);
        }
        return ResultDto.success(gameOuts);
    }

    @Override
    public FungoPageResultDto<GameOut> pageGameByids(String gameIds) {
        try{

        }catch (Exception e){
            logger.error( "分页获取游戏列表异常",e );
        }
        return null;
    }

    @Override
    public FungoPageResultDto<GameKuDto> listGameByTags(String memberId, TagGameDto tagGameDto) {
        int page = tagGameDto.getPage();
        int limit = tagGameDto.getLimit();
        // 计算 sql limit 偏移量
        int offset = (page-1) * limit;
        tagGameDto.setOffset(offset);
        Integer tootal =  gameService.countGameByTags(tagGameDto);

        FungoPageResultDto<GameKuDto> fungoPageResultDto = new FungoPageResultDto<>();
        ArrayList<GameKuDto> gameKuDtos = new ArrayList<>();

        // 优化没有数据情况
        if(tootal == 0 || offset > tootal){
            // 结果封装为客户端通用格式
            fungoPageResultDto.setData(gameKuDtos);
            PageTools.pageToResultDto(fungoPageResultDto,tootal,limit,page);
        }else {
            List<Game> games = gameService.listGameByTags(tagGameDto);
           /* // 远程服务调用 获取游戏对应的圈子id
            List<String> gameIds = getGameIds(games);
            Map<String,String> gameCircleMap = null;*/
            for (Game game : games) {
                GameKuDto gameKuDto = transGameToGameKuDto(memberId,game);
                gameKuDtos.add(gameKuDto);
            }
            // 结果封装为客户端通用格式
            fungoPageResultDto.setData(gameKuDtos);
            PageTools.pageToResultDto(fungoPageResultDto,tootal,limit,page);
        }

        return fungoPageResultDto;
    }

    @Override
    public FungoPageResultDto<GameKuDto> listGameByBang(String memberId, BangGameDto bangGameDto) {
        int page = bangGameDto.getPage();
        int limit = bangGameDto.getLimit();
        // 计算 sql limit 偏移量
        int offset = (page-1) * limit;
        bangGameDto.setOffset(offset);
        Integer tootal =  gameService.countBangBySortType(bangGameDto.getSortType());

        FungoPageResultDto<GameKuDto> fungoPageResultDto = new FungoPageResultDto<>();
        ArrayList<GameKuDto> gameKuDtos = new ArrayList<>();

        // 优化没有数据情况
        if(tootal == 0 || offset > tootal){
            // 结果封装为客户端通用格式
            fungoPageResultDto.setData(gameKuDtos);
            PageTools.pageToResultDto(fungoPageResultDto,tootal,limit,page);
        }else {
            List<Game> games = gameService.listBangBySortType(bangGameDto);
           /* // 远程服务调用 获取游戏对应的圈子id
            List<String> gameIds = getGameIds(games);
            Map<String,String> gameCircleMap = null;*/
            for (Game game : games) {
                GameKuDto gameKuDto = transGameToGameKuDto(memberId, game);
                gameKuDtos.add(gameKuDto);
            }
            // 结果封装为客户端通用格式
            fungoPageResultDto.setData(gameKuDtos);
            PageTools.pageToResultDto(fungoPageResultDto,tootal,limit,page);
        }
        return fungoPageResultDto;
    }

    /**
     * 功能描述:
     * @param: memberId 用户id
     * @param: bangGameDto  封装游戏集合信息
     * @return: FungoPageResultDto
     * @auther: dl.zhang
     * @date: 2020/3/3 10:21
     */
    @Override
    public FungoPageResultDto<GameKuDto> listGameByStatus(String memberId, BangGameDto bangGameDto) {
        FungoPageResultDto<GameKuDto>  resultDto = null;
        try {
            List<Map<String,String>> gameInfoList = bangGameDto.getGameInfo();
            if(gameInfoList == null || gameInfoList.size() == 0) return FungoPageResultDto.FungoPageResultDtoFactory.buildSuccess("暂无更新游戏包");
            List<String> gameNameList = new ArrayList<>(  );
            gameInfoList.stream().forEach( s ->{
                gameNameList.add( s.get( "gamePackageName" ) );
            } );
            List<Game> gameList = gameDao.getGameByName(gameNameList  );
            List<GameOutPage> gameOutPages = new ArrayList<>();
            for (Map<String,String> map : gameInfoList){

                Game game = gameList.stream().filter(  c ->c.getAndroidPackageName().equals( map.get( "gamePackageName" ) )).findFirst().orElse( null);
                if(game == null){

                }else {
                    String gameVersion = game.getVersionMain()+"."+game.getVersionChild();
                    if(CommonUtils.isNewVersion(map.get( "gameVersion" ),gameVersion)){
                        GameOutPage out = new GameOutPage();
                        out.setObjectId(game.getId());
                        out.setName(game.getName());
                        out.setIcon(game.getIcon());
                        //2.4.3
                        out.setAndroidState(game.getAndroidState());
                        out.setIosState(game.getIosState());
                        out.setRating(getGameRating(game.getId()));
                        if (game.getAndroidPackageName() == null) {
                            game.setAndroidPackageName("");
                        }
                        if (game.getApk() == null) {
                            game.setApk("");
                        }
                        out.setApkUrl(game.getApk());
                        out.setItunesId(game.getItunesId());
                        out.setAndroidPackageName(game.getAndroidPackageName());
                        out.setGame_size((long) game.getGameSize());
                        //推荐数据 已弃用
                        Integer recommend_total_count = game.getRecommendNum() + game.getUnrecommendNum();
                        DecimalFormat df = new DecimalFormat("#.00");
                        out.setRecommend_total_rate(recommend_total_count == 0 ? 0 : Double.parseDouble(df.format((double) game.getRecommendNum() / recommend_total_count * 100)));
                        out.setRecommend_total_count(gameEvaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", game.getId()).and("state != -1")));
                        out.setCreatedAt(DateTools.fmtDate(game.getCreatedAt()));
                        out.setUpdatedAt(DateTools.fmtDate(game.getUpdatedAt()));
                        gameOutPages.add(out);
                    }
                }
            }
            resultDto = FungoPageResultDto.FungoPageResultDtoFactory.buildSuccess(gameOutPages);
        }catch (Exception e){
            logger.error( "获取要更新的游戏异常",e);
        }
        return resultDto;
    }

    @Override
    public FungoPageResultDto<GameOutBean> listGameByPackageName( BangGameDto sortType) {
        FungoPageResultDto<GameOutBean>  resultDto = null;
        try {
            List<Map<String,String>> gameInfoList = sortType.getGameInfo();
            if(gameInfoList == null || gameInfoList.size() == 0) return FungoPageResultDto.FungoPageResultDtoFactory.buildSuccess("暂无更新游戏包");
            List<String> gameNameList = new ArrayList<>(  );
            gameInfoList.stream().forEach( s ->{
                gameNameList.add( s.get( "gamePackageName" ) );
            } );
            List<Game> gameList = gameDao.getGameByName(gameNameList  );
            List<GameOutBean> gameOutPages = new ArrayList<>();
            for (Map<String,String> map : gameInfoList){

                Game game = gameList.stream().filter(  c ->c.getAndroidPackageName().equals( map.get( "gamePackageName" ) )).findFirst().orElse( null);
                if(game == null){

                }else {
                    String gameVersion = game.getVersionMain()+"."+game.getVersionChild();
                    if(CommonUtils.isNewVersion(map.get( "gameVersion" ),gameVersion)){
                        GameOutBean out = new GameOutBean();
                        out.setGameId( game.getId());
                        out.setName( game.getName());
                        out.setGamePackageName(map.get( "gamePackageName" )  );
                        out.setGameVersion(  map.get( "gameVersion" ));
                        gameOutPages.add(out);
                    }
                }
            }
            resultDto = FungoPageResultDto.FungoPageResultDtoFactory.buildSuccess(gameOutPages);
        }catch (Exception e){
            logger.error( "获取要更新的游戏异常",e);
        }
        return resultDto;
    }


    private List<String> getGameIds(List<Game> games ){
        List<String> list = new ArrayList<>();
        for (Game game : games) {
            list.add(game.getId());
        }
        return list;
    }


    /**
     *  实体转化
     * @param gameCircleMap key为游戏id 值为圈子id的map
     * @param memberId
     * @param game 游戏实体
     * @return 转化后的展示实体
     */
    private GameKuDto transGameToGameKuDto(String memberId, Game game){
        GameKuDto gameKuDto = new GameKuDto();
        gameKuDto.setGameId(game.getId());
        BeanUtils.copyProperties(game,gameKuDto);

        try {
            if (game.getImages() != null) {
                ObjectMapper mapper = new ObjectMapper();
                gameKuDto.setImages((ArrayList<String>) mapper.readValue(game.getImages(), ArrayList.class));
            }
        } catch (Exception e) {
            logger.error("GameKuDto 转化游戏图片失败",e);
        }
        // 处理 描述展示和圈子标识展示
        gameKuDto.setAndroidStatusDesc(gameStatusDesc(gameKuDto.getAndroidStatusDesc()));
        gameKuDto.setIosStatusDesc(gameStatusDesc(gameKuDto.getIosStatusDesc()));

        // 可优化 -- 看上线效果
        CircleGamePostVo circleGamePostVo = new CircleGamePostVo(CircleGamePostVo.CircleGamePostTypeEnum.GAMESID.getKey(),game.getId(),"");
        ResultDto<String> re = communityFeignClient.getCircleByGame(circleGamePostVo);
        if (CommonEnum.SUCCESS.code().equals(String.valueOf(re.getStatus())) && !re.getData().equals("")) {
            gameKuDto.setCircleId(re.getData());
        }else {
            gameKuDto.setCircleId(null);
        }

        // 处理加速
        String origin = gameKuDto.getOrigin();
        Boolean canFast = gameKuDto.getCanFast();
        if(canFast == null){
            canFast = Boolean.TRUE;
        }
        gameKuDto.setCanFast(origin.equals("中国")?Boolean.FALSE:canFast);
        gameKuDto.setOrigin(origin.equals("中国")?null:origin);

        // 处理 版本问题
        String versionChild = game.getVersionChild();
        String versionMain = game.getVersionMain();
        if(!CommonUtil.isNull(versionChild)){
            gameKuDto.setVersion(versionMain+"."+versionChild);
        }else{
            gameKuDto.setVersion(versionMain);
        }
        Integer androidState = game.getAndroidState();

        // 处理预约状态
        if(StringUtil.isNotNull(memberId)&&androidState!=null&&androidState == 1){
            // 查询当前登录用户是否预约
            String platForm = BuriedPointUtils.getPlatForm();
            GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", game.getId()).eq("phone_model", platForm).eq("state", 0));
            gameKuDto.setMake(srel!=null);
        }else{
            gameKuDto.setMake(false);
        }
        return gameKuDto;
    }

    /**
     *  游戏状态描述 格式  {"dsc":"开启时间待通知","starDate":1571812315000,"endDate":1571985115000}
     * @param databaseDesc 数据库中描述
     */
    private String gameStatusDesc(String databaseDesc){
       if(CommonUtil.isNull(databaseDesc)){
           return null;
       }
       try {
           JSONObject jsonObject = JSONObject.parseObject(databaseDesc);
           String dsc = jsonObject.getString("dsc");
           if(CommonUtil.isNull(dsc)){
               return null;
           }
           long currentTime = System.currentTimeMillis();

           Long starDate = jsonObject.getLong("starDate");

           Long endDate = jsonObject.getLong("endDate");

           if(starDate == null || endDate == null){
               return dsc;
           }

           if(currentTime>=starDate && currentTime<= endDate ){
               return dsc;
           }
       }catch (Exception e){
           logger.error("转换游戏状态描述错误",e);
       }
       return null;
    }


    //可修改
    private ResultDto<List<Map<String, Object>>> getTagByGameId(String gameId, List<String> TagIdList) {
        //获得全部分类以及标签，在选中的打勾
//        迁移微服务 判断BasTagGroup属性值获取BasTagGroup集合
//        2019-05-15
//        lyc
        List<BasTagGroup> basTagGroupList = basTagGroupService.selectList(new EntityWrapper<BasTagGroup>());
       /* BasTagGroupDto basTagGroupDto = new BasTagGroupDto();
        List<BasTagGroupDto> basTagGroupList = iEvaluateProxyService.getBasTagGroupBySelectList(basTagGroupDto);*/
        if (basTagGroupList == null || basTagGroupList.isEmpty()) {
            return ResultDto.error("251", "找不到标签分类信息");
        }
        // 第一集合
        List<Map<String, Object>> totalList = new ArrayList<>();
        // 根据标签分类整理标签
        for (BasTagGroup tagGroup : basTagGroupList) {
            // 第二集合
            Map<String, Object> groupData = new HashMap<String, Object>();
            String groupId = tagGroup.getId();
            groupData.put("name", tagGroup.getName());
            groupData.put("objectId", groupId);
            groupData.put("icon", tagGroup.getIcon());
            // 找出分类关联标签
//            迁移微服务 根据BasTag中group_id属性获取BasTag集合
//            2019-05-15
//            lyc
            List<BasTag> unionTagList = basTagService.selectList(new EntityWrapper<BasTag>().eq("group_id", groupId));
           /* BasTagDto basTagDto = new BasTagDto();
            basTagDto.setGroupId(groupId);
            List<BasTagDto> unionTagList = iEvaluateProxyService.getBasTagBySelectListGroupId(basTagDto);*/
            // 标签个数
            int count = 0;
            // 加入标签
            if (unionTagList != null && unionTagList.size() != 0) {
                // 第三集合
                List<Map<String, Object>> tagList = new ArrayList<>();
                for (BasTag tag : unionTagList) {
                    // 第四集合
                    Map<String, Object> tagData = new HashMap<String, Object>();
                    tagData.put("name", tag.getName());
                    tagData.put("tagId", tag.getId());
                    if (TagIdList.contains(tag.getId())) {
                        tagData.put("isSupported", true);
                    } else {
                        tagData.put("isSupported", false);
                    }
                    tagList.add(tagData);
                    count++;
                }
                groupData.put("tagList", tagList);
            }
            groupData.put("count", count);
            totalList.add(groupData);
        }
        return ResultDto.success(totalList);
    }

    /**
     * ********************************Fein方法区*****************************************
     */
    @Override
    public Boolean updateCountor(Map<String, String> map) {
        return gameDao.updateCountor(map);
    }

    @Override
    public String getMemberIdByTargetId(Map<String, String> map) {
        return gameDao.getMemberIdByTargetId(map);
    }


    /**
     * ********************************Fein方法区*****************************************
     */

}

// 按热力值排序
class HotFun implements Comparator<Game> {
    @Override
    public int compare(Game o1, Game o2) {
        try {
            return (int) (hotFun(o1) - hotFun(o2));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("操作错误");
        }
    }

    public long hotFun(Game g) throws ParseException {
        //游戏热度 = 评论数commentNum + 下载数DownloadNum
        Integer Rn = g.getCommentNum() + g.getDownloadNum();
        /*
        Integer R = Rn == 0 ? 0 : g.getCommentNum() / Rn * 100;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Long t = format.parse("2017-01-01").getTime();
        Long T = g.getCreatedAt().getTime();
        Integer A = 180000;
        return R * Rn + (T - t) / A;
        */
        return Rn;
    }
}

// 评分排序
class RateFun implements Comparator<Game> {
    @Override
    public int compare(Game o1, Game o2) {
        return rateFun(o1) - rateFun(o2);
    }

    public int rateFun(Game g) {
        Integer Rn = g.getRecommendNum() + g.getUnrecommendNum();
        double R = Rn == 0 ? 0 : (double) g.getRecommendNum() / Rn * 100;
        return (int) R;
    }
}


//时间排序
class DateFun implements Comparator<Game> {
    @Override
    public int compare(Game o1, Game o2) {
        return (int) (o1.getCreatedAt().getTime() - o2.getCreatedAt().getTime());
    }
}