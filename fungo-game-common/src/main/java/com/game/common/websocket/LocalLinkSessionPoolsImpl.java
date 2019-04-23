package com.game.common.websocket;

import java.util.*;

public class LocalLinkSessionPoolsImpl implements ILinkSessionPools {
    public static HashMap<String, LinkSession> map = new HashMap<String, LinkSession>();

    private static LocalLinkSessionPoolsImpl instance = null;

    private LocalLinkSessionPoolsImpl() {
    }

    public static synchronized ILinkSessionPools getInstance() {
        if (instance == null) {
            instance = new LocalLinkSessionPoolsImpl();
        }
        return instance;
    }

    @Override
    public void addChannel(LinkSession link) {
        if (!map.containsKey(link.getSid())) {
            map.put(link.getSid(), link);
        }
    }

    @Override
    public void removeChannel(String sid) {
        map.remove(sid);
    }

    @Override
    public LinkSession queryBySid(String sid) {
        return map.get(sid);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public List<LinkSession> queryByUserId(String userId) {
        List<LinkSession> list = new ArrayList<LinkSession>();
        Set<Map.Entry<String, LinkSession>> entryseSet = map.entrySet();
        for (Map.Entry<String, LinkSession> entry : entryseSet) {
            if (userId.equals(entry.getValue().getUserId())) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

    @Override
    public List<LinkSession> queryAllLinkSession() {
        List<LinkSession> list = new ArrayList<LinkSession>();
        Set<Map.Entry<String, LinkSession>> entryseSet = map.entrySet();
        for (Map.Entry<String, LinkSession> entry : entryseSet) {
            list.add(entry.getValue());
        }
        return list;
    }

}
