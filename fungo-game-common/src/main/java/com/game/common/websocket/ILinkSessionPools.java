package com.game.common.websocket;

import java.util.List;

/**
 * 连接管理器
 * @author sam.shi
 */
public interface ILinkSessionPools {
	public  void addChannel(LinkSession link);
	public void removeChannel(String sid);
    public LinkSession queryBySid(String sid);
    public List<LinkSession> queryByUserId(String userId);
	public int size() ;
    public List<LinkSession> queryAllLinkSession();
}
