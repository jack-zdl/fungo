package com.fungo.system.function;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.dao.MemberDao;
import com.fungo.system.entity.IncentRanked;
import com.fungo.system.entity.Member;
import com.fungo.system.service.IMemberIncentRankedDaoService;
import com.fungo.system.service.IncentRankedService;
import com.fungo.system.service.MemberService;
import com.game.common.util.PKUtil;
import com.game.common.util.date.DateTools;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * <p>
 *     解析excel迁移用户等级、fungo身份证等初始化数据
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Component
public class DataTransForUserLevelService {


    private static final Logger logger = LoggerFactory.getLogger(DataTransForUserLevelService.class);


    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private IncentRankedService rankedService;

    @Autowired
    private IMemberIncentRankedDaoService iMemberIncentRankedDaoService;


    private String excelPath = "D:\\MingBoWork\\运营维护/没有等级的用户二期_2.xlsx";


    /**
     * 迁移用户等级数据
     */
    public void excuteParserToUserLevel() {

        try {

            File excel = new File(excelPath);


            if (excel.isFile() && excel.exists()) {

                //.是特殊字符，需要转义！！！！！
                String[] split = excel.getName().split("\\.");

                Workbook wb = null;

                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    //文件流对象
                    FileInputStream fis = new FileInputStream(excel);
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    logger.error("文件类型错误!");
                    return;
                }

                //开始解析  //读取sheet 0
                Sheet sheet = wb.getSheetAt(0);
                //第一行是列名，所以不读
                int firstRowIndex = sheet.getFirstRowNum() + 1;
                int lastRowIndex = sheet.getLastRowNum();

                logger.info("-------执行添加用户等级和fungo身份证数据.....");

                //遍历行
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {

                    Row row = sheet.getRow(rIndex);
                    if (row != null) {

                        //fungo 用户名
                        Cell fungoNickCell = row.getCell(0);
                        fungoNickCell.setCellType(HSSFCell.CELL_TYPE_STRING);

                        String fungoNickData = fungoNickCell.toString();

                        logger.info("fungoNickData:{}", fungoNickData + "\r\n");

                        queryMb(fungoNickData);
                    }

                }

            } else {
                logger.error("找不到指定的文件");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 查询没有等级和fungo身份证图片的用户
     * 且给这些用户添加上图片数据
     */
    public void queryMbWithoutLevelFungoImgs() {

        try {
            List<Member> memberList = memberDao.queryMbWithoutLevelFungoImgs();

            if (null != memberList && !memberList.isEmpty()) {

                logger.info("----查询没有等级和fungo身份证图片的用户数：{}", memberList.size());

                for (Member member : memberList) {
                    String mb_id = member.getId();
                    //是否等级数据
                    boolean isHaveLevel = isHaveLevel(mb_id);
                    logger.info("queryMb-mb_id:{}--isHaveLevel:{}", mb_id, isHaveLevel);
                    if (!isHaveLevel) {
                        addMbLevel(member);
                    }
                    //是否有fungo身份证荣誉数据
                    boolean isHaveFungo = isHaveFungo(mb_id);
                    logger.info("queryMb-mb_id:{}--isHaveFungo:{}", mb_id, isHaveFungo);
                    if (!isHaveFungo) {
                        addMbFungo(member);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 根据用户名查询用户id和级别数据
     * @param user_name
     */
    private void queryMb(String user_name) {


        EntityWrapper<Member> memberEntityWrapper = new EntityWrapper<>();
        memberEntityWrapper.setSqlSelect("id,level,user_name,created_at,mobile_phone_num");

        memberEntityWrapper.like("user_name", user_name);

        List<Member> memberList = memberService.selectList(memberEntityWrapper);
        logger.info("memberList:{}", JSON.toJSONString(memberList));


        if (null != memberList && !memberList.isEmpty()) {
            for (Member member : memberList) {
                String mb_id = member.getId();
                //是否等级数据
                boolean isHaveLevel = isHaveLevel(mb_id);
                logger.info("queryMb-mb_id:{}--isHaveLevel:{}", mb_id, isHaveLevel);
                if (!isHaveLevel) {
                    addMbLevel(member);
                }
                //是否有fungo身份证荣誉数据
                boolean isHaveFungo = isHaveFungo(mb_id);
                logger.info("queryMb-mb_id:{}--isHaveFungo:{}", mb_id, isHaveFungo);
                if (!isHaveFungo) {
                    addMbFungo(member);
                }
            }
        }

    }

    /**
     * 验证用户是否有等级数据
     * @param mb_id
     * @return
     */
    private boolean isHaveLevel(String mb_id) {

        IncentRanked incentRanked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", mb_id).eq("rank_type", 1));

        if (null != incentRanked) {
            return true;
        }
        return false;
    }


    /**
     * 验证用户是否有fungo身份证数据
     * @param mb_id
     * @return
     */
    private boolean isHaveFungo(String mb_id) {
        IncentRanked incentRanked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", mb_id).eq("rank_type", 3));
        if (null != incentRanked) {
            return true;
        }
        return false;
    }


    /**
     * 迁移用户等级数据
     * @param member
     */
    private void addMbLevel(Member member) {

        logger.info("------------迁移用户等级数据开始-member:{} ", member);
        if (null != member) {
            //获取用户id
            String mb_id = member.getId();

            //用户名
            String user_name = member.getUserName();

            //手机号
            String phone = member.getMobilePhoneNum();

            if (StringUtils.isNotBlank(phone)) {
                user_name = phone;
            }


            //用户等级
            Integer level = member.getLevel();

            IncentRanked incentRanked = new IncentRanked();
            incentRanked.setId(PKUtil.getInstance().longPK());

            incentRanked.setMbId(mb_id);
            incentRanked.setMbUserName(user_name);

            incentRanked.setCurrentRankId(level.longValue());
            incentRanked.setCurrentRankName("Lv" + level.longValue());

            ArrayList oldLevelList = new ArrayList();
            for (long j = 1; j <= level; j++) {
                //{rankId,rankName}
                Map<String, String> levelMap = new HashMap<>();
                levelMap.put("rankId", j + "");
                levelMap.put("rankName", "Lv" + j);

                oldLevelList.add(levelMap);
            }

            String rankIdtIds = JSONObject.toJSONString(oldLevelList);
            incentRanked.setRankIdtIds(rankIdtIds);

            incentRanked.setRankType(1);
            incentRanked.setCreatedAt(new Date());
            incentRanked.setUpdatedAt(new Date());
            incentRanked.setExt1("");
            incentRanked.setExt2("");
            incentRanked.setExt3("");

            iMemberIncentRankedDaoService.insert(incentRanked);
        }

        logger.info("------------迁移用户等级数据结束......");
    }


    /**
     * 迁移用户fungo身份证数据
     * @param member
     */
    private void addMbFungo(Member member) {

        logger.info("------------迁移用户fungo身份证数据开始-member:{} ", member);
        if (null != member) {


            //用户名
            String user_name = member.getUserName();

            //手机号
            String phone = member.getMobilePhoneNum();

            if (StringUtils.isNotBlank(phone)) {
                user_name = phone;
            }

            IncentRanked incentRanked = new IncentRanked();

            incentRanked.setMbId(member.getId());
            incentRanked.setMbUserName(user_name);

            List<Map<String, Object>> mapList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("1", 24);
            map.put("2", "FunGo身份证");
            map.put("3", 3);
            map.put("4", DateTools.fmtSimpleDateToString(member.getCreatedAt()));
            mapList.add(map);

            incentRanked.setRankIdtIds(JSON.toJSONString(mapList));
            incentRanked.setCurrentRankId(24L);
            incentRanked.setCurrentRankName("FunGo身份证");
            incentRanked.setRankType(3);


            incentRanked.setCreatedAt(new Date());
            incentRanked.setUpdatedAt(new Date());
            incentRanked.setId(PKUtil.getInstance().longPK());
            incentRanked.insert();

        }
        logger.info("------------迁移用户fungo身份证数据结束......");
    }


    //-----------
}
