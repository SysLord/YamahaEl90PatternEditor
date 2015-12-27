package util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {

	private static final Logger logger = LogManager.getLogger("Log");

	public static void log(String msg) {
		logger.log(Level.INFO, msg);
	}

	public static void logDebug(String msg) {
		logger.log(Level.DEBUG, msg);
	}

	public static void logWarn(String msg) {
		logger.log(Level.WARN, msg);
	}

	public static void log(String format, Object... arg) {
		logger.log(Level.INFO, String.format(format, arg));
	}

	public static void logDebug(String format, Object... arg) {
		logger.log(Level.DEBUG, String.format(format, arg));
	}

	public static void logWarn(String format, Object... arg) {
		logger.log(Level.WARN, String.format(format, arg));
	}

	public static void logDump(Object dump, String description) {
		logger.log(Level.INFO, formatDump(dump, description));
	}

	public static void logDebugDump(Object dump, String description) {
		logger.log(Level.DEBUG, formatDump(dump, description));
	}

	public static void logTraceDump(Object dump, String description) {
		logger.log(Level.TRACE, formatDump(dump, description));
	}

	private static String formatDump(Object dump, String description) {
		return String.format("%s:\n%s", description, dump == null ? "null" : dump.toString());
	}

}
