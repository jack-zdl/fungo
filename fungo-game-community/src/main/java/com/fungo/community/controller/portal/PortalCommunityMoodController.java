package com.fungo.community.controller.portal;

import com.fungo.community.service.IMoodService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.MoodBean;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  PC2.0心情
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/31 15:46
 */
@RestController
@Api(value="",description="PC2.0心情")
public class PortalCommunityMoodController {

    @Autowired
    private IMoodService moodService;

    @ApiOperation(value="PC2.0获取心情", notes="")
    @RequestMapping(value="/api/portal/community/content/mood/{moodId}", method= RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "moodId",value = "心情id",paramType = "path",dataType = "string")
    })
    public ResultDto<MoodBean> getMood(@Anonymous MemberUserProfile memberUserPrefile, @PathVariable("moodId") String moodId) throws Exception {
        String memberId="";
        if(memberUserPrefile!=null) {
            memberId=memberUserPrefile.getLoginId();
        }
        return this.moodService.getMood(memberId, moodId);
    }
}
