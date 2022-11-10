package cn.lanink.gamecore.form.element;

import cn.nukkit.Player;
import cn.nukkit.dialog.response.FormResponseDialog;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;

public class ResponseElementDialogButton extends cn.nukkit.dialog.element.ElementDialogButton {

    private transient BiConsumer<Player, cn.nukkit.dialog.response.FormResponseDialog> clickedListener;

    public ResponseElementDialogButton onClicked(@NotNull BiConsumer<Player, cn.nukkit.dialog.response.FormResponseDialog> clickedListener) {
        this.clickedListener = Objects.requireNonNull(clickedListener);
        return this;
    }

    public boolean callClicked(@NotNull Player player, FormResponseDialog response) {
        if (this.clickedListener != null) {
            this.clickedListener.accept(player, response);
            return true;
        }
        return false;
    }

    public ResponseElementDialogButton(String name, String text) {
        this(name, text, Mode.BUTTON_MODE);
    }

    public ResponseElementDialogButton(String name, String text, Mode mode) {
        this(name, text, mode, 1);
    }

    public ResponseElementDialogButton(String name, String text, Mode mode, int type) {
        super(name, text, null, mode, type);
    }

}
