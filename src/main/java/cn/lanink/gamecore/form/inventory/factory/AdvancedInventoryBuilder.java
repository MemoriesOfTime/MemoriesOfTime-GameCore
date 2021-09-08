package cn.lanink.gamecore.form.inventory.factory;

import cn.lanink.gamecore.GameCore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iGxnon
 * @date 2021/9/8
 */
@SuppressWarnings("rawtypes")
public abstract class AdvancedInventoryBuilder {

    protected static final GameCore GAME_CORE = GameCore.getInstance();

    protected static final List<Class> registeredClasses = new ArrayList<>();

    public static AdvancedInventoryBuilder builder(Class clazz) {
        if(!registeredClasses.contains(clazz)) {
            GAME_CORE.getLogger().warning("[AdvancedInventoryBuilder] 注册列表里无该类 " + clazz.getName());
            return null;
        }
        try {
            return (AdvancedInventoryBuilder) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            GAME_CORE.getLogger().warning("实例化该类发生类错误");
            return null;
        }
    }

    public static void register(Class clazz) {
        registeredClasses.add(clazz);
    }

    public static void init() {
        register(AdvancedEntityInventoryBuilder.class);
    }

}
