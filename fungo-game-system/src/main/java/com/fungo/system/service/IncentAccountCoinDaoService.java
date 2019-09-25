package com.fungo.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.system.entity.IncentAccountCoin;
import com.game.common.dto.ResultDto;
import com.game.common.vo.UserFunVO;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 *       用户虚拟币账户Dao层业务类
 * </p>
 *
 * @author lzh
 * @since 2018-12-03
 */
@Service
public interface IncentAccountCoinDaoService extends IService<IncentAccountCoin> {

    ResultDto<String> deleteUserFun(UserFunVO userFunVO);
}
