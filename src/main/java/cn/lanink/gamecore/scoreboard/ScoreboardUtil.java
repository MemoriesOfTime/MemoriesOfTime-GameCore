package cn.lanink.gamecore.scoreboard;

import cn.lanink.gamecore.api.Info;
import cn.lanink.gamecore.scoreboard.base.IScoreboard;

/**
 * @author lt_name
 */
@SuppressWarnings("unused")
public class ScoreboardUtil {

    private static IScoreboard scoreboard;

    private ScoreboardUtil() {

    }

    @Info("获取计分板简单操作类")
    public synchronized static IScoreboard getScoreboard() {
        if (scoreboard == null) {
            try {
                Class.forName("cn.nukkit.scoreboard.Scoreboard");
                scoreboard = new cn.lanink.gamecore.scoreboard.nukkit.SimpleScoreboard();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return scoreboard;
    }

}
