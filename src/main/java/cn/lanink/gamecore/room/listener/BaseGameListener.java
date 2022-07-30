package cn.lanink.gamecore.room.listener;

import cn.lanink.gamecore.room.IGameRoom;
import cn.lanink.gamecore.utils.exception.GameListenerInitException;
import cn.nukkit.level.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lt_name
 */
@SuppressWarnings("unused")
public abstract class BaseGameListener<T extends IGameRoom> implements IGameListener<T> {

    private String listenerName = null;
    private final ConcurrentHashMap<String, T> listenerRooms = new ConcurrentHashMap<>();

    @Override
    public final void init(String listenerName) throws GameListenerInitException {
        if (this.listenerName == null) {
            if (listenerName == null || listenerName.trim().isEmpty()) {
                throw new GameListenerInitException("Empty parameter");
            }
            this.listenerName = listenerName;
        }else {
            throw new GameListenerInitException("Repeat initialization");
        }
    }

    @Override
    public String getListenerName() {
        return this.listenerName;
    }

    @Override
    public Map<String, T> getListenerRooms() {
        return this.listenerRooms;
    }

    public T getListenerRoom(Level level) {
        return this.getListenerRoom(level.getFolderName());
    }

    @Override
    public T getListenerRoom(String level) {
        return this.listenerRooms.get(level);
    }

    @Override
    public void addListenerRoom(T room) {
        this.listenerRooms.put(room.getLevelName(), room);
    }

    @Override
    public void removeListenerRoom(T room) {
        this.removeListenerRoom(room.getLevelName());
    }

    @Override
    public void removeListenerRoom(String level) {
        this.listenerRooms.remove(level);
    }

    @Override
    public void  clearListenerRooms() {
        this.listenerRooms.clear();
    }

}
