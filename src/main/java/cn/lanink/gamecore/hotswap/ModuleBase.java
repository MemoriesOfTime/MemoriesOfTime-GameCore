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

    @Override
    public final void init(Server server, PluginDescription description, File file, Plugin parentPlugin) {
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
    public final void setEnabled(boolean enabled) {
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

    protected abstract void onEnable();

    protected abstract void onDisable();

    @Override
    public final boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    public final ModuleLogger getLogger() {
        return this.logger;
    }

    @Override
    public final File getFile() {
        return this.file;
    }

    @Override
    public final PluginDescription getDescription() {
        return this.description;
    }

    @Override
    public final Server getServer() {
        return this.server;
    }

    @Override
    public final Plugin getParentPlugin() {
        return this.parentPlugin;
    }

    @Override
    public final String getName() {
        return this.description.getName();
    }
}
