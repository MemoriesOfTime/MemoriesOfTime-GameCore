package cn.lanink.gamecore.scoreboard.simple.packet.data;

/**
 * @author lt_name
 */
public class ScoreData {

    public long scoreId;
    public String objective;
    public int score;

    public byte entityType;
    public String fakeEntity;
    public long entityId;

    @Override
    public String toString() {
        return "SetScorePacket.ScoreEntry(scoreId=" + this.scoreId +
                ", objective=" + this.objective +
                ", score=" + this.score +
                ", entityType=" + this.entityType +
                ", fakeEntity=" + this.fakeEntity +
                ", entityId=" + this.entityId + ")";
    }

}
