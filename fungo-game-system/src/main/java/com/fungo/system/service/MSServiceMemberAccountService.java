package com.fungo.system.service;


import java.io.IOException;

/**
 * <p>
 *   用户账户业务层
 * </p>
 *
 * @author lzh
 * @since 2018-06-22
 */
public interface MSServiceMemberAccountService {

    /**
     * 扣减用户积分
     * @param mb_id
     * @param score
     * @return
     * @throws IOException
     */
    public boolean subtractAccountScore(String mb_id, Integer score) throws IOException ;

}
