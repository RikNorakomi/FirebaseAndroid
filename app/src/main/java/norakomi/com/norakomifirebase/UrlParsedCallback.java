package norakomi.com.norakomifirebase;

import norakomi.com.norakomifirebase.models.WebPage;

/**
 * Created by Rik van Velzen, Norakomi.com, on 12-5-2016.
 * <p/>
 * Check out:
 * https://github.com/RikNorakomi?tab=repositories
 * http://norakomi.blogspot.nl/
 * www.norakomi.com
 */
public interface UrlParsedCallback {
    void onWebPageParsed(WebPage parsedWebPage);
    void onWebPageParsingError(String error);
}
