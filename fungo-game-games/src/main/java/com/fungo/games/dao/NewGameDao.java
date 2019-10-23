package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.games.entity.NewGame;
import com.game.common.bean.NewGameBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 游戏标签 Mapper 接口
 * </p>
 *
 * @author Carlos
 * @since 2018-05-09
 */
@Repository
public interface NewGameDao extends BaseMapper<NewGame> {

    //查询新游信息
    public List<NewGameBean> getNewGameAll(NewGameBean bean);


    /**
     * 新游信息查看往期
     * @param bean
     * @return
     */
    public List<NewGameBean> queryOldGame(NewGameBean bean);
}