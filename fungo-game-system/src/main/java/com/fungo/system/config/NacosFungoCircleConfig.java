package com.fungo.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import java.util.List;

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
    @Value( value = "${fungo.cloud.festival.startDate}")
    public String startDate;
    @Value( value = "${fungo.cloud.festival.endDate}")
    public String endDate;
    @Value( value = "${fungo.cloud.festival.days}")
    public int festivalDays;
    @Value( value = "${fungo.cloud.festival.warnning.switch}")
    public boolean warnningSwitch;
    @Value( value = "${fungo.cloud.festival.operator}")
    public String operatorId;

    @Value( value = "${fungo.cloud.festival.switch}")
    public boolean festivalSwitch;
    @Value( value = "${fungo.cloud.festival.picture}")
    public String festivalPicture;
    @Value( value = "${fungo.cloud.festival.linkUrl}")
    public String festivallinkUrl;

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

    public boolean isFestivalSwitch() {
        return festivalSwitch;
    }

    public void setFestivalSwitch(boolean festivalSwitch) {
        this.festivalSwitch = festivalSwitch;
    }

    public String getFestivalPicture() {
        return festivalPicture;
    }

    public void setFestivalPicture(String festivalPicture) {
        this.festivalPicture = festivalPicture;
    }

    public String getFestivallinkUrl() {
        return festivallinkUrl;
    }

    public void setFestivallinkUrl(String festivallinkUrl) {
        this.festivallinkUrl = festivallinkUrl;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getFestivalDays() {
        return festivalDays;
    }

    public void setFestivalDays(int festivalDays) {
        this.festivalDays = festivalDays;
    }

    public boolean isWarnningSwitch() {
        return warnningSwitch;
    }

    public void setWarnningSwitch(boolean warnningSwitch) {
        this.warnningSwitch = warnningSwitch;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
}
