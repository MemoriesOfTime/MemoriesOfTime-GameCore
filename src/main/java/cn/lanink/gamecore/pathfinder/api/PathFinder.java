package cn.lanink.gamecore.pathfinder.api;

import cn.nukkit.math.Vector3;

import java.util.List;

/**
 * @author iGxnon
 */
public interface PathFinder {

    /**
     * @return 返回下一个路径节点 | 没有路径返回 null
     */
    Vector3 findNext();

    /**
     * @return 返回所有路径节点 | 没有路径返回 null
     */
    List<Vector3> find();

    /**
     * @param async 是否异步
     */
    void find(boolean async);

    /**
     * @return 返回所有路径节点 | 没有路径返回 null
     */
    List<Vector3> getResult();

    /**
     * 展示路径 用于测试
     */
    void show();

}
