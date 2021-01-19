package cn.lanink.gamecore.form.windows;

import cn.nukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author lt_name
 */
public interface SetPlugin {

    void setPlugin(@NotNull Plugin plugin);

    Plugin getPlugin();

}
