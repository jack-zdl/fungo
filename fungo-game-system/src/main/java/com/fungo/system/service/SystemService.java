package com.fungo.system.service;

import com.game.common.bean.TagBean;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.BasTagGroupDto;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.IncentRuleRankDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
import com.game.common.vo.MemberFollowerVo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>系统微服务对外服务层</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/10
 */
public interface SystemService {

    FungoPageResultDto<String> getFollowerUserId(String memberId);

    FungoPageResultDto<MemberFollowerDto> getMemberFollowerList(MemberFollowerVo vo);

    FungoPageResultDto<MemberDto> getMemberDtoList(MemberDto memberDto);

    FungoPageResultDto<IncentRankedDto> getIncentRankedList(IncentRankedDto incentRankedDto);

    ResultDto<String> changeMemberLevel(MemberDto memberDto);

    ResultDto<String> decMemberExp(MemberDto memberDto);

    ResultDto<List<String>> listFollowerCommunityId(String memberId);

    ResultDto<Integer> countActionNum(BasActionDto basActionDto);

    ResultDto<List<String>> listtargetId(BasActionDto basActionDto);

    public ResultDto<String> addAction(BasActionDto basActionDto);

    ResultDto<List<MemberDto>> listMembersByids(List<String> ids, Integer state);

    ResultDto<List<IncentRankedDto>> listIncentrankeByids(List<String> ids, Integer rankType);

    ResultDto<Map<String, Object>> exTask(TaskDto taskDto);

    ResultDto<AuthorBean> getAuthor(String memberId);

    ResultDto<AuthorBean> getUserCard(String cardId, String memberId);

    ResultDto<String> updateActionUpdatedAtByCondition(Map<String,Object> map);

    ResultDto<MemberDto> getMembersByid(String id);

    ResultDto<List<HashMap<String,Object>>> getStatusImage(String memberId);

    ResultDto<List<BasTagDto>> listBasTags(List<String> collect);

    ResultDto<List<MemberDto>> listWatchMebmber(Integer limit, String currentMbId) throws IOException, ClassNotFoundException;

    ResultDto<List<MemberDto>> listRecommendedMebmber(Integer limit,String currentMbId,List<String> wathMbsSet);

    /**
     * 处理用户经验变更服务
     * @param userId 用户id
     * @param changeScore 要变更的经验/积分
     * @return 操作结果
     */
    public ResultDto<String>  processUserScoreChange(String userId,Integer changeScore);

    ResultDto<IncentRuleRankDto> getIncentRuleRankById(String id);

    ResultDto<List<BasActionDto>> listActionByCondition(BasActionDto basActionDto);

    ResultDto<MemberFollowerDto> getMemberFollower1(MemberFollowerDto memberFollowerDto);

    ResultDto<Integer> countActionNumGameUse(BasActionDto basActionDto);

    ResultDto<List<BasTagDto>> listBasTagByGroup(String groupId);

    ResultDto<BasTagDto> getBasTagById(String id);

    ResultDto<List<BasTagGroupDto>> listBasTagGroupByCondition(BasTagGroupDto basTagGroupDto);

    ResultDto<Integer> countSerchUserName(String keyword);

    ResultDto<List<TagBean>> listSortTags(List<String> gameId);
}
