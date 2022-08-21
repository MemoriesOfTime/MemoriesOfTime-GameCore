package cn.lanink.gamecore.ranking;

import cn.lanink.gamecore.GameCore;
import cn.lanink.gamecore.entity.TextFakeEntity;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.Plugin;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author lt_name
 */
public class Ranking {

    private final RankingAPI rankingAPI = RankingAPI.getInstance();
    @Getter
    private final Plugin plugin;
    @Setter
    @Getter
    private String name;
    @Getter
    private Position position;
    @Getter
    private IEntityRanking entityRanking;
    private RankingFormat rankingFormat = RankingFormat.getDefaultFormat();

    private Supplier<Map<String, ? extends Number>> supplier = null;

    private final AtomicBoolean listLock = new AtomicBoolean();
    private final AtomicBoolean needSequence = new AtomicBoolean();

    private final ConcurrentHashMap<String, Number> originalList = new ConcurrentHashMap<>();
    private final LinkedHashMap<String, String> list = new LinkedHashMap<>();

    @Getter
    private int dataUpdateInterval = 20;

    @Getter
    private boolean closed = false;

    /**
     * 排行榜
     *
     * @param plugin 插件主类
     * @param name 排行榜名称
     * @param position 排行榜位置
     */
    public Ranking(@NotNull Plugin plugin, @NotNull String name, @NotNull Position position) {
        this.plugin = plugin;
        this.setName(name);
        this.setPosition(position);
        this.setRankingEntity(TextFakeEntity.class);
        this.schedulerTask();
    }

    private void schedulerTask() {
        this.rankingAPI.getUpdateTask().addRanking(this);
        this.rankingAPI.getAsyncUpdateTask().addRanking(this);
    }

    public void onTick(int i) {
        if (this.isClosed()) {
            return;
        }
        if (i%this.getDataUpdateInterval() == 0) {
            if (this.supplier != null) {
                this.setRankingList(this.supplier.get(), false);
            }
        }
        if (this.entityRanking.needTick()) {
            this.entityRanking.onTick(i);
        }
    }

    public void onAsyncTick(int i) {
        if (this.isClosed()) {
            return;
        }
        if (this.plugin.isDisabled()) {
            this.close();
            return;
        }
        if (i%this.getDataUpdateInterval() == 0) {
            this.rearrangeList();
            this.updateShowText();
        }
        if (this.entityRanking.needAsyncTick()) {
            this.entityRanking.onAsyncTick(i);
        }
    }

