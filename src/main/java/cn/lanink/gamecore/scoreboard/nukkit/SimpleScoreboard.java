package cn.lanink.gamecore.scoreboard.nukkit;

import cn.lanink.gamecore.scoreboard.base.IScoreboard;
import cn.nukkit.Player;
import cn.nukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于 NukkitX 内置记分板 API (cn.nukkit.scoreboard.Scoreboard) 的实现
 *
 * @author lt_name
 */
public class SimpleScoreboard implements IScoreboard {

    private final ConcurrentHashMap<Player, Scoreboard> scoreboards = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Player, String> titles = new ConcurrentHashMap<>();

    @Override
    public String getScoreboardName() {
        return "Nukkit(cn.nukkit.scoreboard)";
    }

    @Override
    public void showScoreboard(Player player, String title, List<String> message) {
        Scoreboard scoreboard = this.scoreboards.get(player);
        String oldTitle = this.titles.get(player);
        if (scoreboard == null || !title.equals(oldTitle)) {
            if (scoreboard != null) {
                scoreboard.hideFor(player);
            }
            scoreboard = new Scoreboard(title, Scoreboard.SortOrder.ASCENDING, Scoreboard.DisplaySlot.SIDEBAR);
            this.scoreboards.put(player, scoreboard);
            this.titles.put(player, title);
        }
        scoreboard.holdUpdates();
        for (String scorer : scoreboard.getScores().keySet()) {
            scoreboard.removeScore(scorer);
        }
        int score = 0;
        for (String line : message) {
            scoreboard.setScore(line, score);
            score++;
        }
        scoreboard.unholdUpdates();
        scoreboard.showTo(player);
    }

    @Override
    public void closeScoreboard(Player player) {
        Scoreboard scoreboard = this.scoreboards.remove(player);
        this.titles.remove(player);
        if (scoreboard != null) {
            scoreboard.hideFor(player);
        }
    }

}
