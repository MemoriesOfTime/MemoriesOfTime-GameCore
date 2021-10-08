package cn.lanink.gamecore.pathfinder.astar;

import cn.lanink.gamecore.pathfinder.utils.BlockUtil;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iGxnon
 * https://github.com/iGxnon/SquarePet/blob/main/src/main/java/xyz/lightsky/squarepet/pet/pathfinder/astar/Node.java
 */
@SuppressWarnings("unused")
public class AstarNode {

    public Position position;
    public double G;
    public double H;
    public AstarNode parent;
    public Block levelBlock;

    public AstarNode(Position position, double G, double H, AstarNode parent) {
        this.position = position;
        this.G = G;
        this.H = H;
        this.parent = parent;
        this.levelBlock = position.getLevelBlock();
    }


    //判断从起点到达是否更近，并更新节点
    public void update(AstarNode node) {
        if (this.G > node.G) {
            this.position = node.position;
            this.G = node.G;
            this.H = node.H;
            this.parent = node.parent;
            this.levelBlock = position.getLevelBlock();
        }
    }

    public CompoundTag getDefaultNBT(Position pos) {
        return Entity.getDefaultNBT(pos).putCompound("Skin", new CompoundTag());
    }

    //获取节点f值
    public double getF(double power) {
        return ((1-power) * G) +  (power * H);
    }

    //获取子节点
    public List<AstarNode> getNextAccessibleNodes(Position target) {
        List<AstarNode> nodes = new ArrayList<>();
        for (int[] motion : BlockUtil.MOTION) {
            Position nextStep = position.add(motion[0], motion[1], motion[2]);
            if (isAccessible(motion, this, nextStep)) {
                AstarNode nextNode = new AstarNode(nextStep, G + getAddG(motion), BlockUtil.MHDistance(nextStep, target), this);
                nodes.add(nextNode);
            }
        }
        return nodes;
    }

    //获取移动的耗费g
    private double getAddG(int[] direct) {
        int count = 0;
        for (int val : direct) {
            if (val != 0) {
                count++;
            }
        }
        switch (count) {
            case 1:
                return 1;
            case 2:
                return 1.4142D;
            case 3:
                return 1.7320D;
            default:
                return 0;
        }
    }

    public boolean isAccessible(int[] selectedMotion, AstarNode selectedNode, Position nextPos) {
        if (!(BlockUtil.isPermeable(nextPos.getLevelBlock()) && BlockUtil.isPermeable(BlockUtil.getNearBlock(nextPos.getLevelBlock(), BlockFace.UP)))) {
            return false;
            // 检查可否向上一格运动
        } else if (selectedMotion[1] == 1 && !BlockUtil.checkUpAccessed(selectedNode.levelBlock)) {
            return false;
            // 水平方向探测 排除极端条件下的浮空运动 排除实体运动到浮空节点(距地面一格高)时再另启一个寻路方法导致的无法寻路bug
        } else if ((selectedMotion[0] != 0 || selectedMotion[2] != 0) && selectedMotion[1] == 0 &&
                !BlockUtil.canStand(BlockUtil.getNearBlock(selectedNode.levelBlock, BlockFace.DOWN))) {
            return !BlockUtil.isPermeable(BlockUtil.getNearBlock(selectedNode.levelBlock.getLevel().getBlock(nextPos), BlockFace.DOWN));
            // 禁止对角(顶)线(点)向下移动, 防止卡进方块
        } else if ((selectedMotion[0] != 0 || selectedMotion[2] != 0) && selectedMotion[1] == -1) {
            return false;
            // 对顶点移动格挡 排除穿过原版不可穿过的对角摆放屏障bug
        } else if(selectedMotion[0] != 0 && selectedMotion[2] != 0 && selectedMotion[1] != 0) {
            return BlockUtil.isPermeable(selectedNode.position.getLevel().getBlock(selectedNode.position.add(selectedMotion[0], selectedMotion[1]))) && BlockUtil.isPermeable(selectedNode.position.getLevel().getBlock(selectedNode.position.add(0, selectedMotion[1], selectedMotion[2])));
            // 对角线透过格挡 排除穿过原版不可穿过的对角摆放屏障bug
        } else if(selectedMotion[0] != 0 && selectedMotion[2] != 0 && BlockUtil.isPermeable(selectedNode.position.getLevel().getBlock(selectedNode.position.add(selectedMotion[0]))) && BlockUtil.isPermeable(selectedNode.position.getLevel().getBlock(selectedNode.position.add(0, 0, selectedMotion[2])))){
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstarNode) {
            AstarNode node = (AstarNode) obj;
            return node.levelBlock.equals(levelBlock);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.levelBlock.hashCode();
    }

    public boolean equals(Position loc) {
        return loc.getLevelBlock().equals(levelBlock);
    }
}
