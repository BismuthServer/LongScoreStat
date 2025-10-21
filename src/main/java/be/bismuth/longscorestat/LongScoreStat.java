package be.bismuth.longscorestat;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LongScoreStat implements ModInitializer {
	public static final String MOD_ID = "longscorestat";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Init 64bit scoreboards and stats mod!");
	}
}
