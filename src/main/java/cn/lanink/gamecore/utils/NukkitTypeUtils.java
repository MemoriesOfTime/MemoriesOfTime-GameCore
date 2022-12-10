package cn.lanink.gamecore.utils;

import cn.nukkit.Server;
import lombok.Getter;

/**
 * @author LT_Name
 */
public class NukkitTypeUtils {

    @Getter
    private static final NukkitType nukkitType;

    static {
        String codename = Server.getInstance().getCodename();
        if ("PowerNukkitX".equalsIgnoreCase(codename)) {
            nukkitType = NukkitType.POWER_NUKKIT_X;
        }else if ("PowerNukkit".equalsIgnoreCase(codename)) {
            nukkitType = NukkitType.POWER_NUKKIT;
        }else if ("PM1E".equalsIgnoreCase(codename)) {
            nukkitType = NukkitType.PM1E;
        }else {
            nukkitType = NukkitType.NUKKITX;
        }
    }

    public enum NukkitType {

        NUKKITX("NukkitX"),

        POWER_NUKKIT("PowerNukkit"),

        POWER_NUKKIT_X("PowerNukkitX"),

        PM1E("Nukkit PetteriM1 Edition");

        @Getter
        private final String showName;

        NukkitType(String showName) {
            this.showName = showName;
        }

    }

}
