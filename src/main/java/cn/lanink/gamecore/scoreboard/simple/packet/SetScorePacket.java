package cn.lanink.gamecore.scoreboard.simple.packet;

import cn.lanink.gamecore.scoreboard.simple.packet.data.ScoreData;
import cn.nukkit.network.protocol.DataPacket;

import java.util.List;

public class SetScorePacket extends DataPacket {

    public static final byte NETWORK_ID = 0x6c;

    public byte type;
    public List<ScoreData> scoreDataList;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.type);
        this.putUnsignedVarInt(this.scoreDataList.size());
        for (ScoreData scoreData : this.scoreDataList) {
            this.putVarLong(scoreData.scoreId);
            this.putString(scoreData.objective);
            this.putLInt(scoreData.score);
            if(this.type == Action.SET.ordinal()) {
                this.putByte(scoreData.entityType);
                switch (scoreData.entityType) {
                    case 3:
                        this.putString(scoreData.fakeEntity);
                        break;
                    case 1:
                    case 2:
                        this.putUnsignedVarLong(scoreData.entityId);
                        break;
                }
            }
        }
    }

    public enum Action {
        SET,
        REMOVE
    }

    public enum Type {
        INVALID,
        PLAYER,
        ENTITY,
        FAKE
    }

}
