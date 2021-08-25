package cn.lanink.gamecore.utils;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.api.Info;
import cn.nukkit.entity.Entity;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lt_name
 */
@SuppressWarnings("unused")
public class EntityUtils {

    private static final ConcurrentHashMap<String, Number> FIELD_CACHE = new ConcurrentHashMap<>();

    private EntityUtils() {

    }

    @Info("解决多版本兼容问题")
    @SuppressWarnings("unchecked")
    public static <T extends Number> T getEntityField(String name, T defaultValue) {
        if (!FIELD_CACHE.containsKey(name)) {
            try {
                Field field = Entity.class.getDeclaredField(name);
                field.setAccessible(true);
                FIELD_CACHE.put(name, (T) field.get(null));
            } catch (Exception e) {
                GameCore.getInstance().getLogger().error("反射获取数据时出现错误！", e);
            }
        }
        return (T) FIELD_CACHE.getOrDefault(name, defaultValue);
    }

}
