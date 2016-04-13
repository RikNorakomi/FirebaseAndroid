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

import java.util.Arrays;
import java.util.List;

import norakomi.com.norakomifirebase.utils.App;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultView = (TextView) findViewById(R.id.resultView);
        button = (Button) findViewById(R.id.button);

        firebaseManager = FirebaseManager.getInstance();
        firebaseManager.setContext(this);
        firebaseManager.setFirebaseUrl(Constants.FIREBASE_HOME);

        // test list for storing in firebase
        List<String> testList = Arrays.asList(

                "sovietart.me/posters/all/page1/2",
                "sovietart.me/posters/all/page1/3",
                "sovietart.me/posters/all/page1/4",
                "sovietart.me/posters/all/page1/1");

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
        String msg = "button clicked. Trying to load data from Json.";
        App.log(TAG, msg);
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
}
