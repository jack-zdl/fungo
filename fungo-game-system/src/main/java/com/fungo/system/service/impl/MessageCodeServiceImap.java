package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.dao.MessageCodeDao;
import com.fungo.system.entity.MessageCode;
import com.fungo.system.service.MessageCodeService;
import com.game.common.aliyun.dysmsapi.AliyunSendSmsClient;
import com.game.common.dto.ResultDto;
import com.game.common.dto.MsgResult;
import com.game.common.util.date.DateTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 短信验证 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-04-13
 */
@Service
public class MessageCodeServiceImap extends ServiceImpl<MessageCodeDao, MessageCode> implements MessageCodeService {

    @Autowired
    private AliyunSendSmsClient aliyunSendSmsClient;

    /**
     * 注入阿里云短信模块编号
     */
    @Value("${aliyun.sms.template.code}")
    private String smsCodeLoginRegsTemp;

    /**
     * 创建验证码
     * @param type
     * @param code
     * @param times
     * @return
     * @throws Exception
     */
    @Override
    public ResultDto<String> sendCode(String type, String phone, String code, int times) throws Exception {
        MessageCode messageCode = new MessageCode();
        messageCode.setMsgType(type);
        messageCode.setCreatedAt(new Date());
        messageCode.setIsUsed("0");
        messageCode.setMsgCode(code);
        messageCode.setExpiration(DateTools.getAfterMinutes(times));
        messageCode.setPhoneNumber(phone);
        messageCode.setState(1);
        this.insert(messageCode);

        //fix:使用新的短信模板[by mxf 2019-03-01]
        //历史短信模板：SMS_132401492
        MsgResult re = aliyunSendSmsClient.sendSms(phone, smsCodeLoginRegsTemp, "{\"code\":\"" + code + "\"}");
//        end
        if (!re.isSuccess()) {
            messageCode.setState(0);
            messageCode.setReMsg(re.getMsg());
            messageCode.updateById();
        }
        return ResultDto.success();
    }

    /**
     * 验证短信
     * @param type
     * @param phone
     * @param code
//     * @param times
     * @return
     */
    @Override
    public ResultDto<String> checkCode(String type, String phone, String code) {
        ResultDto<String> re = new ResultDto<String>();
        MessageCode messageCode = this.selectOne(new EntityWrapper<MessageCode>().eq("msg_type", type).eq("msg_code", code).eq("phone_number", phone).eq("is_used", "0"));
        if (null == messageCode || messageCode.getExpiration().before(new Date())) {
            return ResultDto.error("603", "无效的短信验证码");
        } else {
            re.setData(messageCode.getId());
            return re;
        }
    }

    /**
     * 更新验证码作废
     */
    @Override
    public ResultDto<String> updateCheckCodeSuccess(String msgId) {
        MessageCode messageCode = this.selectById(msgId);
        messageCode.setIsUsed("1");
        messageCode.setUpdatedAt(new Date());
        messageCode.updateById();
        return ResultDto.success();
    }

    /**
     * 验证短信码并且更新作废
     */
    @Override
    public ResultDto<String> checkCodeAndSuccess(String type, String phone, String code) {
        MessageCode messageCode = this.selectOne(new EntityWrapper<MessageCode>().eq("msg_type", type).eq("msg_code", code).eq("phone_number", phone).eq("is_used", "0"));
        if (null == messageCode || messageCode.getExpiration().before(new Date())) {
            return ResultDto.error("603", "验证码错误,请重新输入");
        } else {
            messageCode.setIsUsed("1");
            messageCode.setUpdatedAt(new Date());
            messageCode.updateById();
            return ResultDto.success();
        }
    }

}
