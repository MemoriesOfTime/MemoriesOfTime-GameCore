package cn.lanink.gamecore.utils;

import cn.lanink.gamecore.GameCore;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.NonNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author LT_Name
 */
@SuppressWarnings("unused")
public class ItemInfoUtils {

    private static final ItemInfo UNKNOWN = new ItemInfo(-1, 0, "unknown", "未知", "unknown");
    private static final ArrayList<ItemInfo> ITEM_INFOS = new ArrayList<>();

    static {
        InputStream stream = GameCore.class.getClassLoader().getResourceAsStream("Resources/ItemInfoData.json");
        if (stream == null) {
            throw new AssertionError("Unable to load Resources/ItemInfoData.json");
        }
        JsonArray json = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonArray();
        for (JsonElement element : json) {
            if (!element.isJsonObject()) {
                throw new IllegalStateException("Invalid entry");
            }
            JsonObject entry = element.getAsJsonObject();
            ITEM_INFOS.add(new ItemInfo(
                    entry.get("id").getAsInt(),
                    entry.get("damage").getAsInt(),
                    entry.get("imagePath").getAsString(),
                    entry.get("nameChinese").getAsString(),
                    entry.get("nameEnglish").getAsString()
            ));
        }
    }

    private ItemInfoUtils() {
        throw new RuntimeException("error");
    }

    public static boolean registerCustomItemsFromMaps(@NonNull List<Map<String, Object>> maps) {
        for (Map<String, Object> map : maps) {
            if (!registerCustomItemFromMap(map)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 注册 CustomItem
     */
    public static void registerCustomItem(int id, int damage, String imagePath, String nameChinese, String nameEnglish) {
        ITEM_INFOS.add(new ItemInfo(
                id,
                damage,
                imagePath,
                nameChinese,
                nameEnglish
        ));
    }

    public static boolean registerCustomItemFromMap(@NonNull Map<String, Object> map) {
        try {
            ItemInfo itemInfo = ItemInfo.fromMap(map);
            ITEM_INFOS.add(itemInfo);
        }catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public static ItemInfo getItemInfoById(int id) {
        return getItemInfoById(id, 0);
    }

    public static ItemInfo getItemInfoById(int id, int damage) {
        for (ItemInfo itemInfo : ITEM_INFOS) {
            if (itemInfo.getId() == id && itemInfo.getDamage() == damage) {
                return itemInfo;
            }
        }
        return UNKNOWN;
    }

    public static String getNameChineseById(int id) {
        return getNameChineseById(id, 0);
    }

    public static String getNameChineseById(int id, int damage) {
        return getItemInfoById(id, damage).getNameChinese();
    }

    public static String getNameEnglishById(int id) {
        return getNameEnglishById(id, 0);
    }

    public static String getNameEnglishById(int id, int damage) {
        return getItemInfoById(id, damage).getNameEnglish();
    }

    public static String getImgPathById(int id, int damage) {
        return getItemInfoById(id, damage).getImagePath();
    }

    public static List<Integer> getAllMetaDamagesFromAnId(int id) {
        List<Integer> ret = new LinkedList<>();
        for (ItemInfo itemInfo : ITEM_INFOS) {
            if (itemInfo.id == id) {
                ret.add(itemInfo.damage);
            }
        }
        return ret;
    }


    @Data
    public static class ItemInfo {

        private final int id; //物品id
        private final int damage; //物品特殊值

        private final String imagePath; //贴图路径
        private final String nameChinese; //物品名称（中文）
        private final String nameEnglish; //物品名称（英文）

        private static final ImmutableList<String> availableKeys = new ImmutableList.Builder<String>()
                .add("id")
                .add("damage")
                .add("imagePath")
                .add("nameChinese")
                .add("nameEnglish")
                .build();

        public static ItemInfo fromMap(@NonNull Map<String, Object> map) {
            if (map.size() != availableKeys.size()) throw new RuntimeException("unexpected arguments in formMap(), expect 5 elements in map");
            map.keySet().forEach(key -> {
                if (!availableKeys.contains(key)) throw new RuntimeException("unexpected arguments in formMap(), expect " + availableKeys + " in map");
            });
            return new ItemInfo(
                    (Integer) map.get("id"),
                    (Integer) map.get("damage"),
                    (String) map.get("imagePath"),
                    (String) map.get("nameChinese"),
                    (String) map.get("nameEnglish")
            );
        }

        public LinkedHashMap<String, Object> toMap() {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();

            map.put("id", this.id);
            map.put("damage", this.damage);
            map.put("imagePath", this.imagePath);
            map.put("nameChinese", this.nameChinese);
            map.put("nameEnglish", this.nameEnglish);

            return map;
        }

    }

}
