package be.bismuth.longscorestat.stats.mixins;

import be.bismuth.longscorestat.stats.IPlayerStats;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
	@Redirect(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
	int clamp(int value, int min, int max, @Local ServerStatHandler serverStatHandler) {
		long stat = ((IPlayerStats) serverStatHandler).bismuthServer$getLongStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));

		return (int) MathHelper.clamp(stat, 1, Integer.MAX_VALUE);
	}
}
