package cn.lanink.gamecore.room;

import cn.nukkit.level.Level;

import java.util.HashMap;

/**
 * @author LT_Name
 */
public class GameRoomManager {

    private final HashMap<String, GameRoom> gameRoomMap = new HashMap<>();

    public boolean hasGameRoom(Level level) {
        return this.hasGameRoom(level.getFolderName());
    }

    public boolean hasGameRoom(String world) {
        return this.gameRoomMap.containsKey(world);
    }

}
