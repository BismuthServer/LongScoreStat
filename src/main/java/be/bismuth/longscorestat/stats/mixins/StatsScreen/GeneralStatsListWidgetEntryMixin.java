package be.bismuth.longscorestat.stats.mixins.StatsScreen;

import be.bismuth.longscorestat.stats.IPlayerStats;
import be.bismuth.longscorestat.stats.IStat;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalLongRef;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StatsScreen.GeneralStatsListWidget.Entry.class)
class GeneralStatsListWidgetEntryMixin {
	@Redirect(method = "getFormatted", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/Stat;)I"))
	private int getStat(StatHandler instance, Stat<?> stat, @Share("long") LocalLongRef longRef) {
		long value = ((IPlayerStats) instance).bismuthServer$getLongStat(stat);
		longRef.set(value);
		// Clamp needed for when long value has zeros in all lower 32 bits, but non zero value in upper 32 bits
		return (int) Math.min(value, 2147483647L);
	}

    @Redirect(method = "getFormatted", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/Stat;format(I)Ljava/lang/String;"))
	private String useLongFormat(Stat<?> stat, int value, @Share("long") LocalLongRef longRef) {
		return ((IStat) stat).bismuthServer$longFormat(longRef.get());
	}

	@ModifyConstant(method = "render", constant = @Constant(intValue = 213))
	private int modifyXPosition(int original) {
		return 283;
	}
}

@Mixin(StatsScreen.GeneralStatsListWidget.class)
class GeneralStatsListWidgetMixin extends AlwaysSelectedEntryListWidget<StatsScreen.GeneralStatsListWidget.Entry> {
	public GeneralStatsListWidgetMixin(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
		super(minecraftClient, i, j, k, l, m);
	}

	@Override
	public int getRowWidth() {
		return 290;
	}

	@Override
	public int getScrollbarPositionX() {
		return this.width / 2 + 159;
	}
}
