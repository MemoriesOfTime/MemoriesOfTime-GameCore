package cn.lanink.gamecore.form.inventory.advanced;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.form.inventory.responsible.ResponseItem;
import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Event;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.inventory.InventoryCloseEvent;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import com.google.common.collect.BiMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author iGxnon
 * @date 2021/9/8
 */
@SuppressWarnings({"unused", "unchecked"})
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

    public AdvancedInventory(@NotNull InventoryHolder holder, @NotNull InventoryType type) {
        super(holder, type);
    }

    protected AdvancedInventory(InventoryHolder holder, InventoryType type, Map<Integer, Item> items, Integer overrideSize, String overrideTitle) {
        super(holder, type, items, overrideSize, overrideTitle);
    }

    public AdvancedInventory putItem(int slot, @NotNull ResponseItem item) {
        return this.putItem(slot, item, false);
    }

    public AdvancedInventory putItem(int slot, @NotNull ResponseItem item, boolean force) {
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
        if (closeInventoryListener == null) {
            return;
        }
        closeInventoryListener.accept(player);
    }

    public static void onEvent(Event event) {
        if (event instanceof InventoryEvent) {
            Inventory inventory = ((InventoryEvent) event).getInventory();
            if (!(inventory instanceof AdvancedInventory)) {
                return;
            }
            if (event instanceof InventoryClickEvent) {
                ((AdvancedInventory) inventory).superClickItemListener
                        .accept((InventoryClickEvent) event, ((InventoryClickEvent) event).getPlayer());
            } else if (event instanceof InventoryCloseEvent) {
                ((AdvancedInventory) inventory)
                        .callClose(((InventoryCloseEvent) event).getPlayer());
            }
        }else if (event instanceof InventoryTransactionEvent) {
            InventoryTransactionEvent transactionEvent = (InventoryTransactionEvent) event;
            for (InventoryAction action : transactionEvent.getTransaction().getActions()) {
                if (action instanceof SlotChangeAction) {
                    SlotChangeAction slotChangeAction = (SlotChangeAction) action;
                    if (slotChangeAction.getInventory() instanceof AdvancedInventory) {
                        event.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean open(Player player) {
        final int windowId = player.getWindowId(this);
        if (windowId == -1) {
            player.addWindow(this);
            return true;
        }else {
            return super.open(player);
        }
    }

    @Override
    public void close(Player player) {
        super.close(player);
        if (!tryRemoveWindow(player)) {
            GAME_CORE.getLogger().warning(player.getName() + " 未正常移除背包页面");
        }
    }

    public boolean tryRemoveWindow(Player player) {
        super.close(player);
        Field windowField;
        try {
            windowField = Player.class.getDeclaredField("windows");
            windowField.setAccessible(true);
            final BiMap<Inventory, Integer> windows = (BiMap<Inventory, Integer>) windowField.get(player);
            if (windows.remove(this) == null) {
                return false;
            }
            windowField.set(player, windows);
            return true;
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
            return false;
        }
    }

    public Entity getOwner(){
        return null;
    }

    public BlockEntity getBlockOwner() {
        return null;
    }


}
