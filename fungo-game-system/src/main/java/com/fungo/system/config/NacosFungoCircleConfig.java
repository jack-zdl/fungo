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

    @Value( value = "${fungo.cloud.festival.switch}")
    public boolean festivalSwitch;

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
}
