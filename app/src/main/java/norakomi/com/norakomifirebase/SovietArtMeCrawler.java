package norakomi.com.norakomifirebase;

import android.support.annotation.NonNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Rik van Velzen, Norakomi.com, on 13-4-2016.
 * <p/>
 * Check out:
 * https://github.com/RikNorakomi?tab=repositories
 * http://norakomi.blogspot.nl/
 * www.norakomi.com
 */
public class SovietArtMeCrawler {

    private Document doc = null;

    private String title = "";
    private String category = "";
    private String year = "";
    private String author = "";
    private String imageUrlInfo = "";
    private String imageFileName = "";

    private boolean findTitle = true;
    private boolean findCategory = true;
    private boolean findYear = true;
    private boolean findAuthor = true;
    private boolean findImageInfo = true;

    public SovietArtMeCrawler() {}

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
     *  Tries to find a filepath name:
     *  ImageUrlInfo contains full url (incl. f.e. http:// prefix)
     *  imageFileName contains file name without the uri scheme prefix
     * */
    private void findImageInfo() {
        Elements img = doc.getElementsByTag("img");
        for (Element images : img) {

            if (images.toString().contains("jpg")
                    && !images.toString().contains("random")
                    && images.id().contains("poster")) {

                imageUrlInfo = images.attr("src");

                String fullString = images.attr("src");
                String delimiter = "/";
                String[] tokens = fullString.split(delimiter);
                imageFileName = tokens[tokens.length - 1];
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
        if (title != null) {this.title = title.text();}
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

    public String getImageFileName() { return imageFileName; }
}
