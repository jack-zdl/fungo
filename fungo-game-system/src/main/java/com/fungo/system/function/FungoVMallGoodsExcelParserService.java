package com.fungo.system.function;


import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.fungo.system.mall.daoService.MallVirtualCardDaoService;
import com.fungo.system.mall.entity.MallVirtualCard;
import com.fungo.system.mall.service.consts.FungoMallSeckillConsts;
import com.game.common.util.FungoAESUtil;
import com.game.common.util.FungoCRC32Util;
import com.game.common.util.PKUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * <p>
 *     解析excel用于手动添加虚拟商品
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Component
public class FungoVMallGoodsExcelParserService {


    private static final Logger logger = LoggerFactory.getLogger( FungoVMallGoodsExcelParserService.class);

    //excel文件路径
    private static String excelPath = "F:/vCard.xlsx";

    //excel文件路径
    private static String jingdongexcelPath = "D:/workspace/a2.6/file/秒杀活动虚拟卡11.xlsx";

    //excel文件路径
    private static String googleexcelPath = "D:/workspace/a2.6/file/谷歌账号样式.xls";


    private static String jingdongkapath = "D:/workspace/apc2.1/file/虚拟商品补货信息1213.xlsx";

    @Autowired
    private MallVirtualCardDaoService mallVirtualCardDaoService;

    @Value("${fungo.mall.seckill.aesSecretKey}")
    private String aESSecretKey;

    /**
     * 解析excel
     */
    public  void excuteParserToVCard() {

        try {
            File excel = new File("");

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

                //遍历行
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {

                    Row row = sheet.getRow(rIndex);
                    if (row != null) {

//                        Cell goodIdCell = row.getCell(0);
//                        String goodId = goodIdCell.toString();
                        //面额
                       // Cell valueCell = row.getCell(1);
                        //String valueData = valueCell.toString();

                        //Float value_f = Float.parseFloat(valueData);
                        //Integer value_i = value_f.intValue();


                        //卡号
                        Cell cardSnCell = row.getCell(1);
                        String cardSnData = cardSnCell.toString();

                        //密码
                        Cell pwdCell = row.getCell(2);
                        String pwdData = pwdCell.toString();

                        //密码
//                        Cell emailCell = row.getCell(3);
//                        String emailData = emailCell.toString();

                       // DecimalFormat df = new DecimalFormat("#");

                       /* switch (cardSnCell.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:// 数字
                                cardSnData = df.format(cardSnCell.getNumericCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_STRING:// 字符串
                                cardSnData = df.format(Double.parseDouble(cardSnCell.toString()));
                                break;
                            default:
                                cardSnData = cardSnCell.toString();
                                break;
                        }
*/

                    /*    switch (pwdCell.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:// 数字
                                pwdData = df.format(pwdCell.getNumericCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_STRING:// 字符串
                                pwdData = df.format(Double.parseDouble(pwdCell.toString()));
                                break;
                            default:
                                pwdData = pwdCell.toString();
                                break;
                        }*/

                        //有效期
                        Cell validateCell = row.getCell(3);
                        String validateData = validateCell.toString();


                        //logger.info("valueData:{}---cardSnData:{}---pwdData:{}---validateData:{}", value_i, cardSnData , pwdData,validateData);
                        //logger.info("cardSnData:{}---pwdData:{}", cardSnData , pwdData);
                        if (StringUtils.isNoneBlank(cardSnData)) {

                            logger.info("cardSnData:" + cardSnData );
                            addVCardToDB(cardSnData, pwdData, validateData, 10,"");

                        }
                    }
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 功能描述: 1 现在是给谷歌游戏添加账号   2019/10/23 13:56
     *          2 京东卡添加虚拟账号  2019/11/06 10:56
     * @date: 2019/10/23 13:56
     */
    private   void addVCardToDB (String cardSn ,String cardPwd ,String validPeriodIntro , Integer valueRmb,String email) {

        MallVirtualCard virtualCard = new MallVirtualCard();

        virtualCard.setId(PKUtil.getInstance().longPK());
        // 喜加一Lv.1 2019090410175965512
        // 喜加一Lv.2  2019090410403036092
        // 喜加一Lv.3  2019090410404753342
        // 随机大作    2019090410405561072
        // 谷歌账号   2019102213190244711
//        virtualCard.setGoodsId(2019011810120065413L);
        // 京东卡  2019011810161808013
        virtualCard.setGoodsId(2019011810161808013L);
        String cardSnEncrypt = FungoAESUtil.encrypt( cardSn,
                aESSecretKey + FungoMallSeckillConsts.AES_SALT);

        virtualCard.setCardSn(cardSnEncrypt);


        String cardPwdEncrypt = FungoAESUtil.encrypt( cardPwd,
                aESSecretKey + FungoMallSeckillConsts.AES_SALT);

        virtualCard.setCardPwd(cardPwdEncrypt);

        virtualCard.setValidPeriodIntro(validPeriodIntro);
        virtualCard.setValueRmb(valueRmb);
        virtualCard.setIsSaled(-1);

        long cardCrc32Validate = FungoCRC32Util.getInstance().encrypt(cardSn + cardPwd);
        virtualCard.setCardCrc32(cardCrc32Validate);
        virtualCard.setCardType(22);
        virtualCard.setCreatedAt(new Date());
        virtualCard.setUpdatedAt(new Date());
//        virtualCard.setExt1(email  );
        mallVirtualCardDaoService.insert(virtualCard);
    }

    public static void main(String[] args) {
        FungoVMallGoodsExcelParserService fungoVMallGoodsExcelParserService = new FungoVMallGoodsExcelParserService();
        fungoVMallGoodsExcelParserService.excuteParserToVCard();
    }
    //--------
}
