package cn.lanink.gamecore.scoreboard.powernukkitx;

import cn.lanink.gamecore.scoreboard.base.IScoreboard;
import cn.nukkit.Player;
import cn.nukkit.scoreboard.data.DisplaySlot;
import cn.nukkit.scoreboard.data.SortOrder;
import cn.nukkit.scoreboard.scoreboard.Scoreboard;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LT_Name
 */
public class SimpleScoreboard implements IScoreboard {

    private final ConcurrentHashMap<Player, Scoreboard> scoreboards = new ConcurrentHashMap<>();

    @Override
    public String getScoreboardName() {
        return "PowerNukkitX(cn.nukkit.scoreboard.scoreboard)";
    }

    @Override
    public void showScoreboard(Player player, String title, List<String> message) {
        Scoreboard scoreboard = this.scoreboards.getOrDefault(player,
                new Scoreboard(
                        title,
                        title,
                        "dummy",
                        SortOrder.ASCENDING)
        );
        scoreboard.setLines(message);
        scoreboard.addViewer(player, DisplaySlot.SIDEBAR);
        this.scoreboards.put(player, scoreboard);
    }

    @Override
    public void closeScoreboard(Player player) {
        if (this.scoreboards.containsKey(player)) {
            this.scoreboards.get(player).removeViewer(player, DisplaySlot.SIDEBAR);
            this.scoreboards.remove(player);
        }
    }
}
