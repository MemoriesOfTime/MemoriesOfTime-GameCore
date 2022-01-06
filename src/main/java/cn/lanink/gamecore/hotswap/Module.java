package cn.lanink.gamecore.hotswap;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;

import java.io.File;

/**
 * @author iGxnon
 */
public abstract class Module implements IModule {

    private Server server;
    private boolean isEnabled = false;
    private boolean initialized = false;
    private PluginDescription description;
    private File file;
    private ModuleLogger logger;
    private Plugin parentPlugin;

    public void init(Server server, PluginDescription description, File file, Plugin parentPlugin) {
        if(!initialized) {
            this.description = description;
            this.server = server;
            this.file = file;
            this.parentPlugin = parentPlugin;
            logger = new ModuleLogger(this, parentPlugin);
            initialized = true;
        }
    }

    @Override
    public void setEnabled(boolean val) {
        isEnabled = true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public ModuleLogger getLogger() {
        return logger;
    }

    public File getFile() {
        return file;
    }

    public PluginDescription getDescription() {
        return description;
    }

    public Server getServer() {
        return server;
    }

    public Plugin getParentPlugin() {
        return parentPlugin;
    }

    @Override
    public String getName() {
        return description.getName();
    }
}
