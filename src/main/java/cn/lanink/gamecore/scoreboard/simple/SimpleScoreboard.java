package cn.lanink.gamecore.scoreboard.simple;

import cn.lanink.gamecore.scoreboard.base.IScoreboard;
import cn.nukkit.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 参考项目：
 * https://github.com/Creeperface01/ScoreboardAPI
 * https://github.com/LucGamesYT/ScoreboardAPI
 */
public class SimpleScoreboard implements IScoreboard {

    private HashMap<Player, HashSet<String>> playerObjectiveName = new HashMap<>();
    private final HashMap<Player, Scoreboard> scoreboards = new HashMap<>();

    @Override
    public void showScoreboard(Player player, String title, List<String> message) {
        Scoreboard scoreboard = this.scoreboards.getOrDefault(player,
                new Scoreboard(
                        title,
                        title,
                        ScoreboardData.DisplaySlot.SIDEBAR,
                        ScoreboardData.SortOrder.ASCENDING));
        int line = 0;
        for (String string : message) {
            scoreboard.setLine(line, string, line);
            line++;
        }
        scoreboard.show(player);
        scoreboard.update();
        this.scoreboards.put(player, scoreboard);
    }

    @Override
    public void closeScoreboard(Player player) {
        if (this.scoreboards.containsKey(player)) {
            this.scoreboards.get(player).hide(player);
            this.scoreboards.remove(player);
        }
    }

}