    private void updateShowText() {
        if (this.listLock.get()) {
            return;
        }

        for (Player player : new HashSet<>(this.entityRanking.getShowTextMap().keySet())) {
            if (!player.isOnline()) {
                this.entityRanking.getShowTextMap().remove(player);
            }
        }

        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            StringBuilder showText = new StringBuilder(this.rankingFormat.getTop()
                    .replace("%name%", this.getName())).append("\n");

            int line = 0;
            int showLine = 1;
            double distance = this.getPosition().distance(player);
            for (Map.Entry<Integer, Integer> entry : this.rankingFormat.getShowLine().entrySet()) {
                if (distance <= entry.getKey()) {
                    showLine = entry.getValue();
                    break;
                }
            }
            for (Map.Entry<String, String> entry : this.list.entrySet()) {
                line++;
                if (line > showLine) {
                    break;
                }

                String lineText;
                if (player.getName().equals(entry.getKey())) {
                    lineText = this.rankingFormat.getLineSelf()
                            .replace("%ranking%", line + "")
                            .replace("%player%", entry.getKey())
                            .replace("%score%", entry.getValue());
                }else {
                    lineText = this.rankingFormat.getLine()
                            .replace("%ranking%", line + "")
                            .replace("%player%", entry.getKey())
                            .replace("%score%", entry.getValue());
                }
                showText.append(lineText).append("\n");
            }

            showText.append(this.rankingFormat.getBottom().replace("%name%", this.getName()));
            this.entityRanking.setShowText(player, showText.toString());
        }
    }

    /**
     * 设置排行榜位置
     *
     * @param position 排行榜位置
     */
    public void setPosition(@NotNull Position position) {
        this.position = position;
        if (this.entityRanking != null) {
            this.entityRanking.setPosition(position);
        }
    }

    /**
     * 设置排行榜实体
     *
     * @param newEntityRanking 新排行榜实体
     */
    public void setRankingEntity(@NotNull Class<? extends IEntityRanking> newEntityRanking) {
        if (this.entityRanking != null) {
            this.entityRanking.close();
        }
        try {
            this.entityRanking = newEntityRanking.getConstructor().newInstance();
            this.entityRanking.setPosition(this.position);
        } catch (Exception e) {
            GameCore.getInstance().getLogger().error("[RankingAPI] 创建实体时出现错误：", e);
        }
    }

    /**
     * 设置排行榜格式
     *
     * @param rankingFormat 排行榜格式
     */
    public void setRankingFormat(@NotNull RankingFormat rankingFormat) {
        this.rankingFormat = rankingFormat;
    }


    public void setRankingList(@NotNull Supplier<Map<String, ? extends Number>> supplier) {
        this.supplier = supplier;
        this.setRankingList(this.supplier.get());
    }

    /**
     * 设置需要排行的数据
     *
     * @param newList 新数据
     */
    public synchronized void setRankingList(@NotNull Map<String, ? extends Number> newList) {
        this.setRankingList(newList, true);
    }

    /**
     * 设置需要排行的数据
     *
     * @param newList 新数据
     * @param updateShowText true 现在更新显示数据 false 等待排行榜task更新显示数据
     */
    public synchronized void setRankingList(@NotNull Map<String, ? extends Number> newList, boolean updateShowText) {
        this.originalList.clear();
        this.originalList.putAll(newList);

        this.needSequence.set(true);

        if (updateShowText) {
            this.rearrangeList();
            this.updateShowText();
        }
    }

    private void rearrangeList() {
        if (!this.needSequence.get() || this.listLock.get()) {
            return;
        }
        this.listLock.set(true);

        ArrayList<Map.Entry<String, ? extends Number>> arrayList = new ArrayList<>(this.originalList.entrySet());
        if (this.rankingFormat.getSortOrder() == RankingFormat.SortOrder.ASCENDING) {
            arrayList.sort((o1, o2) -> {
                if (Objects.equals(o1.getValue().doubleValue(), o2.getValue().doubleValue())) {
                    return 0;
                }
                return o1.getValue().doubleValue() > o2.getValue().doubleValue() ? -1 : 1;
            });
        }else {
            arrayList.sort((o1, o2) -> {
                if (Objects.equals(o1.getValue().doubleValue(), o2.getValue().doubleValue())) {
                    return 0;
                }
                return o1.getValue().doubleValue() > o2.getValue().doubleValue() ? 1 : -1;
            });
        }

        this.list.clear();
        for(Map.Entry<String, ? extends Number> entry : arrayList) {
            this.list.put(entry.getKey(), new BigDecimal(entry.getValue().toString()).toString()); //避免使用科学计数法显示
        }

        this.needSequence.set(false);
        this.listLock.set(false);
    }

    /**
     * 设置数据更新间隔
     *
     * @param dataUpdateInterval 更新间隔（单位：tick）
     */
    public void setDataUpdateInterval(int dataUpdateInterval) {
        this.dataUpdateInterval = Math.max(dataUpdateInterval, 1);
    }

    /**
     * 清理排行榜
     */
    public void clearRankingList() {
        this.list.clear();
        if (this.entityRanking != null) {
            this.entityRanking.getShowTextMap().clear();
        }
    }

    /**
     * 关闭排行榜
     */
    public void close() {
        this.closed = true;
        this.rankingAPI.getUpdateTask().removeRanking(this);
        this.rankingAPI.getAsyncUpdateTask().removeRanking(this);
        this.clearRankingList();
        if (this.entityRanking != null) {
            this.entityRanking.close();
        }
    }

}