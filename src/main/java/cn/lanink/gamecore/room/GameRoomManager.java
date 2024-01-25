package cn.lanink.gamecore.room;

import cn.nukkit.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LT_Name
 */
@SuppressWarnings("unused")
public class GameRoomManager<T extends GameRoom> {

    private final ConcurrentHashMap<String, T> gameRoomMap = new ConcurrentHashMap<>();

    public GameRoomManager() {

    }

    public boolean hasGameRoom(Level level) {
        return hasGameRoom(level.getName());
    }

    public boolean hasGameRoom(String world) {
        return this.gameRoomMap.containsKey(world);
    }

    public void addGameRoom(Level level, T gameRoom) {
        addGameRoom(level.getName(), gameRoom);
    }

    public void addGameRoom(String level, T gameRoom) {
        this.gameRoomMap.put(level, gameRoom);
    }

    public void removeGameRoom(Level level) {
        removeGameRoom(level.getName());
    }

    public void removeGameRoom(String level) {
        this.gameRoomMap.remove(level);
    }

    public T getGameRoom(Level level) {
        return getGameRoom(level.getName());
    }

    public T getGameRoom(String level) {
        return this.gameRoomMap.get(level);
    }

    public ConcurrentHashMap<String, T> getGameRoomMap() {
        return gameRoomMap;
    }

    public boolean loadGameRoom(Level level) {
        return loadGameRoom(level.getName());
    }

    public boolean loadGameRoom(String level) {
        return !this.gameRoomMap.containsKey(level);
    }

    public boolean unloadGameRoom(Level level) {
        return unloadGameRoom(level.getName());
    }

    public boolean unloadGameRoom(String world) {
        this.gameRoomMap.remove(world);
        return true;
    }

    public T getCanJoinGameRoom() {
        return null;
    }

    /**
     * @return 玩家可以加入的房间游戏列表
     */
    public List<T> getCanJoinGameRoomList() {
        return new ArrayList<>(this.gameRoomMap.values());
    }

}
