package norakomi.com.norakomifirebase.JSoup;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import norakomi.com.norakomifirebase.ArtWorkCrawler;
import norakomi.com.norakomifirebase.UrlParsedCallback;
import norakomi.com.norakomifirebase.models.SovietArtMePage;
import norakomi.com.norakomifirebase.utils.App;
import norakomi.com.norakomifirebase.utils.NetworkUtils;

/**
 * Created by Rik van Velzen, Norakomi.com, on 13-4-2016.
 * <p/>
 * Check out:
 * https://github.com/RikNorakomi?tab=repositories
 * http://norakomi.blogspot.nl/
 * www.norakomi.com
 *
 *
 * ParseUrl takes care of parsing a webpage by passing in a url to the AsyncTask.execute(url) method
 * a websites data is return as a string to the doInBackground() method which then
 * handles all the logic for what to parse into a result String.
 * When parsing is done the result string is passed to onPostExecute().
 *
 */
public class ParseUrl extends AsyncTask<String, Void, String> {

    private final String TAG = getClass().getSimpleName();
    private UrlParsedCallback callbackHandler;

    public void setCallbackHandler(UrlParsedCallback callbackHandler){
        this.callbackHandler = callbackHandler;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (callbackHandler == null){
            App.log(TAG, "Error. CallbackHandler needs to be set before executing url parse!");
            return "";
        }

        StringBuilder buffer = new StringBuilder();
        String url = strings[0];

        if (url == null) {
            App.log(TAG, "Error. Not a valid url");
            callbackHandler.onError("Not a valid url");
            return buffer.toString();
        }

        /*
        * Preferably you never want to receive malformed url's here.
        * But I've added a little code that tries to append an http:// protocol
        * to the url's string. Better way is to check for malformed url before
        * requesting  connection
        * */
        if (!NetworkUtils.checkUrlHasValidScheme(url)) {
            // Let's try to add scheme to String
            url = "http://" + url;
            App.log(TAG, "trying to add scheme to url. New url: " + url);
        }


        try {
            App.log(TAG, "Trying to connecting to: " + url);
            Document doc = Jsoup.connect(url).get();

            if (doc == null){
                /*
                * If get request somehow returns null without throwing an exception an
                * empty string is returned
                * */
                App.log(TAG,"Error: Unable to get a Document from url: " + url);
                return buffer.toString();
            } else {
                ArtWorkCrawler crawler = new ArtWorkCrawler();
                crawler.process(doc);

                SovietArtMePage sovietArtMePage = new SovietArtMePage();
                sovietArtMePage.setUrl(url);
                sovietArtMePage.setTitle(crawler.getTitle());
                sovietArtMePage.setYear(crawler.getYear());
                sovietArtMePage.setAuthor(crawler.getAuthor());
                sovietArtMePage.setCategory(crawler.getCategory());
                sovietArtMePage.setImageInfo(crawler.getImageInfo());

                callbackHandler.onWebPageParsed(sovietArtMePage);

            }

            // todo remove test code below and see what to do with on postExecute
            App.log(TAG, "Connected to: " + url);

            // Get document (HTML page) title
            String title = doc.title();
            App.log(TAG, "Title [" + title + "]");
            buffer.append("Title: " + title + "rn");

            // Get meta info
            Elements metaElems = doc.select("meta");
            buffer.append("META DATArn");
            for (Element metaElem : metaElems) {
                String name = metaElem.attr("name");
                String content = metaElem.attr("content");
                buffer.append("name [" + name + "] - content [" + content + "] rn");
            }

            Elements topicList = doc.select("h2.topic");
            buffer.append("Topic listrn");
            for (Element topic : topicList) {
                String data = topic.text();

                buffer.append("Data [" + data + "] rn");
            }

        } catch (Throwable t) {
            App.log(TAG, "Error: Caught throwable: " + t.toString());
        }

        return buffer.toString();
    }

//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        App.log(TAG, "onPostExecute: " + s);
//    }
}
