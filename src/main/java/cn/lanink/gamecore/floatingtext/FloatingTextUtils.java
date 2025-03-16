package cn.lanink.gamecore.floatingtext;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.entity.TextFakeTextFakeEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.PluginTask;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LT_Name
 */
public class FloatingTextUtils {

    private static final ConcurrentHashMap<Long, TextFakeTextFakeEntity> ENTITY_MAP = new ConcurrentHashMap<>();

    /**
     * 生成一个浮空字实体
     *
     * @param position 显示位置
     * @param showText 显示的文字
     * @return 浮空字实体
     */
    public static TextFakeTextFakeEntity showTextTemporary(@NotNull Position position, @NotNull String showText) {
        return showTextTemporary(position, showText, -1);
    }

    /**
     * 生成一个浮空字实体，并在指定时间后自动关闭
     *
     * @param position 显示位置
     * @param showText 显示的文字
     * @param showTick 显示时间(tick)
     * @return 浮空字实体
     */
    public static TextFakeTextFakeEntity showTextTemporary(@NotNull Position position, @NotNull String showText, int showTick) {
        TextFakeTextFakeEntity textFakeEntity = new TextFakeTextFakeEntity(getIdleID());
        textFakeEntity.setPosition(position);
        textFakeEntity.setShowText(showText);
        if (showTick > 0) {
            textFakeEntity.setSurviveTick(showTick);
        }
        ENTITY_MAP.put(textFakeEntity.getId(), textFakeEntity);
        return textFakeEntity;
    }

    /**
     * @return 空闲实体id
     */
    public static long getIdleID() {
        for (Map.Entry<Long, TextFakeTextFakeEntity> entry : ENTITY_MAP.entrySet()) {
            if (entry.getValue().isClosed()) {
                return entry.getKey();
            }
        }
        return Entity.entityCount++;
    }

    public static class TickTask extends PluginTask<GameCore> {

        private int tick = 0;

        public TickTask(GameCore owner) {
            super(owner);
        }

        @Override
        public void onRun(int i) {
            for (TextFakeTextFakeEntity textFakeEntity : ENTITY_MAP.values()) {
                try {
                    if (textFakeEntity.needTick()) {
                        textFakeEntity.onTick(this.tick);
                    }
                } catch (Exception e) {
                    GameCore.getInstance().getLogger().error("FloatingTextUtils-TickTask", e);
                }
            }
            this.tick++;
        }
    }

    public static class AsyncTickTask extends PluginTask<GameCore> {

        private int tick = 0;

        public AsyncTickTask(GameCore owner) {
            super(owner);
        }

        @Override
        public void onRun(int i) {
            for (TextFakeTextFakeEntity textFakeEntity : ENTITY_MAP.values()) {
                try {
                    if (textFakeEntity.needAsyncTick()) {
                        textFakeEntity.onAsyncTick(this.tick);
                    }
                }catch (Exception e) {
                    GameCore.getInstance().getLogger().error("FloatingTextUtils-AsyncTickTask", e);
                }
            }
            this.tick++;
        }

        @Override
        public void onCancel() {
            for (TextFakeTextFakeEntity textFakeEntity : ENTITY_MAP.values()) {
                textFakeEntity.close();
            }
        }

    }

}
