package cn.lanink.gamecore.hotswap.load;

import cn.lanink.gamecore.hotswap.ModuleBase;
import cn.lanink.gamecore.utils.Download;
import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.Utils;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author iGxnon
 * 实例化该类然后加载你的子模块
 */
@SuppressWarnings("unused")
public class ModuleLoader {

    private final Plugin plugin;
    private final Server server;

    private final Map<String, Class<?>> classes = new HashMap<>();
    private final Map<String, ModuleClassLoader> classLoaders = new HashMap<>();

    private final ConcurrentHashMap<String, ModuleBase> loadedModules = new ConcurrentHashMap<>();

    public ModuleLoader() {
        throw new RuntimeException("error!");
    }

    public ModuleLoader(Plugin plugin) {
        this.plugin = plugin;
        this.server = plugin.getServer();
    }

    /**
     * 从url里下载jar包并加载
     * 注: 这里调用了setEnabled
     *     储存在loadedModules里的是模块配置文件里的name的值
     * @param url 网络url
     * @param folder 插件的子模块目录
     * @param moduleName 子模块名称
     */
    public void loadModuleFromWebUrl(String url, String folder, String moduleName) {
        Download.download(url, new File(plugin.getDataFolder(), folder + "/" + moduleName + ".jar"), file -> {
            ModuleBase module = loadModule(file);
            module.setEnabled(true);
        });
    }

    public void loadModuleFromWebUrl(String url, File saveTo) {
        Download.download(url, saveTo, file -> {
            ModuleBase module = loadModule(file);
            module.setEnabled(true);
        });
    }

    public ModuleBase loadModuleWithDefault(String moduleName) {
        return loadModule(new File(plugin.getDataFolder() + "/modules/" + moduleName + ".jar"));
    }

    public ModuleBase loadModuleFromModuleFolderAndModuleName(String folder, String moduleName) {
        return loadModule(new File(plugin.getDataFolder() + "/" + folder + "/" + moduleName + ".jar"));
    }


    public ModuleBase loadModule(@NonNull File file) {
        try {
            PluginDescription description = this.getModuleDescription(file);
            if (description != null) {
                String className = description.getMain();
                ModuleClassLoader classLoader;
                try {
                    // 使用了本类的类加载器作为模块到父加载器，本类加载器是Nukkit提供的PluginClassLoader，所以模块可以访问到插件数据
                    classLoader = new ModuleClassLoader(this, this.getClass().getClassLoader(), file);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(description.getName() + " ClassLoader get failed!");
                }
                this.classLoaders.put(description.getName(), classLoader);
                ModuleBase module;
                try {
                    Class<?> javaClass = classLoader.loadClass(className);
                    if (!ModuleBase.class.isAssignableFrom(javaClass)) {
                        throw new RuntimeException("Main class `" + description.getMain() + "' does not extend Module");
                    }
                    try {
                        Class<?> pluginClass = javaClass.asSubclass(ModuleBase.class);
                        module = (ModuleBase) pluginClass.newInstance();
                        this.initModule(module, description, file);
                        this.loadedModules.put(module.getName(), module);
                        return module;
                    } catch (ClassCastException e) {
                        throw new RuntimeException(description.getName() + " Error whilst initializing main class");
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(description.getName() + " load failed case: unknow reason");
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(description.getName() + " main class not found");
                }
            } else {
                throw new RuntimeException(file.getName() + " module.yml or plugin.yml not found!");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private PluginDescription getModuleDescription(@NonNull File file) {
        try (JarFile jar = new JarFile(file)) {
            JarEntry entry = jar.getJarEntry("module.yml");
            if (entry == null) {
                entry = jar.getJarEntry("plugin.yml");
            }
            if (entry == null) {
                return null;
            }
            try (InputStream stream = jar.getInputStream(entry)) {
                return new PluginDescription(Utils.readFile(stream));
            }
        } catch (IOException e) {
            return null;
        }
    }

    private void initModule(ModuleBase module, PluginDescription description, File file) {
        module.init(this.server, description, file, plugin);
    }


    public static void enableModule(ModuleBase module) {
        if (module != null && !module.isEnabled()) {
            module.setEnabled(true);
        }
    }


    public static void disableModule(ModuleBase module) {
        if (module != null && module.isEnabled()) {
            module.setEnabled(false);
        }
    }

    Class<?> getClassByName(final String name) {
        Class<?> cachedClass = classes.get(name);
        if (cachedClass != null) {
            return cachedClass;
        } else {
            for (ModuleClassLoader loader : this.classLoaders.values()) {
                try {
                    cachedClass = loader.findClass(name, false);
                } catch (ClassNotFoundException ignore) {
                }
                if (cachedClass != null) {
                    return cachedClass;
                }
            }
        }
        return null;
    }

    void setClass(final String name, final Class<?> clazz) {
        if (!classes.containsKey(name)) {
            classes.put(name, clazz);
        }
    }

}
