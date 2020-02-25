package com.fungo.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.dto.AppConfigGetInput;
import com.fungo.system.entity.SysVersion;
import com.fungo.system.entity.SysVersionChannel;
import com.fungo.system.service.ICommonService;
import com.fungo.system.service.SysVersionChannelService;
import com.fungo.system.service.SysVersionService;
import com.game.common.dto.*;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共基础接口
 * @author sam
 *
 */
@RestController
@Api(value = "")
public class CommonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ICommonService commonService;
    @Autowired
    private SysVersionService versionService;
    @Autowired
    private SysVersionChannelService channelService;

    @ApiOperation(value = "举报/反馈类型", notes = "")
    @RequestMapping(value = "/api/report/category", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<List<String>> getReportType(HttpServletRequest request) {
        String type = request.getParameter("type");
        return commonService.getReportType(type);
    }

    @ApiOperation(value = "app配置信息", notes = "")
    @RequestMapping(value = "/api/config", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<Map<String, Boolean>> getAppConfig() {
        ResultDto<Map<String, Boolean>> re = new ResultDto<>();
        Map<String, Boolean> map = new HashMap<>();
        SysVersion sysVersion = versionService.selectOne(new EntityWrapper<SysVersion>().eq("new_version", 1));
        map.put("game_download_switch", sysVersion.getGameDownloadSwitch() == 1 ? true : false);
        map.put("index_banner_switch", sysVersion.getIndexBannerSwitch() == 1 ? true : false);
        re.setData(map);
        return re;
    }

    @ApiOperation(value = "app配置信息(2.4渠道)", notes = "")
    @RequestMapping(value = "/api/config", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<Map<String, Boolean>> getAndroidAppConfig(@RequestBody AppConfigGetInput input, HttpServletRequest request) {
        String os = (String) request.getAttribute("os");
        ResultDto<Map<String, Boolean>> re = new ResultDto<>();
        Map<String, Boolean> map = new HashMap<>();
        SysVersion sysVersion = null;
        //查询对应版本
        if (os.equalsIgnoreCase("Android")) {
            sysVersion = versionService.selectOne(new EntityWrapper<SysVersion>().eq("version", input.getVersion()).eq("mobile_type", "Android"));
        } else if (os.equalsIgnoreCase("iOS")) {
            sysVersion = versionService.selectOne(new EntityWrapper<SysVersion>().eq("version", input.getVersion()).eq("mobile_type", "IOS"));
        } else {
            return ResultDto.error("-1", "请求只能由app端调用");
        }
        if (sysVersion == null) {
            return ResultDto.error("-1", "找不到对应版本号");
        }
        //查询对应渠道
        SysVersionChannel channelVersion = channelService.selectOne(new EntityWrapper<SysVersionChannel>().eq("channel_code", input.getChannelCode()).eq("version_id", sysVersion.getId()));
        if (channelVersion == null) {
            return ResultDto.error("-1", "找不到对应版本的目标渠道");
        }
        map.put("game_download_switch",sysVersion.getGameDownloadSwitch() == 1 ? true:  channelVersion.getGameDownloadSwitch() == 1 ? true : false);
        map.put("index_banner_switch", sysVersion.getIndexBannerSwitch() == 1 ? true: channelVersion.getIndexBannerSwitch() == 1 ? true : false);
        //返回是否登录授权字段
        map.put("is_auth", sysVersion.getIsAuth() == 1 ? true : false);
        re.setData(map);
        return re;
    }

    @ApiOperation(value = "用户反馈", notes = "")
    @RequestMapping(value = "/api/mine/feedback", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "content", value = "内容", paramType = "form", dataType = "string")
    })
    public ResultDto<String> addFeedback(MemberUserProfile memberUserPrefile, @RequestBody FeedbackBean feedBack) throws Exception {
        return commonService.feedback(memberUserPrefile.getLoginId(), feedBack);
    }

    @ApiOperation(value = "检查更新(安卓)", notes = "")
    @RequestMapping(value = "/api/checkupdate", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<Map<String, Object>> checkupdate(@Anonymous MemberUserProfile memberUserPrefile) {
        ResultDto<Map<String, Object>> re = new ResultDto<>();
        Map<String, Object> map = new HashMap<>();
        SysVersion sysVersion = versionService.selectOne(new EntityWrapper<SysVersion>().eq("mobile_type", "Android").eq("new_version", 1));
        map.put("code", sysVersion.getCode());
        map.put("name", sysVersion.getVersion());
        map.put("content", sysVersion.getIntro());
        map.put("url", sysVersion.getApkUrl());
        //2.4之前 强制更新
        map.put("is_force", true);
        re.setData(map);
        return re;
    }

    @ApiOperation(value = "检查更新(安卓2.4之后)", notes = "")
    @RequestMapping(value = "/api/checkappupdate", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<Map<String, Object>> checkAppUpdate(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody VersionInput input) {
        ResultDto<Map<String, Object>> re = new ResultDto<>();
        //客户端当前版本
        String currentVersion = input.getCurrentVersion();
        //查看当前版本(如查找不到,直接强制升级)
        SysVersion version = versionService.selectOne(new EntityWrapper<SysVersion>().eq("version", currentVersion).eq("mobile_type", "Android"));
        SysVersion lastNewVersion = versionService.selectOne(new EntityWrapper<SysVersion>().eq("new_version", 1).eq("mobile_type", "Android"));
        Map<String, Object> map = new HashMap<>();
        if (null != lastNewVersion) {
            map.put("code", lastNewVersion.getCode());
            map.put("versionName", lastNewVersion.getVersion());
            map.put("content", lastNewVersion.getIntro());
            map.put("url", lastNewVersion.getApkUrl());
            map.put("is_force", false);
        }
        //验证当前版本与最新版本是否是同一个版本
        if (null != lastNewVersion) {
            if (null != version && null != lastNewVersion) {
                if (!StringUtils.equalsIgnoreCase(version.getCode(), lastNewVersion.getCode()) && !StringUtils.equalsIgnoreCase(version.getVersion(),
                        lastNewVersion.getVersion())) {
                    try {
                        int c_code = Integer.parseInt(version.getCode());
                        int new_code = Integer.parseInt(lastNewVersion.getCode());
                        if(lastNewVersion.getIsForce() == null ){
                            map.put("versionName", input.getCurrentVersion());
                        }else if (new_code > c_code && 1 == lastNewVersion.getIsForce()) {
                            map.put("is_force", true);
                        }
                    } catch (Exception ex) {
                        LOGGER.error( "检查更新异常",ex );
                    }
                }
            }
        }
        re.setData(map);
        return re;
    }

    @ApiOperation(value = "ios检查更新(2.4.3)", notes = "")
    @RequestMapping(value = "/api/checkiosupdate", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<Map<String, Object>> checkIOSUpdate(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody VersionInput input) {
        ResultDto<Map<String, Object>> re = new ResultDto<>();
        String currentVersion = input.getCurrentVersion();
        //查看当前版本(如查找不到,直接强制升级)
        SysVersion version = versionService.selectOne(new EntityWrapper<SysVersion>().eq("version", currentVersion).eq("mobile_type", "IOS"));
        SysVersion lastNewVersion = versionService.selectOne(new EntityWrapper<SysVersion>().eq("new_version", 1).eq("mobile_type", "IOS"));
        Map<String, Object> map = new HashMap<>();
        if (null != lastNewVersion) {
            map.put("code", lastNewVersion.getCode());
            map.put("versionName", lastNewVersion.getVersion());
            map.put("content", lastNewVersion.getIntro());
            map.put("url", lastNewVersion.getApkUrl());
            map.put("is_force", false);
        }
        if (null != lastNewVersion) {
            if (null != version && null != lastNewVersion) {
                if (!StringUtils.equalsIgnoreCase(version.getCode(), lastNewVersion.getCode()) && !StringUtils.equalsIgnoreCase(version.getVersion(),
                        lastNewVersion.getVersion())) {
                    try {
                        int c_code = Integer.parseInt(version.getCode());
                        int new_code = Integer.parseInt(lastNewVersion.getCode());
                        if (new_code > c_code) {
                            if(lastNewVersion.getIsForce() == null ) {
                                map.put( "versionName", input.getCurrentVersion() );
                            }else {
                                //查询新版本对应的渠道更新机制
                                SysVersionChannel sysVersionChannel = channelService.selectOne(new EntityWrapper<SysVersionChannel>().eq("version_id", lastNewVersion.getId()).eq("channel_code", input.getChannelCode()));
                                if (null != sysVersionChannel) {
                                    Integer isForce = sysVersionChannel.getIsForce();
                                    if (1 == isForce) {
                                        map.put("is_force", true);
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                       LOGGER.error( "ios检查更新",ex );
                    }
                }
            }
        }
        re.setData(map);
        return re;
    }

    @ApiOperation(value = "关于我们", notes = "")
    @RequestMapping(value = "/api/about", method = RequestMethod.GET)
    public ResultDto<Map<String, String>> getTaskHelp(@Anonymous MemberUserProfile memberUserPrefile) {
        ResultDto<Map<String, String>> re = new ResultDto<>();
        Map<String, String> map = new HashMap<>();
        map.put("url", "http://static.fungoweb.com/static/site/pages/about_me.html");
        re.setData(map);
        return re;
    }

    @ApiOperation(value = "游戏评价打星说明 (v 2.4)", notes = "")
    @RequestMapping(value = "/api/starInfo", method = RequestMethod.GET)
    public ResultDto<ArrayList<StartInfo>> getStarInfo(@Anonymous MemberUserProfile memberUserPrefile) {
//		ArrayList<StartInfo> list = (ArrayList<StartInfo>) GuavaCache.get("startInfo");
        ArrayList<StartInfo> list = new ArrayList<>();
        list.add(new StartInfo("不会再玩", 1));
        list.add(new StartInfo("很烂", 2));
        list.add(new StartInfo("极差", 3));
        list.add(new StartInfo("较差", 4));
        list.add(new StartInfo("有待观察", 5));
        list.add(new StartInfo("可以尝试", 6));
        list.add(new StartInfo("不错", 7));
        list.add(new StartInfo("好玩", 8));
        list.add(new StartInfo("推荐下载", 9));
        list.add(new StartInfo("强烈安利", 10));
        return ResultDto.success(list);
    }

    @ApiOperation(value = "游戏评价特征说明(v 2.4)", notes = "")
    @RequestMapping(value = "/api/trait", method = RequestMethod.GET)
    public ResultDto<ArrayList<Trait>> gettraitInfo(@Anonymous MemberUserProfile memberUserPrefile) {
        ArrayList<Trait> list = new ArrayList<>();
        list.add(new Trait("trait1", "画面"));
        list.add(new Trait("trait2", "音乐"));
        list.add(new Trait("trait3", "氪金"));
        list.add(new Trait("trait4", "剧情"));
        list.add(new Trait("trait5", "玩法"));
        return ResultDto.success(list);
    }

    @ApiOperation(value = "辅助工具(v 2.4.3)", notes = "")
    @RequestMapping(value = "/api/common/tools", method = RequestMethod.GET)
    public FungoPageResultDto<AidTool> getTools() {
        return null;
    }
}