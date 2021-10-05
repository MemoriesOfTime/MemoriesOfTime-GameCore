package cn.lanink.gamecore.utils;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 玩家数据工具类 - 背包保存 末影箱保存 饥饿值等数据
 *
 * @author LT_Name
 */
public class PlayerDataUtils {

    private PlayerDataUtils() {
        throw new RuntimeException("error");
    }


    public static PlayerData create(@NotNull Player player) {
        return new PlayerData(player);
    }

    public static class PlayerData {

        private Player player;

        private Map<Integer, Item> inventoryContents;
        private Map<Integer, Item> offhandInventoryContents;
        private Map<Integer, Item> enderChestContents;

        private PlayerData(@NotNull Player player) {
            this.player = player;
        }

        /**
         * 保存所有数据
         */
        public void saveAll() {
            this.saveInventory();
            this.saveEnderChestInventory();
            //TODO
        }

        /**
         * 还原所有已保存的数据
         */
        public void restoreAll() {
            this.restoreInventory();
            this.restoreEnderChestInventory();
            //TODO
        }

        /**
         * 保存玩家背包内容
         */
        public void saveInventory() {
            this.inventoryContents = this.player.getInventory().getContents();
            this.offhandInventoryContents = this.player.getOffhandInventory().getContents();
        }

        /**
         * 还原玩家背包内容
         */
        public void restoreInventory() {
            if (this.inventoryContents != null) {
                this.player.getInventory().setContents(this.inventoryContents);
            }
            if (this.offhandInventoryContents != null) {
                this.player.getOffhandInventory().setContents(this.offhandInventoryContents);
            }
        }

        /**
         * 保存玩家末影箱内容
         */
        public void saveEnderChestInventory() {
            this.enderChestContents = this.player.getEnderChestInventory().getContents();
        }

        /**
         * 还原玩家末影箱内容
         */
        public void restoreEnderChestInventory() {
            if (this.enderChestContents != null) {
                this.player.getEnderChestInventory().setContents(this.enderChestContents);
            }
        }

    }

}
