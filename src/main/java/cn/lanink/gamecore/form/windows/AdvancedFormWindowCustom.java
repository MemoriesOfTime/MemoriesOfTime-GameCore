package cn.lanink.gamecore.form.windows;

import cn.nukkit.Player;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
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
public class AdvancedFormWindowCustom extends FormWindowCustom implements SetPlugin {

    protected Plugin plugin = null;
    protected BiConsumer<FormResponseCustom, Player> buttonClickedListener;
    protected Consumer<Player> formClosedListener;

    public AdvancedFormWindowCustom(String title) {
        super(title);
    }

    public AdvancedFormWindowCustom(String title, List<Element> contents) {
        super(title, contents);
    }

    public AdvancedFormWindowCustom(String title, List<Element> contents, String icon) {
        super(title, contents, icon);
    }

    public AdvancedFormWindowCustom(String title, List<Element> contents, ElementButtonImageData icon) {
        super(title, contents, icon);
    }

    @Override
    public void setPlugin(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    public AdvancedFormWindowCustom onResponded(@NotNull BiConsumer<FormResponseCustom, Player> listener) {
        this.buttonClickedListener = listener;
        return this;
    }

    public AdvancedFormWindowCustom onClosed(@NotNull Consumer<Player> listener) {
        this.formClosedListener = Objects.requireNonNull(listener);
        return this;
    }

    protected void callResponded(@NotNull FormResponseCustom formResponseCustom, @NotNull Player player) {
        if (this.buttonClickedListener != null) {
            this.buttonClickedListener.accept(formResponseCustom, player);
        }
    }

    protected void callClosed(@NotNull Player player) {
        if (this.formClosedListener != null) {
            this.formClosedListener.accept(player);
        }
    }

    public static boolean onEvent(@NotNull FormWindow formWindow, FormResponse formResponse, @NotNull Player player, @NotNull Plugin plugin) {
        if (formWindow instanceof AdvancedFormWindowCustom) {
            AdvancedFormWindowCustom advancedFormWindowCustom = (AdvancedFormWindowCustom) formWindow;
            if (advancedFormWindowCustom.plugin != null && advancedFormWindowCustom.plugin != plugin) {
                return false;
            }
            if (advancedFormWindowCustom.wasClosed() || advancedFormWindowCustom.getResponse() == null || formResponse == null) {
                advancedFormWindowCustom.callClosed(player);
            }else {
                advancedFormWindowCustom.callResponded((FormResponseCustom) formResponse, player);
            }
            return true;
        }
        return false;
    }

    @Override
    public String getJSONData() {
        return new Gson().toJson(this, FormWindowCustom.class);
    }

}
