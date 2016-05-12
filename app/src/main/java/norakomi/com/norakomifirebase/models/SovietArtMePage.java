package norakomi.com.norakomifirebase.models;

/**
 * Created by Rik van Velzen, Norakomi.com, on 12-5-2016.
 * <p/>
 * Check out:
 * https://github.com/RikNorakomi?tab=repositories
 * http://norakomi.blogspot.nl/
 * www.norakomi.com
 */
public class SovietArtMePage extends WebPage {

    private String title;
    private String year;
    private String author;
    private String category;
    private String imageInfo;
    private String url;

    public void setUrl(String url){
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageInfo(String imageInfo) {
        this.imageInfo = imageInfo;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getImageInfo() {
        return imageInfo;
    }

    public String getUrl() {
        return url;
    }
}
