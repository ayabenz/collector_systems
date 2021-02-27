package id.co.datascrip.app_collector_systems.proses.interfaces;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by alamsyah_putra on 4/27/2017.
 */
public interface IC_Get_Data {

    @GET("api_collector/get_blacklist_app")
    Call<ResponseBody> GetBlacklistApp();

    @FormUrlEncoded
    @POST("api_collector/update_multiple_waktu_mulai")
    Call<ResponseBody> update_multiple_waktu_mulai(@FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("api_collector/update_waktu_mulai")
    Call<ResponseBody> update_waktu_mulai(@FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("api_collector/update_hasil_kunjungan_faktur")
    Call<ResponseBody> update_hasil_kunjungan_faktur(@FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("api_collector/submit_faktur_customer")
    Call<ResponseBody> submit_faktur_customer(@FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("api_collector/complete_cust_faktur")
    Call<ResponseBody> complete_cust_faktur(@FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("api_collector/update_all_waktu_mulai")
    Call<ResponseBody> update_all_waktu_mulai(@FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("api_collector/get_all_faktur")
    Call<ResponseBody> get_all_faktur(@FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("api_collector/update_all_hasil_kunjungan_faktur")
    Call<ResponseBody> update_all_hasil_kunjungan_faktur(@FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("api_collector/update_multiple_hasil_kunjungan_faktur")
    Call<ResponseBody> update_multiple_hasil_kunjungan_faktur(@FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("api_collector/login")
    Call<ResponseBody> login(@FieldMap Map<String, String> data);

    @FormUrlEncoded
    @POST("api_collector/CheckIn")
    Call<ResponseBody> CheckIn(@FieldMap Map<String, String> data);

}
