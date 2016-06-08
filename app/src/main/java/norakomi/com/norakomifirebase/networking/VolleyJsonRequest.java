package norakomi.com.norakomifirebase.networking;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import norakomi.com.norakomifirebase.utils.App;

/**
 * Created by Rik van Velzen, Norakomi.com, on 27-5-2016.
 * <p/>
 * Check out:
 * https://github.com/RikNorakomi?tab=repositories
 * http://norakomi.blogspot.nl/
 * www.norakomi.com
 */
public class VolleyJsonRequest {

    private final String TAG = getClass().getSimpleName();

    private final String url;
    private final Context context;
    private final Response.Listener resultListener;
    private final Response.ErrorListener errorListener;

    private RequestQueue requestQueue;

    public VolleyJsonRequest(@NonNull Context context,
                             @NonNull String url,
                             @NonNull Response.Listener resultListener,
                             @NonNull Response.ErrorListener errorListener) {
        this.url = url;
        this.context = context;
        this.resultListener = resultListener;
        this.errorListener = errorListener;
        execute(url);
    }

    public void execute(String url) {
        // Instantiate the RequestQueue.
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }

        // String request example
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.GET,
//                url,
//                resultListener,
//                errorListener);
//
//        requestQueue.add(stringRequest);

//        JsonArrayRequest arrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                resultListener,
//                errorListener
//        );

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                resultListener,
                errorListener
        );

        requestQueue.add(objectRequest);

        App.log(TAG,"volley request queue added for url: " + url);
    }
}
