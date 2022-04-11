package cn.lanink.gamecore.utils;

import cn.nukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author LT_Name
 */
public class VersionUtils {

    /**
     * 检查插件是否符合最低版本
     *
     * @param plugin 插件
     * @param leastVersion 最低版本
     * @return 是否大于等于最低版本
     */
    public static boolean checkMinimumVersion(@NotNull Plugin plugin, @NotNull String leastVersion) {
        return compareVersion(plugin.getDescription().getVersion(), leastVersion) != -1;
    }

    public static int compareVersion(@NotNull String v1, @NotNull String v2) {
        if (v1.equalsIgnoreCase(v2)) {
            return 0;
        }
        String[] version1Array = v1.split("[._]");
        String[] version2Array = v2.split("[._]");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        long diff = 0;

        while (index < minLen
                && (diff = Long.parseLong(version1Array[index])
                - Long.parseLong(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Long.parseLong(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Long.parseLong(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

}
