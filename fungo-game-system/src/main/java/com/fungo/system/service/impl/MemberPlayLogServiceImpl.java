package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fungo.system.dao.MemberPlayLogDao;
import com.fungo.system.dto.ALiPayAsynResultDTO;
import com.fungo.system.dto.WeiXinPayAsynResultDTO;
import com.fungo.system.entity.MemberPlayLog;
import com.fungo.system.helper.lingka.LingKaHelper;
import com.fungo.system.service.MemberPlayLogService;
import com.game.common.dto.ResultDto;
import com.game.common.util.JsonUtil;
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

import static com.game.common.util.lingka.LingKaConstant.LINGKA_ALI_PAY_LOG;
import static com.game.common.util.lingka.LingKaConstant.LINGKA_WECHAT_PAY_LOG;

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
    public ResultDto<String> saveAliMemberPalyLog( HttpServletRequest request , Map alipayMap) throws Exception {
        ResultDto<String> resultDto = null;
        try {
            System.out.println("-----------------------------"+JSON.toJSONString( alipayMap ));
            LOGGER.error( "----------------------- " +JSON.toJSONString( alipayMap ));
            if(alipayMap.size() > 0 ){

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
                Map<String,Object> hashMap = lingKaHelper.sendPayLogToLingka(LINGKA_ALI_PAY_LOG,alipayMap);
                if(hashMap != null){
                    memberPlayLog.setState("1");
                    memberPlayLog.setCouponId( (String) hashMap.get("orderId") );
                    memberPlayLog.setCouponType( (String) hashMap.get( "orderType" ) );
                    memberPlayLog.setMemberPhone( (String) hashMap.get("phone") );
                    memberPlayLog.setResponseContent( JSON.toJSONString( hashMap));
                    memberPlayLog.setUpdatedAt(new Date());
                    memberPlayLog.setResponseContent( JSONObject.toJSONString(hashMap) );
                    memberPlayLog.updateById();
                 }
                 resultDto = ResultDto.ResultDtoFactory.buildSuccess( "保存用户支付日志" );

            }else {
                throw new Exception( "保存支付宝用户支付日志异常" );
            }
        }catch (Exception e){
            LOGGER.error( "保存用户支付日志",e );
           throw new Exception( "保存支付宝用户支付日志异常" );
        }
        return resultDto;
    }

    @Override
    public void saveWeChatMemberPalyLog(Map<String,String> prepayMap,HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {

            //5、判断回调信息是否成功
//            if("SUCCESS".equals(sortedMap.get("result_code"))){
//
//                //获取商户订单号
//                //商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
//                String outTradeNo = sortedMap.get("out_trade_no");
//                System.out.println(outTradeNo);
//                //6、数据库查找订单,如果存在则根据订单号更新该订单
//                VideoOrder dbVideoOrder = videoOrderService.findByOutTradeNo(outTradeNo);
//                System.out.println(dbVideoOrder);
//                if(dbVideoOrder != null && dbVideoOrder.getState()==0){  //判断逻辑看业务场景
//                    VideoOrder videoOrder = new VideoOrder();
//                    videoOrder.setOpenid(sortedMap.get("openid"));
//                    videoOrder.setOutTradeNo(outTradeNo);
//                    videoOrder.setNotifyTime(new Date());
//                    //修改支付状态，之前生成的订单支付状态是未支付，这里表面已经支付成功的订单
//                    videoOrder.setState(1);
//                    //根据商户订单号更新订单
//                    int rows = videoOrderService.updateVideoOderByOutTradeNo(videoOrder);
//                    System.out.println(rows);
//                    //7、通知微信订单处理成功
//                    if(rows == 1){
//                        response.setContentType("text/xml");
//                        response.getWriter().println("success");
//                        return;
//                    }
//                }
//            }
//            LOGGER.error("++++++++++++++++++++++++++++++"+sb);
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

//                Map<String, Object> prepayMap = JsonUtil.getPrepayMapInfo(notityXml);
            if(!prepayMap.isEmpty()){
               if("SUCCESS".equals(prepayMap.get("result_code"))){
                    String orderId = prepayMap.get("out_trade_no");
                    String resCode=prepayMap.get("result_code");
                    String returnCode=prepayMap.get("return_code");
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
                    Map<String,Object> hashMap = lingKaHelper.sendPayLogToLingka(LINGKA_WECHAT_PAY_LOG,prepayMap);
                    if(hashMap != null){
                        memberPlayLog.setState("1");
                        memberPlayLog.setCouponId( (String) hashMap.get("orderId") );
                        memberPlayLog.setCouponType( (String) hashMap.get( "orderType" ) );
                        memberPlayLog.setMemberPhone( (String) hashMap.get("phone") );
                        memberPlayLog.setResponseContent( JSON.toJSONString( hashMap));
                        memberPlayLog.setUpdatedAt(new Date());
                        memberPlayLog.updateById();
                    }
                }else {
                   throw new Exception( "result_code不等于SUCCESS");
               }
            }else{
                throw new Exception( "参数为空");
            }
        }catch (Exception e){
            LOGGER.error( "保存用户微信支付日志",e );
            throw new Exception( "保存用户微信支付日志异常");
        }
//        return resultDto;
    }

    /**
     * 功能描述: 檢查所有的未完成回調的支付寶回調
     * @date: 2019/10/28 11:53
     */
    @Override
    @Async
    public void checkAllALiPay() {
        try {
            List<MemberPlayLog> memberPlayLogs = memberPlayLogDao.getAllByState( "0" );
            memberPlayLogs.stream().filter( x -> ("1".equals(x.getPayType()))).forEach( s ->{
                String mapString = s.getDescription();
                JSONObject jsonObject = (JSONObject) JSON.parse( mapString);
                Map<String,Object> hashMap = lingKaHelper.sendPayLogToLingka(LINGKA_ALI_PAY_LOG,jsonObject);
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
            memberPlayLogs.stream().filter( x -> ("2".equals(x.getPayType()))).forEach( s ->{
                String mapString = s.getDescription();
                JSONObject jsonObject = (JSONObject) JSON.parse( mapString);
                Map<String,Object> hashMap = lingKaHelper.sendPayLogToLingka(LINGKA_WECHAT_PAY_LOG,jsonObject);
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
