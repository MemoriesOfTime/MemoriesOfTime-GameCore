package cn.lanink.gamecore.pathfinder.api;

import cn.lanink.gamecore.api.Info;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;

import java.util.List;

/**
 * @author iGxnon
 * @date  2021/08/24
 */
@Info("A星路径查找")
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
     * @return 返回所有路径节点 | 没有路径返回 null
     */
    List<Vector3> find(boolean async);

    /**
     * 展示路径 用于测试
     */
    void show();

}
