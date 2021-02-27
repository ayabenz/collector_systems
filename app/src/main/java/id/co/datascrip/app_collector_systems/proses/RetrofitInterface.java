package id.co.datascrip.app_collector_systems.proses;

import id.co.datascrip.app_collector_systems.helper.AYHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class RetrofitInterface {

    public interface IC_GET_REASON {
        @FormUrlEncoded
        @POST(AYHelper.URL_GET_ALL_REASON)
        Call<ResponseBody> Post(@Field("id_user") String id_user);
    }

}
