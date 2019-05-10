package com.fungo.community.function;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;

/**
 * <p>
 * <p>
 * 阿里视频服务客户端初始化
 * 单例模式构建，整个应用生命周期只初始化一次
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class FGAliVodClientGen {


    private static FGAliVodClientGen fgAliVodClientGen = null;

    /*
         阿里vod客户端
     */
    private static DefaultAcsClient aliyunClient = null;


    private FGAliVodClientGen() {

    }

    /**
     * 初始化单例对象和阿里云vod客户端对象
     *
     * @param vodProfile
     * @param vodAccessKeyId
     * @param vodAccessKeySecret
     */
    private static synchronized void syncInit(String vodProfile, String vodAccessKeyId, String vodAccessKeySecret) {

        if (null == fgAliVodClientGen || null == aliyunClient) {

            fgAliVodClientGen = new FGAliVodClientGen();

            aliyunClient = new DefaultAcsClient(
                    DefaultProfile.getProfile(vodProfile, vodAccessKeyId, vodAccessKeySecret));
        }
    }

    /**
     * 获取单例对象
     *
     * @param vodProfile         阿里vod 存储id
     * @param vodAccessKeyId     阿里vod 身份id
     * @param vodAccessKeySecret 阿里vod 访问秘钥
     * @return
     */
    public static FGAliVodClientGen getInstance(String vodProfile, String vodAccessKeyId, String vodAccessKeySecret) {

        if (null == fgAliVodClientGen || null == aliyunClient) {

            syncInit(vodProfile, vodAccessKeyId, vodAccessKeySecret);

        }

        return fgAliVodClientGen;
    }

    /**
     * 获取阿里云vod客户端对象
     *
     * @return
     */
    public DefaultAcsClient getAliyunVodClient() {
        return aliyunClient;
    }


    //-------------
}
