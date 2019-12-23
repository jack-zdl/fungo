package com.fungo.system.function;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.IncentAccountCoin;
import com.fungo.system.entity.Member;
import com.fungo.system.entity.ScoreLog;
import com.fungo.system.service.IMemberAccountScoreDaoService;
import com.fungo.system.service.IncentAccountCoinDaoService;
import com.fungo.system.service.MemberService;
import com.fungo.system.service.ScoreLogService;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * <p>
 *     解析excel用于手动批量给用户发到金币操作
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Component
public class FungoGrantHandworkService {

    private static final Logger logger = LoggerFactory.getLogger(FungoGrantHandworkService.class);

    //excel文件路径
    //private String excelPath =  "D:\\MingBoWork\\fungoCoin\\dev\\测试服Fun币发放名单.xlsx";
    private String excelPath = "D:\\MingBoWork\\运营维护\\发gungo币/愚人节活动Fun币发放.xls";
    //private String excelPath =  "D:\\MingBoWork\\fungoCoin\\uat\\测试服Fun币发放名单.xlsx";

    //"D:/MingBoWork/fungoCoin/uat/提取信息.xlsx";//
    @Autowired
    private IncentAccountCoinDaoService incentAccountCoinServiceImap;

    @Autowired
    private IMemberAccountScoreDaoService IAccountDaoService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ScoreLogService scoreLogService;


    public static void main(String[] args) {
        new FungoGrantHandworkService().excuteParserToFungoCoin();
    }

    /**
     * 批量给用户添加fungo币
     */
    public void excuteParserToFungoCoin() {

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


                logger.info("-------执行添加fungo币.....");

                //遍历行
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {

                    Row row = sheet.getRow(rIndex);
                    if (row != null) {

                        //fungo昵称
                        Cell fungoNickCell = row.getCell(0);
                        String fungoNickData = fungoNickCell.toString();

                        //手机号码
                        Cell phoneCell = row.getCell(1);
                        String phoneData = phoneCell.toString();


                        DecimalFormat df = new DecimalFormat("#");

                        //昵称


                        //手机号
                        switch (phoneCell.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:// 数字
                                phoneData = df.format(phoneCell.getNumericCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_STRING:// 字符串
                                phoneData = df.format(Double.parseDouble(phoneCell.toString()));
                                break;
                            default:
                                phoneData = phoneCell.toString();
                                break;
                        }


                        //系统发放 fungo数量
                        Cell fungoCoinCell = row.getCell(2);
                        String fungoCoieData = fungoCoinCell.toString();

                        switch (fungoCoinCell.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:// 数字
                                fungoCoieData = df.format(fungoCoinCell.getNumericCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_STRING:// 字符串
                                fungoCoieData = df.format(Double.parseDouble(fungoCoinCell.toString()));
                                break;
                            default:
                                fungoCoieData = fungoCoieData.toString();
                                break;
                        }
                        logger.info("解析excel-fungoNickData:{},--phoneData:{},fungoCoieData:{}", fungoNickData, phoneData, fungoCoieData);
                        if (StringUtils.isNotBlank(phoneData)) {
                            addCoinToMember(phoneData, fungoNickData, fungoCoieData);
                        }
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
     * 给用户添加fungo币
     *  任务类型：21 营销活动  获取fungo币
     */
    private void addCoinToMember(String phone, String nickName, String fungoCoin) {

        //1.查询出用户详情
        Member memberInfo = memberService.selectOne(new EntityWrapper<Member>().eq("mobile_phone_num", phone));
        if (null != memberInfo) {

            Integer score = 0;

            if (StringUtils.isNotBlank(fungoCoin)) {
                score = Integer.valueOf(fungoCoin);
            }

            if (StringUtils.isBlank(nickName)) {
                nickName = memberInfo.getUserName();
            }


            //更新fungo币账户
            IncentAccountCoin coinAccount = incentAccountCoinServiceImap.selectOne(new EntityWrapper<IncentAccountCoin>().eq("mb_id", memberInfo.getId()).
                    eq("account_group_id", 3));
            if (coinAccount == null) {
                coinAccount = IAccountDaoService.createAccountCoin(memberInfo.getId());
            }

            //账户历史fungo币 + 新增fungo币量
            BigDecimal newFungoDB = coinAccount.getCoinUsable().add(new BigDecimal(score));

            logger.info("开始添加fungo币-memberId:{},--phone:{}--accountCode:{}--账户原fungo币量:{}---账户新增加后fungo币量:{}--新增fungo币量:{}",
                    memberInfo.getId(), phone, coinAccount.getAccountCode(),
                    coinAccount.getCoinUsable(), newFungoDB.toString(), fungoCoin);

            coinAccount.setCoinUsable(newFungoDB);
            coinAccount.setUpdatedAt(new Date());
            coinAccount.updateById();

            //记录添加fungo币的记录
            addCoinMbToLog(memberInfo.getId(), phone, nickName, fungoCoin);

        }

    }

    /**
     * 记录用户添加fungo币日志
     * 产生类型
     *          2 管理员
     */
    private void addCoinMbToLog(String userId, String phone, String nickName, String fungoCoin) {

        ScoreLog newLog = new ScoreLog();

        newLog.setMemberId(userId);
        newLog.setTaskType(21);

        newLog.setMbUserName(nickName);

        Integer score = 0;
        if (StringUtils.isNotBlank(fungoCoin)) {
            score = Integer.parseInt(fungoCoin);
        }
        newLog.setScore(score);

        newLog.setProduceType(2);
        newLog.setRuleId("0");
        newLog.setRuleName("系统发放");

        newLog.setCreatedAt(new Date());
        newLog.setUpdatedAt(new Date());
        newLog.setCreatorName("开发手动添加");

        logger.info("记录添加Fungo日志：{}", JSON.toJSONString(newLog));

        scoreLogService.insert(newLog);

    }

}
