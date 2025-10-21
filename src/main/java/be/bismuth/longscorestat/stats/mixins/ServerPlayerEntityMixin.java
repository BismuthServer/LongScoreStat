package be.bismuth.longscorestat.stats.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import be.bismuth.longscorestat.stats.IPlayerStats;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Redirect(method = "resetStat", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/ServerStatHandler;setStat(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/stat/Stat;I)V"))
    public void clearStat(ServerStatHandler instance, PlayerEntity player, Stat<?> stat, int value) {
        ((IPlayerStats) instance).bismuthServer$setLongStat(player, stat, value);
    }

    @Redirect(method = "increaseStat", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/ServerStatHandler;increaseStat(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/stat/Stat;I)V"))
    public void incrementStat(ServerStatHandler instance, PlayerEntity player, Stat<?> stat, int amount) {
        ((IPlayerStats) instance).bismuthServer$longIncrement(player, stat, amount);
    }
}
