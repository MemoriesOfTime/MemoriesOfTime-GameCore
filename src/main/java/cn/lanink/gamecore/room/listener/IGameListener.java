package cn.lanink.gamecore.room.listener;

import cn.lanink.gamecore.room.IGameRoom;
import cn.lanink.gamecore.utils.exception.GameListenerInitException;
import cn.nukkit.event.Listener;

import java.util.Map;

/**
 * @author LT_Name
 */
public interface IGameListener<T extends IGameRoom> extends Listener {

    void init(String listenerName) throws GameListenerInitException;

    String getListenerName();

    Map<String, T> getListenerRooms();

    T getListenerRoom(String level);

    void addListenerRoom(T room);

    void removeListenerRoom(String roomName);

    void removeListenerRoom(T room);

    void  clearListenerRooms();

}
