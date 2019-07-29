package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.BasLog;
import com.game.common.bean.HotValue;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  * 接口前台访问日志 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-07-09
 */
@Repository
public interface BasLogDao extends BaseMapper<BasLog> {

    //获取一天内访问数前十的游戏
    public List<HotValue> getHotValue();

    boolean insertLogic(BasLog basLog);
}