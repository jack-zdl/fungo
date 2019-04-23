package com.game.common.log;

import com.game.common.dto.WebLog;

public interface IRequestLogger {
	public void log(WebLog log);
}
