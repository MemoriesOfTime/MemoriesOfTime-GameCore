package cn.lanink.gamecore.hotswap;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;

import java.io.File;

/**
 * @author iGxnon
 */
public abstract class ModuleBase implements IModule {

    private Server server;
    private boolean isEnabled = false;
    private boolean initialized = false;
    private PluginDescription description;
    private File file;
    private ModuleLogger logger;
    private Plugin parentPlugin;

    public void init(Server server, PluginDescription description, File file, Plugin parentPlugin) {
        if(!this.initialized) {
            this.description = description;
            this.server = server;
            this.file = file;
            this.parentPlugin = parentPlugin;
            this.logger = new ModuleLogger(this, parentPlugin);
            this.initialized = true;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!this.initialized) {
            throw new RuntimeException("Not initialized!");
        }
        this.isEnabled = enabled;
        if(this.isEnabled) {
            this.onEnable();
        }else {
            this.onDisable();
        }
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public ModuleLogger getLogger() {
        return this.logger;
    }

    public File getFile() {
        return this.file;
    }

    public PluginDescription getDescription() {
        return this.description;
    }

    public Server getServer() {
        return this.server;
    }

    public Plugin getParentPlugin() {
        return this.parentPlugin;
    }

    @Override
    public String getName() {
        return this.description.getName();
    }
}
