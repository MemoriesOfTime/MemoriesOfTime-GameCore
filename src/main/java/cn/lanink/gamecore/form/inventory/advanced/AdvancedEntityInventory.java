package cn.lanink.gamecore.form.inventory.advanced;

import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;

/**
 * @author iGxnon
 * @date 2021/9/8
 */
@SuppressWarnings({"unused", "unchecked"})
public class AdvancedEntityInventory<T extends Entity> extends AdvancedInventory {

    /**
     * @param entity 需要继承 InventoryHolder
     * @param type 背包类型
     */
    public AdvancedEntityInventory(T entity, InventoryType type) {
        super((InventoryHolder) entity, type);
        if (!entity.getDataProperties().exists(Entity.DATA_CONTAINER_BASE_SIZE)) {
            throw new RuntimeException("该实体没有`背包大小`的数据!");
        }
    }

    @Override
    public T getOwner() {
        try {
            if (super.getHolder() instanceof Entity) {
                return (T) super.getHolder();
            }else {
                return null;
            }
        }catch (ClassCastException e) {
            return null;
        }
    }

    public AdvancedEntityInventory(InventoryHolder holder, InventoryType type) {
        super(holder, type);
    }

}
