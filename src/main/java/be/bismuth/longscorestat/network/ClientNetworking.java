package be.bismuth.longscorestat.network;

import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;
import be.bismuth.longscorestat.LongScoreStat;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ClientNetworking {
	private final Set<String> allChannels = new HashSet<>();

	public ClientNetworking() {
		this.registerListener(StatisticsPacket::new, ClientNetworkHandler::handleStatistics);
	}

	private <T extends LongscorestatPacket> void registerListener(Supplier<T> initializer, Consumer<T> packetHandler) {
		LongscorestatPacket p = initializer.get();
		String channel = p.getChannel();

		if (this.allChannels.contains(channel)) {
			LongScoreStat.LOGGER.error("Attempted to register packet '{}' on channel '{}' but it already exists!", p.getClass().getSimpleName(), channel);
		} else {
			this.allChannels.add(channel);

			ClientPlayNetworking.registerListener(channel, initializer, (minecraft, handler, packet) -> {
				packetHandler.accept(packet);
				return true;
			});
		}
	}
}
