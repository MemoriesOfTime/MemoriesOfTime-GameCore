package cn.lanink.gamecore.utils.packet;

import lombok.ToString;

@ToString
public class NPCRequestPacket extends cn.nukkit.network.protocol.NPCRequestPacket {

    public long getRequestedEntityRuntimeId() {
        return entityRuntimeId;
    }

    public void setRequestedEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public RequestType getRequestType() {
        return RequestType.valueOf(requestType.name());
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = cn.nukkit.network.protocol.NPCRequestPacket.RequestType.valueOf(requestType.name());
    }

    public String getData() {
        return commandString;
    }

    public void setData(String data) {
        this.commandString = data;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public enum RequestType {

        SET_ACTIONS,
        EXECUTE_ACTION,
        EXECUTE_CLOSING_COMMANDS,
        SET_NAME,
        SET_SKIN,
        SET_INTERACTION_TEXT,
        EXECUTE_OPENING_COMMANDS
    }

}
