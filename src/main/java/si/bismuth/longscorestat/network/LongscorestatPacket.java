package si.bismuth.longscorestat.network;

import net.ornithemc.osl.networking.api.CustomPayload;

public interface LongscorestatPacket extends CustomPayload {
	String getChannel();
}
