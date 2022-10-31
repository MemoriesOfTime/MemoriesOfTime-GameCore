package cn.lanink.gamecore.form.windows;

import cn.lanink.gamecore.form.element.ResponseElementDialogButton;
import cn.nukkit.Player;
import cn.nukkit.dialog.element.ElementDialogButton;
import cn.nukkit.dialog.response.FormResponseDialog;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.StringEntityData;
import cn.nukkit.network.protocol.NPCDialoguePacket;
import cn.nukkit.network.protocol.NPCRequestPacket;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class AdvancedFormWindowDialog extends cn.nukkit.dialog.window.FormWindowDialog {

    protected BiConsumer<Player, FormResponseDialog> formClosedListener;

    private boolean isClosed = false;

    public AdvancedFormWindowDialog(String title, String content, Entity bindEntity) {
        this(title, content,bindEntity, new ArrayList<>());
    }

    public AdvancedFormWindowDialog(String title, String content, Entity bindEntity, List<cn.nukkit.dialog.element.ElementDialogButton> buttons) {
        super(title, content, bindEntity, buttons);
        if (this.getBindEntity() == null) {
            throw new IllegalArgumentException("bindEntity cannot be null!");
        }
    }

    @Deprecated
    public void addButton(String text) {
        this.addButton(new ResponseElementDialogButton(text, text));
    }

    public ResponseElementDialogButton addAdvancedButton(String text) {
        return this.addButton(new ResponseElementDialogButton(text, text));
    }

    public ResponseElementDialogButton addButton(ResponseElementDialogButton button) {
        super.addButton(button);
        return button;
    }

    public AdvancedFormWindowDialog onClosed(@NotNull BiConsumer<Player, FormResponseDialog> listener) {
        this.formClosedListener = Objects.requireNonNull(listener);
        return this;
    }

    protected void callClosed(@NotNull Player player, FormResponseDialog response) {
        if (this.formClosedListener != null && !this.isClosed) {
            this.formClosedListener.accept(player, response);
        }
    }

    public static final Cache<String, AdvancedFormWindowDialog> WINDOW_DIALOG_CACHE = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();

    @Override
    public void send(Player player){
        if(WINDOW_DIALOG_CACHE.getIfPresent(this.getSceneName()) != null) {
            this.updateSceneName();
        }
        String actionJson = this.getButtonJSONData();

        this.getBindEntity().setDataProperty(new ByteEntityData(Entity.DATA_HAS_NPC_COMPONENT, 1));
        this.getBindEntity().setDataProperty(new StringEntityData(Entity.DATA_NPC_SKIN_DATA, this.getSkinData()));
        this.getBindEntity().setDataProperty(new StringEntityData(Entity.DATA_NPC_ACTIONS, actionJson));
        this.getBindEntity().setDataProperty(new StringEntityData(Entity.DATA_INTERACTIVE_TAG, this.getContent()));

        NPCDialoguePacket packet = new NPCDialoguePacket();
        packet.setRuntimeEntityId(this.getEntityId());
        packet.setAction(NPCDialoguePacket.NPCDialogAction.OPEN);
        packet.setDialogue(this.getContent());
        packet.setNpcName(this.getTitle());
        packet.setSceneName(this.getSceneName());
        packet.setActionJson(actionJson);
        WINDOW_DIALOG_CACHE.put(this.getSceneName(), this);
        player.dataPacket(packet);
    }

    public void close(Player player, FormResponseDialog response) {
        NPCDialoguePacket closeWindowPacket = new NPCDialoguePacket();
        closeWindowPacket.setRuntimeEntityId(response.getEntityRuntimeId());
        closeWindowPacket.setAction(NPCDialoguePacket.NPCDialogAction.CLOSE);
        closeWindowPacket.setSceneName(response.getSceneName());
        player.dataPacket(closeWindowPacket);
    }

    public static boolean onEvent(@NotNull NPCRequestPacket packet, @NotNull Player player) {
        AdvancedFormWindowDialog dialog = WINDOW_DIALOG_CACHE.getIfPresent(packet.getSceneName());
        if (dialog == null) {
            return false;
        }

        if (packet.getRequestType() == NPCRequestPacket.RequestType.EXECUTE_CLOSING_COMMANDS) {
            WINDOW_DIALOG_CACHE.invalidate(packet.getSceneName());
        }

        FormResponseDialog response = new FormResponseDialog(packet, dialog);

        ElementDialogButton clickedButton = response.getClickedButton();
        if (packet.getRequestType() == NPCRequestPacket.RequestType.EXECUTE_ACTION && clickedButton != null) {
            if (clickedButton instanceof ResponseElementDialogButton responseElementDialogButton) {
                responseElementDialogButton.callClicked(player, response);
            }
            //点击按钮后，需要关闭当前窗口或者跳转新的窗口，否则对话框会卡住玩家，所以可以认为当前对话框已经关闭
            dialog.isClosed = true;
        }

        if (packet.getRequestType() == NPCRequestPacket.RequestType.EXECUTE_CLOSING_COMMANDS) {
            dialog.callClosed(player, response);
        }
        return true;
    }

}
