package cn.lanink.gamecore.pathfinder.astar;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.pathfinder.api.PathFinder;
import cn.lanink.gamecore.pathfinder.utils.BlockUtil;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author iGxnon
 * https://github.com/iGxnon/SquarePet/blob/main/src/main/java/xyz/lightsky/squarepet/pet/pathfinder/astar/AstarPathfinder.java
 */
@SuppressWarnings("unused")
public class AstarPathFinder implements PathFinder {

    public static final int DEFAULT_TIME_LIMIT = 2 * 50;

    public static final double DEFAULT_POWER = 0.5D;

    private final HashMap<Block, AstarNode> open = new HashMap<>();
    private final HashSet<AstarNode> close = new HashSet<>();
    private final Position start;
    private final Position target;
    private final long timeLimit;
    private AstarNode result;
    private double power = DEFAULT_POWER;

    public AstarPathFinder(Position start, Position target) {
        this(start, target, DEFAULT_TIME_LIMIT);
    }


    public AstarPathFinder(Position start, Position target, int timeLimit) {
        this.start = start;
        this.target = target;
        this.timeLimit = timeLimit;
    }

    public AstarPathFinder(Position start, Position target, long timeLimit, double power) {
        this.start = start;
        this.target = target;
        this.timeLimit = timeLimit;
        this.power = power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    @Override
    public Vector3 findNext() {
        return (find() == null || find().size() == 1) ? null : find().get(1);
    }

    @Override
    public List<Vector3> find(boolean async) {
        if(async) {
            Server.getInstance().getScheduler().scheduleAsyncTask(GameCore.getInstance(), new AsyncTask() {
                @Override
                public void onRun() {
                    find();
                }
            });
            return result == null ? null : getResult();
        }else {
            return find();
        }
    }

    @Override
    public List<Vector3> find() {
        long timeStart = System.currentTimeMillis();
        AstarNode startNode = new AstarNode(start, 0, BlockUtil.MHDistance(start, target), null);
        open.put(startNode.levelBlock, startNode);
        while (!open.isEmpty()) {
            if(System.currentTimeMillis() - timeStart > timeLimit) {
                break;
            }
            AstarNode minFNode = getMinFNode(this.power);
            if (minFNode.equals(target)) {
                result = minFNode;
                break;
            } else {
                open.remove(minFNode.levelBlock);
                close.add(minFNode);
                for (AstarNode nextNode : minFNode.getNextAccessibleNodes(target)) {
                    if (!close.contains(nextNode)) {
                        if (open.containsKey(nextNode.levelBlock)) {
                            open.get(nextNode.levelBlock).update(nextNode);
                        } else {
                            open.put(nextNode.levelBlock, nextNode);
                        }
                    }
                }
            }
        }
        return result == null ? null : getResult();
    }

    @Override
    public void show() {
        if(result != null) {
            getResultNode().forEach(astarNode -> {
                Server.getInstance().getScheduler().scheduleRepeatingTask(new Task() {
                    @Override
                    public void onRun(int i) {
                        astarNode.position.getLevel().addParticle(new DustParticle(astarNode.levelBlock.add(0.5, 0.2, 0.5), 255, 0, 0));
                    }
                }, 10);
            });
        }
    }

    /**
     * @return 获取结果
     */
    public List<AstarNode> getResultNode() {
        List<AstarNode> ret = new ArrayList<>();
        AstarNode node = result;
        while (node != null) {
            ret.add(0, node);
            node = node.parent;
        }
        return ret;
    }

    public List<Vector3> getResult() {
        List<Vector3> ret = new ArrayList<>();
        AstarNode node = result;
        while (node != null) {
            ret.add(0, node.position);
            node = node.parent;
        }
        return ret;
    }

    /**
     * @param power
     * @return 获取 openList中最小 F值的节点
     */
    private AstarNode getMinFNode(double power) {
        double minF = Double.MAX_VALUE;
        AstarNode ret = null;
        for (AstarNode node : open.values()) {
            double f = node.getF(power);
            if (f < minF) {
                ret = node;
                minF = f;
            }
        }
        return ret;
    }
}
