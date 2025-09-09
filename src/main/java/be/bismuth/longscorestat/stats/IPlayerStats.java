package be.bismuth.longscorestat.stats;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;

public interface IPlayerStats {
    void bismuthServer$longIncrement(PlayerEntity player, Stat<?> stat, long amount);

    void bismuthServer$setLongStat(PlayerEntity player, Stat<?> stat, long value);

    long bismuthServer$getLongStat(Stat<?> stat);

	<T> long bismuthServer$getStat(StatType<T> type, T stat);
}
