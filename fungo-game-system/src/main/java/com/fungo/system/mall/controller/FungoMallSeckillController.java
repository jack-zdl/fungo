package com.fungo.system.mall.controller;


import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.fungo.system.mall.service.IFungoMallGoodsService;
import com.fungo.system.mall.service.IFungoMallSeckillService;
import com.fungo.system.mall.service.commons.FungoMallSeckillTaskStateCommand;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.mall.MallOrderInput;
import com.game.common.util.date.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     秒杀controller
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@RestController
public class FungoMallSeckillController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FungoMallSeckillController.class);

    @Autowired
    private IFungoMallSeckillService iFungoMallSeckillService;

    @Autowired
    private IFungoMallGoodsService iFungoMallGoodsService;

    /**
     * 数据迁移接口授权秘钥用于操作授权
     */
    @Value("${fgoTransfAuth}")
    private String fgoTransfAuth;


    /**
     * 获取每日秒杀商品列表接口
     * @return
     */
    @PostMapping("/api/mall/goods/seckill/list")
    public ResultDto<String> getGoodsListForSeckill(MemberUserProfile memberUserPrefile, HttpServletRequest request) {

        String loginId = null;
        if (null != memberUserPrefile) {
            loginId = memberUserPrefile.getLoginId();
        }


        String realIp = "";
        if (null != request) {
            realIp = request.getHeader("x-forwarded-for");
        }

        String goodsListForSeckill = iFungoMallSeckillService.getGoodsListForSeckillJson(loginId, realIp);

        if (StringUtils.isNoneBlank(goodsListForSeckill)) {
            ResultDto<String> resultDto = new ResultDto<String>();

            resultDto.setData(goodsListForSeckill);
            resultDto.setMessage(DateTools.fmtDate(new Date()));

            return resultDto;
        }
        return ResultDto.success(DateTools.fmtDate(new Date()));
    }


    /**
     * 获取秒杀授权码接口
     * @return
     */
    @PostMapping("/api/mall/goods/seckill/code")
    public ResultDto<Map<String, String>> getSeckillGrantCode(MemberUserProfile memberUserPrefile) {

        String grandCode = iFungoMallSeckillService.getSeckillGrantCode(memberUserPrefile.getLoginId());
        if (StringUtils.isNoneBlank(grandCode)) {
            ResultDto<Map<String, String>> resultDto = new ResultDto<Map<String, String>>();
            Map<String, String> codeMap = new HashMap<String, String>();
            codeMap.put("code", grandCode);
            resultDto.setData(codeMap);
            return resultDto;
        }
        return ResultDto.error("-1", "获取秒杀授权码失败");
    }


    /**
     * 秒杀下单接口
     * @return
     */
    @PostMapping("/api/mall/order/seckill")
    public ResultDto<Map> createOrderWithSeckill(MemberUserProfile memberUserPrefile, @RequestBody MallOrderInput mallOrderInput, HttpServletRequest request) {

        ResultDto<Map> resultDto = null;
        try {

            String code = mallOrderInput.getCode();
            if (null == code || StringUtils.isBlank(code)) {
                return ResultDto.success("未被授权的操作，请联系系统管理员!");
            }
            
            String goodsId = mallOrderInput.getGoodsId();
            if (null == mallOrderInput || StringUtils.isBlank(goodsId)) {
                return ResultDto.success("请选择要秒杀的商品");
            }
            mallOrderInput.setMbId(memberUserPrefile.getLoginId());

            String realIp = "";
            if (null != request) {
                realIp = request.getHeader("x-forwarded-for");
            }

            //执行下单
            Map<String, Object> orderMap = iFungoMallSeckillService.createOrderWithSeckill(mallOrderInput, realIp);
            if (null == orderMap) {
                return ResultDto.success("秒杀通道非常拥挤，本次秒杀失败，请继续秒杀");
            }

            resultDto = new ResultDto<Map>();
            resultDto.setData(orderMap);

        } catch (Exception ex) {
            resultDto = ResultDto.error("-1", "秒杀通道非常拥挤，本次秒杀失败，请继续秒杀");
            //新增状态码：-2 在服务器异常的情况下，H5基于此状态码 取消 菊花
            resultDto.setStatus(-2);
            ex.printStackTrace();
        }
        return resultDto;
    }


    /**
     * 秒杀订单修改接口
     * ps:用于秒杀成功后，记录订单收货人信息
     * @return
     */
    @PutMapping("/api/mall/order/seckill")
    public ResultDto<String> updateOrderWithSeckill(MemberUserProfile memberUserPrefile, @RequestBody MallOrderInput mallOrderInput) {

        if (null == mallOrderInput || StringUtils.isBlank(mallOrderInput.getOrderId())) {
            return ResultDto.success("请选择要修改的订单");
        }
        if (null == mallOrderInput || StringUtils.isBlank(mallOrderInput.getConsigneeName())) {
            return ResultDto.success("请输入订单收货人名称");
        }
        if (null == mallOrderInput || StringUtils.isBlank(mallOrderInput.getCsgAddress())) {
            return ResultDto.success("请输入订单收货地址");
        }

        if (null == mallOrderInput || StringUtils.isBlank(mallOrderInput.getCsgMobile())) {
            return ResultDto.success("请输入订单收货人联系电话");
        }

        mallOrderInput.setMbId(memberUserPrefile.getLoginId());
        boolean isUpdate = iFungoMallSeckillService.updateOrderWithSeckill(mallOrderInput);
        if (!isUpdate) {
            return ResultDto.error("-1", "秒杀业务繁忙，修改订单失败，请稍后重试");
        }

        return ResultDto.success("成功修改订单");
    }


    /**
     * 我的秒杀订单列表和查询某个订单详情接口
     *
     * @return
     */
    @PostMapping("/api/mall/user/order/seckill/list")
    public ResultDto<String> getOrdersWithSeckill(MemberUserProfile memberUserPrefile, @RequestBody Map<String, String> param) {


        String orderId = "";
        String orderSn = "";
        if (null != param && !param.isEmpty()) {
            if (param.containsKey("orderId")) {
                orderId = param.get("orderId");
            }
            if (param.containsKey("orderSn")) {
                orderSn = param.get("orderSn");
            }
        }
        String ordersWithSeckill = iFungoMallSeckillService.getOrdersWithSeckillWithJsonStr(memberUserPrefile.getLoginId(), orderId, orderSn);

        if (StringUtils.isBlank(ordersWithSeckill)) {
            return ResultDto.success("暂无订单，请去秒杀商品吧");
        }

        ResultDto<String> resultDto = new ResultDto<String>();
        resultDto.setData(ordersWithSeckill);

        return resultDto;
    }


    /**
     * 在秒杀活动结束后，手动停止 订单扫描定时任务
     * @return
     */
    @RequestMapping(value = "/api/mall/order/scantask", method = RequestMethod.POST)
    public ResultDto<String> grantCoinToMember(@RequestBody Map<String, String> authMap) throws Exception {

        if (isAuthOk(authMap)) {
            String isScanOrder = authMap.get("isScanOrder");
            LOGGER.info("在秒杀活动结束后，手动停止订单扫描定时任务-isScanOrder:{}", isScanOrder);

            if (StringUtils.isNoneBlank(isScanOrder)) {
                FungoMallSeckillTaskStateCommand.getInstance().setIsScanOrder(Integer.parseInt(isScanOrder));
                return ResultDto.success();
            }
            return ResultDto.error("-1", "请输入是否继续扫描订单命令标记");
        }
        return ResultDto.error("-1", "您需要授权操作!");
    }


    /**
     * 添加商品数据
     * @return
     */
    //@PostMapping("/api/mall/goods")
    public ResultDto<String> addGoodsWithSeckill(@RequestBody Map<String, Object> param) {

        Boolean isOk = iFungoMallGoodsService.addGoodsAndSeckill(param);
        if (isOk) {
            return ResultDto.success();
        }
        return ResultDto.error("-1", "添加商品数据失败");
    }


    /**
     * 验证调用该接口是否被授权
     * @param authMap
     * @return
     */
    private Boolean isAuthOk(Map<String, String> authMap) {
        if (null == authMap || authMap.isEmpty()) {
            return false;
        }
        String authValuee = authMap.get("authKey");
        if (StringUtils.equalsIgnoreCase(authValuee, fgoTransfAuth)) {
            return true;
        }
        return false;
    }

}
