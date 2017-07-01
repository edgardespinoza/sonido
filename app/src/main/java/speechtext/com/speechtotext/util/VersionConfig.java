package speechtext.com.speechtotext.util;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class VersionConfig {

    private static final String TAG = VersionConfig.class.getName();
    private static final Pattern DOT_PATTERN = Pattern.compile(".", Pattern.LITERAL);

    private static final Map<String, VersionConfig> configuration = new HashMap<>();

    static {
        configuration.put("5.9.26", new VersionConfig(true, true));
        configuration.put("4.7.13", new VersionConfig(false, false));
    }

    private boolean destroyRecognizer = true;
    private boolean autoStopRecognizer = false;

    private VersionConfig() {
    }

    private VersionConfig(final boolean destroyRecognizer, final boolean autoStopRecognizer) {
        this.destroyRecognizer = destroyRecognizer;
        this.autoStopRecognizer = autoStopRecognizer;
    }

    public static VersionConfig init(final Context context) {
        return getConfigByVersion(context);
    }

    private static VersionConfig getConfigByVersion(final Context context) {
        final long number = numberFromBuildVersion(RecognizerChecker.getGoogleRecognizerVersion(context));

        final VersionConfig config = new VersionConfig();
        long prevVersionNumber = 0;

        for (final Map.Entry<String, VersionConfig> configEntry : configuration.entrySet()) {
            final String versionName = configEntry.getKey();

            if (!TextUtils.isEmpty(versionName)) {
                final long versionNumber = numberFromBuildVersion(versionName);
                if (number >= versionNumber && prevVersionNumber < versionNumber) {
                    config.destroyRecognizer = configEntry.getValue().destroyRecognizer;
                    config.autoStopRecognizer = configEntry.getValue().autoStopRecognizer;
                    prevVersionNumber = versionNumber;
                }
            }
        }

        return config;
    }

    public boolean isDestroyRecognizer() {
        return destroyRecognizer;
    }

    public boolean isAutoStopRecognizer() {
        return autoStopRecognizer;
    }

    private static long numberFromBuildVersion(final String buildVersion) {
        if (TextUtils.isEmpty(buildVersion))
            return 0;

        final String[] parts = DOT_PATTERN.split(buildVersion);

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Math.min(3, parts.length); i++) {
            builder.append(parts[i]);
        }
        try {
            return Long.parseLong(builder.toString());
        } catch (final NumberFormatException ignored) {
            return 0;
        }
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }
}