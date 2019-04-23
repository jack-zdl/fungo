package com.game.common.dto;

import java.io.Serializable;

/**
 * @author sam.shi
 */
public interface UserProfile extends Serializable {

	public String getLoginId();

	public String getName();

}
