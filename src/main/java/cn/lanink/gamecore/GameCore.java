package cn.lanink.gamecore;

import cn.nukkit.plugin.PluginBase;

/**
 * @author lt_name
 */
public class GameCore extends PluginBase {

    public static final String VERSION = "?";

    @Override
    public void onEnable() {
        this.getLogger().info("§eMemoriesOfTime-GameCore §aEnabled! Version:" + VERSION);
    }

}
