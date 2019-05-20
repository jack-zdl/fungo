package com.fungo.games.service;



import com.game.common.dto.DeveloperGame.DeveloperGamePageInput;
import com.game.common.dto.DeveloperGame.DeveloperQueryIn;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.game.GameHistoryOut;
import com.game.common.dto.game.GameReleaseLogDto;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IMQService {
	/**
	 * 游戏更新
	 * @param gameDto1
	 */
	boolean mqGameUpdate(GameDto gameDto1);

	boolean mqGameInsert(GameDto gameDto1);

	boolean mqGameReleaseLogInsert(GameReleaseLogDto gameReleaseLogDto);

	boolean mqCounterUpdate(Map map);

    boolean mqAddGametag(Map map);

    boolean mqGameEvaluationInsert(GameEvaluationDto gameEvaluationDto);

	boolean mqGameEvaluationUpdate(GameEvaluationDto gameEvaluationDto);
}
