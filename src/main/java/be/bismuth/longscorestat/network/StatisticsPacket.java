package be.bismuth.longscorestat.network;

import be.bismuth.longscorestat.LongScoreStat;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.util.Map;

public class StatisticsPacket implements LongscorestatPacket {
	static public Identifier channel = new Identifier(LongScoreStat.MOD_ID, "stats");
    private Object2LongOpenHashMap<Stat<?>> stats;

    public StatisticsPacket(Object2LongOpenHashMap<Stat<?>> stats) {
        this.stats = stats;
    }

	public StatisticsPacket(PacketByteBuf buf) {
		try {
			this.read(buf);
		} catch (IOException e) {
			LongScoreStat.LOGGER.error("Failed to read StatisticsPacket", e);
		}
	}

	private static <T> Stat<T> getStat(StatType<T> statType, int id) {
		return statType.getOrCreateStat(statType.getRegistry().get(id));
	}

	private <T> int getStatNetworkId(Stat<T> stat) {
		return stat.getType().getRegistry().getRawId(stat.getValue());
	}

    @Override
    public void read(PacketByteBuf buf) throws IOException {
		this.stats = buf.readMap(Object2LongOpenHashMap::new, bufx -> {
			int i = bufx.readVarInt();
			int j = bufx.readVarInt();
			return getStat(Registry.STAT_TYPE.get(i), j);
		}, PacketByteBuf::readVarLong);
    }

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeMap(this.stats, (bufx, stat) -> {
			bufx.writeVarInt(Registry.STAT_TYPE.getRawId(stat.getType()));
			bufx.writeVarInt(this.getStatNetworkId(stat));
		}, PacketByteBuf::writeVarLong);
	}

    public Map<Stat<?>, Long> getStats() {
        return this.stats;
    }
}
