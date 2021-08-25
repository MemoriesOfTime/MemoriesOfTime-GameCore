package cn.lanink.gamecore.entity;

import cn.lanink.gamecore.GameCore;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

@SuppressWarnings("unused")
public class EntityPlayerCorpse extends EntityHuman {

    public static EntityPlayerCorpse createPlayerCorpse(Position position) {
        return createPlayerCorpse(position, GameCore.DEFAULT_SKIN);
    }

    public static EntityPlayerCorpse createPlayerCorpse(Position position, Skin skin) {
        EntityPlayerCorpse entityPlayerCorpse = new EntityPlayerCorpse(
                position.getChunk(),
                Entity.getDefaultNBT(position)
                        .putCompound("Skin", new CompoundTag()
                                .putByteArray("Data", skin.getSkinData().data)
                                .putString("ModelId", skin.getSkinId())
                        )
        );
        entityPlayerCorpse.setSkin(skin);
        return entityPlayerCorpse;
    }

    public EntityPlayerCorpse(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.setNameTagVisible(false);
        this.setNameTagAlwaysVisible(false);
    }

}
