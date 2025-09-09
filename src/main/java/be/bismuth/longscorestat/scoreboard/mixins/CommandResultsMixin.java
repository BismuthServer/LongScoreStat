package be.bismuth.longscorestat.scoreboard.mixins;

import net.minecraft.scoreboard.ScoreboardScore;
import net.minecraft.server.command.source.CommandResults;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import be.bismuth.longscorestat.scoreboard.IScoreboardScore;

@Mixin(CommandResults.class)
public class CommandResultsMixin {
    @Redirect(method = "add", at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/ScoreboardScore;set(I)V"))
    private void add(ScoreboardScore instance, int score) {
        ((IScoreboardScore) instance).bismuthServer$setLongScore((long) score);
    }
}
