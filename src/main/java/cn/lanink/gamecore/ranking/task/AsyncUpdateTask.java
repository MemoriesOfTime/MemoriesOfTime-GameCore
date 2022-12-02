package cn.lanink.gamecore.ranking.task;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.ranking.Ranking;
import cn.nukkit.scheduler.PluginTask;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lt_name
 */
public class AsyncUpdateTask extends PluginTask<GameCore> implements IRankingAPITask {

    private final Set<Ranking> updateRankings = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public AsyncUpdateTask(GameCore owner) {
        super(owner);
    }

    @Override
    public Set<Ranking> getRankings() {
        return this.updateRankings;
    }

    @Override
    public boolean addRanking(@NotNull Ranking ranking) {
        return this.updateRankings.add(ranking);
    }

    @Override
    public void removeRanking(@NotNull Ranking ranking) {
        this.updateRankings.remove(ranking);
    }

    @Override
    public void onRun(int i) {
        for (Ranking ranking : this.updateRankings) {
            ranking.onAsyncTick(i);
        }
    }

    @Override
    public void onCancel() {
        for (Ranking ranking : new HashSet<>(this.updateRankings)) {
            ranking.close();
        }
    }
}