package com.game.common.consts;


import com.game.common.config.FungoConfig;

/**
 * <p>
 * 			fungo业务层接口定义
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class FungoCoreApiConstant {

    private static String tag = new FungoConfig().getTag();

    public static final String PUB_POST = "POST_";

    public static final String PUB_CIRCLE = "CIRCLE_";

    //********************************************首页********************************************************
    /**
     * 首页-发现页轮播
     * get
     * post
     */
    public static final String FUNGO_CORE_API_INDEX_RECM_TOPIC = "/api/recommend/topic" +  tag ;//"_cloud";

    /**
     * 首页-安利墙接口
     * POST
     */
    public static final String FUNGO_CORE_API_INDEX_AMWAYWALL = "/api/amwaywall" + tag ;// "_cloud";

    /**
     * 首页-安利墙列表接口
     * POST
     */
    public static final String FUNGO_CORE_API_INDEX_AMWAYWALL_LIST = "/api/amwaywall/list" + tag ;// "_cloud";


    /**
     * 首页-文章帖子列表
     * POST
     */
    public static final String FUNGO_CORE_API_INDEX_POST_LIST = "/api/amwaywall/postlist" + tag ;// "_cloud";

    /**
     * 首页-首页(v2.4)
     * post
     */
    public static final String FUNGO_CORE_API_INDEX_RECOMMEND_INDEX = "/api/recommend/index" + tag ;// "_cloud";


    /**
     * 首页-pc端发现页游戏合集
     * post
     */
    public static final String FUNGO_CORE_API_INDEX_RECOMMEND_PC_GAMEGROUP = "/api/recommend/pc/gamegroup" + tag ;//"_cloud";


    /**
     * 首页-pc端游戏合集详情
     * GET
     */
    public static final String FUNGO_CORE_API_INDEX_RECOMMEND_PC_GROUPDETAIL = "/api/recommend/pc/groupdetail/{groupId}" + tag ;// "_cloud";


    //**************************************************广告**************************************************


    /**
     * 广告- 首页游戏banner接口(v2.3)
     * GET
     */
    public static final String FUNGO_CORE_API_ADVERT_INDEX = "/api/advert" + tag ;// "_cloud";


    /**
     * 广告- 首页游戏banner接口(v2.3)
     * /api/advert接口为了适应pc关键字限制的问题,新开了该同业务功能接口
     * GET
     */
    public static final String FUNGO_CORE_API_ADVERT_BNR = "/api/adt/bnr" + tag ;// "_cloud";


    /**
     * 广告- 发现页轮播
     * GET
     */
    public static final String FUNGO_CORE_API_ADVERT_RECOMMEND_DISCOVER = "/api/recommend/discover" + tag ;//"_cloud";


    //**************************************************游戏**************************************************
    /**
     * 游戏- 游戏合集项列表(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_ITEMS = "/api/content/game/items" + tag ;// "_cloud";


    /**
     * 游戏- 获取最近评论的游戏(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_RECENTEVA = "/api/content/game/recenteva" + tag ;// "_cloud";


    /**
     * 游戏- 官方游戏分类
     * GET
     */
    public static final String FUNGO_CORE_API_GAME_TAG = "/api/recommend/tag/game" + tag ;// "_cloud";


    /**
     * 游戏- 游戏列表
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_LIST = "/api/content/games" + tag ;// "_cloud";


    /**
     * 游戏- 游戏详情(2.4修改)
     * GET
     */
    public static final String FUNGO_CORE_API_GAME_DETAIL = "/api/content/game/{gameId}" + tag ;// "_cloud";


    /**
     * 获得全部游戏标签
     * GET
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_TAG_LIST = "/api/tag/taglist" + tag ;//"_cloud";


    /**
     * 根据游戏ID获取游戏标签列表(2.4修改)
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_TAG_LIST_WITH_GAME_ID = "/api/tag/game/taglist" + tag ;//"_cloud";


    /**
     * 我的游戏评测(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_USER_EVALUATIONLIST = "/api/mine/evaluationList" + tag ;//"_cloud";


    /**
     * 我的游戏列表
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_GAMELIST = "/api/mine/gameList" + tag ;//"_cloud";


    /**
     * 游戏评价列表
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_EVALUATIONS = " /api/content/evaluations" + tag ;//"_cloud";


    //**************************************************系统**************************************************
    //游戏工具
    /**
     * 游戏工具下载(2.4.3)
     * GET
     */
    public static final String FUNGO_CORE_API_SYSTEM_TOOLS_GAME = "/api/tools/game/list/{plaType}" + tag ;//"_cloud";


    //**************************************************我的|会员**************************************************

    /**
     * 获取我的收藏
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_COLLECTION = "/api/mine/collection" + tag ;//"_cloud";


    /**
     * 获取关注内容
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_FOLLW = "/api/mine/follow" + tag ;//"_cloud";


    /**
     * 获取我的粉丝
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_FANS = "/api/mine/fans" + tag ;//"_cloud";


    /**
     * 浏览记录
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_HISTORY = "/api/mine/history" + tag ;//"_cloud";


    /**
     * 获取用户时间线
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_TIME_LINE = "/api/user/timeline" + tag ;//"_cloud";


    /**
     * 其他会员信息
     * GET
     */
    public static final String FUNGO_CORE_API_MEMBER_USER_CARD = "/api/user/card/{cardId}" + tag ;//"_cloud";


    /**
     * web端会员信息
     * GET
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_WEBIINFO = "/api/user/webInfo" + tag ;//"_cloud";


    /**
     * 我的等级信息(2.4.3)
     * GET
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_RANKS_LEVEL = "/api/user/incents/spirit/ranks" + tag ;//"_cloud";


    /**
     * 获取我的发布
     * GET
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_PUBLISH = "/api/mine/publish" + tag ;// "_cloud";


    /**
     * 个人资料
     * GET
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_INFO = "/api/mine/info" + tag ;//"_cloud";


    /**
     * 获取用户fungo比账号获取|消费明细
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST = "/api/user/incents/fortune/coin" + tag ;// "_cloud";


    //**************************************************文章**************************************************

    /**
     * 所有的社区置顶文章(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_ALL_POST_TOPIC_CACHE =  "/api/content/post/topic";
    public static final String FUNGO_CORE_API_ALL_POST_TOPIC = PUB_POST+"/api/content/post/topic" + tag ;// "_cloud";

    /**
     * 我的文章(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_USER_POSTS = "/api/mine/posts" + tag ;// "_cloud";

    public static final String  FUNGO_CORE_API_GAMR_POSTS_CACHE = "/api/community/circle/game/post";
    public static final String FUNGO_CORE_API_GAMR_POSTS = PUB_POST+"/api/community/circle/game/post"+tag;


    public static final String FUNGO_CORE_API_CIRCLE_POST_CACHE = "/api/community/circle/post";
    public static final String FUNGO_CORE_API_CIRCLE_POST = PUB_POST+"/api/community/circle/post"+tag;


    public static final String FUNGO_CORE_API_COMMUNITY_POST_CACHE = "/api/portal/community/content/posts";
    public static final String FUNGO_CORE_API_COMMUNITY_POST = PUB_POST+"/api/portal/community/content/posts"+tag;

    /**
     * 我的评论(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_USER_COMMENTS = "/api/mine/comments" + tag ;//"_cloud";


    /**
     * 社区置顶文章(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_POST_CONTENT_TOPIC = "/api/content/post/topic/{communityId}" + tag ;// "_cloud";


    /**
     * 帖子内容html
     * 内容数据
     * POST
     */
    public static final String FUNGO_CORE_API_POST_CONTENT_HTML_CONTENT = "/api/content/post/html/{postId}/content" + tag ;// "_cloud";


    /**
     * 帖子内容html
     * 标题数据
     * POST
     */
    public static final String FUNGO_CORE_API_POST_CONTENT_HTML_TITLE = "/api/content/post/html/{postId}/title" + tag ;// "_cloud";


    /**
     * 帖子(文章)详情
     * GET
     */
    public static final String FUNGO_CORE_API_POST_CONTENT_DETAIL_CACHE = "/api/content/post/{postId}/detail" ;
    public static final String FUNGO_CORE_API_POST_CONTENT_DETAIL = "/api/content/post/{postId}/detail" + tag ;// "_cloud";


    /**
     * 帖子/心情评论列表
     * POST
     */
    public static final String FUNGO_CORE_API_POST_CONTENT_COMMENTS = "/api/content/comments" + tag ;//"_cloud";

    /**
     * 帖子/心情评论列表
     * POST
     */
    public static final String FUNGO_CORE_API_POST_RECOMMEND_USERS = "/api/recommend/users" + tag ;// "_cloud";


    //**************************************************心情**************************************************


    /**
     * 我的心情(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_USER_MOODS = "/api/mine/moods" + tag ;// "_cloud";


    /**
     * 获取心情动态列表(v2.4)
     * POST
     */
    public static final String FUNGO_CORE_API_MOODS_LIST = "/api/recommend/moods" + tag ;// "_cloud";


    /**
     * 获取心情内容
     * get
     */
    public static final String FUNGO_CORE_API_MOOD_CONTENT_GET = "/api/content/mood/{moodId}" + tag ;//"_cloud";


    //**************************************************社区**************************************************

    /**
     * 社区列表
     * POST
     */
    public static final String FUNGO_CORE_API_COMMUNITYS_LIST = "/api/content/communitys" + tag ;// "_cloud";

    /**
     * 社区详情
     * GET
     */
    public static final String FUNGO_CORE_API_COMMUNITYS_DETAIL_CACHE = "/api/content/community/{communityId}" ;
    public static final String FUNGO_CORE_API_COMMUNITYS_DETAIL = "/api/content/community/{communityId}" + tag ;// "_cloud";


    /**
     * 帖子列表
     * POST
     */
    public static final String FUNGO_CORE_API_COMMUNITYS_POST_LIST = "/api/content/posts" + tag ;// "_cloud";

    public static final String FUNGO_CORE_API_CIRCLE_GAME_CACHE = "/api/community/game/circle";
    public static final String FUNGO_CORE_API_CIRCLE_GAME = PUB_CIRCLE+"/api/community/game/circle"+tag;

    //**************************************************圈子**************************************************
    public static final String FUNGO_CORE_API_CIRCLE_INFO_CACHE = "/api/community/circle/";
    public static final String FUNGO_CORE_API_CIRCLE_INFO = PUB_CIRCLE+"/api/community/circle/"+tag;

    //**************************************************评论**************************************************


    /**
     * 帖子评论详情
     * POST
     */
    public static final String FUNGO_CORE_API_POST_COMMENT_DETAIL = "/api/content/comment/{commentId}" + tag ;//"_cloud";


    /**
     * 心情评论详情
     * POST
     */
    public static final String FUNGO_CORE_API_MOOD_COMMENT_DETAIL = "/api/content/message/{messageId}" + tag ;// "_cloud";


    //**************************************************任务**************************************************


    /**
     * 签到格言(2.4.3)
     * GET
     */
    public static final String FUNGO_CORE_API_TASK_RANK_SIGNINMOTTO = "/api/rank/signinmotto" + tag ;// "_cloud";


    /**
     * 任务及任务虚拟币规则(v2.4.3)
     * GET
     */
    public static final String FUNGO_CORE_API_TASK_USER_INCENTS_RULE = "/api/user/incents/rule/coins" + tag ;// "_cloud";


    /**
     * 获取用户任务完成进度数据
     * GET
     */
    public static final String FUNGO_CORE_API_TASK_USER_TASK_PROGRESS = "/api/user/incents/task/progress" + tag ;// "_cloud";


    public static final String FUNGO_CORE_API_GETSIGNINTASKGROUPANDTASKRULEDATA = "getSignInTaskGroupAndTaskRuleData" + tag ;// "_cloud";


    public static final String FUNGO_CORE_API_SCOREGROUPSV2 = "ScoreGroupsV2" + "_cloud" + tag ;// "_cloud";

    public static final String FUNGO_CORE_API_ScoreRuleListV2 = "ScoreRuleListV2.4.6_Cloud" + tag ;// "_cloud";

    public static final String ISREGISTEREDANDHASPHONE_CLOUD = "isRegisteredAndHasPhone_Cloud" + tag ;//"cloud";

    public static final String GETMEMBERWITHMBIDSNS_CLOUD = "getMemberWithMbIdSNS_Cloud" + tag ;// "_cloud";

    public static final String ISREGISTERED_CLOUD = "isRegistered_Cloud" + tag ;// "_cloud";


    //**************************************************游戏**************************************************
    /**
     * PC2.0圈子首页列表
     */
    public static final String FUNGO_CORE_API_GETCOMMUNITYLISTPC2_0 = "/api/portal/community/content/communitysPCList" + tag ;//;

    //    getRecentBrowseCommunity
    public static final String FUNGO_CORE_API_GETRECENTBROWSECOMMUNITY = "/api/portal/community/content/recentbrowsecommunity" + tag ;//;

    public static final String FUNGO_CORE_API_CIRCLE_EVENT_INDEX = "/api/system/circle/event/list"  + tag ;// "_cloud";


    //-------------------------------------------游戏礼包----------------------------------------
    /**
     * 获取游戏礼包商品列表接口
     */
    public static final String  FUNGO_CORE_API_GAME_GOODS_LIST = "/api/mall/goods/game/list_Post" + tag ;// "_cloud";



    /**
     * Redis 缓存用户详情数据 前缀
     * 从FungoCacheNotice迁移过来
     */
    public static final String REDIS_MEMBER_NOTICE = "memberNotice"+tag ;//  "_cloud";

    //-------------
}
