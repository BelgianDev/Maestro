package be.raftdev.maestro.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {
    private static final StackWalker WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    /**
     * Caller sensitive! Do not wrap!
     *
     * @return logger for the calling class.
     */
    public static Logger getLogger() {
        return LoggerFactory.getLogger(WALKER.getCallerClass());
    }
}
