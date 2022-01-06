package cn.lanink.gamecore.hotswap;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.LogLevel;
import cn.nukkit.utils.Logger;
import lombok.NonNull;

/**
 * @author iGxnon
 */
public class ModuleLogger implements Logger {

    private final Module module;
    private final String prefix;
    private final Plugin parent;

    public ModuleLogger(@NonNull Module module, @NonNull Plugin parentPlugin) {
        this.module = module;
        this.parent = parentPlugin;
        this.prefix = "[" + module.getName() + "]";
    }

    public Module getModule() {
        return module;
    }

    @Override
    public void emergency(String s) {
        parent.getLogger().emergency( prefix + ": " + s);
    }

    @Override
    public void alert(String s) {
        parent.getLogger().alert( prefix + ": " + s);
    }

    @Override
    public void critical(String s) {
        parent.getLogger().critical( prefix + ": " + s);
    }

    @Override
    public void error(String s) {
        parent.getLogger().error( prefix + ": " + s);
    }

    @Override
    public void warning(String s) {
        parent.getLogger().warning( prefix + ": " + s);
    }

    @Override
    public void notice(String s) {
        parent.getLogger().notice( prefix + ": " + s);
    }

    @Override
    public void info(String s) {
        parent.getLogger().info( prefix + ": " + s);
    }

    @Override
    public void debug(String s) {
        parent.getLogger().debug( prefix + ": " + s);
    }

    @Override
    public void log(LogLevel logLevel, String s) {
        // Nothing here
    }

    @Override
    public void emergency(String s, Throwable throwable) {
        parent.getLogger().emergency( prefix + ": " + s, throwable);
    }

    @Override
    public void alert(String s, Throwable throwable) {
        parent.getLogger().alert( prefix + ": " + s, throwable);
    }

    @Override
    public void critical(String s, Throwable throwable) {
        parent.getLogger().critical( prefix + ": " + s, throwable);
    }

    @Override
    public void error(String s, Throwable throwable) {
        parent.getLogger().error( prefix + ": " + s, throwable);
    }

    @Override
    public void warning(String s, Throwable throwable) {
        parent.getLogger().warning( prefix + ": " + s, throwable);
    }

    @Override
    public void notice(String s, Throwable throwable) {
        parent.getLogger().notice( prefix + ": " + s, throwable);
    }

    @Override
    public void info(String s, Throwable throwable) {
        parent.getLogger().info( prefix + ": " + s, throwable);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        parent.getLogger().debug( prefix + ": " + s, throwable);
    }

    @Override
    public void log(LogLevel logLevel, String s, Throwable throwable) {
        // Nothing here
    }
}
