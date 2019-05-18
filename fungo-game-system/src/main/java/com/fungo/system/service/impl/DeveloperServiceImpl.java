package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dao.DeveloperGameRelDao;
import com.fungo.system.dto.DeveloperBean;
import com.fungo.system.entity.*;
import com.fungo.system.helper.mq.MQProduct;
import com.fungo.system.proxy.IDeveloperProxyService;
import com.fungo.system.service.*;
import com.game.common.dto.DeveloperGame.DeveloperGameOut;
import com.game.common.dto.DeveloperGame.DeveloperGamePageInput;
import com.game.common.dto.DeveloperGame.DeveloperQueryIn;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.game.*;
import com.game.common.dto.GameDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.util.CommonUtil;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/28
 */
@Service
public class DeveloperServiceImpl implements IDeveloperService {

    private static final Logger logger = LoggerFactory.getLogger(DeveloperServiceImpl.class);

    @Autowired
    private DeveloperService developerService;

    @Autowired
    private DeveloperGameRelService dgrService;

    @Autowired
    private BasLogService baslogService;

    @Autowired
    private IDeveloperProxyService iDeveloperProxyService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MQProduct mqProduct;
    @Autowired
    private BasActionService actionService;
    @Autowired
    private DeveloperGameRelDao dgrDao;

    @Override
    public ResultDto<String> addDeveloper(String loginId, DeveloperBean input) {
        Developer developer=developerService.selectOne(new EntityWrapper<Developer>().eq("member_id",loginId));
        if(developer!=null) {
            return ResultDto.error("7001", "对不起，您已绑定开发者信息");
        }else {
            developer=new Developer();
            developer.setBusinessLicense(input.getBusinessLicense());
            developer.setBusinessLicenseImage(input.getBusinessLicenseImage());
//			developer.setBusinessPermitLimitDate(input.getBusinessPermitLimitDate());
            developer.setCompanyFullName(input.getCompanyFullName());
            developer.setCompanyName(input.getCompanyName());
            developer.setCompanyShortName(input.getCompanyShortName());
            developer.setCreatedAt(new Date());
            developer.setLiaisonAdress(input.getLiaisonAdress());
            developer.setLiaisonEmail(input.getLiaisonEmail());
            developer.setLiaisonIdImageBack(input.getLiaisonIdImageBack());
            developer.setLiaisonIdNumber(input.getLiaisonIdNumber());
            developer.setLiaisonIdImageFront(input.getLiaisonIdImageFront());
            developer.setLiaisonName(input.getLiaisonName());
            developer.setLiaisonPhone(input.getLiaisonPhone());
            developer.setLogo(input.getLogo());
            developer.setMemberId(loginId);
            developer.setState(0);
            developer.setUpdatedAt(new Date());
            developer.setApproveState(2);
            developerService.insert(developer);
        }
        ResultDto<String> re=new ResultDto<String>();
        re.setMessage("绑定成功");
        return re;
    }

