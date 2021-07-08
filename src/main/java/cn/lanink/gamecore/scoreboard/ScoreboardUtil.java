package cn.lanink.gamecore.scoreboard;

import cn.lanink.gamecore.api.Info;
import cn.lanink.gamecore.scoreboard.base.IScoreboard;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;

/**
 * @author lt_name
 */
public class ScoreboardUtil {

    private static IScoreboard scoreboard;

    private ScoreboardUtil() {

    }

    @Info("获取计分板简单操作类")
    public synchronized static IScoreboard getScoreboard() {
        if (scoreboard == null) {
            try {
                Class.forName("gt.creeperface.nukkit.scoreboardapi.ScoreboardAPI");
                PluginBase plugin = (PluginBase) Server.getInstance().getPluginManager().getPlugin("ScoreboardAPI");
                plugin.setEnabled(true);
                if (plugin.isDisabled()) {
                    throw new Exception("Not Loaded");
                }
                scoreboard = new cn.lanink.gamecore.scoreboard.creeperface.SimpleScoreboard();
            } catch (Exception e) {
                try {
                    Class.forName("de.theamychan.scoreboard.ScoreboardPlugin");
                    PluginBase plugin = (PluginBase) Server.getInstance().getPluginManager().getPlugin("ScoreboardPlugin");
                    plugin.setEnabled(true);
                    if (plugin.isDisabled()) {
                        throw new Exception("Not Loaded");
                    }
                    scoreboard = new cn.lanink.gamecore.scoreboard.theamychan.SimpleScoreboard();
                } catch (Exception e1) {
                    scoreboard = new cn.lanink.gamecore.scoreboard.ltname.SimpleScoreboard();
                }
            }
        }
        return scoreboard;
    }

}
