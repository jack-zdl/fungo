package com.fungo.system.function;

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
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * 解析excel文件做成中秋抽奖顺序表
 */
@Component
public class MallSeckillOrderFileService {

    private static final Logger logger = LoggerFactory.getLogger(MallSeckillOrderFileService.class);

    //excel文件路径
    private String excelPath = "D:\\MingBoWork\\运营维护\\发gungo币/愚人节活动Fun币发放.xls";

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

                        //商品id
                        Cell goodIdCell = row.getCell(0);
                        String goodId = goodIdCell.toString();
                        DecimalFormat df = new DecimalFormat("#");
                        switch (goodIdCell.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:// 数字
                                goodId = df.format(goodIdCell.getNumericCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_STRING:// 字符串
                                goodId = df.format(Double.parseDouble(goodIdCell.toString()));
                                break;
                            default:
                                goodId = goodIdCell.toString();
                                break;
                        }

                        //商品名称
                        Cell goodNameCell = row.getCell(1);
                        String goodName = goodNameCell.toString();

                        //系统发放 商品顺序
                        Cell mallOrderNumCell = row.getCell(2);
                        String mallOrderNum = mallOrderNumCell.toString();


                        //系统发放 商品顺序

                        switch (mallOrderNumCell.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:// 数字
                                mallOrderNum = df.format(mallOrderNumCell.getNumericCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_STRING:// 字符串
                                mallOrderNum = df.format(Double.parseDouble(mallOrderNumCell.toString()));
                                break;
                            default:
                                mallOrderNum = mallOrderNumCell.toString();
                                break;
                        }
                        logger.info("解析excel-goodId:{},--goodName:{},mallOrderNum:{}", goodId, goodName, mallOrderNum);
                        if (StringUtils.isNotBlank(goodId) && StringUtils.isNotBlank(mallOrderNum)) {
                            // @todo 插入顺序表
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
}
