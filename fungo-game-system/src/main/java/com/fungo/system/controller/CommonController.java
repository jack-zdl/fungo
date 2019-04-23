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
@Api(value = "", description = "公共基础接口")
public class CommonController {
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
        ResultDto<Map<String, Boolean>> re = new ResultDto<Map<String, Boolean>>();
        Map<String, Boolean> map = new HashMap<String, Boolean>();
//		map.put("game_download_switch", true);
//		map.put("index_banner_switch", true);
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

        ResultDto<Map<String, Boolean>> re = new ResultDto<Map<String, Boolean>>();
        Map<String, Boolean> map = new HashMap<String, Boolean>();
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
        }else {
        	
        }
        
        map.put("game_download_switch", channelVersion.getGameDownloadSwitch() == 1 ? true : false);
        map.put("index_banner_switch", channelVersion.getIndexBannerSwitch() == 1 ? true : false);
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
        ResultDto<Map<String, Object>> re = new ResultDto<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
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
        ResultDto<Map<String, Object>> re = new ResultDto<Map<String, Object>>();
        String currentVersion = input.getCurrentVersion();

        //查看当前版本(如查找不到,直接强生)
        SysVersion version = versionService.selectOne(new EntityWrapper<SysVersion>().eq("version", currentVersion).eq("mobile_type", "Android"));

        SysVersion lastNewVersion = versionService.selectOne(new EntityWrapper<SysVersion>().eq("new_version", 1).eq("mobile_type", "Android"));
        //验证当前版本与最新版本是否是同一个版本
        if (null != lastNewVersion) {
            if (null != version && null != lastNewVersion) {

                if (!StringUtils.equalsIgnoreCase(version.getCode(), lastNewVersion.getCode()) && !StringUtils.equalsIgnoreCase(version.getVersion(),
                        lastNewVersion.getVersion()) ) {

                    try {

                        int c_code = Integer.parseInt(version.getCode());
                        int new_code = Integer.parseInt(lastNewVersion.getCode());
                        if (new_code > c_code  && 1 == lastNewVersion.getIsForce()) {
                            version.setIsForce(1);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        SysVersion sysVersion = null;
        if (version == null) {//找不到当前版本,自动查找最新版本
            sysVersion = versionService.selectOne(new EntityWrapper<SysVersion>().eq("mobile_type", "Android").eq("new_version", 1));
//          channelService.selectOne(new EntityWrapper<SysVersionChannel>().eq("version_id",sysVersion.getId()));
            map.put("code", sysVersion.getCode());
            map.put("versionName", sysVersion.getVersion());
            map.put("content", sysVersion.getIntro());
            map.put("url", sysVersion.getApkUrl());
            map.put("is_force", true);
//            map.put("game_download_switch", sysVersion.getGameDownloadSwitch() == 1 ? true : false);
//            map.put("index_banner_switch", sysVersion.getIndexBannerSwitch() == 1 ? true : false);
        } else {//当前版本存在 判断是否最新版本
            if (version.getNewVersion() == 0) {
                sysVersion = versionService.selectOne(new EntityWrapper<SysVersion>().eq("mobile_type", "Android").eq("new_version", 1));
                map.put("code", sysVersion.getCode());
                map.put("versionName", sysVersion.getVersion());
                map.put("content", sysVersion.getIntro());
                map.put("url", sysVersion.getApkUrl());
//                map.put("game_download_switch", sysVersion.getGameDownloadSwitch() == 1 ? true : false);
//                map.put("index_banner_switch", sysVersion.getIndexBannerSwitch() == 1 ? true : false);
            } else {
                map.put("code", version.getCode());
                map.put("versionName", version.getVersion());
                map.put("content", version.getIntro());
                map.put("url", version.getApkUrl());
//                map.put("game_download_switch", version.getGameDownloadSwitch() == 1 ? true : false);
//                map.put("index_banner_switch", version.getIndexBannerSwitch() == 1 ? true : false);
            }


            if (1 == version.getIsForce()) {
                map.put("is_force", true);
            } else {
                map.put("is_force", false);
            }

        }
        re.setData(map);
        return re;
    }
    
	@ApiOperation(value="ios检查更新(2.4.3)", notes="")
	@RequestMapping(value="/api/checkiosupdate", method= RequestMethod.POST)
	@ApiImplicitParams({})
	public ResultDto<Map<String,Object>> checkIOSUpdate(@Anonymous MemberUserProfile memberUserPrefile,@RequestBody VersionInput input) {
		ResultDto<Map<String,Object>> re=new ResultDto<Map<String,Object>>();
		String currentVersion = input.getCurrentVersion();
		
		//查看当前版本(如查找不到,直接强生)
		SysVersion version = versionService.selectOne(new EntityWrapper<SysVersion>().eq("version",currentVersion).eq("mobile_type", "IOS"));

		Map<String,Object> map =new HashMap<String,Object>();
		SysVersion sysVersion = null;
		
		if(version == null) {//如果找不到当前版本,查找最新版本
			sysVersion = versionService.selectOne(new EntityWrapper<SysVersion>().eq("mobile_type", "IOS").eq("new_version",1));
			map.put("code", sysVersion.getCode());
			map.put("versionName", sysVersion.getVersion());
			map.put("content", sysVersion.getIntro());
			map.put("url", sysVersion.getApkUrl());
			map.put("is_force", true);
//			map.put("game_download_switch", sysVersion.getGameDownloadSwitch() == 1?true:false);
//			map.put("index_banner_switch", sysVersion.getIndexBannerSwitch() ==1?true:false);
		}else {//当前版本存在 判断是否最新版本
			if(version.getNewVersion() == 0) {
				sysVersion = versionService.selectOne(new EntityWrapper<SysVersion>().eq("mobile_type", "IOS").eq("new_version",1));
				SysVersionChannel sysVersionChannel = channelService.selectOne(new EntityWrapper<SysVersionChannel>().eq("version_id",sysVersion.getId()).eq("channel_code", input.getChannelCode()));
				if(sysVersionChannel == null) {
					return ResultDto.error("-1", "找不到该版本对应渠道");
				}
				map.put("code", sysVersion.getCode());
				map.put("versionName", sysVersion.getVersion());
				map.put("content", sysVersion.getIntro());
				map.put("url", sysVersion.getApkUrl());
//				map.put("game_download_switch", sysVersionChannel.getGameDownloadSwitch() == 1?true:false);
//				map.put("index_banner_switch", sysVersionChannel.getIndexBannerSwitch() ==1?true:false);
				if(1 == sysVersionChannel.getIsForce()) {
					map.put("is_force", true);
				}else {
					map.put("is_force", false);
				}
			}else {
				map.put("code", version.getCode());
 				map.put("versionName", version.getVersion());
				map.put("content", version.getIntro());
				map.put("url", version.getApkUrl());
//				map.put("game_download_switch", version.getGameDownloadSwitch() == 1?true:false);
//				map.put("index_banner_switch", version.getIndexBannerSwitch() ==1?true:false);
				map.put("is_force", false);

			}
			
			
		}
		re.setData(map);
		return re;
	}
	
    

    @ApiOperation(value = "关于我们", notes = "")
    @RequestMapping(value = "/api/about", method = RequestMethod.GET)
    public ResultDto<Map<String, String>> getTaskHelp(@Anonymous MemberUserProfile memberUserPrefile) {
        ResultDto<Map<String, String>> re = new ResultDto<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", "http://static.fungoweb.com/static/site/pages/about_me.html");
        re.setData(map);
        return re;
    }


    @ApiOperation(value = "游戏评价打星说明 (v 2.4)", notes = "")
    @RequestMapping(value = "/api/starInfo", method = RequestMethod.GET)
    public ResultDto<ArrayList<StartInfo>> getStarInfo(@Anonymous MemberUserProfile memberUserPrefile) {
//		ArrayList<StartInfo> list = (ArrayList<StartInfo>) GuavaCache.get("startInfo");
        ArrayList<StartInfo> list = new ArrayList<StartInfo>();
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
        ArrayList<Trait> list = new ArrayList<Trait>();
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