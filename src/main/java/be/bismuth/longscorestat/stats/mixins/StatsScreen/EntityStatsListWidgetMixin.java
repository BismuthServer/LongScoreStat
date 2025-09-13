package be.bismuth.longscorestat.stats.mixins.StatsScreen;

import be.bismuth.longscorestat.stats.IPlayerStats;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalLongRef;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.entity.EntityType;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatsScreen.EntityStatsListWidget.class)
public class EntityStatsListWidgetMixin {
    @Shadow @Final
    StatsScreen screen;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/Stat;)I"))
    public int init(StatHandler instance, Stat<?> stat) {
        return ((IPlayerStats) screen.statHandler).bismuthServer$getLongStat(stat) > 0L ? 1 : 0;
    }
}

@Mixin(StatsScreen.EntityStatsListWidget.Entry.class)
class EntityStatsListWidgetEntryMixin {
	@Shadow @Final
	private Text entityTypeName;

	@Mutable
	@Shadow @Final
	private Text killedText;

	@Mutable
	@Shadow @Final
	private Text killedByText;

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/Stat;)I", ordinal = 0))
	public int init_i(StatHandler instance, Stat<?> stat, @Share("i") LocalLongRef iRef) {
		long i = ((IPlayerStats) instance).bismuthServer$getLongStat(stat);
		iRef.set(i);

		// This int will be used for checking if it is zero so returning an int is fine in this case
		// Clamp needed for when long value has zeros in all lower 32 bits, but non zero value in upper 32 bits
		return (int) Math.min(i, 2147483647L);
	}

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/Stat;)I", ordinal = 1))
	public int init_j(StatHandler instance, Stat<?> stat, @Share("j") LocalLongRef jRef) {
		long j = ((IPlayerStats) instance).bismuthServer$getLongStat(stat);
		jRef.set(j);

		// This int will be used for checking if it is zero so returning an int is fine in this case
		// Clamp needed for when long value has zeros in all lower 32 bits, but non zero value in upper 32 bits
		return (int) Math.min(j, 2147483647L);
	}

	@Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/StatsScreen$EntityStatsListWidget$Entry;killedAny:Z", ordinal = 1, opcode = Opcodes.PUTFIELD))
	void killedText(StatsScreen.EntityStatsListWidget entityStatsListWidget, EntityType<?> entityType, CallbackInfo ci, @Share("i") LocalLongRef iRef) {
		this.killedText = new TranslatableText("stat_type.minecraft.killed", iRef.get(), this.entityTypeName);
	}

	@Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/StatsScreen$EntityStatsListWidget$Entry;killedByAny:Z", ordinal = 1, opcode = Opcodes.PUTFIELD))
	void killedByText(StatsScreen.EntityStatsListWidget entityStatsListWidget, EntityType<?> entityType, CallbackInfo ci, @Share("j") LocalLongRef jRef) {
		this.killedByText = new TranslatableText("stat_type.minecraft.killed_by", this.entityTypeName, jRef.get());
	}
}
