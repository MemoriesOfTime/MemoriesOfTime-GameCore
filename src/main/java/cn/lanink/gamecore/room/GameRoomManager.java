package cn.lanink.gamecore.room;

import cn.nukkit.level.Level;

import java.util.HashMap;

/**
 * @author LT_Name
 */
@SuppressWarnings("unused")
public class GameRoomManager {

    private GameRoomManager() {
        throw new RuntimeException("error");
    }

    private static final HashMap<String, GameRoom> GAME_ROOM_MAP = new HashMap<>();

    public static boolean hasGameRoom(Level level) {
        return hasGameRoom(level.getFolderName());
    }

    public static boolean hasGameRoom(String world) {
        return GAME_ROOM_MAP.containsKey(world);
    }

    public static void addGameRoom(Level level, GameRoom gameRoom) {
        addGameRoom(level.getFolderName(), gameRoom);
    }

    public static void addGameRoom(String level, GameRoom gameRoom) {
        GAME_ROOM_MAP.put(level, gameRoom);
    }

    public static void removeGameRoom(Level level) {
        removeGameRoom(level.getFolderName());
    }

    public static void removeGameRoom(String level) {
        GAME_ROOM_MAP.remove(level);
    }

}
