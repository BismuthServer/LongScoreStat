package be.bismuth.longscorestat;

import net.fabricmc.api.ClientModInitializer;
import be.bismuth.longscorestat.network.ClientNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class LongScoreStatClient implements ClientModInitializer {
	public static ClientNetworking networking;
	public static MinecraftClient minecraft;

	@Override
	public void onInitializeClient() {
		ClientLifecycleEvents.CLIENT_STARTED.register(LongScoreStatClient::start);
		ClientLifecycleEvents.CLIENT_STOPPING.register(LongScoreStatClient::stop);
		ClientPlayConnectionEvents.INIT.register(LongScoreStatClient::connectionStart);
		ClientPlayConnectionEvents.DISCONNECT.register(LongScoreStatClient::connectionStop);
	}


	public static void start(MinecraftClient minecraft) {
		LongScoreStatClient.minecraft = minecraft;
	}

	public static void connectionStart(ClientPlayNetworkHandler clientPlayNetworkHandler, MinecraftClient minecraftClient) {
		LongScoreStatClient.networking = new ClientNetworking();
	}

	public static void stop(MinecraftClient minecraft) {
		LongScoreStatClient.minecraft = null;
	}

	public static void connectionStop(ClientPlayNetworkHandler clientPlayNetworkHandler, MinecraftClient minecraftClient) {
		LongScoreStatClient.networking = null;
	}
}
