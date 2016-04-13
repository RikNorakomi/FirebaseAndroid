package norakomi.com.norakomifirebase.models;

/**
 * Created by norakomi on 3/31/2016.
 */
public class SovietArtMePages {

    private final String defaultKey = "url";

    private String key = defaultKey;
    private String value = "";

    public SovietArtMePages() {}

    public SovietArtMePages(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public void setValue (String value){
        this.value = value;
    }

    public void setKey (String key) {
        this.key = key;
    }
}
