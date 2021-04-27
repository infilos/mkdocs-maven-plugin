package com.infilos.mkdocs;

import org.apache.maven.plugin.logging.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MkdocsLogger {
    private static final Pattern PATTERN = Pattern.compile("^(DEBUG|INFO|WARNING|ERROR)\\s+-\\s+(.*)$");

    private final Log log;
    private String level;

    public MkdocsLogger(Log log) {
        this.log = log;
    }

    public void log(String line) {
        String message = line;

        if (line.startsWith("Traceback")) {
            level = "TRACEBACK";
        } else {
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.matches()) {
                level = matcher.group(1);
                message = matcher.group(2);
            }
        }

        switch (level) {
            case "DEBUG":
                log.debug(message);
                break;

            case "INFO":
                log.info(message);
                break;

            case "WARNING":
                log.warn(message);
                break;

            default:
                log.error(message);
        }
    }
}
