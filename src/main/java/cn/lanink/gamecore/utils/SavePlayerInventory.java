package cn.lanink.gamecore.utils;

import cn.lanink.gamecore.GameCore;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.*;

/**
 * @author SmallasWater
 * @author lt_name
 */
@Deprecated
@SuppressWarnings("unused")
public class SavePlayerInventory {

    /**
     * 保存玩家背包
     * @param plugin 插件（获取存储路径）
     * @param player 玩家
     */
    @Deprecated
    public static void save(Plugin plugin, Player player) {
        save(plugin.getDataFolder() + "/PlayerInventory", player);
    }

    /**
     * 保存玩家背包
     * @param dataFolder 存放路径
     * @param player 玩家
     */
    @Deprecated
    public static void save(String dataFolder, Player player) {
        try {
            throw new RuntimeException();
        }catch (Exception e) {
            GameCore.getInstance().getLogger().warning("SavePlayerInventory#save()方法即将被弃用！", e);
        }

        Config config = new Config(new File(dataFolder + "/" + player.getName() + ".json"), Config.JSON);
        config.set("Inventory", inventoryToLinkedHashMap(player));
        config.save();
        player.getInventory().clearAll();
        player.getUIInventory().clearAll();
    }

    /**
     * 还原玩家背包
     * @param plugin 插件（获取存储路径）
     * @param player 玩家
     */
    @Deprecated
    public static void restore(Plugin plugin, Player player) {
        restore(plugin.getDataFolder() + "/PlayerInventory", player);
    }

    public static void restore(String dataFolder, Player player) {
        try {
            throw new RuntimeException();
        }catch (Exception e) {
            GameCore.getInstance().getLogger().warning("SavePlayerInventory#restore()方法即将被弃用！", e);
        }

        File file = new File(dataFolder + "/" + player.getName() + ".json");
        if (file.exists()) {
            Config config = new Config(file, Config.JSON);
            if (file.delete()) {
                player.getInventory().clearAll();
                player.getUIInventory().clearAll();
                putInventory(player, config.get("Inventory", null));
            }
        }
    }

    /**
     * 玩家背包内容转换为 LinkedHashMap
     * @param player 玩家
     * @return LinkedHashMap
     */
    @Deprecated
    public static LinkedHashMap<String, Object> inventoryToLinkedHashMap(Player player) {
        try {
            throw new RuntimeException();
        }catch (Exception e) {
            GameCore.getInstance().getLogger().warning("SavePlayerInventory#inventoryToLinkedHashMap()方法即将被弃用！", e);
        }

        LinkedHashMap<String, Object> inventory = new LinkedHashMap<>();
        for (int i = -1; i < player.getInventory().getSize() + 4; i++) {
            LinkedList<String> list = new LinkedList<>();
            Item item;
            if (i == -1) {
                item = player.getOffhandInventory().getItem(0);
            }else {
                item = player.getInventory().getItem(i);
            }
            list.add(item.getId() + ":" + item.getDamage());
            list.add(item.getCount() + "");
            String tag = item.hasCompoundTag() ? bytesToBase64(item.getCompoundTag()) : "not";
            list.add(tag);
            inventory.put(i + "", list);
        }
        return inventory;
    }

    /**
     * 字节数组转base64
     * @param src 字节数组
     * @return base64字符串
     */
    @Deprecated
    public static String bytesToBase64(byte[] src) {
        try {
            throw new RuntimeException();
        }catch (Exception e) {
            GameCore.getInstance().getLogger().warning("SavePlayerInventory#bytesToBase64()方法即将被弃用！", e);
        }

        if (src == null || src.length <= 0) {
            return "not";
        }
        return Base64.getEncoder().encodeToString(src);
    }

    /**
     * 物品还原到玩家背包
     * @param player 玩家
     * @param inventory 物品Map
     */
    @Deprecated
    public static void putInventory(Player player, Map<String, Object> inventory) {
        try {
            throw new RuntimeException();
        }catch (Exception e) {
            GameCore.getInstance().getLogger().warning("SavePlayerInventory#putInventory()方法即将被弃用！", e);
        }

        if (inventory == null || inventory.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : inventory.entrySet()) {
            List<String> list = null;
            try {
                list = (List<String>) entry.getValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list == null || list.isEmpty()) {
                break;
            }
            Item item = Item.fromString(list.get(0));
            item.setCount(Integer.parseInt(list.get(1)));
            if (!"not".equals(String.valueOf(list.get(2)))) {
                CompoundTag tag = Item.parseCompoundTag(base64ToBytes(list.get(2)));
                item.setNamedTag(tag);
            }
            int index = Integer.parseInt(entry.getKey());
            if (index == -1) {
                player.getOffhandInventory().setItem(0, item);
            }else if (index > player.getInventory().getSize() + 4) {
                player.getInventory().addItem(item.clone());
            }else {
                player.getInventory().setItem(index, item.clone());
            }
        }
    }

    /**
     * base64转字节数组
     * @param hexString base64
     * @return 字节数组
     */
    @Deprecated
    public static byte[] base64ToBytes(String hexString) {
        try {
            throw new RuntimeException();
        }catch (Exception e) {
            GameCore.getInstance().getLogger().warning("SavePlayerInventory#base64ToBytes()方法即将被弃用！", e);
        }

        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        return Base64.getDecoder().decode(hexString);
    }

}
