package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.dao.IncentAccountCoinDao;
import com.fungo.system.entity.IncentAccountCoin;
import com.fungo.system.entity.Member;
import com.fungo.system.mall.service.commons.FungoMallScanOrderWithSeckillService;
import com.fungo.system.service.IncentAccountCoinDaoService;
import com.fungo.system.service.MemberService;
import com.game.common.buriedpoint.BuriedPointUtils;
import com.game.common.buriedpoint.constants.BuriedPointEventConstant;
import com.game.common.buriedpoint.model.BuriedPointConsumeModel;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.ResultDto;
import com.game.common.repo.cache.facade.FungoCacheTask;
import com.game.common.vo.UserFunVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户分数类账户
积分、经验值等分值 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-12-03
 */
@Service
public class IncentAccountCoinServiceImap extends ServiceImpl<IncentAccountCoinDao, IncentAccountCoin> implements IncentAccountCoinDaoService {

    private static final Logger logger = LoggerFactory.getLogger( IncentAccountCoinServiceImap.class);

    @Autowired
    private IncentAccountCoinDaoService incentAccountCoinService;
    @Autowired
    private FungoMallScanOrderWithSeckillService fungoMallScanOrderWithSeckillServiceImpl;
    @Autowired
    private MemberService memberService;
    @Autowired
    private FungoCacheTask fungoCacheTask;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDto<String> deleteUserFun(UserFunVO userFunVO) {
        ResultDto<String> resultDto = null;
        String memberId = userFunVO.getMemberId();
        int funNumber = userFunVO.getNumber();
        String description  = userFunVO.getDescription();
        Member member = memberService.selectById( memberId);
        try {
            EntityWrapper<IncentAccountCoin> mbAccountCoinEntityWrapper = new EntityWrapper<IncentAccountCoin>();
            Map<String, Object> criteriaMap = new HashMap<String, Object>();
            criteriaMap.put("mb_id", memberId);
            mbAccountCoinEntityWrapper.allEq(criteriaMap);

            IncentAccountCoin incentAccountCoinDB = incentAccountCoinService.selectOne(mbAccountCoinEntityWrapper);

            logger.info("扣减用户fungo币账户被冻结商品价格-扣减前账户详情：{}", JSON.toJSONString(incentAccountCoinDB));
            //账户冻结币量
            BigDecimal coinUsable = incentAccountCoinDB.getCoinUsable();
            //商品价格
            BigDecimal goodsPriceVcyDB = new BigDecimal(funNumber);

            int usableCompResult = coinUsable.compareTo(goodsPriceVcyDB);
            //账户冻结余额 大于等于 商品价格
            if (0 == usableCompResult || 1 == usableCompResult) {

                //账户冻结额 - 被冻结的商品价格 = 新的账户冻结余额
                BigDecimal coinUsableNew = BigDecimal.ZERO;
                coinUsableNew = coinUsableNew.add(coinUsable.subtract(goodsPriceVcyDB));

                IncentAccountCoin incentAccountCoinNew = new IncentAccountCoin();
                incentAccountCoinNew.setCoinUsable(coinUsableNew);

                logger.info("扣减用户fungo币账户被冻结商品价格-扣减计算后，账户详情：{}", JSON.toJSONString(incentAccountCoinNew));
                //执行账户更新
                boolean updateOk = incentAccountCoinService.update(incentAccountCoinNew, mbAccountCoinEntityWrapper);
                logger.info("扣减用户fungo币账户被冻结商品价格-用户Id:{}--fungo币账户被冻结商品价格-商品价格:{}--账户冻结币量:{}--执行结果:{}", userFunVO.getMemberId(), userFunVO.getNumber(),
                        coinUsableNew.toString(), updateOk);

                 resultDto = ResultDto.ResultDtoFactory.buildSuccess( "扣除用户fun币成功" );
                //7. 记录用户fungo币消费明细
                fungoMallScanOrderWithSeckillServiceImpl.addFungoPayLogs(memberId, member.getMobilePhoneNum(), member.getUserName(), String.valueOf(funNumber),
                        description);
            }else {
                //账户冻结额 - 被冻结的商品价格 = 新的账户冻结余额
                BigDecimal coinUsableNew = BigDecimal.ZERO;
//                coinUsableNew = coinUsableNew.add(coinUsable.subtract(goodsPriceVcyDB));

                IncentAccountCoin incentAccountCoinNew = new IncentAccountCoin();
                incentAccountCoinNew.setCoinFreeze(coinUsableNew);

                logger.info("扣减fun币账户详情：{}", JSON.toJSONString(incentAccountCoinNew));
                //执行账户更新
                boolean updateOk = incentAccountCoinService.update(incentAccountCoinNew, mbAccountCoinEntityWrapper);
                logger.info("扣减用户fungo币账户被冻结商品价格-用户Id:{}--fungo币账户被冻结商品价格-商品价格:{}--账户冻结币量:{}--执行结果:{}", userFunVO.getMemberId(), userFunVO.getNumber(),
                        coinUsableNew.toString(), updateOk);

                resultDto = ResultDto.ResultDtoFactory.buildSuccess( "扣除用户fun币成功" );
                if(0 != coinUsable.compareTo( BigDecimal.ZERO ) ){
                    //7. 记录用户fungo币消费明细
                    fungoMallScanOrderWithSeckillServiceImpl.addFungoPayLogs(memberId, member.getMobilePhoneNum(), member.getUserName(), String.valueOf(coinUsable), description);
                }
            }
        }catch (Exception e){
            logger.error( "扣除用户fun异常",e );
            resultDto = ResultDto.ResultDtoFactory.buildSuccess( "-1","扣除用户fun异常" );
        }
        //扫描处理订单-清除缓存的用户fun币消耗
        fungoCacheTask.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST + memberId, "", null);
        // 个人信息缓存
        fungoCacheTask.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + memberId, "", null);
        return resultDto;
    }
}
