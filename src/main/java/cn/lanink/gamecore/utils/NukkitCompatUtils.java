package cn.lanink.gamecore.utils;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.network.protocol.ProtocolInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Nukkit 不同分支之间的运行时兼容工具。
 */
public final class NukkitCompatUtils {

    private static final Class<?> GAME_VERSION_CLASS = findClass("cn.nukkit.GameVersion");
    private static final Method PLAYER_GET_GAME_VERSION = findMethod(Player.class, "getGameVersion");
    private static final Field PLAYER_PROTOCOL_FIELD = findField(Player.class, "protocol");
    private static final Method ITEM_GET_NAMESPACE_ID = findMethod(Item.class, "getNamespaceId");
    private static final Method RUNTIME_ID_WITH_GAME_VERSION = findMethod(
            GlobalBlockPalette.class, "getOrCreateRuntimeId", GAME_VERSION_CLASS, int.class, int.class
    );
    private static final Method RUNTIME_ID_WITH_GAME_VERSION_FULL_ID = findMethod(
            GlobalBlockPalette.class, "getOrCreateRuntimeId", GAME_VERSION_CLASS, int.class
    );
    private static final Method RUNTIME_ID_WITH_PROTOCOL = findMethod(
            GlobalBlockPalette.class, "getOrCreateRuntimeId", int.class, int.class, int.class
    );
    private static final Method RUNTIME_ID_WITH_PROTOCOL_FULL_ID = findMethod(
            GlobalBlockPalette.class, "getOrCreateRuntimeId", int.class, int.class
    );

    private NukkitCompatUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean useExtendedPaletteRuntime() {
        NukkitTypeUtils.NukkitType nukkitType = NukkitTypeUtils.getNukkitType();
        return nukkitType == NukkitTypeUtils.NukkitType.MOT || nukkitType == NukkitTypeUtils.NukkitType.PM1E;
    }

    public static int getBlockRuntimeId(Player player, int id, int meta) {
        if (useExtendedPaletteRuntime()) {
            Object gameVersion = getPlayerGameVersion(player);
            if (gameVersion != null && RUNTIME_ID_WITH_GAME_VERSION != null) {
                return invokeInt(RUNTIME_ID_WITH_GAME_VERSION, null, gameVersion, id, meta);
            }
            Integer protocol = getPlayerProtocol(player);
            if (protocol != null && RUNTIME_ID_WITH_PROTOCOL != null) {
                return invokeInt(RUNTIME_ID_WITH_PROTOCOL, null, protocol, id, meta);
            }
        }
        return GlobalBlockPalette.getOrCreateRuntimeId(id, meta);
    }

    public static int getBlockRuntimeId(Player player, int fullId) {
        if (useExtendedPaletteRuntime()) {
            Object gameVersion = getPlayerGameVersion(player);
            if (gameVersion != null && RUNTIME_ID_WITH_GAME_VERSION_FULL_ID != null) {
                return invokeInt(RUNTIME_ID_WITH_GAME_VERSION_FULL_ID, null, gameVersion, fullId);
            }
            Integer protocol = getPlayerProtocol(player);
            if (protocol != null && RUNTIME_ID_WITH_PROTOCOL_FULL_ID != null) {
                return invokeInt(RUNTIME_ID_WITH_PROTOCOL_FULL_ID, null, protocol, fullId);
            }
        }
        return GlobalBlockPalette.getOrCreateRuntimeId(fullId);
    }

    public static String getItemSaveId(Item item) {
        if (useExtendedPaletteRuntime() && ITEM_GET_NAMESPACE_ID != null) {
            try {
                Object namespaceId = ITEM_GET_NAMESPACE_ID.invoke(item);
                if (namespaceId instanceof String && !((String) namespaceId).isEmpty()) {
                    return (String) namespaceId;
                }
            } catch (Exception ignored) {

            }
        }
        return item.getId() + ":" + item.getDamage();
    }

    private static Object getPlayerGameVersion(Player player) {
        if (PLAYER_GET_GAME_VERSION == null) {
            return null;
        }
        try {
            return PLAYER_GET_GAME_VERSION.invoke(player);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static Integer getPlayerProtocol(Player player) {
        if (PLAYER_PROTOCOL_FIELD != null) {
            try {
                return PLAYER_PROTOCOL_FIELD.getInt(player);
            } catch (Exception ignored) {

            }
        }
        return ProtocolInfo.CURRENT_PROTOCOL;
    }

    private static int invokeInt(Method method, Object owner, Object... args) {
        try {
            return ((Number) method.invoke(owner, args)).intValue();
        } catch (Exception e) {
            throw new IllegalStateException("调用兼容方法失败: " + method.getName(), e);
        }
    }

    private static Class<?> findClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    private static Method findMethod(Class<?> owner, String methodName, Class<?>... parameterTypes) {
        if (owner == null) {
            return null;
        }
        for (Class<?> parameterType : parameterTypes) {
            if (parameterType == null) {
                return null;
            }
        }
        try {
            return owner.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    private static Field findField(Class<?> owner, String fieldName) {
        try {
            Field field = owner.getField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException ignored) {
            return null;
        }
    }

}
