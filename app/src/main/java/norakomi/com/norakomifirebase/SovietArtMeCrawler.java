package norakomi.com.norakomifirebase;

import android.support.annotation.NonNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;

import norakomi.com.norakomifirebase.utils.App;

/**
 * Created by Rik van Velzen, Norakomi.com, on 13-4-2016.
 * <p/>
 * Check out:
 * https://github.com/RikNorakomi?tab=repositories
 * http://norakomi.blogspot.nl/
 * www.norakomi.com
 */
public class SovietArtMeCrawler {

    private final String TAG = getClass().getSimpleName();

    private Document doc = null;

    private String title = "";
    private String category = "";
    private String year = "";
    private String author = "";
    private String imageUrlInfo = "";
    private String imageFileName = "";
    private String highResImageUrl = "";
    private int intId;

    private boolean findTitle = true;
    private boolean findCategory = true;
    private boolean findYear = true;
    private boolean findAuthor = true;
    private boolean findImageInfo = true;
    private boolean findHighResImageUrl = true;
    private boolean createIntId = true;
    private boolean artworkAlreadyProcessed = false;

    public static Set<Integer> processedIDs = new HashSet<>();

    public SovietArtMeCrawler() {
    }

    /**
     * process() should be called in order for the ArtWorkCrawler to parse through the
     * document and try to find values related to the defined keys.
     * Setting a boolean value pre process call to true or false will include/exclude specific
     * info from the parsed result.
     * <p/>
     * Desired values/info can be requested via calls to the getters. E.g.: getTitle, getYear, etc.
     */
    public void process(@NonNull Document doc) {
        this.doc = doc;
        if (findImageInfo)
            findImageInfo(); // image info needs to be gotten before(!) getting int ID because id dependant on filename!
        if (createIntId)
            createIntId();
        if (processedIDs.contains(intId)) {
            App.log(TAG, "already parsed an art object with intId: " + intId + " going to next page and cancelling parse.");
            artworkAlreadyProcessed = true;
            return;
        } else {
            processedIDs.add(intId);
        }


        if (findTitle)
            findTitle();
        if (findCategory)
            findCategory();
        if (findYear)
            findYear();
        if (findAuthor)
            findAuthor();

        if (findHighResImageUrl)
            findHighResImageUrl();


    }

    private void createIntId() {
        if (imageFileName.isEmpty()) {
            App.log(TAG, "Unable to create an id from filename (isEmpty!)");
            intId = -1;
            return;
        }
        App.log(TAG, "filename to split: " + imageFileName);
        String id = imageFileName.split("\\.")[0];
        App.log(TAG, "id string after split = " + id);
        intId = -2;


        try {
            intId = Integer.parseInt(id);
        } catch (NumberFormatException nfe) {
            App.log(TAG, "Couldnt form id: " + id + " into a number");
        }
    }

    /**
     * Tries to find a filepath name:
     * ImageUrlInfo contains full url (incl. f.e. http:// prefix)
     * imageFileName contains file name without the uri scheme prefix
     */
    private void findImageInfo() {
        Elements img = doc.getElementsByTag("img");
        for (Element imageTag : img) {
            if (imageTag.toString().contains("jpg")) {
                if (imageTag.id().contains("poster") && !imageTag.toString().contains("random")) {
                    imageUrlInfo = imageTag.attr("src");

                    String fullString = imageTag.attr("src");
                    String delimiter = "/";
                    String[] tokens = fullString.split(delimiter);
                    imageFileName = tokens[tokens.length - 1];
                }
            }
        }
    }

    private void findHighResImageUrl() {
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            if (link.hasAttr("href") && link.attr("href").contains("print")) {
                highResImageUrl = link.attr("abs:href");
                return;
            }
        }
    }

    private void findAuthor() {
        Elements p = doc.getElementsByTag("p");
        for (Element author : p) {
            if (author.toString().contains("Author")) {
                this.author = author.getElementsByTag("a").text();
                break;
            }
        }

    }

    private void findYear() {
        Elements p = doc.getElementsByTag("p");

        for (Element year : p) {
            if (year.toString().contains("Year")) {
                this.year = year.getElementsByTag("a").text();
                break;
            }
        }
    }

    private void findTitle() {
        Elements title = doc.getElementsByTag("h1");
        if (title != null) {
            this.title = title.text();
        }
    }

    private void findCategory() {
        Elements input = doc.getElementsByTag("input");
        for (Element category : input) {
            if (category.toString().contains("checked")) {
                this.category = category.attr("value");
                break;
            }
        }
    }

    public void setFindTitle(boolean findTitle) {
        this.findTitle = findTitle;
    }

    public void setFindCategory(boolean findCategory) {
        this.findCategory = findCategory;
    }

    public void setFindYear(boolean findYear) {
        this.findYear = findYear;
    }

    public void setFindAuthor(boolean findAuthor) {
        this.findAuthor = findAuthor;
    }

    public void setFindImageInfo(boolean findImageInfo) {
        this.findImageInfo = findImageInfo;
    }


    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrlInfo() {
        return imageUrlInfo;
    }

    public String getHighResImageUrl() {
        return highResImageUrl;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public int getIntId() {
        return intId;
    }

    public boolean isArtworkAlreadyProcessed() {
        return artworkAlreadyProcessed;
    }
}
