package cn.lanink.gamecore.room;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.room.listener.BaseGameListener;
import cn.nukkit.event.HandlerList;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.Plugin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private final ConcurrentHashMap<String, Class<? extends IGameRoom>> gameRoomClassMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, IGameRoom> gameRoomMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Class<? extends BaseGameListener<IGameRoom>>> gameListenerClassMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BaseGameListener<IGameRoom>> gameListenerMap = new ConcurrentHashMap<>();

    private GameRoomManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        this.loadAllListener();
    }

    public void registerGameRoomClass(@NotNull String name, @NotNull Class<? extends IGameRoom> gameRoomClass) {
        this.gameRoomClassMap.put(name, gameRoomClass);
    }

    public void registerListenerClass(@NotNull String name, @NotNull Class<? extends BaseGameListener<IGameRoom>> listerClass) {
        this.gameListenerClassMap.put(name, listerClass);
    }

    public void loadAllListener() {
        this.unloadAllListener();
        for (Map.Entry<String, Class<? extends BaseGameListener<IGameRoom>>> entry : this.gameListenerClassMap.entrySet()) {
            try {
                BaseGameListener<IGameRoom> baseGameListener = entry.getValue().getConstructor().newInstance();
                baseGameListener.init(entry.getKey());
                this.plugin.getServer().getPluginManager().registerEvents(baseGameListener, this.plugin);
                this.gameListenerMap.put(entry.getKey(), baseGameListener);
                if (GameCore.debug) {
                    GameCore.getInstance().getLogger().info("[debug] registerGameListener: [ " + baseGameListener.getListenerName() + " ]");
                }
            } catch (Exception e) {
                GameCore.getInstance().getLogger().error("registerGameListener error", e);
            }
        }
    }

    public void unloadAllListener() {
        for (BaseGameListener<IGameRoom> listener : this.gameListenerMap.values()) {
            HandlerList.unregisterAll(listener);
            if (GameCore.debug) {
                GameCore.getInstance().getLogger().info("[debug] UnregisterListener [ " + listener.getListenerName() + " ]");
            }
        }
        this.gameListenerMap.clear();
    }

    public BaseGameListener<IGameRoom> getGameListener(String name) {
        return this.gameListenerMap.get(name);
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
