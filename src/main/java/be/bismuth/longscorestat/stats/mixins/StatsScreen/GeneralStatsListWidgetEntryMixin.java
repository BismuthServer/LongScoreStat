package be.bismuth.longscorestat.stats.mixins.StatsScreen;

import be.bismuth.longscorestat.stats.IPlayerStats;
import be.bismuth.longscorestat.stats.IStat;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalLongRef;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net/minecraft/client/gui/screen/StatsScreen$GeneralStatsListWidget$Entry")
public class GeneralStatsListWidgetEntryMixin {
	@Redirect(method = "getFormatted", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/Stat;)I"))
	private int getStat(StatHandler instance, Stat<?> stat, @Share("long") LocalLongRef longRef) {
		long value = ((IPlayerStats) instance).bismuthServer$getLongStat(stat);
		longRef.set(value);
		return (int) value;
	}

    @Redirect(method = "getFormatted", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/Stat;format(I)Ljava/lang/String;"))
	private String useLongFormat(Stat<?> stat, int value, @Share("long") LocalLongRef longRef) {
		return ((IStat) stat).bismuthServer$longFormat(longRef.get());
	}
}
