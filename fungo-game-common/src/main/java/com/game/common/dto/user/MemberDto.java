package com.game.common.dto.user;

import com.game.common.api.InputPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 用户会员Dto
 * </p>
 *
 * @author lzh
 * @since 2018-10-25
 */
@ToString
@Getter
@Setter
public class MemberDto extends InputPageDto implements Serializable  {

	private static final long serialVersionUID = 6368560375678982536L;

	private String id;
    /**
     * 用户加密key
     */
	private String salt;
    /**
     * 最后访问时间
     */
	private Date lastVisit;
    /**
     * 邮箱
     */
	private String email;
    /**
     * 用户token
     */
	private String sesionToken;
    /**
     * 密码
	 *  两次加密：
	 *  客户端一次且大写
	 *  后端一次未大写
     */
	private String password;
    /**
     * 经验(积分)
     */
	private Integer exp;
    /**
     * 用户名称
     */
	private String userName;
    /**
     * 状态 :0正常，1:锁定，2：禁止 -1:删除
     */
	private Integer state;
    /**
     * 是否有密码  1:有，0：没有
     */
	private String hasPassword;
    /**
     * 用户等级
     */
	private Integer level;
    /**
     * 是否邮箱验证
     */
	private String emailVerified;
    /**
     * 手机号
     */
	private String mobilePhoneNum;
    /**
     * 头像
     */
	private String avatar;
    /**
     * 是否注册完成
     */
	private String complete;
    /**
     * 性别：0：女  1:男 2:未知
     */
	private Integer gender;
    /**
     * 第三方登录
     */
	private String authData;
    /**
     * 是否导出
     */
	private String importFromParse;
    /**
     * 是否手机认证
     */
	private String mobilePhoneVerified;
    /**
     * 粉丝数
     */
	private Integer followeeNum;
    /**
     * 发布数量
     */
	private Integer publishNum;
    /**
     * 关注数
     */
	private Integer followerNum;
    /**
     * 被举报数
     */
	private Integer reportNum;
    /**
     * 创建时间
     */
	private Date createdAt;
    /**
     * 更新时间
     */
	private Date updatedAt;
    /**
     * 用户签名
     */
	private String sign;
    /**
     * 用户会员编号
     */
	private String memberNo;
    /**
     * qq_open_id
     */
	private String qqOpenId;
    /**
     * 微信OpenId
     */
	private String weixinOpenId;
    /**
     * 微博open_id
     */
	private String weiboOpenId;
    /**
     * 身份 1:普通 2:官方
     */
	private Integer status;
    /**
     * 标记
     */
	private Integer sort;
    /**
     * 类型 1:普通 2:推荐
     */
	private Integer type;

	/**
	 * 是否关注
	 * true 关注
	 * false 未关注
	 */
	private boolean isFollowed;

	private String test;
	//权限  1 权限正常 2:禁言
	private int auth;

	private String circleId;

	@ApiModelProperty(value="用户官方身份(2.4.3)",example="")
	private List<List<HashMap<String,Object>>> statusImgs = new ArrayList<>();

}
