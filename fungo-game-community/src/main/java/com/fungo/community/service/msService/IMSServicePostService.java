package com.fungo.community.service.msService;


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
    public Integer queryCmmPostCount(@RequestBody CmmPostDto postDto);
}
