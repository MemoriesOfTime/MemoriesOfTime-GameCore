package cn.lanink.gamecore.room;

/**
 * @author LT_Name
 */
public enum GameRoomStatus {

    /**
     * 初始化
     */
    INIT_LOADING,

    /**
     * 初始化 重置参数
     */
    INIT_RELOADING,

    /**
     * 房间 空闲
     */
    ROOM_WAIT,

    /**
     * 房间就绪
     */
    ROOM_READY,

    /**
     * 游戏中
     */
    ROOM_GAME,

    /**
     * 胜利结算中
     */
    ROOM_VICTORY;

}
