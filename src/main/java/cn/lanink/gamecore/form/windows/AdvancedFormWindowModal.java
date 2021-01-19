package cn.lanink.gamecore.form.windows;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.plugin.Plugin;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author lt_name
 */
public class AdvancedFormWindowModal extends FormWindowModal implements SetPlugin {

    protected Plugin plugin = null;
    protected Consumer<Player> buttonTrueClickedListener, buttonFalseClickedListener, formClosedListener;

    public AdvancedFormWindowModal(String title, String content, String trueButtonText, String falseButtonText) {
        super(title, content, trueButtonText, falseButtonText);
    }

    @Override
    public void setPlugin(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    public AdvancedFormWindowModal onClickedTrue(@NotNull Consumer<Player> listener) {
        this.buttonTrueClickedListener = Objects.requireNonNull(listener);
        return this;
    }

    public AdvancedFormWindowModal onClickedFalse(@NotNull Consumer<Player> listener) {
        this.buttonFalseClickedListener = Objects.requireNonNull(listener);
        return this;
    }

    public AdvancedFormWindowModal onClosed(@NotNull Consumer<Player> listener) {
        this.formClosedListener = Objects.requireNonNull(listener);
        return this;
    }

    public void callClickedTrue(@NotNull Player player) {
        if (this.buttonTrueClickedListener != null) {
            this.buttonTrueClickedListener.accept(player);
        }
    }

    public void callClickedFalse(@NotNull Player player) {
        if (this.buttonFalseClickedListener != null) {
            this.buttonFalseClickedListener.accept(player);
        }
    }

    public void callClosed(@NotNull Player player) {
        if (this.formClosedListener != null) {
            this.formClosedListener.accept(player);
        }
    }

    public static boolean onEvent(@NotNull FormWindow formWindow, FormResponse formResponse, @NotNull Player player, @NotNull Plugin plugin) {
        if (formWindow instanceof AdvancedFormWindowModal) {
            AdvancedFormWindowModal advancedFormWindowModal = (AdvancedFormWindowModal) formWindow;
            if (advancedFormWindowModal.plugin != null && advancedFormWindowModal.plugin != plugin) {
                return false;
            }
            if (advancedFormWindowModal.wasClosed() || advancedFormWindowModal.getResponse() == null || formResponse == null) {
                advancedFormWindowModal.callClosed(player);
            }else {
                if (advancedFormWindowModal.getResponse().getClickedButtonId() == 0) {
                    advancedFormWindowModal.callClickedTrue(player);
                }else {
                    advancedFormWindowModal.callClickedFalse(player);
                }
            }
            return true;
        }
        return false;
    }

    public String getJSONData() {
        return new Gson().toJson(this, FormWindowModal.class);
    }

}