    @Override
    @Transactional
    public ResultDto<String> addGame(MemberUserProfile memberUserPrefile, AddGameInputBean input) {
        /*
         * apk string 否 安装包 isbnId string 否 网络出版号Id isbnImage string 否 核发单图片 copyrightId
         * string 否 软件著作权登记号 issueId string 否 游戏备案通知单号 credentials array 否 其他证明文件
         * itunesId string 否 appstoreID testNumber number 否 测试人数
         *
         */
        if (input == null) {
            return ResultDto.error("13", "添加内容不存在");
        }

        try {
            //用户有无开发者权限
            Developer developer = developerService
                    .selectOne(new EntityWrapper<Developer>().eq("member_id", memberUserPrefile.getLoginId()));
            if (developer == null || developer.getApproveState() != 2) {
                return ResultDto.error("14", "该用户没有开发者权限");
            }

            GameDto game = new GameDto();

            game.setName(input.getName());
            if (input.getApk() != null) {
                game.setApk(input.getApk());
            }
            String updateLog = input.getUpdateLog();
            game.setUpdateLog(updateLog);

            game.setCoverImage(input.getCoverImage());
            Date date = new Date();
            game.setCreatedAt(date);
            game.setOrigin(input.getOrigin());
            game.setUpdatedAt(date);
            game.setCreatedBy(memberUserPrefile.getLoginId());
            game.setDetail(input.getDetail());
            game.setDeveloper(developer.getCompanyName());
            game.setDownloadNum(0);
            game.setEditedAt(date);
            game.setIcon(input.getIcon());
            if(input.getImages() != null) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    game.setImages(mapper.writeValueAsString(input.getImages()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            game.setIntro(input.getGameIntro());
            if (!CommonUtil.isNull(input.getIsbnId())) {
                game.setIsbnId(input.getIsbnId());
            }
            if (!CommonUtil.isNull(input.getItunesId())) {
                game.setItunesId(input.getItunesId());
            }
            game.setMemberId(memberUserPrefile.getLoginId());
            String version = input.getVersion();
            int i = version.indexOf(".");
            String versionMain = version.substring(0, i);
            String childVersion = version.substring(i + 1);
            game.setVersionMain(versionMain);
            game.setVersionChild(childVersion);
            game.setUpdateLog(input.getUpdateLog());

            ObjectMapper mapper = new ObjectMapper();


            game.setTestNumber(input.getTestNumber());
            game.setTsetDate(input.getTsetDate());
            game.setVideo(input.getVideo());
            game.setCopyrightId(input.getCopyrightId());
            game.setCopyrightImage(input.getCopyrightImage());
            if(input.getCredentials() != null) {
                try {
                    game.setCredentials(mapper.writeValueAsString(input.getCredentials()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            game.setIsbnId(input.getIsbnId());
            game.setIsbnImage(input.getIsbnImage());
            game.setIssueId(input.getIssueId());
            String androidState = input.getAndroidState();
            String iOState = input.getiOState();
            if(androidState != null) {
                game.setAndroidState(Integer.parseInt(androidState));
            }
            if(iOState != null) {
                if("2".equals(iOState)) {
                    return ResultDto.error("-1", "iOS没有预约玩家不能测试");
                }
                game.setIosState(Integer.parseInt(iOState));
            }

            game.setDeveloperId(developer.getId());
            game.setState(3);
            game.setGameSize((long)input.getSize());
            mqProduct.gameInsert(game);
            try {
                game.setCompatibility(mapper.writeValueAsString(new ArrayList<String>()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            CmmCommunityDto c = new CmmCommunityDto();
            c.setGameId(game.getId());
            c.setCoverImage(input.getCoverImage());
            Date d = new Date();
            c.setCreatedAt(d);
            c.setUpdatedAt(d);
            c.setCreatedBy(memberUserPrefile.getLoginId());
            c.setUpdatedBy(memberUserPrefile.getLoginId());
            c.setIcon(game.getIcon());
            c.setIntro(input.getCommunityIntro());
            c.setName(game.getName());
            c.setState(0);
            mqProduct.communityInsert(c);
            game.setCommunityId(c.getId());
            mqProduct.gameUpdate(game);
            if(input.getTagList() != null || !CommonUtil.isNull(input.getCategoryId())) {
                if (input.getTagList().size() > 3) {
                    return ResultDto.error("13", "最多添加3个标签");
                }
                mqProduct.addGameTag(input.getTagList(),input.getCategoryId(),game.getId());
//                ResultDto<String> addGameTag ;
//                if(!addGameTag.isSuccess()) {
//                    throw new BusinessException("-1","添加标签失败:"+addGameTag.getMessage());
//                }
            }
            input.setGameId(game.getId());

            //添加游戏关联
            addDeveloperGameRel(input.getDeveloperList(), game.getId(), developer.getId());

            GameReleaseLogDto log = new GameReleaseLogDto();
            log.setCommunityId(game.getCommunityId());

            //添加游戏记录
            ResultDto<String> dto = copyGameTolog(input,memberUserPrefile,log,developer);
            if(!dto.isSuccess()) {
                throw new BusinessException("-1",dto.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("开发者上传游戏异常",e);
        }
        return ResultDto.success();
    }

    @Transactional
    @Override
    public ResultDto<String> updateGame(MemberUserProfile memberUserPrefile, AddGameInputBean input) {
        String gameId = input.getGameId();
        String loginId = memberUserPrefile.getLoginId();

        //游戏关联表 判断用户是否与该游戏关联
        DeveloperGameRel rel = dgrService.selectOne(new EntityWrapper<DeveloperGameRel>().eq("member_id", loginId).eq("game_id", gameId));
        if (rel == null) {
            return ResultDto.error("234", "当前用户未与目标游戏关联");
        }

        //判断用户有没有开发者权限
        Developer developer = developerService.selectById(rel.getDeveloperId());
        if (developer == null || developer.getApproveState() != 2) {
            return ResultDto.error("14", "该用户没有开发者权限");
        }
        //DONE:根据游戏id查询游戏
        GameDto game =iDeveloperProxyService.selectGame(gameId); //   gameService.selectById(gameId);

        if (CommonUtil.isNull(gameId)) {
            return ResultDto.error("13", "找不到游戏id");
        }

        if(game == null) {
            return ResultDto.error("13", "找不到游戏目标");
        }

        GameReleaseLogDto param = new GameReleaseLogDto();
        param.setGameId(gameId);
        // DONE:   根据游戏版本日志审批对象查询集合
        List<GameReleaseLogDto> logs =  iDeveloperProxyService.selectGameReleaseLog(param);
        GameReleaseLogDto log = logs.get(0);
        //logService.selectOne(
          //      new EntityWrapper<GameReleaseLog>().eq("game_id", gameId).orderBy("created_at", false).last("limit 1"));
        if (log == null) {
            return ResultDto.error("231", "目标游戏更新记录不存在");
        }


        Integer approveState = log.getApproveState();
        if (approveState != 2 && approveState != 3) {
            return ResultDto.error("232", "该游戏最近一次更新尚未审核");
        }

        //添加更新记录
        GameReleaseLogDto logGame = new GameReleaseLogDto();
        if (!CommonUtil.isNull(input.getName())) {
            logGame.setName(input.getName());
        }

        List<String> markList = new ArrayList<>();

        if (!CommonUtil.isNull(input.getApk())) {
            logGame.setApk(input.getApk());
            if(!input.getApk().equals(game.getApk())) {
                markList.add("更新了apk");
            }
        }

        if (!CommonUtil.isNull(input.getCoverImage())) {
            logGame.setCoverImage(input.getCoverImage());
        }
        Date date = new Date();
        logGame.setGameId(gameId);
        logGame.setCreatedAt(date);
        logGame.setUpdatedAt(date);
        logGame.setDetail(input.getDetail());
        logGame.setDeveloper(developer.getCompanyName());
        logGame.setDownloadNum(0);
        logGame.setEditedAt(date);
        logGame.setUpdatedBy(memberUserPrefile.getLoginId());
        logGame.setCreatedBy(memberUserPrefile.getLoginId());
        logGame.setIcon(input.getIcon());
        if(input.getImages() != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                logGame.setImages(mapper.writeValueAsString(input.getImages()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        logGame.setIntro(input.getGameIntro());
        if (!CommonUtil.isNull(input.getIsbnId())) {
            logGame.setIsbnId(input.getIsbnId());
        }
        if (!CommonUtil.isNull(input.getItunesId())) {
            logGame.setItunesId(input.getItunesId());
        }
        logGame.setMemberId(memberUserPrefile.getLoginId());
        String version = input.getVersion();
        int i = version.indexOf(".");
        String versionMain = version.substring(0, i);
        String childVersion = version.substring(i + 1);
        logGame.setVersionMain(versionMain);
        logGame.setVersionChild(childVersion);

        logGame.setUpdateLog(input.getUpdateLog());
        ObjectMapper mapper = new ObjectMapper();

        logGame.setDeveloperId(developer.getId());
        logGame.setTestNumber(input.getTestNumber());
        logGame.setTsetDate(input.getTsetDate());
        logGame.setVideo(input.getVideo());
        logGame.setCopyrightId(input.getCopyrightId());
        logGame.setCopyrightImage(input.getCopyrightImage());
        if(input.getCredentials() != null) {
            try {
                logGame.setCredentials(mapper.writeValueAsString(input.getCredentials()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        logGame.setItunesId(input.getIssueId());
        logGame.setIsbnId(input.getIsbnId());
        logGame.setIsbnImage(input.getIsbnImage());
        logGame.setIssueId(input.getIssueId());

        String androidState = input.getAndroidState();
        String iOState = input.getiOState();
        if(androidState != null) {
            if(!game.getAndroidState().toString().equals(input.getAndroidState())) {
                String changeMsg = checkUpdateState("安卓",game.getAndroidState(),Integer.parseInt(input.getAndroidState()));
                markList.add(changeMsg);
            }
            logGame.setAndroidState(androidState);
        }
        if(iOState != null) {
            if("2".equals(iOState)) {
                GameSurveyRelDto gameSurveyRelParam = new GameSurveyRelDto();
                gameSurveyRelParam.setGameId(game.getId());
                gameSurveyRelParam.setMemberId(memberUserPrefile.getLoginId());
                gameSurveyRelParam.setPhoneModel("iOS");
                // DONE: 根据游戏测试会员关联表对象查询总数
                int selectCount = iDeveloperProxyService.selectCount(gameSurveyRelParam);
                        //surveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().eq("game_id",game.getId()).eq("member_id",memberUserPrefile.getLoginId()).eq("phone_model","iOS"));
                if(selectCount < 1) {
                    return ResultDto.error("-1", "iOS没有预约玩家不能测试");
                }
            }
            if(!game.getIosState().toString().equals(input.getiOState())) {
                String changeMsg = checkUpdateState("苹果",game.getIosState(),Integer.parseInt(input.getiOState()));
                markList.add(changeMsg);
            }
            logGame.setIosState(iOState);
        }
        markList.add("更新了游戏信息");
        try {
            logGame.setRemark(mapper.writeValueAsString(markList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logGame.setGameSize(input.getSize());
        logGame.setOrigin(input.getOrigin());

        //标签
        Map<String,Object> tags = new HashMap<>();
        tags.put("categoryId",!CommonUtil.isNull(input.getCategoryId())?input.getCategoryId():"");
        tags.put("tags", input.getTagList());
        try {
            logGame.setTags(mapper.writeValueAsString(tags));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            logGame.setCompatibility(mapper.writeValueAsString(new ArrayList<String>()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logGame.setCommunityId(game.getCommunityId());
        logGame.setCommunityIntro(input.getCommunityIntro());
        logGame.setState(1);

        Map<String,String> map = new HashMap<>();
        map.put("gameId", game.getId());
        map.put("memberId",developer.getMemberId());
        //删除原先的关联
        dgrDao.delLinkedMember(map);

        input.getDeveloperList().remove(developer.getMemberId());
        addDeveloperGameRel(input.getDeveloperList(), game.getId(), developer.getId());

        // @todo  logGame.insert()
        mqProduct.gamereleaselogInsert(logGame);
        return ResultDto.success();
    }

    @Override
    public ResultDto<DeveloperGameOut> gameDetail(String gameId, String userId) {
        DeveloperGameOut out = new DeveloperGameOut();
        try {
            //开发者游戏关联表,判断开发者有没有与该游戏关联
            DeveloperGameRel rel = dgrService
                    .selectOne(new EntityWrapper<DeveloperGameRel>().eq("member_id", userId).eq("game_id", gameId));

            if (rel == null) {
                return ResultDto.error("234", "当前开发者未与目标游戏关联");
            }

            //该游戏最近更新的游戏记录
            GameReleaseLogDto param = new GameReleaseLogDto();
            param.setGameId(gameId);
            param.setMemberId(userId);

            List<GameReleaseLogDto> logs =  iDeveloperProxyService.selectGameReleaseLog(param);
            GameReleaseLogDto log = logs.get(0);

            out.setGameId(log.getId());
            out.setAndroidState(log.getAndroidState() == null ? 0 : Integer.parseInt(log.getAndroidState()));
            out.setiOState(log.getIosState() == null ? 0 : Integer.parseInt(log.getIosState()));
            out.setApk(log.getApk());
            out.setCheckState(log.getApproveState());
            String communityId = log.getCommunityId();

            //		CmmCommunity community = communityService.selectById(communityId);
//		if(community != null) {
//			out.setCommunityIntro(community.getIntro());
//		}
            out.setCommunityIntro(log.getCommunityIntro());

            out.setCoverImage(log.getCoverImage());
            out.setDetail(log.getDetail());
            out.setDeveloper(log.getDeveloper());
            out.setUpdatedAt(DateTools.fmtDate(log.getUpdatedAt()));
            out.setGameId(gameId);
            out.setGameIntro(log.getIntro());
            out.setIcon(log.getIcon());
            ObjectMapper mapper = new ObjectMapper();
            try {
                if(log.getImages()!=null) {
                    out.setImages((ArrayList<String>)mapper.readValue(log.getImages(),ArrayList.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.setIssueId(log.getIssueId());
            out.setIsbnId(log.getIsbnId());
            out.setIsbnImage(log.getIsbnImage());
            out.setItunesId(log.getItunesId());
            out.setName(log.getName());
            out.setTestNumber(log.getTestNumber() == null ? 0 : log.getTestNumber());
            if (log.getTsetDate() != null) {
                out.setTsetDate(DateTools.fmtDate(log.getTsetDate()));
            }
            if(log.getTsetDate() != null) {
                out.setTsetDate(DateTools.fmtDate(log.getTsetDate()));
            }
            out.setUpdateLog(log.getUpdateLog());
            out.setVideo(log.getVideo());
            out.setVersion(log.getVersionMain() + "." + log.getVersionChild());
            out.setCommunityId(communityId);
            out.setCopyrightId(log.getCopyrightId());
            out.setCopyrightImage(log.getCopyrightImage());

            out.setOrigin(log.getOrigin());
            out.setGameSize(log.getGameSize());

            //游戏标签
            try {
                Map<String,Object> map = mapper.readValue(log.getTags(), HashMap.class);
                out.setCategoryId((String)map.get("categoryId"));
                ArrayList<String> tagList = (ArrayList<String>) map.get("tags");
                out.setTagList(tagList);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

//		if(log.getCompatibility() != null) {
//			try {
//				out.setco(mapper.readValue(log.getCompatibility(),ArrayList.class));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
            if(log.getCredentials() != null) {
                try {
                    out.setCredentials(mapper.readValue(log.getCredentials(),ArrayList.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//		List<DeveloperGameRel> rl = dgrService.selectList(new EntityWrapper<DeveloperGameRel>().eq("game_id",gameId));
//		List<String> developerList = new ArrayList<>();
//		for(DeveloperGameRel gamerel:rl ) {
//			developerList.add(gamerel.getMemberId());
//		}
            Developer developer = developerService.selectById(rel.getDeveloperId());
            out.setDeveloperList(getDeveloperList(gameId,developer.getMemberId()));
            out.setGameLogId(log.getId());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("获取开发者游戏详情异常",e);
        }
        return ResultDto.success(out);
    }

    @Override
    public FungoPageResultDto<GameOutBean> gameList(DeveloperGamePageInput input, String userId) {
        FungoPageResultDto<GameOutBean> re = new FungoPageResultDto<>();
        //查询游戏开发者关联表
        List<DeveloperGameRel> relList = dgrService
                .selectList(new EntityWrapper<DeveloperGameRel>().eq("member_id",userId));

        if(relList != null && relList.size() > 0) {
            List<String> collect = new ArrayList<>();
            for(DeveloperGameRel rel:relList) {
                if(!CommonUtil.isNull(rel.getGameId())) {
                    collect.add(rel.getGameId());
                }
            }
            return iDeveloperProxyService.gameList(collect,input.getPage(),input.getLimit());
        }
        return re;
    }

    @Override
    public FungoPageResultDto<GameHistoryOut> gameHistory(String userId, DeveloperGamePageInput input) {
        //		Developer developer = developerService.selectOne(new EntityWrapper<Developer>().eq("member_id", userId));
//		if (developer == null) {
//			return FungoPageResultDto.error("14", "该用户没有开发者权限");
//		}

        String gameId = input.getGameId();
        GameReleaseLogDto param = new GameReleaseLogDto();
        param.setGameId(gameId);
        param.setMemberId(userId);
        //DONE 游戏接口
        List<GameReleaseLogDto> list = iDeveloperProxyService.selectGameReleaseLog(param);
        //logService.selectPage(new Page<>(input.getPage(), input.getLimit()),
//                new EntityWrapper<GameReleaseLog>().eq("game_id", gameId).eq("member_id", userId);
//        List<GameReleaseLog> list = page.getRecords();
        Page page = new Page();
        page.setTotal(list.size());
        page.setRecords(list);
        List<GameHistoryOut> olist = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (GameReleaseLogDto log : list) {
            GameHistoryOut out = new GameHistoryOut();
            out.setCreatedAt(DateTools.fmtDate(log.getCreatedAt()));
            out.setMessage(log.getUpdateLog());
            out.setCheckState(log.getApproveState());
            out.setVersion(log.getVersionMain() + "." + log.getVersionChild());
            try {
                out.setRemark(mapper.readValue(log.getRemark(), ArrayList.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
            olist.add(out);
        }
        FungoPageResultDto<GameHistoryOut> re = new FungoPageResultDto<>();
        PageTools.pageToResultDto(re, page);
        re.setData(olist);
        return re;
    }

    @Override
    public ResultDto<List<Map<String, Object>>> gemeAnalyzeLog(DeveloperQueryIn input) {
        String gameId = input.getGameId();
        //浏览 下载 预约 测试 评价 推荐率
        List<Map<String,Object>> list = new ArrayList<>();
        //浏览
        int abrowses = 0; //baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/content/game/"+gameId).eq("channel", "Android"));
        int ibrowses = 0; //baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/content/game/"+gameId).eq("channel", "iOS"));
        int totalbrowse = 0; //baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/content/game/"+gameId));
        int wbrowses = totalbrowse - abrowses -ibrowses;
        Map<String,Object> browse = new HashMap<>();
        browse.put("name","浏览量");
        browse.put("web", wbrowses);
        browse.put("android", abrowses);
        browse.put("IOS", ibrowses);
        browse.put("total", totalbrowse);
        list.add(browse);
        //下载
        int ad = 0; //baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/action/download").eq("channel", "Android").like("input_data",gameId));
        int id = 0; //baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/action/download").eq("channel", "IOS").like("input_data",gameId));
        int td = 0; //baslogService.selectCount(new EntityWrapper<BasLog>().eq("path", "/api/action/download").like("input_data",gameId));
        int wd = td - ad -id;
        Map<String,Object> down = new HashMap<>();
        down.put("name","下载请求数");
        down.put("web", wd);
        down.put("android", ad);
        down.put("IOS", id);
        down.put("total", td);
        list.add(down);
        //预约
        // DONE: 2019/5/5
        GameSurveyRelDto gameSurveyRelParam = new GameSurveyRelDto();
        gameSurveyRelParam.setGameId(gameId);
        int tr =   iDeveloperProxyService.selectCount(gameSurveyRelParam); //surveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().eq("game_id", gameId));
        gameSurveyRelParam.setPhoneModel("Android");
        int ar = iDeveloperProxyService.selectCount(gameSurveyRelParam);  //surveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().eq("game_id", gameId).eq("phone_model", "Android"));
        gameSurveyRelParam.setPhoneModel("IOS");
        int ir =   iDeveloperProxyService.selectCount(gameSurveyRelParam); //surveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().eq("game_id", gameId).eq("phone_model", "IOS"));
        int wr = tr -ar -ir;
        Map<String,Object> appoint = new HashMap<>();
        appoint.put("name","预约数");
        appoint.put("web", wr);
        appoint.put("android", ar);
        appoint.put("IOS", ir);
        appoint.put("total", tr);
        list.add(appoint);
        //测试
        Map<String,Object> test = new HashMap<>();
        test.put("name","测试数");
        test.put("web", 0);
        test.put("android", 0);
        test.put("IOS", 0);
        test.put("total", 0);
        list.add(test);
        //评价
        // DONE: 2019/5/5
        GameEvaluationDto gameEvaluationParam = new GameEvaluationDto();
        gameEvaluationParam.setGameId(gameId);
        int te = iDeveloperProxyService.selectGameEvaluationCount(gameEvaluationParam);//evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId));
        gameEvaluationParam.setPhoneModel("Android");
        int ae = iDeveloperProxyService.selectGameEvaluationCount(gameEvaluationParam); //evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "Android"));
        gameEvaluationParam.setPhoneModel("IOS");
        int ie = iDeveloperProxyService.selectGameEvaluationCount(gameEvaluationParam); //evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "IOS"));

        int we = te -ae - ie;
        Map<String,Object> eva = new HashMap<>();
        eva.put("name","评价数");
        eva.put("web", we);
        eva.put("android", ae);
        eva.put("IOS", ie);
        eva.put("total", te);
        list.add(eva);
        //推荐率
        gameEvaluationParam.setPhoneModel("");
        gameEvaluationParam.setIsRecommend("1");
        int tc = iDeveloperProxyService.selectGameEvaluationCount(gameEvaluationParam);  //evaluationService.selectCount(new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("is_recommend", 1));
        gameEvaluationParam.setPhoneModel("Android");
        int ac =  iDeveloperProxyService.selectGameEvaluationCount(gameEvaluationParam); //evaluationService.selectCount
//                (new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "Android").eq("is_recommend", 1));
        gameEvaluationParam.setPhoneModel("IOS");
        int ic = iDeveloperProxyService.selectGameEvaluationCount(gameEvaluationParam); //evaluationService.selectCount
//                (new EntityWrapper<GameEvaluation>().eq("game_id", gameId).eq("phone_model", "IOS").eq("is_recommend", 1));

        int wc = tc - ac -ic;
        Map<String,Object> rate = new HashMap<>();
        rate.put("name","推荐率");
        rate.put("web", we == 0?"暂无推荐":wc== 0?"0%":rateTransfor(wc,we));
        rate.put("android", ae == 0?"暂无推荐":ac== 0?"0%":rateTransfor(ac,ae));
        rate.put("IOS",  ie == 0?"暂无推荐":ic== 0?"0%":rateTransfor(ic,ie));
        rate.put("total", te == 0?"暂无推荐":tc== 0?"0%":rateTransfor(tc,te));
        list.add(rate);
        return ResultDto.success(list);
    }

    @Override
    public ResultDto<List<Map<String, Object>>> messageList() {

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> m1 = new HashMap<>();
        m1.put("content","您的游戏'狂扁小朋友'更新成功");
        m1.put("createdAt", DateTools.fmtDate(new Date()));
        m1.put("type", "");

        Map<String,Object> m2 = new HashMap<>();
        m2.put("content","第一届翔游争霸赛正式开幕");
        m2.put("createdAt", "2018-7-17 10:07:32");
        m2.put("type", "");

        Map<String,Object> m3 = new HashMap<>();
        m3.put("content","用户心理学---论怎样骗氪");
        m3.put("createdAt", "2018-7-17 10:07:32");
        m3.put("type", "");

        Map<String,Object> m4 = new HashMap<>();
        m4.put("content","你的app真不错,不过下一秒就是我的了---手把手教你借鉴");
        m4.put("createdAt", "2018-7-17 9:07:32");
        m4.put("type", "");

        Map<String,Object> m5 = new HashMap<>();
        m5.put("content","您的账号被多次举报涉黄,请遵守用户守则,发表健康内容");
        m5.put("createdAt", new Date());
        m5.put("type", "");
        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);
        list.add(m5);
        return ResultDto.success(list);
    }

    @Override
    public ResultDto<Map<String, Integer>> communityAnalyze(DeveloperQueryIn input) throws ParseException {
        //		String gameId = input.getGameId();
//		Game game = gameService.selectOne(Condition.create().setSqlSelect("id,community_id").eq("id",gameId));
        String cid = input.getCommunityId();
        if(CommonUtil.isNull(cid)) {
            return ResultDto.error("-1", "社区id不能为空");
        }
        Map<String,Integer> map = new HashMap<>();
        // /api/content/community/55a25aee44d9040044fe8f94
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date nowData = format.parse(format.format(new Date()));
        //浏览
        int tpv = 0;//baslogService.selectCount(new EntityWrapper<BasLog>().eq("path","/api/content/community/"+cid));
        int dpv = 0;//baslogService.selectCount(new EntityWrapper<BasLog>().eq("path","/api/content/community/"+cid).ge("created_at", nowData));
        map.put("todayPV", dpv);
        map.put("totalPV", tpv);
        //文章
        CmmPostDto cmmPostDto = new CmmPostDto();
        cmmPostDto.setCommunityId(cid);
        int tpo =  0;//iDeveloperProxyService.selectPostCount(cmmPostDto); //postService.selectCount(new EntityWrapper<CmmPost>().eq("community_id",cid));
        cmmPostDto.setCreatedAt(nowData);
        int dpo =  0;//iDeveloperProxyService.selectPostCount(cmmPostDto);  //postService.selectCount(new EntityWrapper<CmmPost>().eq("community_id",cid).ge("created_at", nowData));
        map.put("todayPost", dpo);
        map.put("totalPost", tpo);
        //关注 type 5 target_id target_type;
        int tf = 0;//actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).eq("target_id", cid));
        int df = 0;//actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).eq("target_id", cid).ge("created_at", nowData));
        map.put("todayFollow", df);
        map.put("totalFollow", tf);
        //热度
        map.put("todayHotValue", df+dpo);
        map.put("totalHotValue", tf+tpo);

        return ResultDto.success(map);
    }

    @Override
    public boolean checkDpPower(String memberId) {
        Developer developer = developerService.selectOne(new EntityWrapper<Developer>().eq("member_id", memberId));
        if(developer == null) {
            return false;
        }else if(developer.getApproveState() != 2 ) {
            return false;
        }
        return true;
    }

    //开发组成员
    public List<Map<String, Object>> getDeveloperList(String gameId,String memberId) {
        List<DeveloperGameRel> relList = dgrService.selectList(new EntityWrapper<DeveloperGameRel>().eq("game_id",gameId));
        List<String> collect = relList.stream().map(DeveloperGameRel::getMemberId).collect(Collectors.toList());
        @SuppressWarnings("unchecked")
        List<Member> memberList = memberService.selectList(Condition.create().setSqlSelect("id,user_name,member_no").in("id", collect));
        List<Map<String,Object>> developerList = new ArrayList<>();
        for(Member m:memberList ) {
            Map<String,Object> map = new HashMap<>();
            map.put("memberId", m.getId());
            map.put("username", m.getUserName());
            map.put("memberNo", m.getMemberNo());
            if(memberId.equals(m.getId())) {
                map.put("isDeveloper", true);
            }else {
                map.put("isDeveloper", false);
            }
            developerList.add(map);
        }
        return developerList;
    }

    private void addDeveloperGameRel(List<String> developerList,String gameId,String developerId) {
        for(String memberId:developerList) {
            DeveloperGameRel rel = new DeveloperGameRel();
            rel.setCreatedAt(new Date());
            rel.setUpdatedAt(new Date());
            rel.setGameId(gameId);
            rel.setDeveloperId(developerId);
            rel.setMemberId(memberId);
            dgrService.insert(rel);
        }
    }

    public ResultDto<String> copyGameTolog(AddGameInputBean input, MemberUserProfile memberUserPrefile, GameReleaseLogDto logGame, Developer developer) {

        if (!CommonUtil.isNull(input.getName())) {
            logGame.setName(input.getName());
        }
        if (!CommonUtil.isNull(input.getApk())) {
            logGame.setApk(input.getApk());

        }
        if (!CommonUtil.isNull(input.getCoverImage())) {
            logGame.setCoverImage(input.getCoverImage());
        }
        Date date = new Date();
        logGame.setGameId(input.getGameId());
        logGame.setCreatedAt(new Date());
        logGame.setUpdatedAt(date);
        logGame.setDetail(input.getDetail());
        logGame.setDeveloper(developer.getCompanyName());
        logGame.setDownloadNum(0);
        logGame.setEditedAt(date);
        logGame.setUpdatedBy(memberUserPrefile.getLoginId());
        logGame.setCreatedBy(memberUserPrefile.getLoginId());
        logGame.setIcon(input.getIcon());
        if(input.getImages() != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                logGame.setImages(mapper.writeValueAsString(input.getImages()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        logGame.setIntro(input.getGameIntro());
        if (!CommonUtil.isNull(input.getIsbnId())) {
            logGame.setIsbnId(input.getIsbnId());
        }
        if (!CommonUtil.isNull(input.getItunesId())) {
            logGame.setItunesId(input.getItunesId());
        }
        logGame.setMemberId(memberUserPrefile.getLoginId());
        String version = input.getVersion();
        int i = version.indexOf(".");
        String versionMain = version.substring(0, i);
        String childVersion = version.substring(i + 1);
        logGame.setVersionMain(versionMain);
        logGame.setVersionChild(childVersion);

        logGame.setUpdateLog(input.getUpdateLog());
        ObjectMapper mapper = new ObjectMapper();

        logGame.setOrigin(input.getOrigin());
        logGame.setDeveloperId(developer.getId());
        logGame.setTestNumber(input.getTestNumber());
        logGame.setTsetDate(input.getTsetDate());
        logGame.setVideo(input.getVideo());
        logGame.setCopyrightId(input.getCopyrightId());
        logGame.setCopyrightImage(input.getCopyrightImage());
        if(input.getCredentials() != null) {
            try {
                logGame.setCredentials(mapper.writeValueAsString(input.getCredentials()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        logGame.setItunesId(input.getItunesId());
        logGame.setIsbnId(input.getIsbnId());
        logGame.setIsbnImage(input.getIsbnImage());
        logGame.setIssueId(input.getIssueId());
        String androidState = input.getAndroidState();
        String iOState = input.getiOState();
        if(androidState != null) {
            logGame.setAndroidState(androidState);
        }
        if(iOState != null) {
            logGame.setIosState(iOState);
        }
        List<String> rm = new ArrayList<>();
        rm.add("新增游戏");
        try {
            logGame.setRemark(mapper.writeValueAsString(rm));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logGame.setGameSize(input.getSize());
        try {
            logGame.setCompatibility(mapper.writeValueAsString(new ArrayList<String>()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //communityId
        logGame.setCommunityIntro(input.getCommunityIntro());;
        Map<String,Object> tags = new HashMap<>();
        tags.put("categoryId",!CommonUtil.isNull(input.getCategoryId())?input.getCategoryId():"");
        tags.put("tags", input.getTagList());
        try {
            logGame.setTags(mapper.writeValueAsString(tags));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logGame.setState(0);

//        logGame.insert();
        // @todo  mq
        mqProduct.gamereleaselogInsert(logGame);
        return ResultDto.success();
    }

    @Transactional
    /**
     * 修改游戏标签
     *
     * @param tags 后台标签id集合
     * @param categoryId 分类标签id(官方标签)
     * @param gameId
     * @return
     */
    public ResultDto<String> addGameTag(List<String> tags,String categoryId,String gameId){
        return ResultDto.success();
    }

    	//检测游戏状态变化
	private String checkUpdateState(String phone,int preState,int nextState){
		String pre = getUpdateState(preState);
		String next = getUpdateState(nextState);
		return phone+"上线状态由'"+pre+"'变更为'"+next+"'";
	}

	private String getUpdateState(int i) {
		if(i == 0) {
			return "待开启";
		}else if(i == 1) {
			return "预约";
		}else if(i == 2) {
			return "测试";
		}else if(i == 3) {
			return "已上线";
		}else if (i == 4) {
			return "可下载";
		}
		return "";
	}

    public String rateTransfor(int numerator,int Denominator) {
        double c = (double)numerator / Denominator;
        return String.format("%.2f", c*100) + "%";
    }

}
