package norakomi.com.norakomifirebase.api;

import norakomi.com.norakomifirebase.models.JSONResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by norakomi on 3/31/2016.
 */
public interface RetrofitService {

    @GET("test.json")
    Call<JSONResponse> getJson();
}
