package cn.lanink.gamecore.ranking.task;

import cn.lanink.gamecore.ranking.Ranking;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author lt_name
 */
public interface IRankingAPITask {

    Set<Ranking> getRankings();

    boolean addRanking(@NotNull Ranking ranking);

    void removeRanking(@NotNull Ranking ranking);

}