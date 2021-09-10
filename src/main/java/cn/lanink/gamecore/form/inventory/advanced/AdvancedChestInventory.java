package cn.lanink.gamecore.form.inventory.advanced;

import cn.nukkit.block.BlockChest;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

/**
 * @author iGxnon
 * @date 2021/9/10
 */
@SuppressWarnings("unused")
public class AdvancedChestInventory extends AdvancedInventory {

    public AdvancedChestInventory(@NotNull BlockChest blockChest) {
        this((BlockEntityChest) blockChest.getLevel().getBlockEntity(blockChest));
    }

    public AdvancedChestInventory(@NotNull BlockEntityChest holder) {
        super(holder, InventoryType.CHEST);
        if(holder.getPair() != null) {
            throw new RuntimeException("该箱子不能和另外一个箱子合并!");
        }
    }

    @Override
    public Entity getOwner() {
        return null;
    }

    @Override
    public BlockEntityChest getBlockOwner() {
        return (BlockEntityChest) getHolder();
    }

}
