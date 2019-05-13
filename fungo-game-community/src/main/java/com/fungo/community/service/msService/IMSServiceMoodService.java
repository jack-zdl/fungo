package com.fungo.community.service.msService;


import com.game.common.dto.community.MooMoodDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 *      社区-心情微服务业务层
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
public interface IMSServiceMoodService {


    /**
     * 查询社区心情数据列表
     * @return
     */
    public List<MooMoodDto> queryCmmMoodList(@RequestBody MooMoodDto mooMoodDto);

}
