package com.fungo.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.entity.Member;
import com.fungo.system.entity.ScoreLog;
import com.fungo.system.entity.ScoreRule;
import com.game.common.dto.ResultDto;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 评分日志 服务类
 * </p>
 *
 * @author lzh
 * @since 2018-05-07
 */
public interface ScoreLogService extends IService<ScoreLog> {
	
	/**
	 * 任务完成 (旧版,已弃用)
	 * @param userId
	 * @param code
	 * @param object
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public ResultDto<String> finishTask(String userId, String code, Object object) throws IOException, ParseException;

	/**
	 * 更新等级
	 * @param user
	 * @return
	 */
	public Member updateLevel(Member user);

	/**
	 * 完成fun币任务
	 * @param userId
	 * @param icode 任务code_idt
	 * @param object
	 * @param target_id 目标id
	 * @param target_type 目标类型
	 * @return
	 * @throws Exception
	 */
	public ResultDto<Integer> funCoinTask(String userId, int icode, Object object, String target_id, int target_type) throws Exception;

	/**
	 * 完成经验值任务
	 * @param userId
	 * @param icode 任务code_idt
	 * @param object
	 * @param target_id 目标id
	 * @param target_type 目标类型

	 * @return Integer 用户完成任务该的次数()
	 * @throws Exception
	 */
	public ResultDto<Integer> expTask(String userId, int icode, Object object, String target_id, int target_type) throws Exception;

	/**
	 * 根据经验值判断用户等级
	 * @param exp
	 * @return
	 */
	public int getLevel(int exp);

	/**
	 * 更新用户任务记录
	 * @param userId
	 * @param expRule 当前任务
	 * @param mapper
	 * @return
	 * @throws Exception
	 */
	public int updateTasked(String userId, ScoreRule expRule, ObjectMapper mapper) throws Exception;

	/**
	 * 更新任务日志
	 * @param userId
	 * @param object
	 * @param expRule 当前完成的任务
	 * @param score
	 * @param mapper
	 * @param target_id
	 * @param target_type
	 * @throws Exception
	 */
	public void addTaskLog(String userId, Object object, ScoreRule expRule, Integer score, ObjectMapper mapper, String target_id, int target_type)
			throws Exception ;

	/**
	 * 获取荣誉徽章
	 * @param userId
	 * @param mapper
	 * @param idt 荣誉idt
	 * @throws Exception
	 */
	void updateRanked(String userId, ObjectMapper mapper, int idt) throws Exception;


	void updateRanked(String userId, ObjectMapper mapper, int idt, Date createdAt) throws Exception;

	/**
	 * 荣誉徽章日志
	 * @param userId 用户id
	 * @param rankRule 当前获取的徽章
	 */
	void addRankLog(String userId, IncentRuleRank rankRule);


	/**
	 * 根据任务的属性获取用户完成该任务的日志记录
	 * @param mb_id 用户ID
	 * @param task_type 任务类型
	 * @param task_code_idt 任务编码
	 * @return
	 * @throws Exception
	 */
	public List<ScoreLog> getScoreLogWithMbAndTask(String mb_id, int task_type, int task_code_idt) throws Exception ;


	/**
	 * 根据任务的属性和完成的时间范围获取用户完成该任务的日志记录
	 * @param mb_id
	 * @param task_type
	 * @param task_code_idt
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<ScoreLog> getScoreLogWithMbAndTask(String mb_id, int task_type, int task_code_idt, String startDate, String endDate) throws Exception ;


	/**
	 * 点赞勋章
	 * @return
	 * @throws Exception
	 */
	public void updateRankedMedal(String userId ,int rankidt) throws IOException;


}
