
package com.fungo.games.service;

import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.*;
import com.game.common.dto.search.GameSearchOut;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface IGameService {

	/**
	 * 游戏详情
	 * @param gameId 游戏id
	 * @param memberId 用户id
	 * @param ptype 平台类型 iOS Android
	 * @return
	 * @throws Exception
	 */
	public ResultDto<GameOut> getGameDetail(String gameId, String memberId, String ptype) throws Exception;

	ResultDto<GameOut> getGameDetailByNumber(String gameNumber,String memberId,String ptype);
	/**
	 * 游戏列表
	 * @param gameInputDto
	 * @param memberId
	 * @param os 平台类型 iOS Android
	 * @return
	 */
	public FungoPageResultDto<GameOutPage> getGameList(GameInputPageDto gameInputDto, String memberId, String os);
	
	/**
	 * 官方分类标签
	 * @return
	 */
	public ResultDto<List<TagOutPage>> getGameTags();

	/**
	 * 用户最近评论的游戏
	 * @param userId
	 * @param input
	 * @return
	 */
	public FungoPageResultDto<GameOutPage> recentEvaluatedGamesByMember(String userId, InputPageDto input);

	/**
	 * 游戏合集下的游戏内容
	 * @param memberId
	 * @param input
	 * @param os
	 * @return
	 */
	public FungoPageResultDto<GameItem> getGameItems(String memberId, GameItemInput input, String os);

	/**
	 * 游戏综合评分
	 * @param gameId
	 * @return
	 */
	double getGameRating(String gameId);

	/**
	 * 游戏列表
	 * @param input
	 * @param memberId
	 * @return
	 */
	FungoPageResultDto<GameOutBean> getGameList(GameListVO input, String memberId);

	/**
	 * 根据表名(动态)修改
	 * @param map
	 * @return
	 */
	Boolean updateCountor(Map<String, String> map);

	/**
	 * 被点赞用户的id
	 * @param map
	 * @return
	 */
    String getMemberIdByTargetId(Map<String, String> map);

	/**
	 * 我的游戏列表
	 * @param loginId
	 * @param inputPage
	 * @param os
	 * @return
	 */
	FungoPageResultDto<MyGameBean> getMyGameList(String loginId, MyGameInputPageDto inputPage, String os);

	/**
	 * 搜索游戏
	 * @param page
	 * @param limit
	 * @param keyword
	 * @param tag
	 * @param sort
	 * @param os
	 * @param memberId
	 * @return
	 */
    FungoPageResultDto<GameSearchOut> searchGames(int page, int limit, String keyword, String tag, String sort, String os, String memberId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

	/**
	 * 搜索游戏
	 * @param keyword
	 * @return
	 */
	FungoPageResultDto<GameSearchOut> searchGamesCount(String keyword) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

	/**
	 * 搜索游戏
	 *
	 */
	FungoPageResultDto<String> searchGamesKeyword(int page, int limit, String keyword, String tag, String sort, String os, String memberId);
	/**
	 * 根据游戏ID获取游戏标签列表
	 * @param gameId
	 * @param userId
	 * @return
	 */
    ResultDto<List> getGameTagList(String gameId, String userId);

	/**
	 * 新增游戏标签
	 * @param idList
	 * @param userId
	 * @param gameId
	 * @return
	 */
	ResultDto<String> addGameTag(String[] idList, String userId, String gameId);

	/**
	 * 发表游戏态度
	 * @param userId
	 * @param tagRelId
	 * @param attitude
	 * @return
	 */
	ResultDto<String> addGameTagAttitude(String userId, String tagRelId, Integer attitude);

	/**
	 * 获取热门游戏标签
	 * @return
	 */
	ResultDto<List> getHotTagList();

	/**
	 * 获取游戏预选标签
	 * @param memberUserPrefile
	 * @param tagInput
	 * @return
	 */
	ResultDto<TagSelectOut> getSelectTagList(MemberUserProfile memberUserPrefile, TagInput tagInput);

	/**
	 * 根据游戏id集合获取FungoPageResultDto<GameOutBean>
	 * @param input
	 * @return
	 */
    FungoPageResultDto<GameOutBean> getGameList1(GameItemInput input);

    ResultDto<List<GameOutPage>> viewGames(String memberId);

    ResultDto<List<GameOut>> listGameByids(String gameIds);


	FungoPageResultDto<GameOut> pageGameByids(String gameIds);

	FungoPageResultDto<GameKuDto> listGameByTags(String memberId, TagGameDto tagGameDto);

    FungoPageResultDto<GameKuDto> listGameByBang(String memberId, BangGameDto sortType);

	FungoPageResultDto<GameKuDto> listGameByStatus(String memberId, BangGameDto sortType);

	FungoPageResultDto<GameOutBean> listGameByPackageName( BangGameDto sortType);
}

