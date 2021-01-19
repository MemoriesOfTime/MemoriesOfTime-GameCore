package cn.lanink.gamecore.form;

import cn.lanink.gamecore.form.windows.AdvancedFormWindowModal;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowSimple;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.plugin.Plugin;

/**
 * @author lt_name
 */
public class FormListener implements Listener {

    private Plugin plugin;

    FormListener(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResponded(PlayerFormRespondedEvent event) {
        if (AdvancedFormWindowSimple.onEvent(event.getWindow(), event.getResponse(), event.getPlayer(), this.plugin)) {
            return;
        }
        if (AdvancedFormWindowModal.onEvent(event.getWindow(), event.getResponse(), event.getPlayer(), this.plugin)) {
            return;
        }

    }

}
