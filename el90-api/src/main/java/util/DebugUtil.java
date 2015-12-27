package util;

public class DebugUtil {

	public static final boolean DEBUGMODE = true;

	public static void onDebug(Runnable r) {
		if (DEBUGMODE) {
			r.run();
		}
	}

}
