package com.game.common.util.lingka;

/**
 * <p></p>
 * success: 操作是否成功
 * status: 礼品券状态
 * cardId：礼品券id
 * password: 礼品券密码
 * @Author: dl.zhang
 * @Date: 2019/10/23
 */
public class BindGiftcardDto {

    private boolean result;

    private String  success;

    private String status;

    private  int cardId;

    private String password;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
