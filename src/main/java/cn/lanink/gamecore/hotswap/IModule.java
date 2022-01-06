package cn.lanink.gamecore.hotswap;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;

import java.io.File;

/**
 * @author iGxnon
 */
public interface IModule {

    boolean isEnabled();

    void init(Server server, PluginDescription description, File file, Plugin parentPlugin);

    void setEnabled(boolean val);

    void onEnable();

    void onDisable();

    String getName();

}
