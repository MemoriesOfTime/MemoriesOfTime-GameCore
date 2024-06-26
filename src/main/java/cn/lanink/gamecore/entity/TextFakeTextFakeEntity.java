package cn.lanink.gamecore.entity;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.utils.EntityUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lt_name
 */
public class TextFakeTextFakeEntity extends Position implements ITextFakeEntity {

    @Getter
    protected final long id;

    public static final EntityMetadata entityMetadata;

    static {
        //使用反射获取，保证数据是最新的
        entityMetadata = new EntityMetadata()
                .putLong(EntityUtils.getEntityField("DATA_FLAGS", Entity.DATA_FLAGS), 0)
                .putByte(EntityUtils.getEntityField("DATA_COLOR", Entity.DATA_COLOR), 0)
                .putString(EntityUtils.getEntityField("DATA_NAMETAG", Entity.DATA_NAMETAG), "")
                .putLong(EntityUtils.getEntityField("DATA_LEAD_HOLDER_EID", Entity.DATA_LEAD_HOLDER_EID), -1L)
                .putFloat(EntityUtils.getEntityField("DATA_SCALE", Entity.DATA_SCALE), 1F)
                .putBoolean(EntityUtils.getEntityField("DATA_ALWAYS_SHOW_NAMETAG", Entity.DATA_ALWAYS_SHOW_NAMETAG), true);
        long flags = entityMetadata.getLong(EntityUtils.getEntityField("DATA_FLAGS", Entity.DATA_FLAGS));
        flags ^= 1L << EntityUtils.getEntityField("DATA_FLAG_IMMOBILE", Entity.DATA_FLAG_IMMOBILE);
        entityMetadata.put(new LongEntityData(EntityUtils.getEntityField("DATA_FLAGS", Entity.DATA_FLAGS), flags));
    }

    @Getter
    private boolean closed = false;
    @Setter
    @Getter
    private long surviveTick = -1;

    private final Set<Player> hasSpawned = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Getter
    protected String defaultShowText;
    @Getter
    protected final Map<Player, String> showTextMap = new ConcurrentHashMap<>();

    @Getter
    private int maxCanSeeDistance = 16 * Server.getInstance().getViewDistance();

    protected final Set<Player> hiddenPlayers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public TextFakeTextFakeEntity() {
        this.id = Entity.entityCount++;
    }

    public TextFakeTextFakeEntity(long id) {
        this.id = id;
    }

    @Override
    public void setPosition(@NotNull Position position) {
        this.x = position.getX();
        this.y = position.getY();
        this.z = position.getZ();
        this.setLevel(position.getLevel());

        MoveEntityAbsolutePacket pk = new MoveEntityAbsolutePacket();
        pk.eid = this.getId();
        pk.x = this.getX();
        pk.y = this.getY();
        pk.z = this.getZ();
        pk.yaw = 0D;
        pk.headYaw = 0D;
        pk.pitch = 0D;
        for (Player player : this.hasSpawned) {
            player.dataPacket(pk.clone());
        }
    }

    @Override
    public Position getPosition() {
        return this;
    }

    public void setShowText(@NotNull String showText) {
        this.defaultShowText = showText;
    }

    @Override
    public void setShowText(@NotNull Player player, @NotNull String showText) {
        this.showTextMap.put(player, showText);
    }

    @Override
    public void setMaxCanSeeDistance(int maxCanSeeDistance) {
        if (maxCanSeeDistance < 1) {
            maxCanSeeDistance = 1;
        }
        this.maxCanSeeDistance = maxCanSeeDistance;
    }

    @Override
    public boolean needTick() {
        return false;
    }

    @Override
    public void onTick(int i) {
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            GameCore.getInstance().getLogger().error("错误调用！", e);
        }
    }

    @Override
    public boolean needAsyncTick() {
        return true;
    }

    @Override
    public void onAsyncTick(int i) {
        if (this.isClosed() || i%20 != 0) {
            return;
        }
        if (this.surviveTick > 0) {
            this.surviveTick--;
            if (this.surviveTick == 0) {
                this.close();
                return;
            }
        }
        if (this.hasDefaultShowText()) {
            for (Player player : this.getPosition().getLevel().getPlayers().values()) {
                if (this.showTextMap.containsKey(player) || this.distance(player) > this.getMaxCanSeeDistance()) {
                    continue;
                }
                if (!this.hasSpawned.contains(player) || i % 2400 == 0) {
                    this.spawnTo(player);
                }
                this.sendText(player, this.getDefaultShowText());
            }
        }
        for (Map.Entry<Player, String> entry : this.getShowTextMap().entrySet()) {
            if (entry.getKey().getLevel() == this.getLevel() &&
                    this.distance(entry.getKey()) <= this.getMaxCanSeeDistance()) {
                if (!this.hasSpawned.contains(entry.getKey()) || i % 2400 == 0) {
                    this.spawnTo(entry.getKey());
                }
                this.sendText(entry.getKey(), entry.getValue());
            }
        }
        for (Player player : this.hasSpawned) {
            if ((!this.getShowTextMap().containsKey(player) && !this.hasDefaultShowText()) ||
                    !player.isOnline() ||
                    player.getLevel() != this.getLevel() ||
                    this.distance(player) > this.getMaxCanSeeDistance()) {
                this.despawnFrom(player);
            }
        }
    }

    public boolean hasDefaultShowText() {
        return this.defaultShowText != null && !this.defaultShowText.isEmpty();
    }

    @Override
    public void spawnTo(@NotNull Player player) {
        if (!this.canSee(player)) {
            return;
        }
        if (this.hasSpawned.contains(player)) {
            this.despawnFrom(player);
        }
        this.hasSpawned.add(player);
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityRuntimeId = this.getId();
        pk.entityUniqueId = this.getId();
        pk.type = 64;
        pk.yaw = 0;
        pk.headYaw = 0;
        pk.pitch = 0;
        pk.x = (float) this.getX();
        pk.y = (float) this.getY();
        pk.z = (float) this.getZ();
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.metadata = entityMetadata;
        player.dataPacket(pk);
    }

    @Override
    public void despawnFrom(@NotNull Player player) {
        if (this.hasSpawned.contains(player)) {
            RemoveEntityPacket pk = new RemoveEntityPacket();
            pk.eid = this.getId();
            player.dataPacket(pk);
            this.hasSpawned.remove(player);
        }
    }

    @Override
    public void close() {
        this.closed = true;
        this.getShowTextMap().clear();
        for (Player player : this.hasSpawned) {
            this.despawnFrom(player);
        }
    }

    private void sendText(@NotNull Player player, @NotNull String string) {
        this.sendData(new Player[] {player},
                (new EntityMetadata()).putString(EntityUtils.getEntityField("DATA_NAMETAG", Entity.DATA_NAMETAG), string));
    }

    private void sendData(@NotNull Player[] players, @NotNull EntityMetadata data) {
        SetEntityDataPacket pk = new SetEntityDataPacket();
        pk.eid = this.getId();
        pk.metadata = data;

        for (Player player : players) {
            if (this.hasSpawned.contains(player)) {
                player.dataPacket(pk.clone());
            }
        }
    }

    public boolean canSee(@NotNull Player player) {
        return !this.hiddenPlayers.contains(player);
    }

    public void hideToPlayer(@NotNull Player player) {
        this.hiddenPlayers.add( player);
        this.despawnFrom(player);
    }

    public void showToPlayer(@NotNull Player player) {
        this.hiddenPlayers.remove(player);
        if (player.isOnline()) {
            this.spawnTo(player);
        }
    }
}
