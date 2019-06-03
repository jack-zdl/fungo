package com.game.common.consts;


/**
 * <p>
 * 			fungo业务层接口定义
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class FungoCoreApiConstant {

    //********************************************首页********************************************************
    /**
     * 首页-发现页轮播
     * get
     * post
     */
    public static final String FUNGO_CORE_API_INDEX_RECM_TOPIC = "/api/recommend/topic";

    /**
     * 首页-安利墙接口
     * POST
     */
    public static final String FUNGO_CORE_API_INDEX_AMWAYWALL = "/api/amwaywall";

    /**
     * 首页-安利墙列表接口
     * POST
     */
    public static final String FUNGO_CORE_API_INDEX_AMWAYWALL_LIST = "/api/amwaywall/list";


    /**
     * 首页-文章帖子列表
     * POST
     */
    public static final String FUNGO_CORE_API_INDEX_POST_LIST = "/api/amwaywall/postlist";

    /**
     * 首页-首页(v2.4)
     * post
     */
    public static final String FUNGO_CORE_API_INDEX_RECOMMEND_INDEX = "/api/recommend/index";


    /**
     * 首页-pc端发现页游戏合集
     * post
     */
    public static final String FUNGO_CORE_API_INDEX_RECOMMEND_PC_GAMEGROUP = "/api/recommend/pc/gamegroup";


    /**
     * 首页-pc端游戏合集详情
     * GET
     */
    public static final String FUNGO_CORE_API_INDEX_RECOMMEND_PC_GROUPDETAIL = "/api/recommend/pc/groupdetail/{groupId}";


    //**************************************************广告**************************************************


    /**
     * 广告- 首页游戏banner接口(v2.3)
     * GET
     */
    public static final String FUNGO_CORE_API_ADVERT_INDEX = "/api/advert";


    /**
     * 广告- 首页游戏banner接口(v2.3)
     * /api/advert接口为了适应pc关键字限制的问题,新开了该同业务功能接口
     * GET
     */
    public static final String FUNGO_CORE_API_ADVERT_BNR = "/api/adt/bnr";


    /**
     * 广告- 发现页轮播
     * GET
     */
    public static final String FUNGO_CORE_API_ADVERT_RECOMMEND_DISCOVER = "/api/recommend/discover";



    //**************************************************游戏**************************************************
    /**
     * 游戏- 游戏合集项列表(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_ITEMS = "/api/content/game/items";


    /**
     * 游戏- 获取最近评论的游戏(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_RECENTEVA = "/api/content/game/recenteva";


    /**
     * 游戏- 官方游戏分类
     * GET
     */
    public static final String FUNGO_CORE_API_GAME_TAG = "/api/recommend/tag/game";


    /**
     * 游戏- 游戏列表
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_LIST = "/api/content/games";


    /**
     * 游戏- 游戏详情(2.4修改)
     * GET
     */
    public static final String FUNGO_CORE_API_GAME_DETAIL = "/api/content/game/{gameId}";


    /**
     * 获得全部游戏标签
     * GET
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_TAG_LIST = "/api/tag/taglist";


    /**
     * 根据游戏ID获取游戏标签列表(2.4修改)
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_TAG_LIST_WITH_GAME_ID = "/api/tag/game/taglist";


    /**
     * 我的游戏评测(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_USER_EVALUATIONLIST = "/api/mine/evaluationList";


    /**
     * 我的游戏列表
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_GAMELIST = "/api/mine/gameList";


    /**
     * 游戏评价列表
     * POST
     */
    public static final String FUNGO_CORE_API_GAME_EVALUATIONS= " /api/content/evaluations";



    //**************************************************系统**************************************************
    //游戏工具
    /**
     * 游戏工具下载(2.4.3)
     * GET
     */
    public static final String FUNGO_CORE_API_SYSTEM_TOOLS_GAME = "/api/tools/game/list/{plaType}";


    //**************************************************我的|会员**************************************************

    /**
     * 获取我的收藏
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_COLLECTION = "/api/mine/collection";


    /**
     * 获取关注内容
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_FOLLW = "/api/mine/follow";


    /**
     * 获取我的粉丝
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_FANS = "/api/mine/fans";


    /**
     * 浏览记录
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_HISTORY = "/api/mine/history";


    /**
     * 获取用户时间线
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_TIME_LINE = "/api/user/timeline";


    /**
     * 其他会员信息
     * GET
     */
    public static final String FUNGO_CORE_API_MEMBER_USER_CARD = "/api/user/card/{cardId}";


    /**
     * web端会员信息
     * GET
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_WEBIINFO = "/api/user/webInfo";


    /**
     * 我的等级信息(2.4.3)
     * GET
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_RANKS_LEVEL = "/api/user/incents/spirit/ranks";


    /**
     * 获取我的发布
     * GET
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_PUBLISH = "/api/mine/publish";


    /**
     * 个人资料
     * GET
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_INFO = "/api/mine/info";


    /**
     * 获取用户fungo比账号获取|消费明细
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST = "/api/user/incents/fortune/coin";



    //**************************************************文章**************************************************

    /**
     * 我的文章(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_USER_POSTS = "/api/mine/posts";


    /**
     * 我的评论(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_USER_COMMENTS = "/api/mine/comments";


    /**
     * 社区置顶文章(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_POST_CONTENT_TOPIC = "/api/content/post/topic/{communityId}";


    /**
     * 帖子内容html
     * 内容数据
     * POST
     */
    public static final String FUNGO_CORE_API_POST_CONTENT_HTML_CONTENT = "/api/content/post/html/{postId}/content";


    /**
     * 帖子内容html
     * 标题数据
     * POST
     */
    public static final String FUNGO_CORE_API_POST_CONTENT_HTML_TITLE = "/api/content/post/html/{postId}/title";


    /**
     * 帖子(文章)详情
     * GET
     */
    public static final String FUNGO_CORE_API_POST_CONTENT_DETAIL = "/api/content/post/{postId}/detail";


    /**
     * 帖子/心情评论列表
     * POST
     */
    public static final String FUNGO_CORE_API_POST_CONTENT_COMMENTS = "/api/content/comments";


    /**
     * 帖子/心情评论列表
     * POST
     */
    public static final String FUNGO_CORE_API_POST_RECOMMEND_USERS = "/api/recommend/users";







    //**************************************************心情**************************************************


    /**
     * 我的心情(2.4.3)
     * POST
     */
    public static final String FUNGO_CORE_API_MEMBER_USER_MOODS = "/api/mine/moods";



    /**
     * 获取心情动态列表(v2.4)
     * POST
     */
    public static final String FUNGO_CORE_API_MOODS_LIST = "/api/recommend/moods";


    /**
     * 获取心情内容
     * get
     */
    public static final String FUNGO_CORE_API_MOOD_CONTENT_GET = "/api/content/mood/{moodId}";



    //**************************************************社区**************************************************

    /**
     * 社区列表
     * POST
     */
    public static final String FUNGO_CORE_API_COMMUNITYS_LIST = "/api/content/communitys";

    /**
     * 社区详情
     * GET
     */
    public static final String FUNGO_CORE_API_COMMUNITYS_DETAIL = "/api/content/community/{communityId}";


    /**
     * 帖子列表
     * POST
     */
    public static final String FUNGO_CORE_API_COMMUNITYS_POST_LIST = "/api/content/posts";




    //**************************************************评论**************************************************


    /**
     * 帖子评论详情
     * POST
     */
    public static final String FUNGO_CORE_API_POST_COMMENT_DETAIL = "/api/content/comment/{commentId}";


    /**
     * 心情评论详情
     * POST
     */
    public static final String FUNGO_CORE_API_MOOD_COMMENT_DETAIL = "/api/content/message/{messageId}";











    //**************************************************任务**************************************************


    /**
     * 签到格言(2.4.3)
     * GET
     */
    public static final String FUNGO_CORE_API_TASK_RANK_SIGNINMOTTO = "/api/rank/signinmotto";



    /**
     * 任务及任务虚拟币规则(v2.4.3)
     * GET
     */
    public static final String FUNGO_CORE_API_TASK_USER_INCENTS_RULE = "/api/user/incents/rule/coins";


    /**
     * 获取用户任务完成进度数据
     * GET
     */
    public static final String FUNGO_CORE_API_TASK_USER_TASK_PROGRESS = "/api/user/incents/task/progress";
    /**
     * PC2.0圈子首页列表
     */
    public static final String FUNGO_CORE_API_GETCOMMUNITYLISTPC2_0 = "/api/portal/community/content/communitysPCList";
//    getRecentBrowseCommunity
    public static final String FUNGO_CORE_API_GETRECENTBROWSECOMMUNITY = "/api/portal/community/content/recentbrowsecommunity";


    //**************************************************游戏**************************************************


    //-------------
}
