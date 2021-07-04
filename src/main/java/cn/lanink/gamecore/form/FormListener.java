package cn.lanink.gamecore.form;

import cn.lanink.gamecore.form.windows.AdvancedFormWindowCustom;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowModal;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowSimple;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;

/**
 * @author lt_name
 */
public class FormListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFormResponded(PlayerFormRespondedEvent event) {
        if (AdvancedFormWindowSimple.onEvent(event.getWindow(), event.getPlayer())) {
            return;
        }
        if (AdvancedFormWindowModal.onEvent(event.getWindow(), event.getPlayer())) {
            return;
        }
        AdvancedFormWindowCustom.onEvent(event.getWindow(), event.getPlayer());
    }

}
