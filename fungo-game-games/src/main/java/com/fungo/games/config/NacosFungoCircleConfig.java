package com.fungo.games.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * <p>nacos的config配置
 * dl.zhang
 * @Date: 2019/7/1
 */
@Component
@RefreshScope
public class NacosFungoCircleConfig {

//    @Value("#{'${nacos.cloud}'.split(',')}")
    @Value(value = "${nacos.cloud.basLog}")
    public String basLog;

    @Value(value = "${nacos.cloud.basLogone}")
    public String basLogone;

    @Value(value = "${nacos.cloud.basLogtwo}")
    public String basLogtwo;

    @Value(value = "${nacos.es.game.search:true}")
    public boolean gameSearch;
    @Value(value = "${nacos.es.game.associate.search}")
    public boolean keywordGameSearch;

    @Value(value = "${es.cluster-nodes.ip}")
    private String esHttpIp;

    @Value(value = "${es.cluster-nodes.port}")
    private int esHttpPort;

    @Value(value = "${es.cluster-nodes.game.index}")
    private String index;

    @Value( value = "${es.cluster-node.game.type}")
    private String searchIndexType;

    public String getBasLog() {
        return basLog;
    }

    public void setBasLog(String basLog) {
        this.basLog = basLog;
    }

    public String getBasLogone() {
        return basLogone;
    }

    public void setBasLogone(String basLogone) {
        this.basLogone = basLogone;
    }

    public String getBasLogtwo() {
        return basLogtwo;
    }

    public void setBasLogtwo(String basLogtwo) {
        this.basLogtwo = basLogtwo;
    }

    public boolean isGameSearch() {
        return gameSearch;
    }

    public void setGameSearch(boolean gameSearch) {
        this.gameSearch = gameSearch;
    }

    public boolean isKeywordGameSearch() {
        return keywordGameSearch;
    }

    public void setKeywordGameSearch(boolean keywordGameSearch) {
        this.keywordGameSearch = keywordGameSearch;
    }

    public String getEsHttpIp() {
        return esHttpIp;
    }

    public void setEsHttpIp(String esHttpIp) {
        this.esHttpIp = esHttpIp;
    }

    public int getEsHttpPort() {
        return esHttpPort;
    }

    public void setEsHttpPort(int esHttpPort) {
        this.esHttpPort = esHttpPort;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getSearchIndexType() {
        return searchIndexType;
    }

    public void setSearchIndexType(String searchIndexType) {
        this.searchIndexType = searchIndexType;
    }
}
