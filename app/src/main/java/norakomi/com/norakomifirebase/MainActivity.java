package norakomi.com.norakomifirebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import norakomi.com.norakomifirebase.api.RetrofitService;
import norakomi.com.norakomifirebase.utils.App;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    /**
     * Firebase documentation:
     *
     * https://www.firebase.com/docs/android/quickstart.html
     * https://www.firebase.com/docs/android/guide/understanding-data.html
     * Firebase en arrays: https://www.firebase.com/blog/2014-04-28-best-practices-arrays-in-firebase.html
     *
     */

    private Firebase myFirebaseRef;
    private TextView resultView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultView = (TextView) findViewById(R.id.resultView);
        button = (Button) findViewById(R.id.button);

        Firebase.setAndroidContext(this);

        myFirebaseRef = new Firebase("https://" + Constants.FIREBASE_BASE_PATH + ".firebaseio.com/");

        final String childNode = "soviet_art_me_pages";


        // test list for storing in firebase
        List<String> testList = Arrays.asList(
                "sovietart.me/posters/all/page1/1",
                "sovietart.me/posters/all/page1/2",
                "sovietart.me/posters/all/page1/3");

        writeToFirebase(childNode, testList);


        readFromFirebase(childNode, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String resultText = "Received data from " + childNode + ": " + dataSnapshot.toString();
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

    private void writeToFirebase(@NonNull String childNode, @NonNull Object firebaseObject) {
        if (myFirebaseRef == null) {
            App.log(TAG, "firebase refenerence is null!");
        }
        if (!isValidFirebaseObject(firebaseObject)) {
            Log.e("", "not a valid firebase object");
            return;
        }

        App.log(TAG, "writing to firebase on childnode: " + childNode + " with value(s): " + firebaseObject.toString());
        myFirebaseRef.child(childNode).setValue(firebaseObject);
    }

    private void readFromFirebase(String childNode, ValueEventListener responseListener) {
        myFirebaseRef.child(childNode).addValueEventListener(responseListener);
    }

    protected void exampleArrayLike(){
        Firebase julieRef = new Firebase("https://SampleChat.firebaseIO-demo.com/users/julie/");
        julieRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                List<String> messages = snapshot.getValue(t);
                if( messages == null ) {
                    System.out.println("no mssage");
                }
                else {
                    System.out.println("The first message is: " + messages.get(0) );
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

    /**
     * Writing Data:
     * Once we have a reference to your data, we can write any Boolean, Long, Double,
     * Map<String, Object> or List object to it using setValue(). This method checks whether or not
     * we have a valid object
     */
    private boolean isValidFirebaseObject(Object firebaseObject) {
        if (firebaseObject instanceof Boolean ||
                firebaseObject instanceof Long ||
                firebaseObject instanceof Double ||
                firebaseObject instanceof List ||
                firebaseObject instanceof Map) {
            return true;
        }

        return false;
    }

    public void buttonClicked(View view) {
        String msg = "button clicked. Trying to load data from Json.";
        App.log(TAG, msg);
        resultView.setText(resultView.getText() + "\n" + msg);
    }

    private void getJsonData(){
        new Thread(() -> {
            List<String> posters = new ArrayList<>();



            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.NORAKOMI_JSONS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // prepare call in Retrofit 2.0
            RetrofitService api = retrofit.create(RetrofitService.class);

            Call<SovietArtMePosters> call = api.loadPostersData();
            //asynchronous call
            call.enqueue(new Callback<SovietArtMePosters>() {
                @Override
                public void onResponse(Response<SovietArtMePosters> response, Retrofit retrofit) {
                    App.log(TAG, "onResponse sovietArtMeApi returned posters#: " + response.body().posters.size());
                    posters.addAll(response.body().posters);
                    mArtFeedAdapter.setArtWorkCollection(posters);
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
