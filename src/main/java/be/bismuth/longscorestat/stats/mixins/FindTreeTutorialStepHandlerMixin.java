package be.bismuth.longscorestat.stats.mixins;

import net.minecraft.client.tutorial.FindTreeTutorialStepHandler;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import be.bismuth.longscorestat.stats.IPlayerStats;

@Mixin(FindTreeTutorialStepHandler.class)
public class FindTreeTutorialStepHandlerMixin {
    @Redirect(method = "hasBrokenTreeBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/StatHandler;getStat(Lnet/minecraft/stat/Stat;)I"))
    private static int wasPreviouslyCompleted(StatHandler instance, Stat<?> stat) {
        return ((IPlayerStats) instance).bismuthServer$getLongStat(stat) > 0L ? 1 : 0;
    }
}
