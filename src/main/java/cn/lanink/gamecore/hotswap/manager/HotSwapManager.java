package cn.lanink.gamecore.hotswap.manager;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.hotswap.ModuleBase;
import cn.lanink.gamecore.hotswap.load.ModuleLoader;
import cn.lanink.gamecore.hotswap.load.ModulesFileLoader;
import cn.lanink.gamecore.utils.Download;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import lombok.Getter;

import java.io.File;
import java.util.*;
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
            boolean checked = Download.downloadAsync(v, new File(this.parentPlugin.getDataFolder() + "/modules", k + ".jar"), null);
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
        // 依赖图
        Map<String, List<String>> modules2dependencies = new LinkedHashMap<>();
        Map<String, File> moduleFiles = new HashMap<>();
        Arrays.stream(Objects.requireNonNull(modules
                        .listFiles()))
                .filter(File::isFile)
                .filter(file -> file.getName().endsWith(".jar"))
                .forEach(file -> {
                    PluginDescription description = ModuleLoader.getModuleDescription(file);
                    if (description == null) {
                        GameCore.getInstance().getLogger().info(file.getName() + " module.yml or plugin.yml not found!");
                        return;
                    }
                    modules2dependencies.put(description.getName(), description.getDepend());
                    moduleFiles.put(description.getName(), file);
                });
        modules2dependencies.forEach((module, v) -> {
            dfs(modules2dependencies, moduleFiles, module, new HashSet<>());
        });
    }

    private boolean dfs(Map<String, List<String>> map, Map<String, File> files, String start, HashSet<String> walked) {
        walked.add(start);
        if (!map.containsKey(start)) {
            GameCore.getInstance().getLogger().warning("§c cannot find dependency module " + start);
            return false;
        }
        if (!map.get(start).isEmpty()) {
            boolean nextLoaded = true;
            boolean claimCircle = false;
            for (String next : map.get(start)) {
                if (walked.contains(next)) {
                    nextLoaded = false;
                    claimCircle = true;  // 肯定这一步就会有环
                    GameCore.getInstance().getLogger().warning("§c detect circle depend, stop load module " + start);
                    continue;
                }
                if (claimCircle) { // 如果这一步就有环，没必要看下一步没有有环
                    dfs(map, files, next, walked);
                    continue;
                }
                nextLoaded = dfs(map, files, next, walked); // 看一下下一步会不会有环
            }
            if (!nextLoaded) {
                GameCore.getInstance().getLogger().warning("§c cannot load module " + start + " because it's dependency cannot be loaded or cannot be found");
                return false;
            }
            this.moduleLoader.loadModule(files.get(start));
        }else if (!this.moduleLoader.getLoadedModules().containsKey(start)) {
            this.moduleLoader.loadModule(files.get(start));
        }
        return true; // 无向有环，有向无环
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

    public void disableModule(String name) {
        this.getLoadedModules().get(name).setEnabled(false);
    }

    public void disableAllModules() {
        this.getLoadedModules().forEach((k, v) -> {
            v.setEnabled(false);
        });
    }

}
