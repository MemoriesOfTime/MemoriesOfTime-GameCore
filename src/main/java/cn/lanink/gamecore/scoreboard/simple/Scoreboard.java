package cn.lanink.gamecore.scoreboard.simple;

import cn.lanink.gamecore.scoreboard.simple.packet.RemoveObjectivePacket;
import cn.lanink.gamecore.scoreboard.simple.packet.SetObjectivePacket;
import cn.lanink.gamecore.scoreboard.simple.packet.SetScorePacket;
import cn.lanink.gamecore.scoreboard.simple.packet.data.ScoreData;
import cn.nukkit.Player;

import java.util.*;

/**
 * @author lt_name
 */
public class Scoreboard {

    private final String criteriaName = "dummy";
    private final String objectiveName;
    private final String displayName;
    private final ScoreboardData.DisplaySlot displaySlot;
    private final ScoreboardData.SortOrder sortOrder;
    private final HashMap<Integer, ScoreboardData.ScoreboardLine> line = new HashMap<>();
    private final HashMap<Integer, ScoreboardData.ScoreboardLine> oldLine = new HashMap<>();
    private final HashSet<Player> showPlayers = new HashSet<>();

    public Scoreboard(String objective, String title, ScoreboardData.DisplaySlot displaySlot, ScoreboardData.SortOrder sortOrder) {
        this.objectiveName = objective;
        this.displayName = title;
        this.displaySlot = displaySlot;
        this.sortOrder = sortOrder;
    }

    public void show(Player player) {
        if (this.showPlayers.add(player)) {
            SetObjectivePacket setObjectivePacket = new SetObjectivePacket();
            setObjectivePacket.criteriaName = this.criteriaName;
            setObjectivePacket.displayName = this.displayName;
            setObjectivePacket.objectiveName = this.objectiveName;
            setObjectivePacket.displaySlot = this.displaySlot.name().toLowerCase();
            setObjectivePacket.sortOrder = this.sortOrder.ordinal();
            player.dataPacket(setObjectivePacket);
            this.updateDisplay(player);
        }
    }

    public void hide(Player player) {
        if (this.showPlayers.contains(player)) {
            RemoveObjectivePacket removeObjectivePacket = new RemoveObjectivePacket();
            removeObjectivePacket.objectiveName = this.objectiveName;
            player.dataPacket(removeObjectivePacket);
            this.showPlayers.remove(player);
        }
    }

    public void updateDisplay() {
        for (Player player : this.showPlayers) {
            this.updateDisplay(player);
        }
    }

    public void updateDisplay(Player player) {
        List<ScoreData> scoreDataList = new LinkedList<>();
        for (Map.Entry<Integer, ScoreboardData.ScoreboardLine> entry : this.line.entrySet()) {
            ScoreData scoreData = new ScoreData();
            scoreData.scoreId = entry.getValue().getScore();
            scoreData.objective = this.objectiveName;
            scoreData.score = entry.getValue().getScore();
            scoreData.entityType = (byte) SetScorePacket.Type.FAKE.ordinal();
            scoreData.fakeEntity = entry.getValue().getMessage();
            scoreData.entityId = 0;
            scoreDataList.add(scoreData);
        }
        SetScorePacket pk = new SetScorePacket();
        pk.type = (byte) SetScorePacket.Action.SET.ordinal();
        pk.scoreDataList = scoreDataList;
        player.dataPacket(pk);
    }

    public void removeDisplay() {
        for (Player player : this.showPlayers) {
            this.removeDisplay(player);
        }
    }

    public void removeDisplay(Player player) {
        List<ScoreData> scoreDataList = new LinkedList<>();
        for (Map.Entry<Integer, ScoreboardData.ScoreboardLine> entry : this.line.entrySet()) {
            ScoreData scoreData = new ScoreData();
            scoreData.scoreId = entry.getValue().getScore();
            scoreData.objective = this.objectiveName;
            scoreData.score = entry.getValue().getScore();
            scoreData.entityType = (byte) SetScorePacket.Type.FAKE.ordinal();
            scoreData.fakeEntity = entry.getValue().getMessage();
            scoreData.entityId = 0;
            scoreDataList.add(scoreData);
        }
        SetScorePacket pk = new SetScorePacket();
        pk.type = (byte) SetScorePacket.Action.REMOVE.ordinal();
        pk.scoreDataList = scoreDataList;
        player.dataPacket(pk);
    }

    public HashMap<Integer, ScoreboardData.ScoreboardLine> getAllLine() {
        return this.line;
    }

    public void setLine(int id, String message, int score) {
        this.line.put(id, new ScoreboardData.ScoreboardLine(message, score));
    }

    public int addLine(String message, int score) {
        return this.addLine(this.line.size() + 1, message, score);
    }

    public int addLine(int id, String message, int score) {
        this.line.put(id, new ScoreboardData.ScoreboardLine(message, score));
        return id;
    }

    public void removeLine(int id) {
        this.line.remove(id);
    }

}
