package cn.lanink.gamecore.hotswap;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.Utils;
import com.google.common.base.Preconditions;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;

/**
 * @author iGxnon
 */
public abstract class ModuleBase implements IModule {

    private Server server;
    private boolean isEnabled = false;
    private boolean initialized = false;
    private PluginDescription description;
    private File dataFolder;
    private File file;
    private File configFile;
    private Config config;
    private ModuleLogger logger;
    private Plugin parentPlugin;

    @Override
    public final void init(Server server, PluginDescription description, File file, Plugin parentPlugin) {
        if(!this.initialized) {
            this.server = server;
            this.description = description;
            this.file = file;
            this.parentPlugin = parentPlugin;
            this.dataFolder = new File(parentPlugin.getDataFolder() + "/modules/" + this.description.getName());
            this.configFile = new File(this.dataFolder, "config.yml");
            this.logger = new ModuleLogger(this, parentPlugin);
            this.initialized = true;
        }
    }

    @Override
    public final void setEnabled(boolean enabled) {
        if (!this.initialized) {
            throw new RuntimeException("Not initialized!");
        }
        if (this.isEnabled == enabled) {
            return;
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

    public InputStream getResource(String filename) {
        return this.getClass().getClassLoader().getResourceAsStream(filename);
    }

    public boolean saveResource(String filename) {
        return this.saveResource(filename, false);
    }

    public boolean saveResource(String filename, boolean replace) {
        return this.saveResource(filename, filename, replace);
    }

    public boolean saveResource(String filename, String outputName, boolean replace) {
        Preconditions.checkArgument(filename != null && outputName != null, "Filename can not be null!");
        Preconditions.checkArgument(filename.trim().length() != 0 && outputName.trim().length() != 0, "Filename can not be empty!");
        File out = new File(this.dataFolder, outputName);
        if (!out.exists() || replace) {
            try (InputStream resource = getResource(filename)) {
                if (resource != null) {
                    File outFolder = out.getParentFile();
                    if (!outFolder.exists()) {
                        outFolder.mkdirs();
                    }
                    Utils.writeFile(out, resource);
                    return true;
                }
            } catch (IOException e) {
                Server.getInstance().getLogger().logException(e);
            }
        }
        return false;
    }

    public Config getConfig() {
        if (this.config == null) {
            this.config = new Config(configFile, Config.YAML);
        }
        return this.config;
    }

    public void saveConfig() {
        if (!this.getConfig().save()) {
            this.getLogger().critical("Could not save config to " + this.configFile.toString());
        }
    }

    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.saveResource("config.yml", false);
        }
    }

    public void reloadConfig() {
        this.config = new Config(this.configFile);
        InputStream configStream = this.getResource("config.yml");
        if (configStream != null) {
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(dumperOptions);
            try {
                this.config.setDefault(yaml.loadAs(Utils.readFile(this.configFile), LinkedHashMap.class));
            } catch (IOException e) {
                Server.getInstance().getLogger().logException(e);
            }
        }
    }

    public final File getDataFolder() {
        return this.dataFolder;
    }

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
