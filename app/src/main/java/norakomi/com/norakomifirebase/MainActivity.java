package norakomi.com.norakomifirebase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import norakomi.com.norakomifirebase.JSoup.ParseUrl;
import norakomi.com.norakomifirebase.models.SovietArtMePage;
import norakomi.com.norakomifirebase.models.WebPage;
import norakomi.com.norakomifirebase.networking.VolleyJsonRequest;
import norakomi.com.norakomifirebase.utils.App;

public class MainActivity extends AppCompatActivity implements UrlParsedCallback, Response.Listener<JSONObject>, Response.ErrorListener {

    /**
     * MainActivity creates a ParseUrl instance to which a weakReference of UrlParsedCallback interface
     * is given which holds the callback method onWebPageParsed() and onWebPageParsingError() which are
     * implemented by this activity and respectively handle passing a SovietArtMePage (/WebPage) object
     * that has information on an artWork (title, year, imageUrl, etc.)
     * <p/>
     * {@link SovietArtMePage}
     * {@link WebPage}
     */

    private final String TAG = getClass().getSimpleName();

    /**
     * Firebase documentation:
     * <p/>
     * https://www.firebase.com/docs/android/quickstart.html
     * https://www.firebase.com/docs/android/guide/understanding-data.html
     * Firebase en arrays: https://www.firebase.com/blog/2014-04-28-best-practices-arrays-in-firebase.html
     */


    private TextView resultView;
    private Button buttonCrawl;
    private FirebaseManager firebaseManager;
    private ArrayList<String> urlsToCrawl;
    private ArrayList<SovietArtMePage> sovietArtMePagesArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultView = (TextView) findViewById(R.id.resultView);
//        resultView.setMovementMethod(new ScrollingMovementMethod());
        buttonCrawl = (Button) findViewById(R.id.buttonCrawl);


        // create FirebaseManager
        firebaseManager = FirebaseManager.getInstance();
        firebaseManager.setContext(this);
        firebaseManager.setFirebaseUrl(Constants.FIREBASE_HOME); // https://dazzling-inferno-8912.firebaseio.com/


        // create an array of url for crawler to handle
        // test list for storing in firebase
        List<String> testList = Arrays.asList(
                "sovietart.me/posters/all/page1/2",
                "sovietart.me/posters/all/page1/3",
                "sovietart.me/posters/all/page1/4",
                "sovietart.me/posters/all/page1/1"
        );

        urlsToCrawl = new ArrayList<>();
        urlsToCrawl.addAll(testList);
        App.log(TAG, "url to crawl = " + urlsToCrawl.get(0));

        firebaseManager.writeToFirebase(Constants.childNodePages, testList);
        firebaseManager.readFromFirebase(Constants.childNodePages, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String resultText = "Received data from " + Constants.childNodePages + ": " + dataSnapshot.toString();
                App.log(TAG, resultText);
                if (resultView != null) {
                    resultView.setText(resultText);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                App.log(TAG, "could get data from firebase: firebaseError=" + firebaseError.toString());
                App.log(TAG, "Error details:" + firebaseError.getDetails());

            }
        });
    }

    public void buttonSendDataToFirebase(View view) {
        App.log(TAG, "firebase test");
        firebaseManager.writeToFirebaseTest(sovietArtMePagesArray);

//        App.log(TAG, "in buttonSendDataToFirebase");
//        if (sovietArtMePagesArray.isEmpty()) {
//            Toast.makeText(this, "Currently no crawled pages collected", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        for (SovietArtMePage s: sovietArtMePagesArray   ) {
//            App.log(TAG, "writing Page to Firebase: " + s.toString());
//            firebaseManager.writeToFirebase(Constants.childNodePosters, (int) s.getIntID(), s);
//        }
    }

    public void buttonCrawlPagesClicked(View view) {
        String msg = "buttonCrawl clicked. Trying to load parse test data with JSoup.";
        App.log(TAG, msg);
        resultView.setText(msg);

        int maxUrls = 10;
        for (int i = 0; i < maxUrls; i++) {
            ParseUrl urlParser = new ParseUrl(new WeakReference<UrlParsedCallback>(this));
            urlParser.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, urlsToCrawl.get(i));
            App.log(TAG, "#" + i + " crawling url: " + urlsToCrawl.get(i));
        }
    }

    public void buttonReadJsonClicked(View view) {
        resultView.setText("Read JSON for pages");
        String url = Constants.URL_JSON_SOVIET_ART_ME_POSTERS_PAGES; // http://www.norakomi.com/assets/json/soviet_art_me_poster_pages.json
        App.log(TAG, "starting volleyRequest. in buttonReadJsonClicked()");
        new VolleyJsonRequest(this, url, this, this);
    }

    //Start region UrlParseCallback methods

    /**
     * When a artwork's webPage is parsed we check if data is not already in on fireBase and if
     * so add the artwork info to our database
     */
    @Override
    public void onWebPageParsed(WebPage parsedWebPage) {
        App.log(TAG, "in onWebPageParsed for url: " + parsedWebPage.getUrl());
        if (parsedWebPage instanceof SovietArtMePage) {
            //todo
            App.log(TAG, "onWebPageParsed.callback provided SovietArtMePage object. Setting result to ui.");
            final String msg = "\n" +
                    "intId: " + ((SovietArtMePage) parsedWebPage).getIntID() + "\n" +
                    "title: " + ((SovietArtMePage) parsedWebPage).getTitle() + "\n" +
                    "author: " + ((SovietArtMePage) parsedWebPage).getAuthor() + "\n" +
                    "year: " + ((SovietArtMePage) parsedWebPage).getYear() + "\n" +
                    "category: " + ((SovietArtMePage) parsedWebPage).getCategory() + "\n" +
                    "fileName: " + ((SovietArtMePage) parsedWebPage).getImageFileName() + "\n" +
                    "highRes fileName: " + ((SovietArtMePage) parsedWebPage).getHighResImageUrl() + "\n" +
                    "imageUrlInfo: " + ((SovietArtMePage) parsedWebPage).getImageUrlInfo() + "\n";

            App.log(TAG, msg);
            sovietArtMePagesArray.add((SovietArtMePage) parsedWebPage);

            // todo upload to firebase

        }
    }

    @Override
    public void onWebPageParsingError(String error) {
        //todo
        App.log(TAG, "in onWebPageParsingError: " + error);
    }
    //End region UrlParseCallback methods


    //start region Volley callback methods
    @Override
    public void onResponse(JSONObject response) {
        App.log(TAG, "in onResponse for volleyRequest");

        final String jsonArrayKeyforUrls = "soviet_art_me_pages";
        JSONArray urlArray;
        try {
            urlArray = response.getJSONArray(jsonArrayKeyforUrls);
        } catch (JSONException e) {
            App.log(TAG, "Couldn't get json array from json object!");
            e.printStackTrace();
            return;
        }

        // clear previous url list before setting urls on array
        urlsToCrawl.clear();

        // trying to get each individual url from json array
        for (int i = 0; i < urlArray.length(); i++) {
            try {
                JSONObject jo = urlArray.getJSONObject(i);
                String url = jo.getString("url");
                urlsToCrawl.add(url);
                App.log(TAG, "found url: " + url);

            } catch (JSONException e) {
                App.log(TAG, "Couldn't get urls from array !");
                e.printStackTrace();
                break;
            }
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        String errorMsg = "VolleyError!: " + error.toString();
        App.log(TAG, errorMsg);
        resultView.setText(errorMsg);
    }

    //End region volley callback methods
}
