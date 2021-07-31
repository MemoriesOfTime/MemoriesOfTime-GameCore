package cn.lanink.gamecore.entity;

import cn.nukkit.entity.EntityHuman;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

@SuppressWarnings("unused")
public class EntityPlayerCorpse extends EntityHuman {

    public EntityPlayerCorpse(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.setNameTagVisible(false);
        this.setNameTagAlwaysVisible(false);
    }

}
