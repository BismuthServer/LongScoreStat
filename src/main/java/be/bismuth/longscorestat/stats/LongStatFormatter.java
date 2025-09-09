package be.bismuth.longscorestat.stats;

import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public interface LongStatFormatter {
	DecimalFormat DECIMAL_FORMAT = Util.make(
		new DecimalFormat("########0.00"), decimalFormat -> decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT))
	);
	LongStatFormatter DEFAULT = NumberFormat.getIntegerInstance(Locale.US)::format;
	LongStatFormatter DIVIDE_BY_TEN = i -> DECIMAL_FORMAT.format(i * 0.1);
	LongStatFormatter DISTANCE = i -> {
		double d = i / 100.0;
		double e = d / 1000.0;
		if (e > 0.5) {
			return DECIMAL_FORMAT.format(e) + " km";
		} else {
			return d > 0.5 ? DECIMAL_FORMAT.format(d) + " m" : i + " cm";
		}
	};
	LongStatFormatter TIME = i -> {
		double d = i / 20.0;
		double e = d / 60.0;
		double f = e / 60.0;
		double g = f / 24.0;
		double h = g / 365.0;
		if (h > 0.5) {
			return DECIMAL_FORMAT.format(h) + " y";
		} else if (g > 0.5) {
			return DECIMAL_FORMAT.format(g) + " d";
		} else if (f > 0.5) {
			return DECIMAL_FORMAT.format(f) + " h";
		} else {
			return e > 0.5 ? DECIMAL_FORMAT.format(e) + " m" : d + " s";
		}
	};

    String bismuthServer$format(long value);
}
