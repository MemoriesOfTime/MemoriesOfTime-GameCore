package cn.lanink.gamecore.pathfinder;

import cn.lanink.gamecore.pathfinder.api.PathFinder;
import cn.lanink.gamecore.pathfinder.astar.AstarPathFinder;
import cn.nukkit.level.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author iGxnon
 * @date  2021/08/24
 */
@SuppressWarnings("unused")
public class PathFinderUtil {

    public static final FinderType DEFAULT_FINDER_TYPE = FinderType.ASTAR;

    public static PathFinder quickPathBuild(@NotNull Position start, @NotNull Position target) {
        return quickPathBuild(null, start, target);
    }

    public static PathFinder quickPathBuild(FinderType type, @NotNull Position start, @NotNull Position target) {
        Objects.requireNonNull(start, "null parameter given!");
        Objects.requireNonNull(target,"null parameter given!");
        if(type == null) {
            return quickPathBuild(DEFAULT_FINDER_TYPE, start, target);
        }
        switch (type) {
            case ASTAR:
            default:
                return new AstarPathFinder(start, target);
        }
    }

    public static Builder builder(FinderType type) {
        switch (type) {
            case ASTAR:
            default:
                return new PathFinderUtil.AstarPathBuilder();
        }
    }

    public static class AstarPathBuilder extends Builder {

        /**
         * 权重 数值越接近 1, G值影响越大，越偏向 Dijkstra 算法, 趋向于寻找最短路径, 耗时越大
         *      数值越接近 0, H值影响越大,越偏向 BFS 算法, 耗时偏少
         *  建议在 0~1 之间 寻找一个 sweet point
         */
        private double power = 0.5D;

        /**
         * 时间限制, 规定时间内未计算完成就认为计算失败
         */
        private int timeLimit = 2 * 50;

        private Position start;

        private Position target;

        public AstarPathBuilder start(Position start) {
            this.start = start;
            return this;
        }

        public AstarPathBuilder target(Position target) {
            this.target = target;
            return this;
        }

        public AstarPathBuilder timeLimit(int timeLimit) {
            this.timeLimit = timeLimit;
            return this;
        }

        public AstarPathBuilder power(double power) {
            this.power = power;
            return this;
        }

        @Override
        public AstarPathFinder build() {
            return new AstarPathFinder(start, target, timeLimit, power);
        }
    }

    public abstract static class Builder {
        abstract PathFinder build();
    }

    public enum FinderType {
        ASTAR
    }
}
