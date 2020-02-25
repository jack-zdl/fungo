package com.fungo.system.controller.portal;

import com.fungo.system.dto.VodIntroInput;
import com.fungo.system.service.IFGoGameApiAliVodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "", description = "视频点播")
public class PortalSystemVideoController {

    private static final Logger LOGGER = LoggerFactory.getLogger( PortalSystemVideoController.class);

    @Autowired
    private IFGoGameApiAliVodService ifGoGameApiAliVodService;

    /**
     * 从阿里云获取上传视频凭证
     *
     * @param vodIntroInput
     * @return
     */
    @ApiOperation(value = "阿里云视频点播-获取和失败刷新上传地址和凭证", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoId", value = "视频id，若是刷新视频上传凭证,  必选", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "vdFileName", value = "视频文件名称(包含文件后缀名) 必选", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "vdTitle", value = "视频标题  必选", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "vdTags", value = "视频标签 可选 ,多个标签中间用,逗号分隔", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "vdDesc", value = "视频描述 可选", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "cateId", value = "视频分类ID,可选", paramType = "form", dataType = "string")
    })
    @RequestMapping(value = "/api/portal/system/vd/ali/auth", method = RequestMethod.POST)
    public String getUploadAuthFromAliVod(@RequestBody VodIntroInput vodIntroInput) {
        return ifGoGameApiAliVodService.getUploadAuth(vodIntroInput);
    }

}
