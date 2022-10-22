package cn.lanink.gamecore.hotswap;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.Config;

import java.io.File;
import java.io.InputStream;

/**
 * @author iGxnon
 */
public interface IModule {

    /**
     * 返回一个模块是否被加载
     * @return boolean
     */
    boolean isEnabled();

    /**
     * 初始化模块
     */
    void init(Server server, PluginDescription description, File file, Plugin parentPlugin);

    /**
     * 加载/卸载模块
     * 注：请不要使用onEnable()或onDisable()来加载卸载模块!
     */
    void setEnabled(boolean val);

    /**
     * 获取模块名称
     * @return 模块名称
     */
    String getName();

    /**
     * 获取服务器对象
     */
    Server getServer();

    /**
     * 获取父插件对象
     */
    Plugin getParentPlugin();

    /**
     * 获取模块的配置
     */
    PluginDescription getDescription();

    InputStream getResource(String filename);

    boolean saveResource(String filename);

    boolean saveResource(String filename, boolean replace);

    boolean saveResource(String filename, String outputName, boolean replace);

    Config getConfig();

    void saveConfig();

    void saveDefaultConfig();

    void reloadConfig();

    /**
     * 获取模块文件
     */
    File getFile();

    /**
     * 获取模块数据文件夹
     */
    File getDataFolder();

    /**
     * 获取模块的Logger
     */
    ModuleLogger getLogger();

}
