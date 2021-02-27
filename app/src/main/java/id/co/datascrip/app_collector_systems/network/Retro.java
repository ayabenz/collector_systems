package id.co.datascrip.app_collector_systems.network;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.datascrip.app_collector_systems.Utils;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Retro {

    private static final String STATUS_SUCCESS = "success";

    public static String getString(Response<ResponseBody> response) {
        try {
            String respon = response.body().string().trim();
            if (response.isSuccessful()) {
                if (!Utils.isEmpty(respon))
                    return respon;
                else
                    return null;
            }
        } catch (Exception e) {
            //new Function().writeToText("getString", e.toString());
            Log.e("R.R.->getString", e.toString());
            return null;
        }
        return null;
    }

    public static Status getStatus(Response<ResponseBody> response, String json) {
        if (json == null) {
            if (response != null && response.code() != 200)
                return new Status(false, response.code() + " " + response.message(), null);
            return new Status(false, "Network Error.", null);
        } else {
            Status status = new Status();
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(json);
                if (jsonObject.has("status"))
                    status.setSuccess(jsonObject.getString("status").trim().toLowerCase().equals(STATUS_SUCCESS));
                if (jsonObject.has("msg"))
                    status.setMessage(jsonObject.getString("msg").trim());
                if (jsonObject.has("sql"))
                    status.setQuery(jsonObject.getString("sql").trim());
            } catch (JSONException e) {
                Log.e("R.R.->getStatus", e.toString());
            }
            return status;
        }
    }

    public static void showError(final Context context, String title, String message) {
        if (message != null && title != null)
            new AlertDialog.Builder(context)
                    .setTitle(Html.fromHtml(title))
                    .setMessage(Html.fromHtml(message))
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
    }

    public static boolean isSuccess(Response<ResponseBody> response, String json) {
        return getStatus(response, json).isSuccess();
    }

}
