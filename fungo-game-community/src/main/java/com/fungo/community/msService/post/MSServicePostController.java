package com.fungo.community.msService.post;


import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmPostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *      社区-帖子|文章微服务接口
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
@RestController
public class MSServicePostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServicePostController.class);

    /**
     * 查询社区帖子|文章数据
     * @return
     */
    @PostMapping("/ms/service/cmm/posts")
    public ResultDto<String> queryCmmPostList(@RequestBody CmmPostDto param) {


        return ResultDto.error("-1", "添加商品数据失败");
    }


}
