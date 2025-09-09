package be.bismuth.longscorestat.stats.mixins;

import be.bismuth.longscorestat.network.StatisticsPacket;
import be.bismuth.longscorestat.stats.IPlayerStats;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

import static net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.canSend;

@Mixin(ServerStatHandler.class)
public abstract class ServerStatHandlerMixin extends StatHandlerMixin {
    @Shadow @Final
    private Set<Stat<?>> pendingStats;

    @Shadow
    protected abstract Set<Stat<?>> takePendingStats();

	@Shadow
	private static <T> Identifier getStatId(Stat<T> stat) {
		return null;
	}

	@Unique
	@Override
    public void bismuthServer$setLongStat(PlayerEntity player, Stat<?> stat, long value) {
        super.bismuthServer$setLongStat(player, stat, value);
        this.pendingStats.add(stat);
    }

	/**
	 * @author thdaele
	 * @reason Overwriten to set long stat instead of int stat
	 */
	@Overwrite
	public void setStat(PlayerEntity player, Stat<?> stat, int value) {
		super.bismuthServer$setLongStat(player, stat, value);
		this.pendingStats.add(stat);
	}

	/**
	 * @author thdaele
	 * @reason Overwriten to use long stat instead of int stat
	 */
	@Overwrite
	public void updateStatSet() {
		this.pendingStats.addAll(this.counters.keySet());
	}

    @Inject(method = "sendStats", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntOpenHashMap;<init>()V", remap = false), cancellable = true)
    public void sendStats(ServerPlayerEntity player, CallbackInfo ci) {
        if (canSend(player, StatisticsPacket.channel)) {
			Object2LongOpenHashMap<Stat<?>> map = new Object2LongOpenHashMap<>();

			for(Stat<?> stat : this.takePendingStats()) {
				map.put(stat, this.bismuthServer$getLongStat(stat));
			}

			StatisticsPacket packet = new StatisticsPacket(map);
			PacketByteBuf buf = PacketByteBufs.create();
			packet.write(buf);

			ServerPlayNetworking.send(player, StatisticsPacket.channel, buf);

            ci.cancel();
        }
        // If player doesn't have the 64bit support we resort to the normal sending of the packets
    }

    @Redirect(method = "sendStats", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/ServerStatHandler;getStat(Lnet/minecraft/stat/Stat;)I"))
    public int sendStatsVanilla(ServerStatHandler instance, Stat<?> stat) {
        // Cast all the long stats to int
        return (int) ((IPlayerStats) instance).bismuthServer$getLongStat(stat);
    }

	@Inject(method = "asString", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;object2IntEntrySet()Lit/unimi/dsi/fastutil/objects/ObjectSet;"), remap = false)
	public void asStringInject(CallbackInfoReturnable<String> cir, @Local Map<StatType<?>, JsonObject> map) {
		// Just to make sure the local ref is created after the map
		for (Object2LongMap.Entry<Stat<?>> entry : this.counters.object2LongEntrySet()) {
			Stat<?> stat = entry.getKey();
			assert getStatId(stat) != null;
			map.computeIfAbsent(stat.getType(), statType -> new JsonObject()).addProperty(getStatId(stat).toString(), entry.getLongValue());
		}
	}

	@Redirect(method = "jsonToCompound", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putInt(Ljava/lang/String;I)V"))
	private static void jsonToCompound(NbtCompound instance, String key, int value, @Local JsonPrimitive jsonPrimitive) {
		instance.putLong(key, jsonPrimitive.getAsLong());
	}

	@Redirect(method = "method_17990", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;put(Ljava/lang/Object;I)I"), remap = false)
	int parse(Object2IntMap<Stat<?>> instance, Object o, int i, @Local(argsOnly = true) Stat<?> stat, @Local(argsOnly = true) String string2, @Local(argsOnly = true) NbtCompound nbtCompound2x) {
		long value = nbtCompound2x.getLong(string2);

		this.counters.put(stat, value);
		return i;
	}
}
