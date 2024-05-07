package cn.lanink.gamecore.entity;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author lt_name
 */
public interface ITextFakeEntity {

    void setPosition(@NotNull Position position);

    Position getPosition();

    Map<Player, String> getShowTextMap();

    void setShowText(@NotNull Player player, @NotNull String showText);

    void setMaxCanSeeDistance(int maxCanSeeDistance);

    default boolean needTick() {
        return true;
    }

    default boolean needAsyncTick() {
        return true;
    }

    void onTick(int i);

    void onAsyncTick(int i);

    void spawnTo(@NotNull Player player);

    void despawnFrom(@NotNull Player player);

    void close();

    boolean canSee(@NotNull Player player);

    void hideToPlayer(@NotNull Player player);

    void showToPlayer(@NotNull Player player);

}
