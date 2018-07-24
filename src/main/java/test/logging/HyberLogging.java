package test.logging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class HyberLogging {

    @NotNull
    public static LogMessage msgOf() {
        return new LogMessage(Configuration.moduleName);
    }

    @NotNull
    public static LogMessage msgOf(String moduleName) {
        return new LogMessage(moduleName);
    }

    @NotNull
    public static LogMessage msgOf(@Nullable Object clientId) {
        return msgOf().clientId(clientId);
    }

    @NotNull
    public static LogMessage camelErrorMsgOf(@Nullable Object messageId, @Nullable Object clientId) {
        return msgOf()
                .msgId(messageId)
                .clientId(clientId)
                .exception("${exception.message}")
                .headers("${headers}")
                .add("stacktrace", "${exception.stacktrace}");
    }

    private HyberLogging() {
    }

    public static final class Configuration {

        private static String moduleName = "";

        public static void setModuleName(@NotNull String moduleName) {
            if (!Configuration.moduleName.isEmpty()) {
                throw new IllegalStateException("Module name is already installed");
            }
            Configuration.moduleName = Objects.requireNonNull(moduleName, "moduleName");
        }
    }
}
