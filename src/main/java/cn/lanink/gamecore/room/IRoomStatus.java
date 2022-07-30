package cn.lanink.gamecore.room;

import cn.lanink.gamecore.api.Info;

/**
 * @author lt_name
 */
@Deprecated
@SuppressWarnings("unused")
public interface IRoomStatus {

    @Info("地图未加载")
    int ROOM_STATUS_LEVEL_NOT_LOADED = -1;
    @Info("Task未初始化")
    int ROOM_STATUS_TASK_NEED_INITIALIZED = 0;
    @Info("等待玩家加入")
    int ROOM_STATUS_WAIT = 1;
    @Info("游戏中")
    int ROOM_STATUS_GAME = 2;
    @Info("胜利结算中")
    int ROOM_STATUS_VICTORY = 3;

    /**
     * 设置房间状态
     *
     * @param status 房间状态
     */
    void setStatus(int status);

    /**
     * 获取房间状态
     *
     * @return 房间状态
     */
    int getStatus();

}
