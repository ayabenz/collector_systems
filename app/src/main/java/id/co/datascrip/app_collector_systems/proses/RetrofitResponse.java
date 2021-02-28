package id.co.datascrip.app_collector_systems.proses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.datascrip.app_collector_systems.Function;
import id.co.datascrip.app_collector_systems.data.DataReason;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class RetrofitResponse {

    public static String getString(Response<ResponseBody> response) {
        try {
            String respon = response.body().string().trim();
            if (response.isSuccessful()) {
                if (!Function.isEmpty(respon))
                    return respon;
                else
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static boolean isSuccess(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            return jsonObject.has("status") && jsonObject.getString("status").trim().toLowerCase().equals("success");
        } catch (JSONException e) {
            return false;
        }
    }

    public static ArrayList<DataReason> getListReason(String json) {
        ArrayList<DataReason> listReason = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonReason = jsonArray.getJSONObject(i);
                DataReason reason = new DataReason();
                reason.setCode(jsonReason.getString(DataReason.CODE));
                reason.setDescription(jsonReason.getString(DataReason.DESCRIPTION));
                reason.setTipe(jsonReason.getString(DataReason.TIPE));
                reason.setTipe_code(jsonReason.getString(DataReason.TIPE_CODE));
                listReason.add(reason);
            }
        } catch (Exception e) {
            return listReason;
        }
        return listReason;
    }

    public static String getErrorMessage(String json) {
        String msg = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            msg = jsonObject.getString("msg");
        } catch (JSONException e) {
        }
        return msg;
    }

    public static String getString(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key))
                return jsonObject.getString(key);
        } catch (JSONException e) {
        }
        return "";
    }
}
