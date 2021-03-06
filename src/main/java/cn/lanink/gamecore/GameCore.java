package cn.lanink.gamecore;

import cn.lanink.gamecore.form.FormListener;
import cn.nukkit.plugin.PluginBase;
import com.google.gson.Gson;

/**
 * @author lt_name
 */
public class GameCore extends PluginBase {

    public static final Gson GSON = new Gson();
    public static final String VERSION = "?";

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new FormListener(), this);
        this.getLogger().info("§eMemoriesOfTime-GameCore §aEnabled! Version:" + VERSION);
    }

}
