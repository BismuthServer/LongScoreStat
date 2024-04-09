package si.bismuth.longscorestat;

import net.ornithemc.osl.entrypoints.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LongScoreStat implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("LongScoreStat");

	@Override
	public void init() {
		LOGGER.info("Init 64bit scoreboards and stats mod!");
	}
}
