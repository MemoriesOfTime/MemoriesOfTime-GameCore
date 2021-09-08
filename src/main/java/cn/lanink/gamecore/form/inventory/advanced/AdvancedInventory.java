package cn.lanink.gamecore.form.inventory.advanced;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.form.inventory.responsed.ResponseItem;
import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.inventory.InventoryCloseEvent;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author iGxnon
 * @date 2021/9/8
 */
@SuppressWarnings("unused")
public abstract class AdvancedInventory extends ContainerInventory {

    protected static final GameCore GAME_CORE = GameCore.getInstance();

    protected ConcurrentMap<Integer, ResponseItem> containedResponseItem = new ConcurrentHashMap<>();

    private Consumer<Player> closeInventoryListener;

    protected final BiConsumer<InventoryClickEvent, Player> superClickItemListener = ((clickEvent, player) -> {
        int slotPos = clickEvent.getSlot();
        if (this.containedResponseItem.containsKey(slotPos)) {
            this.containedResponseItem.get(slotPos).callClick(clickEvent, player);
        }
    });


    public AdvancedInventory(InventoryHolder holder, InventoryType type) {
        super(holder, type);
    }

    public AdvancedInventory putItem(int slot, ResponseItem item) {
        return this.putItem(slot, item, false);
    }

    public AdvancedInventory putItem(int slot, ResponseItem item, boolean force) {
        this.setItem(slot, item.getItem());
        if (force) {
            this.containedResponseItem.put(slot, item);
        }else {
            this.containedResponseItem.putIfAbsent(slot, item);
        }
        return this;
    }

    public AdvancedInventory onClose(@NotNull Consumer<Player> listener) {
        closeInventoryListener = listener;
        return this;
    }

    public void callClose(@NotNull Player player) {
        closeInventoryListener.accept(player);
    }

    public static void onEvent(Event event) {
        if (!(event instanceof InventoryEvent)) {
            GAME_CORE.getLogger().warning("[AdvancedInventory] 传入的事件不属于背包事件");
            return;
        }
        Inventory inventory = ((InventoryEvent) event).getInventory();
        if (!(inventory instanceof AdvancedInventory)) {
            GAME_CORE.getLogger().warning("[AdvancedInventory] 传入的背包不属于 AdvancedInventory");
            return;
        }
        if (event instanceof InventoryClickEvent) {
            ((AdvancedInventory) inventory).superClickItemListener
                    .accept((InventoryClickEvent) event, ((InventoryClickEvent) event).getPlayer());
        }else if (event instanceof InventoryCloseEvent) {
            ((AdvancedInventory) inventory)
                    .callClose(((InventoryCloseEvent) event).getPlayer());
        }
    }

}