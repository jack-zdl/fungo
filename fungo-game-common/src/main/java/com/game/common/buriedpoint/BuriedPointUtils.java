package com.game.common.buriedpoint;

import com.game.common.buriedpoint.analysysjavasdk.AnalysysException;
import com.game.common.buriedpoint.analysysjavasdk.AnalysysJavaSdk;
import com.game.common.buriedpoint.analysysjavasdk.DEBUG;
import com.game.common.buriedpoint.analysysjavasdk.SyncCollecter;
import com.game.common.buriedpoint.constants.BuriedPointPlatformConstant;
import com.game.common.buriedpoint.event.BuriedPointEvent;
import com.game.common.buriedpoint.model.BuriedPointModel;
import com.game.common.buriedpoint.util.map.ObjectMappingMapUtil;
import com.game.common.buriedpoint.util.map.handler.impl.ValueEmptyHandler;
import com.game.common.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Optional;

/**
 * 易观埋点工具类
 * ysx
 *  易观java-sdk文档说明: https://docs.analysys.cn/ark/integration/sdk/java#31-debug-mo-shi
 */
public class BuriedPointUtils {

    private static Logger logger = LoggerFactory.getLogger(BuriedPointUtils.class);

    /**
     * 易观项目APP_KEY
     */
    private final static String APP_KEY = "2700ad010735c83b";
    /**
     * 埋点数据上报地址
     */
    private final static String ANALYSIS_SERVICE_URL = "https://ark-customer.analysys.cn:4089";

    /**
     * 咨询过易观 该对象可单例，对应方法线程安全 因此直接默认初始化即可
     */
    private final static AnalysysJavaSdk analysys = new AnalysysJavaSdk(new SyncCollecter(ANALYSIS_SERVICE_URL), APP_KEY);

    /**
     * http 存放平台信息的请求头
     */
    private final static String PLATFORM_HEAD = "os";

    static {
        //设置debug模式 调试阶段为 DEBUG.OPENNOSAVE 上线阶段为 DEBUG.CLOSE
        analysys.setDebugMode(DEBUG.OPENNOSAVE);
    }

    /**
     * 工具方法构造私有
     */
    private BuriedPointUtils() {
    }


    /**
     * 在spring容器中发布埋点事件(spring异步事件多播器处理后相当于异步上传埋点数据至易观-不会阻塞业务线程)
     */
    public static void publishBuriedPointEvent(BuriedPointModel pointModel) {
        BuriedPointEvent buriedPointEvent = new BuriedPointEvent(pointModel);
        // 对应的事件监听器为 BuriedPointEventListener
        SpringUtil.getContext().publishEvent(buriedPointEvent);
    }

    /**
     *  同步上传埋点数据至易观 （项目中可直接调用--阻塞业务线程）
     */
    public static void buriedPoint(BuriedPointModel pointModel) {

        //获取埋点属性
        Map<String, Object> trackPropertie = ObjectMappingMapUtil.objectToMap(pointModel, BuriedPointModel.class,new ValueEmptyHandler());
        //过滤 map中为空的属性 - 易观要求 属性值为空 该属性不可传递过去
        // ps map工具做了优化-用户可自定义过滤策略 这里使用了空过滤策略 new ValueEmptyHandler() 所以不用在再外层做 二次过滤了 节约性能
        // trackPropertie = filterNullMapvalue(trackPropertie);

        //这里捕获异常 埋点不可对正常的业务有影响
        try {
            // 将数据上报给易观数据分析系统
            analysys.track(pointModel.getDistinctId(), true, pointModel.getEventName(), trackPropertie, pointModel.getPlatForm());
        } catch (AnalysysException e) {
            logger.error("用户 {} 产生 {} 事件埋点异常 {}", pointModel.getDistinctId(), pointModel.getEventName(), e.getMessage());
        }
    }

    /**
     * 单项目获取请求来源 -- 注意 跨项目(微服务调用 MQ解耦等)无法获取 获取失败 默认来源为Server
     */
    public static String getPlatForm() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(at -> (ServletRequestAttributes) at)
                .map(ServletRequestAttributes::getRequest)
                .map(request -> request.getHeader(PLATFORM_HEAD))
                .orElse(BuriedPointPlatformConstant.PLATFORM_SERVER);
    }


 /*   *//*
     * 过滤map集合中值为空的属性
     *
     * @param map 要过滤的原始集合
     * @return 过滤后新产生的map集合
     *//*
    private static Map<String, Object> filterNullMapvalue(Map<String, Object> map) {
        return map.entrySet().stream()
                .filter((e) -> !isValueEmpty(e.getValue()))
                .collect(Collectors.toMap(
                        (e) -> (String) e.getKey(),
                        Map.Entry::getValue
                ));
    }*/

  /*  *//*
     * 判断对象是否为 null 对于字符串 null " " 等也视为空
     *
     * @param object 要判断的对象
     * @return 对象是不是空
     *//*
    private static boolean isValueEmpty(Object object) {
        if (object == null) {
            return true;
        }
        return object instanceof String && ("".equals(((String) object).trim()) || "null".equalsIgnoreCase((String) object));
    }
*/


}

