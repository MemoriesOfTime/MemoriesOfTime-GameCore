package cn.lanink.gamecore.room;

import cn.nukkit.level.Level;

/**
 * @author lt_name
 */
public interface IRoom {

    Level getLevel();

    String getLevelName();

    /*void joinRoom(Player player, boolean spectator);

    void quitRoom(Player player);

    void startGame();

    void endGame(int victory);*/

}
