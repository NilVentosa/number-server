package xyz.ventosa.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String DEFAULT_PORT = "4000";

    public static final String DEFAULT_MAX_CONCURRENT_CLIENTS = "5";

    public static final String DEFAULT_REPORT_FREQUENCY = "10000";

    public static final int FLUSHING_FREQUENCY = 1000;

    public static final String DEFAULT_FILE_NAME = "numbers.log";

}
