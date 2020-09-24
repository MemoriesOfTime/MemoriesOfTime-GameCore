package cn.lanink.gamecore.listener;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.room.BaseRoom;
import cn.lanink.gamecore.utils.exception.GameListenerInitException;
import cn.nukkit.event.Listener;

import java.util.Map;

/**
 * @author lt_name
 */
public interface IGameListener<T extends BaseRoom> extends Listener {

    void init(GameCore gameCore, String listenerName) throws GameListenerInitException;

    String getListenerName();

    Map<String, T> getListenerRooms();

    void addListenerRoom(T room);

    void removeListenerRoom(String roomName);

    void removeListenerRoom(T room);

}
