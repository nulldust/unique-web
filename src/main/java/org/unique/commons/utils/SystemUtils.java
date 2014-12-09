package org.unique.commons.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SystemUtils {

    public static final String JAVA_HOME = System.getProperty("java.home");
    public static final String JAVA_IO_TMPDIR = System.getProperty("java.io.tmpdir");
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String FILE_ENCODING = System.getProperty("file.encoding");
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    public static final boolean IS_OS_WINDOWS = (File.separatorChar == '\\');
    public static final boolean IS_OS_UNIX = (File.separatorChar == '/');

    public static void die() {
        die(null, null);
    }

    public static void die(String message, Throwable cause) {
        if (message == null) {
            message = "die";
        }
        Throwable ex = new Exception(message, cause);

        Logger log = LoggerFactory.getLogger(SystemUtils.class);
        log.error("***************************************************");
        log.error("!!! SYSTEM DEAD !!!");

        log.error("------Exception------");
        log.error(message, ex);

        log.error("------System.getProperties------");
        log.error(System.getProperties().toString());

        log.error("------System.getenv------");
        log.error(System.getenv().toString());

        log.error("***************************************************");

        System.exit(1);
    }
}
