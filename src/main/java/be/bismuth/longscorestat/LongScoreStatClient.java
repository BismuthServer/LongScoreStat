package be.bismuth.longscorestat;

import net.minecraft.client.Minecraft;
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import be.bismuth.longscorestat.network.ClientNetworking;

public class LongScoreStatClient implements ClientModInitializer {
	public static final ClientNetworking networking = new ClientNetworking();
	public static Minecraft minecraft;

	@Override
	public void initClient() {
		MinecraftClientEvents.START.register(LongScoreStatClient::start);
		MinecraftClientEvents.STOP.register(LongScoreStatClient::stop);
	}

	public static void start(Minecraft minecraft) {
		LongScoreStatClient.minecraft = minecraft;
	}

	public static void stop(Minecraft minecraft) {
		LongScoreStatClient.minecraft = null;
	}
}
