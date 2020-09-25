package cn.lanink.gamecore.scoreboard;

import cn.lanink.gamecore.scoreboard.base.IScoreboard;
import cn.lanink.gamecore.scoreboard.simple.SimpleScoreboard;

/**
 * @author lt_name
 */
public class ScoreboardUtil {

    private static IScoreboard scoreboard;

    private ScoreboardUtil() {

    }

    public synchronized static IScoreboard getScoreboard() {
        if (scoreboard == null) {
            try {
                Class.forName("gt.creeperface.nukkit.scoreboardapi.ScoreboardAPI");
                scoreboard = new cn.lanink.gamecore.scoreboard.creeperface.Scoreboard();
            } catch (ClassNotFoundException e) {
                try {
                    Class.forName("de.theamychan.scoreboard.ScoreboardPlugin");
                    scoreboard = new cn.lanink.gamecore.scoreboard.theamychan.Scoreboard();
                } catch (ClassNotFoundException error) {
                    scoreboard = new SimpleScoreboard();
                }
            }
        }
        return scoreboard;
    }

}
