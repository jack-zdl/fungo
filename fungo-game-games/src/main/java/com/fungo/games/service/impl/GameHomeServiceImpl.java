package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.fungo.games.dao.FindCollectionGroupDao;
import com.fungo.games.dao.NewGameDao;
import com.fungo.games.entity.FindCollectionGroup;
import com.fungo.games.entity.HomePage;
import com.fungo.games.service.*;
import com.game.common.api.InputPageDto;
import com.game.common.bean.AdminCollectionGroup;
import com.game.common.bean.CollectionItemBean;
import com.game.common.bean.NewGameBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.repo.cache.facade.FungoCacheIndex;
import com.game.common.vo.AdminCollectionVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * <p></p>
 *
 * @Author: Carlos
 * @Date: 2019/7/29
 */
@Service
public class GameHomeServiceImpl implements GameHomeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHomeService.class);


    @Autowired
    private FungoCacheIndex fungoCacheIndex;
    @Autowired
    private HomePageService homePageService;
    @Autowired
    private NewGameDao newGameDao;
    @Autowired
    private FindCollectionGroupService findCollectionGroupService;
    @Autowired
    private FindCollectionItemService findCollectionItemService;
    @Autowired
    private FindCollectionGroupDao findCollectionGroupDao;



    /**
     * 首页显示内容
     * @return
     */
    @Override
    public FungoPageResultDto<HomePage> queryHomePage() {
        FungoPageResultDto<HomePage> re = new FungoPageResultDto<HomePage>();
        //查询有制顶标识的数据
        List<HomePage> topList =  homePageService.selectList(new EntityWrapper<HomePage>().eq("state",3));
        //查询正常的数据，并按修改时间排序
        EntityWrapper<HomePage> entityWrapper = new EntityWrapper<HomePage>();
        entityWrapper.eq("state",0);
        entityWrapper.orderBy("updated_at",false);
        List<HomePage> list =  homePageService.selectList(entityWrapper);
        //排序规则未开发***********************************************
        if(!topList.isEmpty()){

        }
        re.setData(list);
        return re;
    }



    /**
     * 新游信息查询
     * @return
     */
    @Override
    public ResultDto<List<NewGameBean>> queryNewGame() {
        //查询选择日期字段在今天开始往后30天内的所有数据，先根据时间排序，在对每天内的排序号(sort)进行排序
        ResultDto<List<NewGameBean>> re = new ResultDto<List<NewGameBean>>();
//        Date date=new Date();//此时date为当前的时间
//        SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //查询有制顶标识的数据
        NewGameBean bean = new NewGameBean();
        bean.setChooseDate(calendar.getTime());
        //查询有制顶标识的数据
        List<NewGameBean> list = newGameDao.getNewGameAll(bean);
        for(NewGameBean newGameBean:list){
            if(StringUtils.isNotBlank(newGameBean.getTags())){
                List<String> labels = Arrays.asList(newGameBean.getTags().split(","));
                if(!labels.isEmpty()){
                    newGameBean.setLabels(labels);
                }
            }
            if(null!=newGameBean.getChooseDate()){
                newGameBean.setTime(newGameBean.getChooseDate().getTime());
            }
        }
        re.setData(list);
        return re;
    }




    /**
     * 查看往期新游信息
     * @return
     */
    @Override
    public ResultDto<List<NewGameBean>> queryOldGame(InputPageDto inputPageDto) {
        //查询选择日期字段在今天开始往后30天内的所有数据，先根据时间排序，在对每天内的排序号(sort)进行排序
        ResultDto<List<NewGameBean>> re = new ResultDto<List<NewGameBean>>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //查询分页列表startOffset
        int startOffset = (inputPageDto.getPage() - 1) * inputPageDto.getLimit();
        //查询有制顶标识的数据
        NewGameBean bean = new NewGameBean();
        bean.setStartOffset(startOffset);
        bean.setChooseDate(calendar.getTime());
        bean.setPageSize(inputPageDto.getLimit());
        List<NewGameBean> list = newGameDao.queryOldGame(bean);
        for(NewGameBean newGameBean:list){
            if(StringUtils.isNotBlank(newGameBean.getTags())){
                List<String> labels = Arrays.asList(newGameBean.getTags().split(","));
                if(!labels.isEmpty()){
                    newGameBean.setLabels(labels);
                }
            }
            if(null!=newGameBean.getChooseDate()){
                newGameBean.setTime(newGameBean.getChooseDate().getTime());
            }
        }
        re.setData(list);
        return re;
    }




    /**
     * 合集信息查询
     * @return
     */
    @Override
    @Transactional
    public ResultDto<List<AdminCollectionGroup>> queryCollectionGroup(AdminCollectionVo input) {
        ResultDto<List<AdminCollectionGroup>> re = new ResultDto<List<AdminCollectionGroup>>();
        List<AdminCollectionGroup> colLists = new ArrayList<>();
        Wrapper<FindCollectionGroup> wrapper = new EntityWrapper<FindCollectionGroup>();
        if(StringUtils.isNotBlank(input.getId())){
            wrapper.eq("id", input.getId());
        }
        wrapper.eq("state", 0);
        wrapper.eq("is_online", 0);
        wrapper.orderBy("sort",false);
        List<FindCollectionGroup> collectionList = findCollectionGroupService.selectList(wrapper);
        if(!collectionList.isEmpty()){
            AdminCollectionGroup adminCollectionGroup = null;
            for(FindCollectionGroup findCollectionGroup : collectionList){
                adminCollectionGroup = new AdminCollectionGroup();
                adminCollectionGroup.setCoverPicture(findCollectionGroup.getCoverPicture());
                adminCollectionGroup.setSort(findCollectionGroup.getSort());
                adminCollectionGroup.setName(findCollectionGroup.getName());
                adminCollectionGroup.setDetail(findCollectionGroup.getDetail());
                adminCollectionGroup.setId(findCollectionGroup.getId());
                List<CollectionItemBean> itemList = findCollectionGroupDao.getCollectionItemAll(findCollectionGroup.getId());
                for(CollectionItemBean collectionItemBean:itemList){
                    if(StringUtils.isNotBlank(collectionItemBean.getTags())){
                        collectionItemBean.setLabels(Arrays.asList(collectionItemBean.getTags().split(",")));
                    }
                }
                adminCollectionGroup.setList(itemList);
                colLists.add(adminCollectionGroup);
            }
        }
        re.setData(colLists);
        return re;
    }




}
