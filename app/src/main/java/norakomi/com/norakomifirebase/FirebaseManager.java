package norakomi.com.norakomifirebase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import java.util.List;
import java.util.Map;

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

    public void readFromFirebase(String childNode, ValueEventListener responseListener) {
        firebaseReference.child(childNode).addValueEventListener(responseListener);
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
}
