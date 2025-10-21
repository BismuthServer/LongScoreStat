package be.bismuth.longscorestat.stats.mixins.StatsScreen;

import net.minecraft.client.gui.screen.StatsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StatsScreen.ItemStatsListWidget.class)
public interface ItemStatsListWidgetAccessor {
	@Accessor("field_18752")
	StatsScreen getField_18752();
}
