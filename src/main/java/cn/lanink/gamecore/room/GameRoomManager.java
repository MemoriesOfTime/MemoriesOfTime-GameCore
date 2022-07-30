package cn.lanink.gamecore.room;

import cn.nukkit.level.Level;
import cn.nukkit.plugin.Plugin;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LT_Name
 */
@SuppressWarnings("unused")
public class GameRoomManager {

    private static final ConcurrentHashMap<Plugin, GameRoomManager> GAME_ROOM_MANAGER_MAP = new ConcurrentHashMap<>();

    public static GameRoomManager get(Plugin plugin) {
        return GAME_ROOM_MANAGER_MAP.computeIfAbsent(plugin, GameRoomManager::new);
    }

    @Getter
    private final Plugin plugin;
    private final ConcurrentHashMap<String, IGameRoom> gameRoomMap = new ConcurrentHashMap<>();

    private GameRoomManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean hasGameRoom(Level level) {
        return hasGameRoom(level.getFolderName());
    }

    public boolean hasGameRoom(String world) {
        return this.gameRoomMap.containsKey(world);
    }

    public IGameRoom getGameRoom(Level level) {
        return this.getGameRoom(level.getFolderName());
    }

    public IGameRoom getGameRoom(String world) {
        return this.gameRoomMap.get(world);
    }

    public List<IGameRoom> getGameRoomsByMode(String gameMode) {
        return this.getGameRoomsByMode(gameMode, false);
    }

    public List<IGameRoom> getGameRoomsByMode(String gameMode, boolean checkCanJoin) {
        ArrayList<IGameRoom> list = new ArrayList<>();
        for (IGameRoom room : this.gameRoomMap.values()) {
            if (checkCanJoin && room.isCanJoin()) {
                if (room.getGameMode().equalsIgnoreCase(gameMode)) {
                    list.add(room);
                }
            }
        }
        return list;
    }

    public void addGameRoom(Level level, IGameRoom iGameRoom) {
        this.addGameRoom(level.getFolderName(), iGameRoom);
    }

    public void addGameRoom(String level, IGameRoom iGameRoom) {
        this.gameRoomMap.put(level, iGameRoom);
    }

    public void removeGameRoom(Level level) {
        this.removeGameRoom(level.getFolderName());
    }

    public void removeGameRoom(String level) {
        this.gameRoomMap.remove(level);
    }

}
