package cn.lanink.gamecore.listener;

import cn.lanink.gamecore.room.BaseRoom;
import cn.lanink.gamecore.utils.exception.GameListenerInitException;
import cn.nukkit.level.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lt_name
 */
public abstract class BaseGameListener<T extends BaseRoom> implements IGameListener<T> {

    private String listenerName = null;
    private final ConcurrentHashMap<String, T> listenerRooms = new ConcurrentHashMap<>();

    @Override
    public final void init(String listenerName) throws GameListenerInitException {
        if (this.listenerName == null) {
            if (listenerName == null || listenerName.trim().isEmpty()) {
                throw new GameListenerInitException("空参数");
            }
            this.listenerName = listenerName;
        }else {
            throw new GameListenerInitException("重复初始化");
        }
    }

    @Override
    public String getListenerName() {
        return this.listenerName;
    }

    public Map<String, T> getListenerRooms() {
        return this.listenerRooms;
    }

    public T getListenerRoom(Level level) {
        return this.getListenerRoom(level.getFolderName());
    }

    public T getListenerRoom(String level) {
        return this.listenerRooms.get(level);
    }

    public void addListenerRoom(T room) {
        this.listenerRooms.put(room.getLevelName(), room);
    }

    public void removeListenerRoom(T room) {
        this.removeListenerRoom(room.getLevelName());
    }

    public void removeListenerRoom(String level) {
        this.listenerRooms.remove(level);
    }

}
