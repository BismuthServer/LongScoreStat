package be.bismuth.longscorestat.network;

import be.bismuth.longscorestat.LongScoreStatClient;
import be.bismuth.longscorestat.stats.IPlayerStats;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.StatsListener;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ClientNetworking {
	public ClientNetworking() {
		this.handleStatistics();
	}

	private void handleStatistics() {
		Identifier channel = StatisticsPacket.channel;

		ClientPlayNetworking.registerReceiver(channel, (client, handler, buf, responseSender) -> {
			StatisticsPacket packet = new StatisticsPacket(buf);

			client.execute(() -> {
				for(Map.Entry<Stat<?>, Long> entry : packet.getStats().entrySet()) {
					Stat<?> stat = entry.getKey();
					long i = entry.getValue();
					assert LongScoreStatClient.minecraft.player != null;
					((IPlayerStats) LongScoreStatClient.minecraft.player.getStatHandler()).bismuthServer$setLongStat(LongScoreStatClient.minecraft.player, stat, i);
				}

				if (LongScoreStatClient.minecraft.currentScreen instanceof StatsListener) {
					((StatsListener)LongScoreStatClient.minecraft.currentScreen).onStatsReady();
				}
			});
		});
	}
}
