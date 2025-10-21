package be.bismuth.longscorestat.stats.mixins;

import be.bismuth.longscorestat.stats.IPlayerStats;
import net.minecraft.predicate.PlayerPredicate;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerPredicate.class)
public class PlayerPredicateMixin {
	@Redirect(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/Stat;)I"))
	int test(StatHandler instance, Stat<?> stat) {
		long value = ((IPlayerStats) instance).bismuthServer$getLongStat(stat);
		// Clamp needed for when long value has zeros in all lower 32 bits, but non zero value in upper 32 bits
		return (int) Math.min(value, 2147483647L);
	}
}
