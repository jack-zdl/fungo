package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fungo.system.dao.MemberPlayLogDao;
import com.fungo.system.dto.ALiPayAsynResultDTO;
import com.fungo.system.dto.ALiPayResultDTO;
import com.fungo.system.entity.MemberPlayLog;
import com.fungo.system.service.MemberPlayLogService;
import com.game.common.dto.ResultDto;
import com.game.common.vo.MemberPlayLogVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/10/22
 */
@Service
public class MemberPlayLogServiceImpl implements MemberPlayLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger( MemberPlayLogServiceImpl.class);

    @Autowired
    private MemberPlayLogDao memberPlayLogDao;

    /**
     * 功能描述: 保存用户支付日志
     * @date: 2019/10/22 10:44
     */
    @Override
    public ResultDto<String> saveMemberPalyLog(Map alipayMap) {
        ResultDto<String> resultDto = null;
        try {
            String str = JSON.toJSONString(alipayMap);// java对象转为jsonString
            ALiPayAsynResultDTO aLiPayAsynResultDTO = JSON.parseObject(str, ALiPayAsynResultDTO.class);// jsonString转为java对象

            MemberPlayLog param = new MemberPlayLog();
            param.setPlayId( aLiPayAsynResultDTO.getTrade_no());
            MemberPlayLog memberPlay = memberPlayLogDao.selectOne( param);
            if(memberPlay != null && StringUtils.isNotBlank(memberPlay.getId())){
                resultDto = ResultDto.ResultDtoFactory.buildSuccess( "用户支付日志重复记录" );
                return resultDto;
            }
            MemberPlayLog memberPlayLog = new MemberPlayLog();
            memberPlayLog.setPlayId(aLiPayAsynResultDTO.getTrade_no()  );
            memberPlayLog.setBusinessId(aLiPayAsynResultDTO.getOut_trade_no() );
            memberPlayLog.setPayType("1"  ); //1 支付宝
            memberPlayLog.setOutBizNo( aLiPayAsynResultDTO.getOut_biz_no() );
            memberPlayLog.setBuyerLogonId( aLiPayAsynResultDTO.getBuyer_logon_id() );
            memberPlayLog.setSellerRmail( aLiPayAsynResultDTO.getSeller_email());
            memberPlayLog.setTradeStatus( aLiPayAsynResultDTO.getTrade_status());
            memberPlayLog.setReceiptAmount( aLiPayAsynResultDTO.getReceipt_amount());
            memberPlayLog.setBuyerPayAmount( aLiPayAsynResultDTO.getBuyer_pay_amount());
            memberPlayLog.setRefundFee( aLiPayAsynResultDTO.getRefund_fee() );
            JSONObject jsonObject = new JSONObject(  );
            jsonObject.put( "subject",aLiPayAsynResultDTO.getSubject() );
            jsonObject.put( "body",aLiPayAsynResultDTO.getBody() );
            jsonObject.put( "fund_bill_list",aLiPayAsynResultDTO.getFund_bill_list() );
            jsonObject.put( "passback_params", aLiPayAsynResultDTO.getPassback_params());
            jsonObject.put( "voucher_detail_list", aLiPayAsynResultDTO.getVoucher_detail_list());
            memberPlayLog.setContent( JSON.toJSONString( jsonObject ) );
            memberPlayLog.setGmtPayment( aLiPayAsynResultDTO.getGmt_payment() );
            memberPlayLog.setGmtRefund( aLiPayAsynResultDTO.getGmt_refund() );
            jsonObject = new JSONObject(  );
            jsonObject.put( "gmt_create",aLiPayAsynResultDTO.getGmt_create() );
            jsonObject.put( "gmt_close",aLiPayAsynResultDTO.getGmt_close() );
            jsonObject.put( "notify_time",aLiPayAsynResultDTO.getNotify_time() );
            memberPlayLog.setDateContent( JSON.toJSONString( jsonObject ) );
            jsonObject = new JSONObject(  );
            jsonObject.put( "buyer_id",aLiPayAsynResultDTO.getBuyer_id() );
            jsonObject.put( "seller_id",aLiPayAsynResultDTO.getSeller_id() );
            jsonObject.put( "total_amount",aLiPayAsynResultDTO.getTotal_amount() );
            jsonObject.put( "invoice_amount",aLiPayAsynResultDTO.getInvoice_amount() );
            jsonObject.put( "point_amount",aLiPayAsynResultDTO.getPoint_amount());
            memberPlayLog.setMoneyContent( JSON.toJSONString( jsonObject ) );
            jsonObject = new JSONObject(  );
            jsonObject.put( "notify_type",aLiPayAsynResultDTO.getNotify_type() );
            jsonObject.put( "notify_type",aLiPayAsynResultDTO.getNotify_id() );
            jsonObject.put( "notify_type",aLiPayAsynResultDTO.getCharset() );
            jsonObject.put( "notify_type",aLiPayAsynResultDTO.getVersion() );
            jsonObject.put( "notify_type",aLiPayAsynResultDTO.getSign_type() );
            jsonObject.put( "notify_type",aLiPayAsynResultDTO.getSign() );
            memberPlayLog.setHeaderContent(  JSON.toJSONString( jsonObject ));
            memberPlayLog.setIsactive("1");
            memberPlayLog.setCreatedAt( new Date());
            memberPlayLog.setUpdatedAt(new Date());
            memberPlayLog.setRversion(1);
            memberPlayLog.setDescription("");
            memberPlayLog.insert();
            resultDto = ResultDto.ResultDtoFactory.buildSuccess( "保存用户支付日志" );
        }catch (Exception e){
            LOGGER.error( "保存用户支付日志",e );
            resultDto = ResultDto.ResultDtoFactory.buildError( "保存用户支付日志异常" );
        }
        return resultDto;
    }
}
