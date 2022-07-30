package cn.lanink.gamecore.room;

import cn.nukkit.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @author LT_Name
 */
public interface IGameRoom {

    /**
     * 获取房间游戏模式
     *
     * @return 游戏模式
     */
    String getGameMode();

    default boolean isCanJoin() {
        return this.isCanJoin(null);
    }

    /**
     * 判断玩家是否可以加入房间
     *
     * @param player 玩家
     * @return 可以加入返回true，否则返回false
     */
    boolean isCanJoin(@Nullable Player player);

}
