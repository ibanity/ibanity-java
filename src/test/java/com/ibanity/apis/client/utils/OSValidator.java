package com.ibanity.apis.client.utils;

public class OSValidator {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static OperatingSystem getSystemOperatingSystem() {

        if (isWindows()) {
            return OperatingSystem.windows;
        } else if (isMac()) {
            return OperatingSystem.mac;
        } else if (isUnix()) {
            return OperatingSystem.unix;
        } else if (isSolaris()) {
            return OperatingSystem.solaris;
        } else {
            return OperatingSystem.unknown;
        }
    }

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
    }

    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }
}
