package be.bismuth.longscorestat.network;

import net.minecraft.network.PacketByteBuf;

import java.io.IOException;

public interface LongscorestatPacket {
	void read(PacketByteBuf buffer) throws IOException;
	void write(PacketByteBuf buffer) throws IOException;
}
