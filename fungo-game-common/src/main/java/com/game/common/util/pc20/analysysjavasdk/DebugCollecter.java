package com.game.common.util.pc20.analysysjavasdk;

/**
 * @author admin
 */
public class DebugCollecter extends SyncCollecter {
    /**
     * 构造方法
     *
     * @param serverUrl 数据接收服务地址
     */
    public DebugCollecter(String serverUrl) {
        super(serverUrl, true);
    }
}
