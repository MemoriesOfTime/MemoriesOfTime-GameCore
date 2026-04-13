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

    private static final ItemInfo UNKNOWN = new ItemInfo(-1, 0, "unknown", "unknown", "未知", "unknown");
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
                    entry.has("stringId") && !entry.get("stringId").isJsonNull() ? entry.get("stringId").getAsString() : null,
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
        registerCustomItem(id, damage, null, imagePath, nameChinese, nameEnglish);
    }

    /**
     * 注册带字符串ID的 CustomItem
     */
    public static void registerCustomItem(int id, int damage, String stringId, String imagePath, String nameChinese, String nameEnglish) {
        ITEM_INFOS.add(new ItemInfo(
                id,
                damage,
                stringId,
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

    public static ItemInfo getItemInfoByStringId(String stringId) {
        String normalizedStringId = normalizeStringId(stringId);
        if (normalizedStringId == null) {
            return UNKNOWN;
        }
        for (ItemInfo itemInfo : ITEM_INFOS) {
            if (normalizedStringId.equals(itemInfo.getStringId())) {
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

    public static String getNameChineseByStringId(String stringId) {
        return getItemInfoByStringId(stringId).getNameChinese();
    }

    public static String getNameEnglishByStringId(String stringId) {
        return getItemInfoByStringId(stringId).getNameEnglish();
    }

    public static String getImgPathById(int id, int damage) {
        return getItemInfoById(id, damage).getImagePath();
    }

    public static String getImgPathByStringId(String stringId) {
        return getItemInfoByStringId(stringId).getImagePath();
    }

    public static List<Integer> getAllMetaDamagesFromAnId(int id) {
        List<Integer> ret = new LinkedList<>();
        for (ItemInfo itemInfo : ITEM_INFOS) {
            if (itemInfo.getId() == id) {
                ret.add(itemInfo.getDamage());
            }
        }
        return ret;
    }

    private static String normalizeStringId(String stringId) {
        if (stringId == null) {
            return null;
        }
        String normalizedStringId = stringId.trim();
        if (normalizedStringId.isEmpty()) {
            return null;
        }
        return normalizedStringId.toLowerCase(Locale.ROOT);
    }

    private static int getIntValue(@NonNull Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (!(value instanceof Number)) {
            throw new RuntimeException("unexpected arguments in fromMap(), \"" + key + "\" should be a number");
        }
        return ((Number) value).intValue();
    }

    private static String getStringValue(@NonNull Map<String, Object> map, String key, boolean required) {
        Object value = map.get(key);
        if (value == null) {
            if (required) {
                throw new RuntimeException("unexpected arguments in fromMap(), missing \"" + key + "\" in map");
            }
            return null;
        }
        if (!(value instanceof String)) {
            throw new RuntimeException("unexpected arguments in fromMap(), \"" + key + "\" should be a string");
        }
        return (String) value;
    }


    @Data
    public static class ItemInfo {

        private final int id; //物品id
        private final int damage; //物品特殊值
        private final String stringId; //物品字符串id

        private final String imagePath; //贴图路径
        private final String nameChinese; //物品名称（中文）
        private final String nameEnglish; //物品名称（英文）

        private static final ImmutableList<String> requiredKeys = new ImmutableList.Builder<String>()
                .add("id")
                .add("damage")
                .add("imagePath")
                .add("nameChinese")
                .add("nameEnglish")
                .build();

        private static final ImmutableList<String> availableKeys = new ImmutableList.Builder<String>()
                .addAll(requiredKeys)
                .add("stringId")
                .build();

        public ItemInfo(int id, int damage, String imagePath, String nameChinese, String nameEnglish) {
            this(id, damage, null, imagePath, nameChinese, nameEnglish);
        }

        public ItemInfo(int id, int damage, String stringId, String imagePath, String nameChinese, String nameEnglish) {
            this.id = id;
            this.damage = damage;
            this.stringId = normalizeStringId(stringId);
            this.imagePath = imagePath;
            this.nameChinese = nameChinese;
            this.nameEnglish = nameEnglish;
        }

        public static ItemInfo fromMap(@NonNull Map<String, Object> map) {
            map.keySet().forEach(key -> {
                if (!availableKeys.contains(key)) throw new RuntimeException("unexpected arguments in fromMap(), expect " + availableKeys + " in map");
            });
            requiredKeys.forEach(key -> {
                if (!map.containsKey(key)) throw new RuntimeException("unexpected arguments in fromMap(), missing \"" + key + "\" in map");
            });
            return new ItemInfo(
                    getIntValue(map, "id"),
                    getIntValue(map, "damage"),
                    getStringValue(map, "stringId", false),
                    getStringValue(map, "imagePath", true),
                    getStringValue(map, "nameChinese", true),
                    getStringValue(map, "nameEnglish", true)
            );
        }

        public LinkedHashMap<String, Object> toMap() {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();

            map.put("id", this.id);
            map.put("damage", this.damage);
            if (this.stringId != null) {
                map.put("stringId", this.stringId);
            }
            map.put("imagePath", this.imagePath);
            map.put("nameChinese", this.nameChinese);
            map.put("nameEnglish", this.nameEnglish);

            return map;
        }

    }

}
