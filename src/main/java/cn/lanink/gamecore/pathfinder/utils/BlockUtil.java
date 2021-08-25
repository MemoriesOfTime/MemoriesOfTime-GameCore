package cn.lanink.gamecore.pathfinder.utils;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

/**
 * @author iGxnon
 * @date  2021/08/24
 */
public class BlockUtil {

    /**
     * 除自身外26个方向
     */
    public final static int[][] MOTION = {
            {-1, 0, 0},
            {1, 0, 0},
            {0, -1, 0},
            {0, 1, 0},
            {-1, -1, 0},
            {1, -1, 0},
            {-1, 1, 0},
            {1, 1, 0},
            {0, 0, 1},
            {-1, 0, 1},
            {1, 0, 1},
            {0, -1, 1},
            {0, 1, 1},
            {-1, -1, 1},
            {1, -1, 1},
            {-1, 1, 1},
            {1, 1, 1},
            {0, 0, -1},
            {-1, 0, -1},
            {1, 0, -1},
            {0, -1, -1},
            {0, 1, -1},
            {-1, -1, -1},
            {1, -1, -1},
            {-1, 1, -1},
            {1, 1, -1}
    };

    /**
     * 获取曼哈顿距离
     * @param start 当前点
     * @param target 终点
     * @return 曼哈顿距离
     */
    public static double MHDistance(Vector3 start, Vector3 target) {
        return Math.abs(start.x - target.x) + Math.abs(start.y - target.y) + Math.abs(start.z - target.z);
    }

    /**
     * @return 是否可透过
     */
    public static boolean isPermeable(Block block) {
        return !block.isSolid()
                || block.getId() == Block.END_ROD
                || block.getId() == Block.DOUBLE_PLANT
                || block.getId() == Block.VINE
                || block.getId() == Block.BROWN_MUSHROOM
                || block.getId() == Block.TORCH
                || block.getId() == Block.LADDER
                || block.getId() == Block.SNOW
                || block.getId() == Block.TRIPWIRE_HOOK
                || block.getId() == Block.DEAD_BUSH
                || block.getId() == Block.FLOWER
                || block.getId() == Block.LEVER
                || block.getId() == Block.STONE_BUTTON
                || block.getId() == Block.POWERED_RAIL
                || block.getId() == Block.RED_MUSHROOM
                || block.getId() == Block.REDSTONE_BLOCK
                || block.getId() == Block.DETECTOR_RAIL
                || block.getId() == Block.SAPLING
                || block.getId() == Block.SNOW_LAYER
                || block.getId() == Block.ACTIVATOR_RAIL;
    }

    /**
     * @param block 检测对象
     * @return 是否可以立足
     */
    public static boolean canStand(Block block) {
        return !isPermeable(block)
                || block.getId() == Block.VINE
                || block.getId() == Block.LADDER;
    }


    /**
     * @param block 检测对象
     * @return 是否可以垂直上升一格 [爬梯子,脚手架]
     */
    public static boolean checkUpAccessed(Block block) {
        return canStand(block.getLevel().getBlock(block.add(0, -1, 0)))
                || block.getId() == Block.VINE
                || block.getId() == Block.LADDER;
    }

    /**
     * @param block 检测对象
     * @param face 方块相对世界坐标的面
     * @return 邻居方块
     */
    public static Block getNearBlock(Block block, BlockFace face) {
        switch (face) {
            case UP:
                return block.getLevel().getBlock(block.add(0, 1, 1));
            case DOWN:
                return block.getLevel().getBlock(block.add(0, -1, 0));
            case EAST:
                return block.getLevel().getBlock(block.add(1, 0, 0));
            case WEST:
                return block.getLevel().getBlock(block.add(-1, 0, 0));
            case NORTH:
                return block.getLevel().getBlock(block.add(0, 0, -1));
            case SOUTH:
                return block.getLevel().getBlock(block.add(0, 0, 1));
            default:
                return new BlockAir();
        }
    }

}
