package cn.lanink.gamecore.utils;

import cn.lanink.gamecore.api.Info;

/**
 * @author LT_Name
 */
@SuppressWarnings("unused")
@Info("注册自定义实体标识符  注意：此工具类仅在NK有效，PNX PM1E均有自己的API用于实现自定义实体！")
@Deprecated
public class CustomEntityUtils {

    private CustomEntityUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 是否已注册此实体标识符的自定义实体
     *
     * @param name 实体标识符
     * @return 是否已注册
     */
    public static boolean hasCustomEntity(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据实体标识符获取RuntimeId(NetworkID)
     *
     * @param identifier 实体标识符
     * @return RuntimeId
     */
    public static int getRuntimeId(String identifier) {
        throw new UnsupportedOperationException();
    }

    /**
     * 注册自定义实体标识符
     *
     * @param identifier 实体标识符
     */
    public static void registerCustomEntity(String identifier) {
        throw new UnsupportedOperationException();
    }

}
