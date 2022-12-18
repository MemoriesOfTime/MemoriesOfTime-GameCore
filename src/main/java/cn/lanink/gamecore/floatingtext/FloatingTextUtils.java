package cn.lanink.gamecore.floatingtext;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.entity.TextFakeTextFakeEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.PluginTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LT_Name
 */
public class FloatingTextUtils {

    private static final HashMap<Long, TextFakeTextFakeEntity> ENTITY_MAP = new HashMap<>();

    /**
     * 临时显示一个浮空字
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

        public TickTask(GameCore owner) {
            super(owner);
        }

        @Override
        public void onRun(int i) {
            for (TextFakeTextFakeEntity textFakeEntity : ENTITY_MAP.values()) {
                try {
                    if (textFakeEntity.needTick()) {
                        textFakeEntity.onTick(i);
                    }
                }catch (Exception e) {
                    GameCore.getInstance().getLogger().error("FloatingTextUtils-TickTask", e);
                }
            }
        }
    }

    public static class AsyncTickTask extends PluginTask<GameCore> {

        public AsyncTickTask(GameCore owner) {
            super(owner);
        }

        @Override
        public void onRun(int i) {
            for (TextFakeTextFakeEntity textFakeEntity : ENTITY_MAP.values()) {
                try {
                    if (textFakeEntity.needAsyncTick()) {
                        textFakeEntity.onAsyncTick(i);
                    }
                }catch (Exception e) {
                    GameCore.getInstance().getLogger().error("FloatingTextUtils-AsyncTickTask", e);
                }
            }
        }

        @Override
        public void onCancel() {
            for (TextFakeTextFakeEntity textFakeEntity : ENTITY_MAP.values()) {
                textFakeEntity.close();
            }
        }

    }

}
