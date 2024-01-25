package cn.lanink.gamecore.entity;

import cn.nukkit.entity.EntityHuman;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author LT_Name
 */
public class RobotEntity extends EntityHuman {

    public RobotEntity(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public void saveNBT() {
        //不保存数据
    }
}