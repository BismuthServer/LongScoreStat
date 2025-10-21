package be.bismuth.longscorestat.stats.mixins.StatsScreen;

import net.minecraft.client.gui.screen.StatsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(StatsScreen.class)
public class StatsScreenMixin {
	@ModifyConstant(method = "getColumnX", constant = @Constant(intValue = 115))
	public int getColumnX1(int original) {
		return 135;
	}

	@ModifyConstant(method = "getColumnX", constant = @Constant(intValue = 40))
	public int getColumnX2(int original) {
		return 80;
	}
}
