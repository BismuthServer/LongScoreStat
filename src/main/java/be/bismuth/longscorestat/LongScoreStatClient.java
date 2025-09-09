package be.bismuth.longscorestat;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import be.bismuth.longscorestat.network.ClientNetworking;

public class LongScoreStatClient implements ClientModInitializer {
	public static final ClientNetworking networking = new ClientNetworking();
	public static Minecraft minecraft;

	@Override
	public void onInitializeClient() {
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
