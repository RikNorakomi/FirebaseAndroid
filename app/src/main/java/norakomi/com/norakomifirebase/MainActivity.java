package norakomi.com.norakomifirebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import norakomi.com.norakomifirebase.JSoup.ParseUrl;
import norakomi.com.norakomifirebase.models.SovietArtMePage;
import norakomi.com.norakomifirebase.models.WebPage;
import norakomi.com.norakomifirebase.utils.App;

public class MainActivity extends AppCompatActivity implements UrlParsedCallback {
    private final String TAG = getClass().getSimpleName();

    /**
     * Firebase documentation:
     * <p/>
     * https://www.firebase.com/docs/android/quickstart.html
     * https://www.firebase.com/docs/android/guide/understanding-data.html
     * Firebase en arrays: https://www.firebase.com/blog/2014-04-28-best-practices-arrays-in-firebase.html
     */


    private TextView resultView;
    private Button button;
    private FirebaseManager firebaseManager;
    private ParseUrl urlParser;
    private ArrayList<String> urlsToCrawl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultView = (TextView) findViewById(R.id.resultView);
        button = (Button) findViewById(R.id.button);

        // create url parser
        urlParser = new ParseUrl(new WeakReference<UrlParsedCallback>(this));

        // create FirebaseManager
        firebaseManager = FirebaseManager.getInstance();
        firebaseManager.setContext(this);
        firebaseManager.setFirebaseUrl(Constants.FIREBASE_HOME);

        // create an array of url for crawler to handle
        // test list for storing in firebase
        List<String> testList = Arrays.asList(
                "sovietart.me/posters/all/page1/2",
                "sovietart.me/posters/all/page1/3",
                "sovietart.me/posters/all/page1/4",
                "sovietart.me/posters/all/page1/1"
        );

        urlsToCrawl = new ArrayList<>();
        urlsToCrawl.add(testList.get(0));
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

    protected void exampleArrayLike() {
        Firebase julieRef = new Firebase("https://SampleChat.firebaseIO-demo.com/users/julie/");
        julieRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                };
                List<String> messages = snapshot.getValue(t);
                if (messages == null) {
                    System.out.println("no mssage");
                } else {
                    System.out.println("The first message is: " + messages.get(0));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

    public void buttonClicked(View view) {
        String msg = "button clicked. Trying to load parse test data with JSoup.";
        App.log(TAG, msg);

        for (String url : urlsToCrawl) {
            urlParser.execute(url);
        }


        resultView.setText(resultView.getText() + "\n" + msg);
    }

    private void getJsonData() {
//        new Thread(() -> {
//            List<String> posters = new ArrayList<>();
////
////
////
////            Retrofit retrofit = new Retrofit.Builder()
////                    .baseUrl(Constants.NORAKOMI_JSONS_BASE_URL)
////                    .addConverterFactory(GsonConverterFactory.create())
////                    .build();
////
////            // prepare call in Retrofit 2.0
////            RetrofitService api = retrofit.create(RetrofitService.class);
////
////            Call<SovietArtMePosters> call = api.loadPostersData();
////            //asynchronous call
////            call.enqueue(new Callback<SovietArtMePosters>() {
////                @Override
////                public void onResponse(Response<SovietArtMePosters> response, Retrofit retrofit) {
////                    App.log(TAG, "onResponse sovietArtMeApi returned posters#: " + response.body().posters.size());
////                    posters.addAll(response.body().posters);
////                    mArtFeedAdapter.setArtWorkCollection(posters);
////                }
////
////                @Override
////                public void onFailure(Throwable t) {
////                    Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
////                }
////            });
//        }).start();
    }


    /**
     * When a artwork's webPage is parsed we check if data is not already in on fireBase and if
     * so add the artwork info to our database
    * */
    @Override
    public void onWebPageParsed(WebPage parsedWebPage) {
        App.log(TAG, "in onWebPageParsed for url: " + parsedWebPage.getUrl());
        if (parsedWebPage instanceof SovietArtMePage){
            //todo
            App.log(TAG, "onWebPageParsed.callback provided SovietArtMePage object. Setting result to ui.");
            String msg = resultView.getText() + "\n" +
                    "title: "+((SovietArtMePage) parsedWebPage).getTitle() + "\n" +
                    "author: "+((SovietArtMePage) parsedWebPage).getAuthor() + "\n" +
                    "year: "+((SovietArtMePage) parsedWebPage).getYear() +"\n" +
                    "category: "+((SovietArtMePage) parsedWebPage).getCategory() +"\n" +
                    "fileName: "+((SovietArtMePage) parsedWebPage).getImageFileName() + "\n"
                    ;

            runOnUiThread(() -> resultView.setText(msg));
        }

    }

    @Override
    public void onError(String error) {
                    //todo
        App.log(TAG, "in onError: " + error);
    }
}
