package id.co.datascrip.app_collector_systems.proses;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import id.co.datascrip.app_collector_systems.helper.AYHelper;
import id.co.datascrip.app_collector_systems.network.Retro;
import id.co.datascrip.app_collector_systems.proses.interfaces.IC_Get_Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alamsyah_putra on 4/27/2017.
 */
public class Proses_Get_Data {
    private final Context context;
    private ProgressDialog progressDialog;
    private final IC_Get_Data IC;

    public Proses_Get_Data(Context context, boolean showDialog) {
        this.context = context;
        this.IC = AYHelper.createRetrofit(context).create(IC_Get_Data.class);
        if (showDialog) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void getBlacklistApp() {
        if (AYHelper.isOnline(context)) {
            IC.GetBlacklistApp().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    String respon = Retro.getString(response);
                    if (Retro.isSuccess(response, respon)) {
                        AYHelper.list_blacklist_app.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(respon);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++)
                                AYHelper.list_blacklist_app.add(jsonArray.get(i).toString());
                        } catch (Exception e) {
                            //Log.e("getBlacklistApp", e.toString());
                        }
                    }
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }
            });
        } else {
            if (progressDialog != null) progressDialog.dismiss();
        }
    }

}
