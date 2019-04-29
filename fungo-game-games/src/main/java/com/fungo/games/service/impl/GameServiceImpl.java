package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.entity.Game;
import com.fungo.games.service.IGameService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.*;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GameServiceImpl implements IGameService {

//    @Autowired
//    private GameService gameService;
//
//    @Autowired
//    private BasActionService actionService;
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private GameEvaluationService gameEvaluationService;
//
//    @Autowired
//    private GameTagService gameTagService;
//
//    @Autowired
//    private BasTagService bagTagService;
//
//    @Autowired
//    private GameSurveyRelService surveyRelService;
//    @Autowired
//    private GameDao gameDao;
//
//    @Autowired
//    private GameCollectionItemService gameCollectionItemService;
//    @Autowired
//    private IUserService iuserService;
//
//    @Autowired
//    private FungoCacheGame fungoCacheGame;

//    @Override
//    public ResultDto<GameOut> getGameDetail(String gameId, String memberId, String ptype)
//            throws Exception {
//
//        GameOut outResult =  (GameOut)fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + gameId, memberId + ptype);
//        if (null != outResult){
//           return ResultDto.success(outResult);
//        }
//
//        Game game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameId).eq("state", "0"));
//        if (game == null) {
//            return ResultDto.error("211", "找不到目标游戏");
//        }
//        // 根据图片比例数据生成相应的返回字段
//        int width = 16, height = 9;
//        if (game.getImageRatio() == 1) {
//            width = 9;
//            height = 16;
//        }
//        GameOut out = new GameOut();
//        out.setName(game.getName());
//        out.setGame_size(formatGameSize(game.getGameSize()));
//        out.setImage_height(height);
//        out.setImage_width(width);
//        out.setVersion(game.getVersionMain() + "." + game.getVersionChild());
//
//
//        //根据当前运行环境生成链接
//        String env = Setting.RUN_ENVIRONMENT;
//        if (env.equals("dev")) {
//            out.setLink_url(Setting.DEFAULT_SHARE_URL_DEV + "/game/" + gameId);
//        } else if (env.equals("pro")) {
//            out.setLink_url(Setting.DEFAULT_SHARE_URL_PRO + "/game/" + gameId);
//        } else if (env.equals("uat")) {
//            out.setLink_url(Setting.DEFAULT_SHARE_URL_DEV + "/game/" + gameId);
//        }
//
//        // 上架计划增加下载按钮的开关（关）
//        out.setIs_download(false);
//        try {
//            if (game.getCompatibility() != null) {
//                ObjectMapper mapper = new ObjectMapper();
//                out.setCompatibility((ArrayList<String>) mapper.readValue(game.getCompatibility(), ArrayList.class));
//            }
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        out.setPackageName(game.getAndroidPackageName());
//
//        out.setVideo(game.getVideo());
//        out.setApk(game.getApk());
//        out.setCover_image(game.getCoverImage());
//        out.setDetail(game.getDetail());
//        out.setDeveloper(game.getDeveloper());
//        out.setIcon(game.getIcon());
//        out.setImage_ratio(game.getImageRatio());
//        out.setIsbn_id(game.getIsbnId());
//        out.setIntro(game.getIntro());
//        out.setItunes_id(game.getItunesId());
//        out.setObjectId(game.getId());
//        out.setLink_community(game.getCommunityId());
//        out.setOrigin(game.getOrigin());
//        out.setRelease_image(game.getReleaseImage());
//        out.setState(game.getState());
//        out.setUpdate_log(game.getUpdateLog());
//        out.setVersion_child(game.getVersionChild());
//        out.setVersion_main(game.getVersionMain());
//
//        // 生成推荐相关数据
//        //fix bug: 修改排序规则 按时间升序 [by mxf 2019-03-01]
//        List<GameEvaluation> recoList = gameEvaluationService.selectList(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("state", 0).orderBy("created_at", true));
//        //end
////		List<BasAction> recoList = actionService.selectList(new EntityWrapper<BasAction>().eq("target_id", gameId).in("type", new Integer[] { 8, 9 }).and("state != -1").orderBy("created_at", false));
//        // List<String> userIdList =
//        // actionList.stream().map(BasAction::getMemberId).collect(Collectors.toList());
////		int likeCountRecent = 0;
////		int unlikeCountRecent = 0;
////		int recentDayCount = 7;
////		int recentTime = recentDayCount * 24 * 60 * 60 * 1000;
////		boolean isRecent = true;
//        List<Map<String, Object>> recommendList = new ArrayList<>();
//        if (recoList != null && recoList.size() != 0) {
//            for (GameEvaluation g : recoList) {
//                if (recommendList.size() < 8) {
//                    Map<String, Object> map = new HashMap<>();
//
//                    Member user = memberService.selectById(g.getMemberId());
//                    if (null != user) {
//                        map.put("username", user.getUserName());
//                        map.put("avatar", user.getAvatar());
//                        map.put("statusImg", iuserService.getStatusImage(memberId));
//                        recommendList.add(map);
//                    }
//                }
////				if (isRecent && new Date().getTime() - g.getCreatedAt().getTime() <= recentTime) {
////					if ("1".equals(g.getIsRecommend())) {
////						likeCountRecent = likeCountRecent + 1;
////					} else {
////						unlikeCountRecent = unlikeCountRecent + 1;
////					}
////				} else {
////					isRecent = false;
////				}
//            }
//            out.setRecommend_list(recommendList);
//        }
////		int recommend_recent_count = likeCountRecent + unlikeCountRecent;
////		int recommend_total_count = game.getRecommendNum() + game.getUnrecommendNum();
////		//gameMap.put("recommend_list", recommendList);
////		out.setRecent_day_count(recentDayCount);
////		out.setRecommend_recent_count(recommend_recent_count);
////		DecimalFormat df = new DecimalFormat("#.00");
////		out.setRecommend_recent_rate(recommend_recent_count == 0 ? 0 : Double.parseDouble(df.format((double)likeCountRecent / recommend_recent_count * 100)));
////		out.setRecommend_total_rate(recommend_total_count == 0 ? 0 :  Double.parseDouble(df.format((double)game.getRecommendNum() / recommend_total_count * 100)));
////		out.setRecommend_total_count(recommend_total_count);
//        out.setDownload_num(game.getDownloadNum());
//        // 查询评论数量
//        int evaCount = gameEvaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).and("state != -1"));
//        out.setEvaluation_num(evaCount);
//
//        //2.4  平均分 每颗星占比 雷达图
//        //没人评分 有人评分
//        int count = gameEvaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId));
//        if (count > 0) {
//            HashMap<String, BigDecimal> rateData = gameDao.getRateData(gameId);
//            if (rateData != null) {
//                if (rateData.get("avgRating") != null) {
//                    out.setRating(Double.parseDouble(rateData.get("avgRating").toString()));
//                }
//                rateData.remove("gameId");
//                rateData.remove("avgRating");
//            } else {
//                rateData = new HashMap<String, BigDecimal>();
//                rateData.put("avgTrait1", BigDecimal.valueOf(0));
//                rateData.put("avgTrait2", BigDecimal.valueOf(0));
//                rateData.put("avgTrait3", BigDecimal.valueOf(0));
//                rateData.put("avgTrait4", BigDecimal.valueOf(0));
//                rateData.put("avgTrait5", BigDecimal.valueOf(0));
//            }
//            out.setTraitList(traitFormat(rateData));
//
//            //获取每个游戏评分所占百分比 key:评分  value:百分比
//            HashMap<String, BigDecimal> percentData = gameDao.getPercentData(gameId);
//            HashMap<String, Double> perMap = new HashMap<>();
//
//            if (percentData != null) {//评分两两分组,统计占比 1：1-2分， 2：3-4分， 3：5-6分， 4：7-8分， 5：9-10分
//                perMap.put("1", percentData.get("1").doubleValue() + percentData.get("2").doubleValue());
//                perMap.put("2", percentData.get("3").doubleValue() + percentData.get("4").doubleValue());
//                perMap.put("3", percentData.get("5").doubleValue() + percentData.get("6").doubleValue());
//                perMap.put("4", percentData.get("7").doubleValue() + percentData.get("8").doubleValue());
//                perMap.put("5", percentData.get("9").doubleValue() + percentData.get("10").doubleValue());
//            } else {
//                perMap.put("1", 0.0);
//                perMap.put("2", 0.0);
//                perMap.put("3", 0.0);
//                perMap.put("4", 0.0);
//                perMap.put("5", 0.0);
//            }
//            perMap = percentFormat(perMap);
//
//            out.setRatingList(rateFormat(perMap));
//        } else {
//            List<TraitBean> traitList = new ArrayList<>();
//            for (int i = 1; i <= 5; i++) {
//                TraitBean tb = new TraitBean();
//                tb.setKey("avgTrait" + i);
//                tb.setKeyName(transKey(tb.getKey()));
//                tb.setValue(BigDecimal.valueOf(0));
//                traitList.add(tb);
//            }
//            out.setTraitList(traitList);
//        }
//
//        // 查询游戏标签
//        List<GameTag> gameTagList = gameTagService.selectList(new EntityWrapper<GameTag>().eq("game_id", gameId).eq("type", 1));
//        //andNew("type = {0}",1).or("like_num > {0}",5).orderBy("like_num", false).last("LIMIT 5"));
//        if (gameTagList != null && gameTagList.size() > 0) {
//            List<BasTag> tagList = bagTagService.selectList(new EntityWrapper<BasTag>().in("id",
//                    gameTagList.stream().map(GameTag::getTagId).collect(Collectors.toList())));
//            if (tagList != null && tagList.size() > 0) {
//                List<String> tagNames = new ArrayList<>();
//                tagList.forEach(tag -> tagNames.add(tag.getName()));
//                //gameMap.put("tags", tagNames);
//                out.setTags(tagNames);
//            }
//        }
//
//        out.setCreatedAt(DateTools.fmtDate(game.getCreatedAt()));
//        out.setUpdatedAt(DateTools.fmtDate(game.getUpdatedAt()));
//        try {
//            if (game.getImages() != null) {
//                ObjectMapper mapper = new ObjectMapper();
//                out.setImages((ArrayList<String>) mapper.readValue(game.getImages(), ArrayList.class));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        out.setAuthor(game.getMemberId());
//
//        if (game.getAndroidState() != null) {
//            out.setAndroidState(game.getAndroidState());
//        }
//        if (game.getIosState() != null) {
//            out.setIosState(game.getIosState());
//        }
//
//        if (!"".equals(memberId) && !"".equals(ptype)) {//游戏预约测试信息
//            GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", game.getId()).eq("phone_model", ptype).eq("state", 0));
//            if (srel != null) {
//                out.setBinding(!StringUtils.isNullOrEmpty(srel.getAppleId()));
//                out.setClause(1 == srel.getAgree() ? true : false);
//                out.setMake(true);
//            }
//        }
//
//        //redis cache
//        fungoCacheGame.excIndexCache(true,FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + gameId, memberId + ptype,out);
//        return ResultDto.success(out);
//    }

//    @Override
//    public FungoPageResultDto<GameOutPage> getGameList(GameInputPageDto gameInputDto, String memberId, String os) {
//
//        String keySuffix = JSON.toJSONString(gameInputDto) + os;
//        FungoPageResultDto<GameOutPage> re = (FungoPageResultDto<GameOutPage> )fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_LIST + memberId, keySuffix);
//        if (null != re  && null != re.getData() && re.getData().size() > 0){
//            return  re;
//        }
//
//        Wrapper wrapper = new EntityWrapper<Game>().where("state = {0}", 0);
//        int limit = gameInputDto.getLimit();
//        int page = gameInputDto.getPage();
//        int sort = gameInputDto.getSort();
//        String tag = gameInputDto.getTag();
//        if (tag != null && tag.replace(" ", "") != "") {
//            wrapper = wrapper.like("tags", tag);
//        }
//
//        @SuppressWarnings("unchecked")
////		Page<Game> gamePage = gameService.selectPage(new Page<>(page, limit),wrapper);
////		List<Game> gameList = gamePage.getRecords();
//                List<Game> gameList = gameService.selectList(wrapper);
//        if (gameList.size() == 0) {
//            return new FungoPageResultDto<>();
//        }
//        Comparator<Game> hotFun = new HotFun();// 排序接口
//        Comparator<Game> rateFun = new RateFun();// 排序接口
//        Comparator<Game> dateFun = new DateFun();// 排序接口
//
//        if (sort == 1) {// 时间正序
//            Collections.sort(gameList, dateFun);
//        } else if (sort == 2) {// 时间倒序
//            Collections.sort(gameList, Collections.reverseOrder(dateFun));
//        } else if (sort == 3) {// 热力值正序
//            Collections.sort(gameList, hotFun);
//        } else if (sort == 4) {// 热力值倒序
//            Collections.sort(gameList, Collections.reverseOrder(hotFun));
//        } else if (sort == 5) {// 评分正序
//            Collections.sort(gameList, rateFun);
//        } else if (sort == 6) {// 评分倒序
//            Collections.sort(gameList, Collections.reverseOrder(rateFun));
//        } else {//默认排序 评分倒序
//            Collections.sort(gameList, Collections.reverseOrder(rateFun));
//        }
//
//        boolean m = false;
//        if (!CommonUtil.isNull(memberId)) {
//            m = true;
//        }
//
//        Page<Game> p = pageFormat(gameList, page, limit);
//        List<Game> records = p.getRecords();
//        List<GameOutPage> dataList = new ArrayList<>();
//        for (Game game : records) {
//            GameOutPage out = new GameOutPage();
//            out.setObjectId(game.getId());
//            out.setName(game.getName());
//            out.setIcon(game.getIcon());
//            //2.4.3
//            out.setAndroidState(game.getAndroidState());
//            out.setIosState(game.getIosState());
//            out.setRating(getGameRating(game.getId()));
//            out.setApkUrl(game.getApk());
//            out.setItunesId(game.getItunesId());
//            out.setAndroidPackageName(game.getAndroidPackageName());
//            out.setGame_size((long) game.getGameSize());
//            //推荐数据 已弃用
//            Integer recommend_total_count = game.getRecommendNum() + game.getUnrecommendNum();
//            DecimalFormat df = new DecimalFormat("#.00");
//            out.setRecommend_total_rate(recommend_total_count == 0 ? 0 : Double.parseDouble(df.format((double) game.getRecommendNum() / recommend_total_count * 100)));
//            out.setRecommend_total_count(gameEvaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", game.getId()).and("state != -1")));
//            //
//
//            out.setCreatedAt(DateTools.fmtDate(game.getCreatedAt()));
//            out.setUpdatedAt(DateTools.fmtDate(game.getUpdatedAt()));
//
//            if (m) {
//                GameSurveyRel srel = this.surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("game_id", game.getId()).eq("phone_model", os).eq("state", 0));
//                if (srel != null) {
//                    out.setBinding(!StringUtils.isNullOrEmpty(srel.getAppleId()));
//                    out.setClause(1 == srel.getAgree() ? true : false);
//                    out.setMake(true);
//                }
//            }
//
//            dataList.add(out);
//        }
//        re = new FungoPageResultDto<GameOutPage>();
//        PageTools.pageToResultDto(re, p);
//        re.setData(dataList);
//        //redis cache
//        fungoCacheGame.excIndexCache(true,FungoCoreApiConstant.FUNGO_CORE_API_GAME_LIST + memberId, keySuffix,re);
//        return re;
//    }

//    @Override
//    public ResultDto<List<TagOutPage>> getGameTags() {
//
//        List<TagOutPage> outList =  (List<TagOutPage>)fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_TAG, "");
//        if (null != outList && !outList.isEmpty()){
//           return ResultDto.success(outList);
//        }
//        //官方标签
//        List<BasTag> tagList = bagTagService.selectList(new EntityWrapper<BasTag>().eq("group_id", "5ad55eb4ac502e0042ae29d9"));
//        outList = new ArrayList<>();
//        for (BasTag tag : tagList) {
//            TagOutPage out = new TagOutPage();
//            out.setObjectId(tag.getId());
//            out.setName(tag.getName());
//            out.setGroupId(tag.getGroupId());
//            out.setGameNum(tag.getGameNum());
//            out.setSort(tag.getSort());
//            out.setCreatedAt(DateTools.fmtDate(tag.getCreatedAt()));
//            out.setUpdatedAt(DateTools.fmtDate(tag.getUpdatedAt()));
//            outList.add(out);
//        }
//        //redis cache
//        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_TAG, "", outList);
//        return ResultDto.success(outList);
//    }

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

//	public PageResultDto getResult(int page, int limit, List list) {
//		PageDto pageDto = new PageDto();
//		pageDto.setTotal(list.size());
//		pageDto.setPageNum(page);
//		pageDto.setPageSize(limit);
//		pageDto.setList(list);
//
//		return PageResultDto.success(pageDto);
//
//	}

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


//    public List<TraitBean> traitFormat(HashMap<String, BigDecimal> rateData) {
//        List<TraitBean> traitList = new ArrayList<>();
//
//        if (traitList.size() == 0) {
//            for (int i = 1; i <= 5; i++) {
//                TraitBean tb = new TraitBean();
//                tb.setKey("avgTrait" + i);
//                tb.setKeyName(transKey(tb.getKey()));
//                tb.setValue(rateData.get(tb.getKey()) == null ? BigDecimal.valueOf(0) : rateData.get(tb.getKey()));
//                traitList.add(tb);
//            }
//        }
//        return traitList;
//    }

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
        return null;
    }

    @Override
    public FungoPageResultDto<GameOutPage> getGameList(GameInputPageDto gameInputDto, String memberId, String os) {
        return null;
    }

    @Override
    public ResultDto<List<TagOutPage>> getGameTags() {
        return null;
    }

    @Override
    public FungoPageResultDto<GameOutPage> recentEvaluatedGamesByMember(String userId, InputPageDto input) {
        return null;
    }

    @Override
    public FungoPageResultDto<GameItem> getGameItems(String memberId, GameItemInput input, String os) {
        return null;
    }

    @Override
    public double getGameRating(String gameId) {
        return 0;
    }

