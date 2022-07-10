package cn.lanink.gamecore.form.response;

import cn.lanink.gamecore.form.element.ResponseElementDialogButton;
import cn.lanink.gamecore.utils.packet.NPCRequestPacket;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowDialog;
import lombok.Getter;

@Getter
public class FormResponseDialog {

    private final long entityRuntimeId;
    private final String data;
    private ResponseElementDialogButton clickedButton;//can be null
    private final String sceneName;
    private final NPCRequestPacket.RequestType requestType;
    private final int skinType;

    public FormResponseDialog(NPCRequestPacket packet, AdvancedFormWindowDialog dialog) {
        this.entityRuntimeId = packet.getRequestedEntityRuntimeId();
        this.data = packet.getData();
        try {
            this.clickedButton = dialog.getButtons().get(packet.getSkinType());
        }catch (IndexOutOfBoundsException e){
            this.clickedButton = null;
        }
        this.sceneName = packet.getSceneName();
        this.requestType = packet.getRequestType();
        this.skinType = packet.getSkinType();
    }
}
