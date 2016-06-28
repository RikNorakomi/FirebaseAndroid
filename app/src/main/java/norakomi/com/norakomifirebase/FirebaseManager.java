package norakomi.com.norakomifirebase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import norakomi.com.norakomifirebase.models.SovietArtMePage;
import norakomi.com.norakomifirebase.utils.App;

/**
 * Created by Rik van Velzen, Norakomi.com, on 13-4-2016.
 * <p/>
 * Check out:
 * https://github.com/RikNorakomi?tab=repositories
 * http://norakomi.blogspot.nl/
 * www.norakomi.com
 */
public class FirebaseManager {

    private final String TAG = getClass().getSimpleName();
    private static FirebaseManager instance;
    private Firebase firebaseReference;

    private FirebaseManager() {
    }

    public static FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }

        return instance;
    }

    public Firebase getFirebaseReference(){return firebaseReference;}

    public void writeToFirebase(@NonNull String childNode, @NonNull Object firebaseObject) {
        if (firebaseReference == null) {
            App.log(TAG, "Error.firebase refenerence is null! First call setFirebaseUrl() to create a reference to your database");
        }
        if (!isValidFirebaseObject(firebaseObject)) {
            App.log(TAG, "not a valid firebase object");
            return;
        }

        App.log(TAG, "writing to firebase on childnode: " + childNode + " with value(s): " + firebaseObject.toString());
        firebaseReference.child(childNode).setValue(firebaseObject);
    }

//    public void writeToFirebaseTest(ArrayList<SovietArtMePage> sovietArtMePagesArray){
//
//        Firebase poster = firebaseReference.child("posters").child();
//        User alan = new User("Alan Turing", 1912);
//        poster.setValue(alan);
//    }

    public void writeToFirebaseTest(ArrayList<SovietArtMePage> sovietArtMePage) {
        for (int i = 0; i < sovietArtMePage.size(); i++) {
//            String id = String.valueOf(sovietArtMePage.get(i).getIntID());
            Firebase posterRef = firebaseReference.child("posters");
            posterRef.setValue(sovietArtMePage);
        }
    }

    public void writeToFirebase(@NonNull String childNode, int childId, @NonNull Object firebaseObject) {
        if (firebaseReference == null) {
            App.log(TAG, "Error.firebase refenerence is null! First call setFirebaseUrl() to create a reference to your database");
        }
        if (!isValidFirebaseObject(firebaseObject)) {
            App.log(TAG, "not a valid firebase object");
            return;
        }

        Firebase childRef = firebaseReference.child(childNode).child(Integer.toString(childId));
        childRef.setValue(firebaseObject);
        App.log(TAG, "writing to firebase on childnode: " + childNode + " with value(s): " + firebaseObject.toString());
//        firebaseReference.child(childNode).setValue(firebaseObject);
    }

    public void readFromFirebase(String childNode, ValueEventListener valueEventListener) {
        firebaseReference.child(childNode).addValueEventListener(valueEventListener);
    }

    /**
     * Writing Data:
     * Once we have a reference to your data, we can write any Boolean, Long, Double,
     * Map<String, Object> or List object to it using setValue(). This method checks whether or not
     * we have a valid object
     */
    private boolean isValidFirebaseObject(Object firebaseObject) {
        return firebaseObject instanceof Boolean ||
                firebaseObject instanceof Long ||
                firebaseObject instanceof Double ||
                firebaseObject instanceof List ||
                firebaseObject instanceof Map;

    }

    public void setContext(Context context) {
        Firebase.setAndroidContext(context);
    }

    public void setFirebaseUrl(String url){
        firebaseReference = new Firebase(url);

    }



    // test
    public class User {
        private int birthYear;
        private String fullName;
//        public User() {}
        public User(String fullName, int birthYear) {
            this.fullName = fullName;
            this.birthYear = birthYear;
        }
        public long getBirthYear() {
            return birthYear;
        }
        public String getFullName() {
            return fullName;
        }
    }
}
