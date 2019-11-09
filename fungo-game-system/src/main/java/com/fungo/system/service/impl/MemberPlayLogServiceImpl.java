package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fungo.system.dao.MemberPlayLogDao;
import com.fungo.system.dto.ALiPayAsynResultDTO;
import com.fungo.system.dto.ALiPayResultDTO;
import com.fungo.system.dto.WeiXinPayAsynResultDTO;
import com.fungo.system.entity.MemberPlayLog;
import com.fungo.system.helper.lingka.LingKaHelper;
import com.fungo.system.service.MemberPlayLogService;
import com.game.common.dto.ResultDto;
import com.game.common.util.JsonUtil;
import com.game.common.vo.MemberPlayLogVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
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
    @Autowired
    private LingKaHelper lingKaHelper;
    /**
     * 功能描述: 保存用户支付日志
     * @date: 2019/10/22 10:44
     */
    @Override
    public ResultDto<String> saveMemberPalyLog( HttpServletRequest request , Map alipayMap) {
        ResultDto<String> resultDto = null;
        try {
            System.out.println("-----------------------------"+JSON.toJSONString( alipayMap ));
            LOGGER.error( "----------------------- " +JSON.toJSONString( alipayMap ));
            String str = JSON.toJSONString(alipayMap);// java对象转为jsonString
            ALiPayAsynResultDTO aLiPayAsynResultDTO = JSON.parseObject(str, ALiPayAsynResultDTO.class);// jsonString转为java对象

            MemberPlayLog param = new MemberPlayLog();
            param.setPlayId( aLiPayAsynResultDTO.getTrade_no());
            MemberPlayLog memberPlay = memberPlayLogDao.selectOne( param);
            if(memberPlay != null && StringUtils.isNotBlank(memberPlay.getId())){
                resultDto = ResultDto.ResultDtoFactory.buildSuccess( "用户支付日志重复记录" );
                LOGGER.error( "用户支付日志重复记录,主键id="+aLiPayAsynResultDTO.getTrade_no());
                return resultDto;
            }
            MemberPlayLog memberPlayLog = new MemberPlayLog();
            memberPlayLog.setPlayId(aLiPayAsynResultDTO.getTrade_no()  );
            memberPlayLog.setBusinessId(aLiPayAsynResultDTO.getOut_trade_no() );
            memberPlayLog.setPayType("1"  ); //1 支付宝
            memberPlayLog.setPayState( "1");
            memberPlayLog.setAppId(aLiPayAsynResultDTO.getApp_id() );
            memberPlayLog.setOutBizNo( aLiPayAsynResultDTO.getOut_biz_no() );
            memberPlayLog.setBuyerLogonId( aLiPayAsynResultDTO.getBuyer_logon_id() );
            memberPlayLog.setSellerRmail( aLiPayAsynResultDTO.getSeller_email());
            memberPlayLog.setTradeStatus( aLiPayAsynResultDTO.getTrade_status());
            memberPlayLog.setReceiptAmount( String.valueOf(aLiPayAsynResultDTO.getReceipt_amount()));
            memberPlayLog.setBuyerPayAmount(String.valueOf(aLiPayAsynResultDTO.getBuyer_pay_amount()));
            memberPlayLog.setRefundFee(String.valueOf(aLiPayAsynResultDTO.getRefund_fee()));
            JSONObject jsonObject = new JSONObject(  );
            jsonObject.put( "subject",aLiPayAsynResultDTO.getSubject() );
            jsonObject.put( "body",aLiPayAsynResultDTO.getBody() );
            jsonObject.put( "fund_bill_list",aLiPayAsynResultDTO.getFund_bill_list() );
            jsonObject.put( "passback_params", aLiPayAsynResultDTO.getPassback_params());
            jsonObject.put( "voucher_detail_list", aLiPayAsynResultDTO.getVoucher_detail_list());
            memberPlayLog.setContent( JSON.toJSONString( jsonObject ) );
            memberPlayLog.setGmtPayment(String.valueOf(aLiPayAsynResultDTO.getGmt_payment()));
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
            jsonObject.put( "notify_id",aLiPayAsynResultDTO.getNotify_id() );
            jsonObject.put( "charset",aLiPayAsynResultDTO.getCharset() );
            jsonObject.put( "version",aLiPayAsynResultDTO.getVersion() );
            jsonObject.put( "sign_type",aLiPayAsynResultDTO.getSign_type() );
            jsonObject.put( "sign",aLiPayAsynResultDTO.getSign() );
            memberPlayLog.setHeaderContent(  JSON.toJSONString( jsonObject ));
            memberPlayLog.setState("0");
            memberPlayLog.setIsactive("1");
            memberPlayLog.setCreatedAt( new Date());
            memberPlayLog.setUpdatedAt(new Date());
            memberPlayLog.setRversion(1);
            memberPlayLog.setDescription(JSON.toJSONString( alipayMap ));
            memberPlayLog.insert();
            Map<String,Object> hashMap = lingKaHelper.sendPayLogToLingka(alipayMap);
            if(hashMap != null){
                memberPlayLog.setState("1");
                memberPlayLog.setCouponId( (String) hashMap.get("orderId") );
                memberPlayLog.setCouponType( (String) hashMap.get( "orderType" ) );
                memberPlayLog.setMemberPhone( (String) hashMap.get("phone") );
                memberPlayLog.setResponseContent( JSON.toJSONString( hashMap));
                memberPlayLog.setUpdatedAt(new Date());
                memberPlayLog.updateById();
            }
            resultDto = ResultDto.ResultDtoFactory.buildSuccess( "保存用户支付日志" );
        }catch (Exception e){
            LOGGER.error( "保存用户支付日志",e );
            resultDto = ResultDto.ResultDtoFactory.buildError( "保存用户支付日志异常" );
        }
        return resultDto;
    }

    @Override
    public ResultDto<String> saveWeiXinMemberPalyLog(HttpServletRequest request, HttpServletResponse response) {
        ResultDto<String> resultDto = null;
        try {
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder("");
            try
            {
                br = request.getReader();
                String str;
                while ((str = br.readLine()) != null)
                {
                    sb.append(str);
                }
                br.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            System.out.println("++++++++++++++++++++++++++++++"+sb);
            LOGGER.error("++++++++++++++++++++++++++++++"+sb);
//            ResultBean result = new ResultBean();
            String inputLine="";
            String notityXml = "";
            try {
                while ((inputLine = request.getReader().readLine()) != null) {
                    notityXml += inputLine;
                }
                request.getReader().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(!notityXml.isEmpty()){
                Map<String, Object> prepayMap = JsonUtil.getPrepayMapInfo(notityXml);
                if(!prepayMap.isEmpty()){
                    String orderId = prepayMap.get("out_trade_no")+"";
                    String resCode=prepayMap.get("result_code")+"";
                    String returnCode=prepayMap.get("return_code")+"";
                    System.out.println("解析并读取统一下单中的参数信息:"+orderId+"==="+resCode+"==="+returnCode);
                    String str = JSON.toJSONString(prepayMap);// java对象转为jsonString
                    WeiXinPayAsynResultDTO weiXinPayAsynResultDTO = JSON.parseObject(str, WeiXinPayAsynResultDTO.class);// jsonString转为java对象
                    MemberPlayLog memberPlayLog = new MemberPlayLog();
                    memberPlayLog.setPlayId(weiXinPayAsynResultDTO.getTransaction_id()  );
                    memberPlayLog.setBusinessId(weiXinPayAsynResultDTO.getOut_trade_no() );
                    memberPlayLog.setPayType("2"  ); //1 支付宝
                    memberPlayLog.setPayState( "1");
                    memberPlayLog.setAppId( weiXinPayAsynResultDTO.getAppid());
                    memberPlayLog.setBuyerPayAmount( weiXinPayAsynResultDTO.getCash_fee());
                    memberPlayLog.setState("0");
                    memberPlayLog.setIsactive("1");
                    memberPlayLog.setCreatedAt( new Date());
                    memberPlayLog.setUpdatedAt(new Date());
                    memberPlayLog.setRversion(1);
                    memberPlayLog.setDescription( JSON.toJSONString(prepayMap));
                    memberPlayLog.insert();
                    Map<String,Object> hashMap = lingKaHelper.sendPayLogToLingka(prepayMap);
                    if(hashMap != null){
                        memberPlayLog.setState("1");
                        memberPlayLog.setCouponId( (String) hashMap.get("orderId") );
                        memberPlayLog.setCouponType( (String) hashMap.get( "orderType" ) );
                        memberPlayLog.setMemberPhone( (String) hashMap.get("phone") );
                        memberPlayLog.setResponseContent( JSON.toJSONString( hashMap));
                        memberPlayLog.setUpdatedAt(new Date());
                        memberPlayLog.updateById();
                    }
                }
            }else{
//                result.fillCode(ResultBeanCodeEnum.OPERA_FAIL);
            }
            resultDto = ResultDto.ResultDtoFactory.buildSuccess( "保存用户微信支付日志" );
        }catch (Exception e){
            LOGGER.error( "保存用户微信支付日志",e );
            resultDto = ResultDto.ResultDtoFactory.buildError( "保存用户微信支付日志异常" );
        }
        return resultDto;
    }

    /**
     * 功能描述: 檢查所有的為完成回調的支付寶回調
     * @date: 2019/10/28 11:53
     */
    @Override
    @Async
    public void checkAllALiPay() {
        try {
            List<MemberPlayLog> memberPlayLogs = memberPlayLogDao.getAllByState( "0" );
            memberPlayLogs.stream().forEach( s ->{
                String mapString = s.getDescription();
                JSONObject jsonObject = (JSONObject) JSON.parse( mapString);
                Map<String,Object> hashMap = lingKaHelper.sendPayLogToLingka(jsonObject);
                if(hashMap != null){
                    s.setState("1");
                    s.setCouponId( (String) hashMap.get("orderId") );
                    s.setCouponType( (String) hashMap.get( "orderType" ) );
                    s.setMemberPhone( (String) hashMap.get("phone") );
                    s.setResponseContent( JSON.toJSONString( hashMap));
                    s.setUpdatedAt(new Date());
                    s.updateById();
                }
            });
        }catch (Exception e){
            LOGGER.error( "檢查所有的為完成回調的支付寶回調异常",e );
        }
    }
}
