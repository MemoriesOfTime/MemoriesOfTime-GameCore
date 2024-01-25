package cn.lanink.gamecore.form.inventory.advanced;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.scheduler.AsyncTask;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 参考项目：
 * https://github.com/CloudburstMC/FakeInventories/blob/master/src/main/java/com/nukkitx/fakeinventories/inventory/FakeInventory.java
 */
@SuppressWarnings("unused")
public class AdvancedFakeChestInventory extends AdvancedChestInventory {

    private static final ConcurrentHashMap<Player, AdvancedInventory> OPEN = new ConcurrentHashMap<>();

    private final HashMap<String, List<BlockVector3>> blockPositions = new HashMap<>();

    public AdvancedFakeChestInventory(String title) {
        super(new FakeEntity(), InventoryType.CHEST, new HashMap<>(), InventoryType.CHEST.getDefaultSize(), title);
        ((FakeEntity) this.getHolder()).setInventory(this);
    }

    protected UpdateBlockPacket getDefaultPack(String id, BlockVector3 pos) {
        UpdateBlockPacket updateBlock = new UpdateBlockPacket();
        updateBlock.blockRuntimeId = Block.get(id).getRuntimeId();
        updateBlock.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
        updateBlock.x = pos.x;
        updateBlock.y = pos.y;
        updateBlock.z = pos.z;
        return updateBlock;
    }

    @Override
    public void onOpen(Player who) {
        this.viewers.add(who);
        if (OPEN.putIfAbsent(who, this) != null) {
            return;
        }

        List<BlockVector3> blocks = this.onOpenBlock(who);
        this.blockPositions.put(who.getName(), blocks);

        this.onFakeOpen(who, blocks);
    }

    protected void onFakeOpen(Player who, List<BlockVector3> blocks) {
        BlockVector3 blockPosition = blocks.isEmpty() ? new BlockVector3(0, 0, 0) : blocks.get(0);

        ContainerOpenPacket containerOpen = new ContainerOpenPacket();
        containerOpen.windowId = who.getWindowId(this);
        containerOpen.type = this.getType().getNetworkType();
        containerOpen.x = blockPosition.x;
        containerOpen.y = blockPosition.y;
        containerOpen.z = blockPosition.z;

        who.dataPacket(containerOpen);

        this.sendContents(who);
    }

    protected List<BlockVector3> onOpenBlock(Player who) {
        BlockVector3 blockPosition = new BlockVector3((int) who.x, who.getFloorY() + 3, (int) who.z);

        this.placeFakeChest(who, blockPosition);

        return Collections.singletonList(blockPosition);
    }

    protected void placeFakeChest(Player who, BlockVector3 pos) {
        who.dataPacket(this.getDefaultPack(BlockID.CHEST, pos));
        BlockEntityDataPacket blockEntityData = new BlockEntityDataPacket();
        blockEntityData.x = pos.x;
        blockEntityData.y = pos.y;
        blockEntityData.z = pos.z;
        blockEntityData.namedTag = getNbt(pos, this.getName());

        who.dataPacket(blockEntityData);
    }

    protected static byte[] getNbt(BlockVector3 pos, String name) {
        CompoundTag tag = new CompoundTag()
                .putString("id", BlockEntity.CHEST)
                .putInt("x", pos.x)
                .putInt("y", pos.y)
                .putInt("z", pos.z)
                .putString("CustomName", name == null ? "Chest" : name);

        try {
            return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create NBT for chest");
        }
    }

    @Override
    public void onClose(Player who) {
        super.onClose(who);
        OPEN.remove(who, this);
        try {
            if (this.blockPositions.containsKey(who.getName())) {
                List<BlockVector3> blocks = this.blockPositions.get(who.getName());
                for (int i = 0, size = blocks.size(); i < size; i++) {
                    final int index = i;
                    Server.getInstance().getScheduler().scheduleAsyncTask(GAME_CORE, new AsyncTask() {
                        @Override
                        public void onRun() {
                            Vector3 blockPosition = blocks.get(index).asVector3();
                            UpdateBlockPacket updateBlock = new UpdateBlockPacket();
                            updateBlock.blockRuntimeId = who.getLevel().getBlock(blockPosition).getRuntimeId();
                            updateBlock.flags = UpdateBlockPacket.FLAG_ALL_PRIORITY;
                            updateBlock.x = blockPosition.getFloorX();
                            updateBlock.y = blockPosition.getFloorY();
                            updateBlock.z = blockPosition.getFloorZ();
                            who.dataPacket(updateBlock);
                        }
                    });
                }
            }
        } catch (Exception ignore) {

        }
    }

    private static class FakeEntity implements InventoryHolder {

        @Setter
        @Getter
        private Inventory inventory;

    }

}
