package cn.lanink.gamecore.ranking;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.ranking.task.AsyncUpdateTask;
import cn.lanink.gamecore.ranking.task.UpdateTask;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginLogger;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author lt_name
 */
public class RankingAPI {

    private static RankingAPI rankingAPI;

    @Getter
    private boolean isEnabled;

    @Getter
    private AsyncUpdateTask asyncUpdateTask;
    @Getter
    private UpdateTask updateTask;

    public static RankingAPI getInstance() {
        return rankingAPI;
    }

    public void onLoad() {
        rankingAPI = this;
    }

    public void onEnable() {
        this.asyncUpdateTask = new AsyncUpdateTask();
        GameCore.getInstance().getServer().getScheduler().scheduleAsyncTask(GameCore.getInstance(), this.asyncUpdateTask);

        this.updateTask = new UpdateTask();
        GameCore.getInstance().getServer().getScheduler().scheduleRepeatingTask(GameCore.getInstance(), this.updateTask, 1);
        this.isEnabled = true;
    }

    public void onDisable() {
        for (Ranking ranking : this.asyncUpdateTask.getRankings()) {
            ranking.close();
        }
        for (Ranking ranking : this.updateTask.getRankings()) {
            ranking.close();
        }
    }

    /**
     * 创建一个新的排行榜
     *
     * @param plugin 插件主类
     * @param name 排行榜名称
     * @param position 排行榜位置
     * @return 排行榜实例
     */
    public static Ranking createRanking(@NotNull Plugin plugin, @NotNull String name, @NotNull Position position) {
        return new Ranking(plugin, name, position);
    }


    public PluginLogger getLogger() {
        return GameCore.getInstance().getLogger();
    }

}
