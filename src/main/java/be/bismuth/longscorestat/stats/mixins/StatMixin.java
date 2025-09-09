package be.bismuth.longscorestat.stats.mixins;

import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import be.bismuth.longscorestat.stats.IStat;
import be.bismuth.longscorestat.stats.LongStatFormatter;

// TODO fix
@Mixin(Stat.class)
public class StatMixin implements IStat {
    @Shadow @Final
    private StatFormatter formatter;

	@Override
    public String bismuthServer$longFormat(long value) {
		LongStatFormatter longStatFormatter;
		if (this.formatter == StatFormatter.DEFAULT) {
			longStatFormatter = LongStatFormatter.DEFAULT;
		} else if (this.formatter == StatFormatter.TIME) {
			longStatFormatter = LongStatFormatter.TIME;
		} else if (this.formatter == StatFormatter.DISTANCE) {
			longStatFormatter = LongStatFormatter.DISTANCE;
		} else if (this.formatter == StatFormatter.DIVIDE_BY_TEN) {
			longStatFormatter = LongStatFormatter.DIVIDE_BY_TEN;
		} else {
			throw new IllegalStateException("Unknown formatter: " + this.formatter);
		}
		return longStatFormatter.bismuthServer$format(value);
    }
}
