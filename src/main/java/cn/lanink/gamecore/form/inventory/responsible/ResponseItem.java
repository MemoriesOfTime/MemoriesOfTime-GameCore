package cn.lanink.gamecore.form.inventory.responsible;

import cn.nukkit.Player;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.item.Item;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * @author iGxnon
 * @date 2021/9/8
 */
@Data
@SuppressWarnings("unused")
public abstract class ResponseItem {

    private final Item item;

    private BiConsumer<InventoryClickEvent, Player> clickItemListener;

    public ResponseItem(@NotNull Item item) {
        this.item = item;
    }

    public ResponseItem onClick(@NotNull BiConsumer<InventoryClickEvent, Player> listener) {
        this.clickItemListener = listener;
        return this;
    }

    public void callClick(@NotNull InventoryClickEvent event, @NotNull Player player) {
        this.clickItemListener.accept(event, player);
    }

    public Item getItem() {
        return item.clone();
    }
}
