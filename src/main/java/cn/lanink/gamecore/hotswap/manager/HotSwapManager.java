package cn.lanink.gamecore.hotswap.manager;

import cn.lanink.gamecore.hotswap.ModuleBase;
import cn.lanink.gamecore.hotswap.load.ModuleLoader;
import cn.lanink.gamecore.hotswap.load.ModulesFileLoader;
import cn.lanink.gamecore.utils.Download;
import cn.nukkit.plugin.Plugin;
import lombok.Getter;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@SuppressWarnings("unused")
public class HotSwapManager {

    private final ModuleLoader moduleLoader;
    private final Plugin parentPlugin;

    public HotSwapManager(Plugin parentPlugin) {
        this.parentPlugin = parentPlugin;
        this.moduleLoader = new ModuleLoader(parentPlugin);
    }

    public void downloadModules() {
        ModulesFileLoader loader = new ModulesFileLoader(new File(this.parentPlugin.getDataFolder(), "modules.txt"));
        loader.getHttpUrls().forEach((k, v) -> {
            boolean checked = Download.download(v, new File(this.parentPlugin.getDataFolder() + "/modules", k + ".jar"), null);
            if (!checked) {
                this.parentPlugin.getLogger().info(k + ".jar already exists, skip downloading it");
            }
        });
    }

    public void loadModulesFromWeb() {
        ModulesFileLoader loader = new ModulesFileLoader(new File(this.parentPlugin.getDataFolder(), "modules.txt"));
        loader.getHttpUrls().forEach((k, v) -> {
            this.moduleLoader.loadModuleFromWebUrl(v, "modules", k);
        });
    }

    public void loadModulesFromWeb(File localConfig, String saveToFolder) {
        ModulesFileLoader loader = new ModulesFileLoader(localConfig);
        loader.getHttpUrls().forEach((k, v) -> {
            this.moduleLoader.loadModuleFromWebUrl(v, saveToFolder, k);
        });
    }

    public void loadModulesFromWeb(File localConfig, File saveToFolder) {
        ModulesFileLoader loader = new ModulesFileLoader(localConfig);
        loader.getHttpUrls().forEach((k, v) -> {
            this.moduleLoader.loadModuleFromWebUrl(v, new File(saveToFolder, k + ".jar"));
        });
    }

    public void loadModulesFromLocal() {
        this.loadModulesFromLocal("modules");
    }

    public void loadModulesFromLocal(String folder) {
        File modules = new File(this.parentPlugin.getDataFolder(), folder);
        modules.mkdirs();
        Arrays.stream(Objects.requireNonNull(modules
                        .listFiles()))
                .filter(File::isFile)
                .filter(file -> file.getName().endsWith(".jar"))
                .forEach(this.moduleLoader::loadModule);
    }

    public ModuleBase loadModuleFromLocal(File file) {
        return this.moduleLoader.loadModule(file);
    }

    public ModuleBase loadModuleFromLocal(String folder, String moduleName) {
        return this.moduleLoader.loadModuleFromModuleFolderAndModuleName(folder, moduleName);
    }

    public ConcurrentHashMap<String, ModuleBase> getLoadedModules() {
        return this.moduleLoader.getLoadedModules();
    }

    public void enableModule(String name) {
        if (this.getLoadedModules().containsKey(name)) {
            this.getLoadedModules().get(name).setEnabled(true);
        }
    }

    public void enableAllModules() {
        this.getLoadedModules().forEach((k, v) -> {
            v.setEnabled(true);
        });
    }

    public void disableModule(String name) {
        if (this.getLoadedModules().containsKey(name)) {
            this.getLoadedModules().get(name).setEnabled(false);
        }
    }

    public void disableAllModules() {
        this.getLoadedModules().forEach((k, v) -> {
            v.setEnabled(false);
        });
    }

}
