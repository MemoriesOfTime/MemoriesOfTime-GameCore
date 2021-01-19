package cn.lanink.gamecore.form.windows;

import cn.lanink.gamecore.form.element.ResponseElementButton;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.plugin.Plugin;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author lt_name
 */
public class AdvancedFormWindowSimple extends FormWindowSimple implements SetPlugin {

    protected Plugin plugin = null;
    protected BiConsumer<ElementButton, Player> buttonClickedListener;
    protected Consumer<Player> formClosedListener;

    public AdvancedFormWindowSimple(String title) {
        this(title, "");
    }

    public AdvancedFormWindowSimple(String title, String content) {
        super(title, content);
    }

    public AdvancedFormWindowSimple(String title, String content, List<ElementButton> buttons) {
        super(title, content, buttons);
    }

    @Override
    public void setPlugin(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    public void addButton(String text, Consumer<Player> listener) {
        this.addButton(new ResponseElementButton(text).onClicked(listener));
    }

    public AdvancedFormWindowSimple onClicked(@NotNull BiConsumer<ElementButton, Player> listener) {
        this.buttonClickedListener = Objects.requireNonNull(listener);
        return this;
    }

    public AdvancedFormWindowSimple onClosed(@NotNull Consumer<Player> listener) {
        this.formClosedListener = Objects.requireNonNull(listener);
        return this;
    }

    public void callClicked(@NotNull ElementButton elementButton, @NotNull Player player) {
        if (this.buttonClickedListener != null) {
            this.buttonClickedListener.accept(elementButton, player);
        }
    }

    public void callClosed(@NotNull Player player) {
        if (this.formClosedListener != null) {
            this.formClosedListener.accept(player);
        }
    }

    public static boolean onEvent(@NotNull FormWindow formWindow, FormResponse formResponse, @NotNull Player player, @NotNull Plugin plugin) {
        if (formWindow instanceof AdvancedFormWindowSimple) {
            AdvancedFormWindowSimple advancedFormWindowSimple = (AdvancedFormWindowSimple) formWindow;
            if (advancedFormWindowSimple.plugin != null && advancedFormWindowSimple.plugin != plugin) {
                return false;
            }
            if (advancedFormWindowSimple.wasClosed() || advancedFormWindowSimple.getResponse() == null || formResponse == null) {
                advancedFormWindowSimple.callClosed(player);
            }else {
                ElementButton elementButton = advancedFormWindowSimple.getResponse().getClickedButton();
                if (elementButton instanceof ResponseElementButton && ((ResponseElementButton) elementButton).callClicked(player)) {
                    return true;
                }else {
                    advancedFormWindowSimple.callClicked(elementButton, player);
                }
            }
            return true;
        }
        return false;
    }

    public String getJSONData() {
        return new Gson().toJson(this, FormWindowSimple.class);
    }

}
