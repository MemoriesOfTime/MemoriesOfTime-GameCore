package cn.lanink.gamecore.form.inventory.advanced;

import cn.nukkit.Player;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

/**
 * @author iGxnon
 * @date 2021/9/9
 */
@SuppressWarnings("unused")
public class AdvancedPlayerInventory extends AdvancedInventory{

    public AdvancedPlayerInventory(@NotNull Player player) {
        this(player, InventoryType.PLAYER);
    }

    public AdvancedPlayerInventory(@NotNull Player player, @NotNull InventoryType type) {
        super(player, type);
    }

    public AdvancedPlayerInventory(@NotNull InventoryHolder holder, @NotNull InventoryType type) {
        super(holder, type);
    }

    @Override
    public Player getOwner() {
        return this.getHolder();
    }

    @Override
    public Player getHolder() {
        return (Player) super.getHolder();
    }

}
