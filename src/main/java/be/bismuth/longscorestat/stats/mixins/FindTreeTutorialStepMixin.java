package be.bismuth.longscorestat.stats.mixins;

import net.minecraft.client.tutorial.FindTreeTutorialStep;
import net.minecraft.stat.PlayerStats;
import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import be.bismuth.longscorestat.stats.IPlayerStats;

@Mixin(FindTreeTutorialStep.class)
public class FindTreeTutorialStepMixin {
    @Redirect(method = "wasPreviouslyCompleted", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/PlayerStats;get(Lnet/minecraft/stat/Stat;)I"))
    private static int wasPreviouslyCompleted(PlayerStats instance, Stat stat) {
        return ((IPlayerStats) instance).bismuthServer$getLongStat(stat) > 0L ? 1 : 0;
    }
}
