package be.bismuth.longscorestat.stats.mixins.StatsScreen;

import be.bismuth.longscorestat.stats.IPlayerStats;
import be.bismuth.longscorestat.stats.IStat;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalLongRef;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StatsScreen.ItemStatsListWidget.class)
class ItemStatsListWidgetMixin {
    @Shadow @Final
    StatsScreen field_18752;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/Stat;)I"))
    public int init(StatHandler instance, Stat<?> stat) {
        return ((IPlayerStats) field_18752.statHandler).bismuthServer$getLongStat(stat) > 0L ? 1 : 0;
    }

	@ModifyConstant(method = "getRowWidth", constant = @Constant(intValue = 375))
	public int getRowWidth(int original) {
		return 600;
	}

	@ModifyConstant(method = "getScrollbarPositionX", constant = @Constant(intValue = 140))
	public int getScrollbarPositionX(int original) {
		return 250;
	}
}

@Mixin(StatsScreen.ItemStatsListWidget.Entry.class)
class ItemStatsListWidgetEntryMixin {
	@Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/stat/Stat;IIZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/Stat;)I"))
	public int render(StatHandler instance, Stat<?> stat, @Share("long") LocalLongRef longRef) {
		long value = ((IPlayerStats) instance).bismuthServer$getLongStat(stat);
		longRef.set(value);
		return (int) value;
	}

	@Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/stat/Stat;IIZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/Stat;format(I)Ljava/lang/String;"))
	public String format(Stat<?> stat, int value, @Share("long") LocalLongRef longRef) {
		return ((IStat) stat).bismuthServer$longFormat(longRef.get());
	}
}

@Mixin(StatsScreen.ItemStatsListWidget.ItemComparator.class)
class ItemStatsListWidgetItemComparatorMixin {
	@Shadow @Final
	StatsScreen.ItemStatsListWidget field_2662;

	/**
	 * @author thdaele
	 * @reason well too much changes, doing it any other way is just pain
	 */
	@Overwrite
	public int compare(StatsScreen.ItemStatsListWidget.Entry entry, StatsScreen.ItemStatsListWidget.Entry entry2) {
		Item item = entry.getItem();
		Item item2 = entry2.getItem();
		long i;
		long j;
		StatsScreen screen = ((ItemStatsListWidgetAccessor) field_2662).getField_18752();
		IPlayerStats stats = (IPlayerStats) screen.statHandler;
		if (field_2662.selectedStatType == null) {
			i = 0;
			j = 0;
		} else if (field_2662.blockStatTypes.contains(field_2662.selectedStatType)) {
			StatType<Block> statType = (StatType<Block>) field_2662.selectedStatType;

			i = item instanceof BlockItem ? stats.bismuthServer$getStat(statType, ((BlockItem)item).getBlock()) : -1;
			j = item2 instanceof BlockItem ? stats.bismuthServer$getStat(statType, ((BlockItem)item2).getBlock()) : -1;
		} else {
			StatType<Item> statType = (StatType<Item>) field_2662.selectedStatType;
			i = stats.bismuthServer$getStat(statType, item);
			j = stats.bismuthServer$getStat(statType, item2);
		}

		return i == j
			? field_2662.listOrder * Integer.compare(Item.getRawId(item), Item.getRawId(item2))
			: field_2662.listOrder * Long.compare(i, j);
	}
}
