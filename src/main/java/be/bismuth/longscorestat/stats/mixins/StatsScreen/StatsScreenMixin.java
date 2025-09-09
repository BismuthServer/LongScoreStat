package be.bismuth.longscorestat.stats.mixins.StatsScreen;

import net.minecraft.client.gui.screen.StatsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(StatsScreen.class)
public class StatsScreenMixin {
	@ModifyConstant(method = "getColumnX", constant = @Constant(intValue = 40))
	public int getColumnX(int original) {
		return 80;
	}
}
