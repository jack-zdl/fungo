package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.games.dao.BasTagDao;
import com.fungo.games.dao.GameCollectionItemDao;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameCollectionGroup;
import com.fungo.games.entity.GameCollectionItem;
import com.fungo.games.service.GameCollectionGroupService;
import com.fungo.games.service.GameCollectionItemService;
import com.fungo.games.service.GameService;
import com.game.common.bean.TagBean;
import com.game.common.dto.index.CardDataBean;
import com.game.common.dto.index.CardIndexBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 游戏合集项 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-06-28
 */
@Service
public class GameCollectionItemServiceImap extends ServiceImpl<GameCollectionItemDao, GameCollectionItem> implements GameCollectionItemService {
    @Autowired
    private GameCollectionGroupService gameCollectionGroupService;
    @Autowired
    private GameService gameService;
    @Autowired
    private BasTagDao tagDao;


    public CardIndexBean selectedGames() {
        CardIndexBean indexBean = new CardIndexBean();
        GameCollectionGroup co = gameCollectionGroupService.selectOne(new EntityWrapper<GameCollectionGroup>().eq("state", "0").orderBy("RAND()").last("limit 1"));
        List<GameCollectionItem> ilist = new ArrayList<>();
        if (co != null) {
            ilist = this.selectList(new EntityWrapper<GameCollectionItem>().eq("group_id", co.getId()).eq("show_state", "1").last("limit 3").orderBy("sort", false));
        }
        if (ilist.size() == 0) {
            return null;
        }
        ArrayList<CardDataBean> gameDateList = new ArrayList<>();
        for (GameCollectionItem gameCollectionItem : ilist) {
            CardDataBean bean = new CardDataBean();
            Game game = this.gameService.selectById(gameCollectionItem.getGameId());
            bean.setMainTitle(game.getName());
            String intro = game.getIntro();
            bean.setSubtitle(intro.length() > 25 ? intro.substring(0, 25) : intro);
            bean.setImageUrl(game.getIcon());
            List<TagBean> tags = tagDao.getSortTags(game.getId());
            String tag = "";
            if (tags.size() > 0) {
                for (TagBean tagBean : tags) {
                    tag += tagBean.getName() + " ";
                }
            }
            bean.setLowerLeftCorner(tag);
            bean.setActionType("1");
            bean.setTargetId(game.getId());
            bean.setTargetType(3);
            gameDateList.add(bean);
        }
        indexBean.setCardName("大家都在玩");
        indexBean.setCardType(7);
        indexBean.setOrder(8);
        indexBean.setDataList(gameDateList);
        indexBean.setSize(gameDateList.size());
        indexBean.setUprightFlag(true);
        return indexBean;
    }
}
