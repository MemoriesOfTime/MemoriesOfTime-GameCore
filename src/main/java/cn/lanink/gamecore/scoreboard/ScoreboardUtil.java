package cn.lanink.gamecore.scoreboard;

import cn.lanink.gamecore.api.Info;
import cn.lanink.gamecore.scoreboard.base.IScoreboard;

/**
 * @author lt_name
 */
public class ScoreboardUtil {

    private static IScoreboard scoreboard;

    private ScoreboardUtil() {

    }

    @Info("推荐仅在插件加载时调用并缓存数据")
    public synchronized static IScoreboard getScoreboard() {
        if (scoreboard == null) {
            try {
                Class.forName("gt.creeperface.nukkit.scoreboardapi.ScoreboardAPI");
                scoreboard = new cn.lanink.gamecore.scoreboard.creeperface.SimpleScoreboard();
            } catch (Exception e) {
                try {
                    Class.forName("de.theamychan.scoreboard.ScoreboardPlugin");
                    scoreboard = new cn.lanink.gamecore.scoreboard.theamychan.SimpleScoreboard();
                } catch (Exception e1) {
                    scoreboard = new cn.lanink.gamecore.scoreboard.ltname.SimpleScoreboard();
                }
            }
        }
        return scoreboard;
    }

}
