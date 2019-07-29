package com.fungo.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

@Setter
@Getter
@ToString
public class AdminUserInputDTO {

	private Integer sort;
	private String avatar;
	private Integer gender;
	private String user_name;
	private String mobilePhoneNum;
	private String sign;
	private String password;
	private int level;
	private String hasPassword;

}
