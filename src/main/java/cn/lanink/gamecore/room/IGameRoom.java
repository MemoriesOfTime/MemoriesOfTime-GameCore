package cn.lanink.gamecore.room;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * @author LT_Name
 */
public interface IGameRoom {

    Level getLevel();

    String getLevelName();

    /**
     * 获取房间游戏模式
     *
     * @return 游戏模式
     */
    String getGameMode();

    /**
     * 设置房间状态
     *
     * @param gameRoomStatus 新的房间状态
     */
    void setGameStatus(GameRoomStatus gameRoomStatus);

    /**
     * 获取房间状态
     *
     * @return 房间状态
     */
    GameRoomStatus getStatus();

    default boolean isCanJoin() {
        return this.isCanJoin(null);
    }

    /**
     * 判断玩家是否可以加入房间
     *
     * @param player 玩家
     * @return 可以加入返回true，否则返回false
     */
    default boolean isCanJoin(@Nullable Player player) {
        return this.getStatus() == GameRoomStatus.ROOM_READY;
    }

}
