package com.fungo.system.service.impl;

import com.fungo.system.service.IPKNoService;
import com.game.common.util.PKUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PKNoServiceImpl implements IPKNoService {

    private static final Logger logger = LoggerFactory.getLogger(PKNoServiceImpl.class);


    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;

    @Override
    public String genUniqueMbNo(String mb_id) {

        String mb_no = "";
        //2019031115581071711
        Integer clusterIndex_i = Integer.parseInt(clusterIndex);
        long longPK = PKUtil.getInstance(clusterIndex_i).longPK();
        String longPKStr = String.valueOf(longPK);
        //截取2位年2位月
        String idYearMonth = StringUtils.substring(longPKStr, 2, 6);
//        PC2.0需求变更 member_no改为8位
//        2019-06-04
//        lyc
        idYearMonth = idYearMonth.substring(1, 2) + idYearMonth.substring(3);
        //默认是2位年2位月 + 后6位
        if (StringUtils.isBlank(mb_id)) {
            String grapNo = StringUtils.substring(longPKStr, longPKStr.length() - 6);
            mb_no = idYearMonth + grapNo;
            return mb_no;
        }

        String mbIdHashCode = String.valueOf(Math.abs(mb_id.hashCode()));
        //获取PK的后2位
        String grapNoPKStr = StringUtils.substring(longPKStr, longPKStr.length() - 2);
        //补全6位尾数
        int mbCodelen = mbIdHashCode.length();
        if (4 == mbCodelen) {
            mb_no = idYearMonth + grapNoPKStr + mbIdHashCode;
        } else if (4 > mbCodelen) {
            //缺的位数
            int grapLen = 4 - mbCodelen;
            String grapNo = StringUtils.substring(longPKStr, longPKStr.length() - grapLen - grapNoPKStr.length());
            mb_no = idYearMonth + grapNo + mbIdHashCode;
        } else if (4 < mbCodelen) {
            //多的位数
            String grapNo = StringUtils.substring(mbIdHashCode, mbIdHashCode.length() - 4);
            mb_no = idYearMonth + grapNoPKStr + grapNo;
        }

        return mb_no;
    }

}
