package com.game.common.dto;

import com.game.common.enums.GoodsTypeStatusEnum;
import com.game.common.enums.ShippingStatusEnum;
import com.game.common.utils.annotation.EnumConver;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>礼包管理页面VO类</p>
 * @Author: dl.zhang
 * @Date: 2019/4/1
 */
public class GoodsDto {

     private String orderId;
     private String orderSn;

     private String goodsName;

     private BigDecimal price;

     @EnumConver(enumType = GoodsTypeStatusEnum.class)
     private String type;

     private String mbNickName;

     private String mbName;

     private String mbMobile;

     private String csgAddress;

     private Date createTime;

     @EnumConver(enumType = ShippingStatusEnum.class)
     private int shippingStatus;

//     private int shippingStatusName;


     public String getOrderId() {
          return orderId;
     }

     public void setOrderId(String orderId) {
          this.orderId = orderId;
     }

     public String getOrderSn() {
          return orderSn;
     }

     public void setOrderSn(String orderSn) {
          this.orderSn = orderSn;
     }

     public String getGoodsName() {
          return goodsName;
     }

     public void setGoodsName(String goodsName) {
          this.goodsName = goodsName;
     }

     public BigDecimal getPrice() {
          return price;
     }

     public void setPrice(BigDecimal price) {
          this.price = price;
     }

     public String getType() {
          return type;
     }

     public void setType(String type) {
          this.type = type;
     }

     public String getMbNickName() {
          return mbNickName;
     }

     public void setMbNickName(String mbNickName) {
          this.mbNickName = mbNickName;
     }

     public String getMbName() {
          return mbName;
     }

     public void setMbName(String mbName) {
          this.mbName = mbName;
     }

     public String getMbMobile() {
          return mbMobile;
     }

     public void setMbMobile(String mbMobile) {
          this.mbMobile = mbMobile;
     }

     public String getCsgAddress() {
          return csgAddress;
     }

     public void setCsgAddress(String csgAddress) {
          this.csgAddress = csgAddress;
     }

     public Date getCreateTime() {
          return createTime;
     }

     public void setCreateTime(Date createTime) {
          this.createTime = createTime;
     }

     public int getShippingStatus() {
          return shippingStatus;
     }

     public void setShippingStatus(int shippingStatus) {
          this.shippingStatus = shippingStatus;
     }

//     public int getShippingStatusName() {
//          return shippingStatusName;
//     }
//
//     public void setShippingStatusName(int shippingStatusName) {
//          this.shippingStatusName = shippingStatusName;
//     }
}
