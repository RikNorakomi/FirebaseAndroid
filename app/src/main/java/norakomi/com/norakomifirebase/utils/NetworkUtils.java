package norakomi.com.norakomifirebase.utils;

import java.util.Arrays;

/**
 * Created by Rik van Velzen, Norakomi.com, on 13-4-2016.
 * <p/>
 * Check out:
 * https://github.com/RikNorakomi?tab=repositories
 * http://norakomi.blogspot.nl/
 * www.norakomi.com
 */
public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    private static String[] validSchemes = {"http://", "https://"};

    public static boolean checkUrlhasHttpScheme(String url) {
        return url.startsWith("http://");
    }

    public static boolean checkUrlHasValidScheme(String url) {
        for (String scheme : validSchemes) {
            if (url.startsWith(scheme)) {
                return true;
            }
        }

        App.log(TAG, "Url doesn't have valid scheme. Valid schemes are: " + Arrays.toString(validSchemes));
        return false;
    }
}
