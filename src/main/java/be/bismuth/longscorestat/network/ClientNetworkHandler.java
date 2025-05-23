package be.bismuth.longscorestat.network;

import net.minecraft.client.gui.screen.menu.StatsListener;
import net.minecraft.stat.Stat;
import be.bismuth.longscorestat.LongScoreStatClient;
import be.bismuth.longscorestat.stats.IPlayerStats;

import java.util.Map;
public class ClientNetworkHandler {
    public static void handleStatistics(StatisticsPacket packet) {
        for(Map.Entry<Stat, Long> entry : packet.getStats().entrySet()) {
            Stat stat = entry.getKey();
            long i = entry.getValue();
            ((IPlayerStats) LongScoreStatClient.minecraft.player.getStats()).bismuthServer$setLongStat(LongScoreStatClient.minecraft.player, stat, i);
        }

        if (LongScoreStatClient.minecraft.screen instanceof StatsListener) {
            ((StatsListener)LongScoreStatClient.minecraft.screen).createGui();
        }
    }
}
