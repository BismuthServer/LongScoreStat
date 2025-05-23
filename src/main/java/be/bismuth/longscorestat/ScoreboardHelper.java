package be.bismuth.longscorestat;

public class ScoreboardHelper {
	public static final String upperScoreboardScorePrefix = "$";

	public static String getUpperScoreboardScoreName(String name) {
		return upperScoreboardScorePrefix + name;
	}
}
