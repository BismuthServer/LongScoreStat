package be.bismuth.longscorestat.stats.mixins;

import be.bismuth.longscorestat.stats.IPlayerStats;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(StatHandler.class)
public class StatHandlerMixin implements IPlayerStats {
	@Unique
	protected final Object2LongMap<Stat<?>> counters = Object2LongMaps.synchronize(new Object2LongOpenHashMap<>());

    public void bismuthServer$longIncrement(PlayerEntity player, Stat<?> stat, long amount) {
        this.bismuthServer$setLongStat(player, stat, this.bismuthServer$getLongStat(stat) + amount);
    }

    public void bismuthServer$setLongStat(PlayerEntity player, Stat<?> stat, long value) {
		this.counters.put(stat, value);
    }

    public long bismuthServer$getLongStat(Stat<?> stat) {
        return this.counters.getOrDefault(stat, 0);
    }

	public <T> long bismuthServer$getStat(StatType<T> type, T stat) {
		return type.hasStat(stat) ? this.bismuthServer$getLongStat(type.getOrCreateStat(stat)) : 0;
	}
}
