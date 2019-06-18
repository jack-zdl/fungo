package com.fungo.community.service.msService;


import com.game.common.bean.CollectionBean;
import com.game.common.dto.community.CmmPostDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *      社区-帖子|文章微服务业务层
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
public interface IMSServicePostService {


    /**
     * 查询社区帖子|文章数据列表
     * @return
     */
    public List<CmmPostDto> queryCmmPostList(@RequestBody CmmPostDto postDto);



    /**
     * 精品帖子数大于2的用户
     * @return
     */
    public List<Map> getHonorQualificationOfEssencePost();


    /**
     * 查询 社区帖子总数
     * @return
     */
    public Integer queryCmmPostCount(CmmPostDto postDto);



    /**
     *  查询 社区置顶文章集合
     * @param cmmPostDto
     * @return
     */
    List<CmmPostDto> listCmmPostTopicPost(CmmPostDto cmmPostDto);


    /**
     * 获取我的收藏（文章）
     * @param pageNum
     * @param limit
     * @param  postIds
     * @return
     */
    public List<CollectionBean> getCollection(int pageNum,int limit, List<String> postIds);

    /**
     * PC2.0新增浏览量 根据跟用户ID获取文章的浏览量
     * @param cardId
     * @return
     */
    Integer getPostBoomWatchNumByCardId(String cardId);
}
