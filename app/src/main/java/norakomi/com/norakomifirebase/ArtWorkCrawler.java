package norakomi.com.norakomifirebase;

import android.support.annotation.NonNull;

import org.jsoup.nodes.Document;

/**
 * Created by Rik van Velzen, Norakomi.com, on 13-4-2016.
 * <p/>
 * Check out:
 * https://github.com/RikNorakomi?tab=repositories
 * http://norakomi.blogspot.nl/
 * www.norakomi.com
 */
public class ArtWorkCrawler {

    private Document doc = null;

    private String title = "Unknown";
    private String category = "Unknown";
    private String year = "Unknown";
    private String author = "Unknown";
    private String imageInfo = "Unknown";

    private boolean findTitle = true;
    private boolean findCategory = true;
    private boolean findYear = true;
    private boolean findAuthor = true;
    private boolean findImageInfo = true;

    public ArtWorkCrawler() {}

    /**
     * process() should be called in order for the ArtWorkCrawler to parse through the
     * document and try to find values related to the defined keys.
     * Setting a boolean value pre process call to true or false will include/exclude specific
     * info from the parsed result.
     *
     * Desired values/info can be requested via calls to the getters. E.g.: getTitle, getYear, etc.
    * */
    public void process(@NonNull Document doc) {
        this.doc = doc;
        if (findTitle) findTitle();
        if (findCategory) findCategory();
        if (findYear) findYear();
        if (findAuthor) findAuthor();
        if (findImageInfo) findImageInfo();
    }

    /**
     *  Tries to find a filepath name
     * */
    private void findImageInfo() {

    }

    private void findAuthor() {

    }

    private void findYear() {
    }

    private void findTitle() {

    }

    private void findCategory() {

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

    public String getImageInfo() {
        return imageInfo;
    }
}