//    //分数区间
//    public List<RatingBean> rateFormat(HashMap<String, Double> perMap) {
//        List<RatingBean> blist = new ArrayList<>();
//        RatingBean bean1 = new RatingBean();
//        bean1.setKey("1-2分");
//        bean1.setValue(perMap.get("1"));
//
//
//        RatingBean bean2 = new RatingBean();
//        bean2.setKey("3-4分");
//        bean2.setValue(perMap.get("2"));
//
//
//        RatingBean bean3 = new RatingBean();
//        bean3.setKey("5-6分");
//        bean3.setValue(perMap.get("3"));
//
//
//        RatingBean bean4 = new RatingBean();
//        bean4.setKey("7-8分");
//        bean4.setValue(perMap.get("4"));
//
//
//        RatingBean bean5 = new RatingBean();
//        bean5.setKey("9-10分");
//        bean5.setValue(perMap.get("5"));
//        blist.add(bean5);
//        blist.add(bean4);
//        blist.add(bean3);
//        blist.add(bean2);
//        blist.add(bean1);
//
//        return blist;
//
//    }

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
        Integer Rn = g.getRecommendNum() + g.getUnrecommendNum();
        Integer R = Rn == 0 ? 0 : g.getRecommendNum() / Rn * 100;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Long t = format.parse("2017-01-01").getTime();
        Long T = g.getCreatedAt().getTime();
        Integer A = 180000;
        return R * Rn + (T - t) / A;
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