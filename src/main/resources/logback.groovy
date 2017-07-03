import ch.qos.logback.classic.encoder.PatternLayoutEncoder

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    }
}

appender("FILE", FileAppender) {
    file = "dusty_logs/" + timestamp("yyyy-MM-dd'T'HH.mm.ssZ") + ".log"
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %level %logger - %msg%n"
    }
}

root(DEBUG, ["STDOUT", "FILE"])
