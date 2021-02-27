package id.co.datascrip.app_collector_systems.proses;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.ArrayList;

import id.co.datascrip.app_collector_systems.SessionManager;
import id.co.datascrip.app_collector_systems.data.DataReason;
import id.co.datascrip.app_collector_systems.helper.AYHelper;
import id.co.datascrip.app_collector_systems.sqllite.ReasonSQLite;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Proses_Load_Data {

    private Context context;
    private String id_user = "";
    private RetrofitCallback.CBLoadData CB;
    private ProgressDialog progressDialog;

    public Proses_Load_Data(Context context, RetrofitCallback.CBLoadData CB) {
        this.context = context;
        this.CB = CB;

        id_user = new SessionManager(context).getIduser();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        //progressDialog.show();
        getAllReason();
    }

    private void getAllReason() {
        new ReasonSQLite(context).deleteAll();
        if (new ReasonSQLite(context).getAll().size() == 0) {
            RetrofitInterface.IC_GET_REASON IC = AYHelper.createRetrofit(context).create(RetrofitInterface.IC_GET_REASON.class);
            IC.Post(id_user).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String result = RetrofitResponse.getString(response);
                    if (RetrofitResponse.isSuccess(result)) {
                        ReasonSQLite reasonSQLite = new ReasonSQLite(context);
                        ArrayList<DataReason> list_reason = RetrofitResponse.getListReason(result);
                        for (DataReason reason : list_reason) {
                            reasonSQLite.Post(reason);
                        }
                    } else {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        CB.OnCB(false);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    CB.OnCB(false);
                }
            });
        }else {
            progressDialog.dismiss();
        }
    }

}
