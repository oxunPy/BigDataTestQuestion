package org.simple_coding.common;

import java.util.Date;

public class ExecutingTimeInSeconds {

    public static int callMethod(MethodCall function) {
        long startTime = getCurrentTime();
        function.call();
        long endTime = getCurrentTime();

        long executedTime = endTime - startTime;
        return millisToSecond(executedTime);
    }

    private static long getCurrentTime() {
        return new Date().getTime();
    }

    private static int millisToSecond(long millis) {
        return (int) (millis / 1000);
    }
}
