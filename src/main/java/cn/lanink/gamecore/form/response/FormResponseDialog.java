package cn.lanink.gamecore.form.response;

import cn.lanink.gamecore.form.element.ResponseElementDialogButton;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowDialog;
import cn.nukkit.network.protocol.NPCRequestPacket;
import lombok.Getter;

@Getter
public class FormResponseDialog {

    private final long entityRuntimeId;
    private final String data;
    private ResponseElementDialogButton clickedButton;//can be null
    private final String sceneName;
    private final NPCRequestPacket.RequestType requestType;
    private final int actionType;

    public FormResponseDialog(NPCRequestPacket packet, AdvancedFormWindowDialog dialog) {
        this.entityRuntimeId = packet.entityRuntimeId;
        this.data = packet.commandString;
        try {
            this.clickedButton = dialog.getButtons().get(packet.actionType);
        }catch (IndexOutOfBoundsException e){
            this.clickedButton = null;
        }
        this.sceneName = packet.sceneName;
        this.requestType = packet.requestType;
        this.actionType = packet.actionType;
    }
}
