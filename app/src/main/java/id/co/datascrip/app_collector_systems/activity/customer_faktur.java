package id.co.datascrip.app_collector_systems.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.adapter.AdapterFaktur;
import id.co.datascrip.app_collector_systems.custom.dialog_cari_faktur;
import id.co.datascrip.app_collector_systems.custom.dialog_input_faktur;
import id.co.datascrip.app_collector_systems.custom.dialog_multiple_faktur;
import id.co.datascrip.app_collector_systems.custom.dialog_update_all_faktur;
import id.co.datascrip.app_collector_systems.data.DataFaktur;
import id.co.datascrip.app_collector_systems.helper.AYHelper;
import id.co.datascrip.app_collector_systems.helper.BaseActivity;
import id.co.datascrip.app_collector_systems.helper.Constan;
import id.co.datascrip.app_collector_systems.helper.GPSTracker;
import id.co.datascrip.app_collector_systems.network.Retro;
import id.co.datascrip.app_collector_systems.proses.interfaces.IC_Get_Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customer_faktur extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final int INTENT_SCANNING = 1000;
    final public static String no = "";
    public static final String TAG_ID = "id";
    public static final String TAG_FAKTURNO = "fakturno";
    public static final String TAG_TANDA = "tanda";
    private static final String TAG = customer_faktur.class.getSimpleName();
    private static final String url_select = AYHelper.BASE_URL + "api_collector/sett_list_invoice/";
    public static ArrayList<String> selectedStrings = new ArrayList<String>();
    public static String hasildialog = "";
    public static String hasilupdateall = "";
    public static String hasilupdat_multiple = "";
    public static String hasilcarifaktur = "";
    public static String hasilnofaktur = "";
    public static Map<String, String> selesai = new HashMap<>();
    public static Map<String, String> selesai_updateall = new HashMap<>();
    public static Map<String, String> selesai_update_multiple = new HashMap<>();
    String cons_custid, cons_custname, cons_custalamat, cons_rowson;
    Button btn_complete;
    String[] fakturcari;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataFaktur> itemList = new ArrayList<DataFaktur>();
    AdapterFaktur adapter;
    private double lat, log;
    private AlertDialog dialog_dt_setting, dialog_gps_setting;

    private IC_Get_Data icGetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_faktur);
        icGetData = AYHelper.createRetrofit(this).create(IC_Get_Data.class);
        settingView();
        checkDateTimeSetting();
        checkGPS();
    }

    private void checkGPS() {
        if (dialog_gps_setting != null)
            dialog_gps_setting.dismiss();
        if (!AYHelper.isGPSActive(this)) {
            dialog_gps_setting = new AlertDialog.Builder(this)
                    .setTitle("GPS tidak aktif.")
                    .setMessage("Silakan aktifkan GPS untuk melanjutkan.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AYHelper.GPS_SETTING);
                    })
                    .create();
            dialog_gps_setting.show();
        }
    }

    private void checkDateTimeSetting() {
        if (dialog_dt_setting != null)
            dialog_dt_setting.dismiss();
        if (!AYHelper.isAutoTime(this)) {
            dialog_dt_setting = new AlertDialog.Builder(this)
                    .setTitle("Date/Time Setting tidak aktif.")
                    .setMessage("Silakan aktifkan Automatic Date/Time pada Setting -> Date & Time.")
                    .setCancelable(false)
                    .setPositiveButton("Setting", (dialog, which) -> {
                        dialog_dt_setting.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), AYHelper.DATE_TIME_SETTING);
                    })
                    .create();
            dialog_dt_setting.show();
        }
    }

    private void settingView() {
        cons_custid = getIntent().getStringExtra(Constan.CUST_ID);
        cons_custname = getIntent().getStringExtra(Constan.CUST_NAMA);
        cons_custalamat = getIntent().getStringExtra(Constan.CUST_ALAMAT);
        cons_rowson = getIntent().getStringExtra(Constan.CUST_ROWNO);
        setTitle(cons_custname);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // menghubungkan variablel pada layout dan pada java
        swipe = findViewById(R.id.swipe_refresh_layout_inv);
        list = findViewById(R.id.list_inv);
        btn_complete = findViewById(R.id.btn_complete);


        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new AdapterFaktur(customer_faktur.this, itemList);
        list.setAdapter(adapter);

        // menampilkan widget refresh
        swipe.setOnRefreshListener(this);
        swipe.post(() -> {
                    swipe.setRefreshing(true);
                    itemList.clear();
                    adapter.notifyDataSetChanged();
                    callVolley();
                }
        );

        list.setItemsCanFocus(true);
        list.setOnItemClickListener((parent, view, position, id) -> {
            final String idx = itemList.get(position).getId();
            final String invoicex = itemList.get(position).getInvoceno();
            /*Buat triger untuk update tgl mulai kunjungan data dan post ke server*/
            GPSTracker gps = new GPSTracker(c);
            if (!gps.canGetLocation() && !AYHelper.isGPSActive(c)) {
                dialog_gps_setting = new AlertDialog.Builder(c)
                        .setTitle("GPS tidak aktif.")
                        .setMessage("Silakan aktifkan GPS untuk melanjutkan.")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AYHelper.GPS_SETTING);
                        })
                        .create();
                dialog_gps_setting.show();
            } else if (AYHelper.isUsingFakeGPS(c)) {
                AYHelper.alert_dialog(c, "INFORMASI", "Anda menggunakan lokasi palsu. Permintaan anda tidak dapat dilanjutkan.");
            } else {
                lat = gps.getLatitude();
                log = gps.getLongitude();
                String url_update = AYHelper.BASE_URL + "api_collector/update_waktu_mulai";
                Map<String, String> params = new HashMap<>();
                params.put("id", idx);
                params.put("collector", sesi.getIduser());
                params.put("invoiceno", invoicex);
                params.put("txt_lat", AYHelper.s(lat));
                params.put("txt_log", AYHelper.s(log));
                params.put("txt_wkt_device", AYHelper.tglJamSql());

                ProgressDialog pd = new ProgressDialog(c);
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.setInverseBackgroundForced(false);
                pd.setCanceledOnTouchOutside(false);
                pd.setMessage("Loading....");

                icGetData.update_waktu_mulai(params).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String hasil = Retro.getString(response);
                        if (hasil != null) {
                            //AYHelper.pre("Respon : "+ hasil);
                            try {
                                JSONObject json = new JSONObject(hasil);
                                final String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    Bundle bundle_faktur = new Bundle();
                                    bundle_faktur.putString("TitleScan", "Update Hasil Faktur");
                                    bundle_faktur.putString("id", idx);
                                    bundle_faktur.putString("inv", invoicex);
                                    dialog_input_faktur dialog = new dialog_input_faktur(customer_faktur.this, bundle_faktur);
                                    dialog.setOnDismissListener(dialog1 -> {
                                        if (hasildialog.equalsIgnoreCase("true")) {
                                            if (AYHelper.isStringEmpyt(selesai.get("ket"))) {
                                                AYHelper.alert_dialog(c, "Peringatan", "Kolom Keterangan Kosong, Kolom Keterangan Harus Di Isi.");
                                            } else if (selesai.get("kode_alasan").equalsIgnoreCase("8") && AYHelper.isStringEmpyt(selesai.get("jml_bayar"))) {
                                                AYHelper.alert_dialog(c, "Peringatan", "Untuk Tipe Berhasil Terima Pembayaran, Kolom Jumlah Bayar Harus Di Isi.");
                                            } else {
                                                update_selesai(selesai);
                                            }
                                        }
                                    });
                                } else {
                                    AYHelper.alert_dialog(c, "ERROR", pesan);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AYHelper.pesan(c, "Error, " + e);
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                                AYHelper.pesan(c, "Error Parsing Data : " + e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        AYHelper.alert_dialog(c, "ERROR", throwable.getMessage());
                    }
                });
            }
        });

        btn_complete.setOnClickListener(v -> complete_customer(cons_custid));
    }

    private void complete_customer(String custid) {
        GPSTracker gps = new GPSTracker(c);
        if (!gps.canGetLocation() && AYHelper.isGPSActive(c)) {
            dialog_gps_setting = new AlertDialog.Builder(c)
                    .setTitle("GPS tidak aktif.")
                    .setMessage("Silakan aktifkan GPS untuk melanjutkan.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AYHelper.GPS_SETTING);
                    })
                    .create();
            dialog_gps_setting.show();
        } else if (AYHelper.isUsingFakeGPS(c)) {
            AYHelper.alert_dialog(c, "INFORMASI", "Anda menggunakan lokasi palsu. Permintaan anda tidak dapat dilanjutkan.");
        } else {
            lat = gps.getLatitude();
            log = gps.getLongitude();
            String url_complete = AYHelper.BASE_URL + "api_collector/complete_cust_faktur";
            Map<String, String> complete = new HashMap<>();
            complete.put("collector", sesi.getIduser());
            complete.put("cust_no", custid);
            complete.put("cust_name", cons_custname);
            complete.put("cust_alamat", cons_custalamat);
            complete.put("txt_lat", AYHelper.s(lat));
            complete.put("txt_log", AYHelper.s(log));
            complete.put("txt_wkt_device", AYHelper.tglJamSql());
            ProgressDialog pd = new ProgressDialog(c);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setInverseBackgroundForced(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Loading....");

            try {
                AYHelper.pre("url : " + url_complete + ", params: " + complete.toString());

                icGetData.complete_cust_faktur(complete).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String hasil = Retro.getString(response);
                        if (hasil != null) {
                            try {
                                JSONObject json = new JSONObject(hasil);
                                String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    /*intent untuk kembali ke main menu dan refresh adapter*/
                                    Intent i = new Intent(c, main_menu.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    AYHelper.alert_dialog(c, "Peringatan", pesan);
                                    Refresh_voley();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AYHelper.alert_dialog(c, "Error", e.getMessage());
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                                AYHelper.alert_dialog(c, "Error Parsing Data", e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        AYHelper.alert_dialog(c, "ERROR", throwable.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                AYHelper.pesan(c, "Error, " + e);
            }
        }
    }

    private void update_selesai(Map<String, String> selesai) {
        hasildialog = "false";
        GPSTracker gps = new GPSTracker(c);
        if (!gps.canGetLocation() && AYHelper.isGPSActive(c)) {
            dialog_gps_setting = new AlertDialog.Builder(c)
                    .setTitle("GPS tidak aktif.")
                    .setMessage("Silakan aktifkan GPS untuk melanjutkan.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AYHelper.GPS_SETTING);
                    })
                    .create();
            dialog_gps_setting.show();
        } else if (AYHelper.isUsingFakeGPS(c)) {
            AYHelper.alert_dialog(c, "INFORMASI", "Anda menggunakan lokasi palsu. Permintaan anda tidak dapat dilanjutkan.");
        } else {
            lat = gps.getLatitude();
            log = gps.getLongitude();
            String url_selesai = AYHelper.BASE_URL + "api_collector/update_hasil_kunjungan_faktur";
            selesai.put("collector", sesi.getIduser());
            selesai.put("txt_lat", AYHelper.s(lat));
            selesai.put("txt_log", AYHelper.s(log));
            selesai.put("txt_wkt_device", AYHelper.tglJamSql());
            ProgressDialog pd = new ProgressDialog(c);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setInverseBackgroundForced(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Loading....");
            try {
                AYHelper.pre("url : " + url_selesai + ", params: " + selesai.toString());

                icGetData.update_hasil_kunjungan_faktur(selesai).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String hasil = Retro.getString(response);
                        if (hasil != null) {
                            AYHelper.pre("Respon : " + hasil);
                            try {
                                JSONObject json = new JSONObject(hasil);
                                String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    Refresh_voley();
                                } else {
                                    AYHelper.alert_dialog(c, "ERROR", pesan);
                                    Refresh_voley();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AYHelper.alert_dialog(c, "Error", e.getMessage());
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                                AYHelper.alert_dialog(c, "Error Parsing Data", e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        AYHelper.alert_dialog(c, "ERROR", throwable.getMessage());
                    }
                });
                /*
                query.progress(pd).ajax(url_selesai, selesai, String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String hasil, AjaxStatus status) {
                        if (hasil != null) {
                            AYHelper.pre("Respon : " + hasil);
                            try {
                                JSONObject json = new JSONObject(hasil);
                                String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    Refresh_voley();
                                } else {
                                    AYHelper.alert_dialog(c, "ERROR", pesan);
                                    Refresh_voley();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AYHelper.alert_dialog(c, "Error", e.getMessage());
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                                AYHelper.alert_dialog(c, "Error Parsing Data", e.getMessage());
                            }
                        }
                    }
                });
                */
            } catch (Exception e) {
                e.printStackTrace();
                AYHelper.pesan(c, "Error, " + e);
            }
        }
    }

    private void Refresh_voley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scan_header, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*
        if (id == R.id.scan_faktur) {
            startScan();
            return true;
        }else
        */
        if (id == R.id.action_cari_faktur) {
            if (fakturcari.length > 0) {
                Bundle bundle_faktur = new Bundle();
                bundle_faktur.putString("TitleInput", "Cari No. Faktur");
                bundle_faktur.putStringArray("arrayfaktur", fakturcari);
                dialog_cari_faktur dialog = new dialog_cari_faktur(customer_faktur.this, bundle_faktur);
                dialog.setOnDismissListener(dialog1 -> {
                    if (hasilcarifaktur.equalsIgnoreCase("true")) {
                        //System.out.println(hasilnofaktur.toString());
                        callvolleyfaktur(hasilnofaktur);
                    }
                });
            } else {
                AYHelper.alert_dialog(c, "PERHATIAN", "Anda Belum Melakukan Proses Scan faktur Atau Get All Faktur.");
            }
            return true;
        } else if (id == R.id.action_get_all_faktur) {
            get_all_faktur(cons_custid);
            return true;
        } else if (id == R.id.action_update_faktur) {
            update_all_faktur(cons_custid);
            return true;
        } else if (id == R.id.action_multiple_faktur) {
            if (selectedStrings.size() > 0) {
                update_multiple_faktur(selectedStrings);
            } else {
                AYHelper.alert_dialog(c, "PERHATIAN", "Anda Belum Memilih Faktur.");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void update_multiple_faktur(ArrayList<String> selectedStrings) {
        GPSTracker gps = new GPSTracker(c);
        if (!gps.canGetLocation() && AYHelper.isGPSActive(c)) {
            dialog_gps_setting = new AlertDialog.Builder(c)
                    .setTitle("GPS tidak aktif.")
                    .setMessage("Silakan aktifkan GPS untuk melanjutkan.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AYHelper.GPS_SETTING);
                    })
                    .create();
            dialog_gps_setting.show();
        } else if (AYHelper.isUsingFakeGPS(c)) {
            AYHelper.alert_dialog(c, "INFORMASI", "Anda menggunakan lokasi palsu. Permintaan anda tidak dapat dilanjutkan.");
        } else {
            lat = gps.getLatitude();
            log = gps.getLongitude();
            String url_multiple_waktumulai = AYHelper.BASE_URL + "api_collector/update_multiple_waktu_mulai";
            Map<String, String> update_multiple_mulai = new HashMap<>();
            update_multiple_mulai.put("collector", sesi.getIduser());
            update_multiple_mulai.put("cust_no", cons_custid);
            update_multiple_mulai.put("cust_name", cons_custname);
            update_multiple_mulai.put("cust_alamat", cons_custalamat);
            update_multiple_mulai.put("txt_lat", AYHelper.s(lat));
            update_multiple_mulai.put("txt_log", AYHelper.s(log));
            update_multiple_mulai.put("txt_wkt_device", AYHelper.tglJamSql());
            update_multiple_mulai.put("txt_id_multiple", selectedStrings.toString());

            ProgressDialog pd = new ProgressDialog(c);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setInverseBackgroundForced(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Loading....");

            try {
                AYHelper.pre("url : " + url_multiple_waktumulai + ", params: " + update_multiple_mulai.toString());

                icGetData.update_multiple_waktu_mulai(update_multiple_mulai).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String hasil = Retro.getString(response);
                        if (hasil != null) {
                            try {
                                JSONObject json = new JSONObject(hasil);
                                String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    Bundle bundel_multiple_update = new Bundle();
                                    bundel_multiple_update.putString("TitleScan", "Update Multiple Hasil Faktur");
                                    bundel_multiple_update.putString("id_selected", json.getString("id_select"));
                                    dialog_multiple_faktur multiple_faktur = new dialog_multiple_faktur(customer_faktur.this, bundel_multiple_update);
                                    multiple_faktur.setOnDismissListener(dialog -> {
                                        if (hasilupdat_multiple.equalsIgnoreCase("true")) {
                                            if (AYHelper.isStringEmpyt(selesai_update_multiple.get("ket"))) {
                                                AYHelper.alert_dialog(c, "Peringatan", "Untuk Tipe Kolom Keterangan Harus Di Isi.");
                                            } else {
                                                multiple_selesai(selesai_update_multiple);
                                            }
                                        }
                                    });
                                } else {
                                    AYHelper.alert_dialog(c, "PERHATIAN", pesan);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AYHelper.alert_dialog(c, "Error", e.getMessage());
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                                AYHelper.alert_dialog(c, "Error Parsing Data", e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        AYHelper.alert_dialog(c, "ERROR", throwable.getMessage());
                    }
                });
            } catch (Exception e) {
                AYHelper.alert_dialog(c, "ERROR", e.getMessage());
            }
        }
    }

    private void update_all_faktur(String cons_custid) {
        GPSTracker gps = new GPSTracker(c);
        if (!gps.canGetLocation() && AYHelper.isGPSActive(c)) {
            dialog_gps_setting = new AlertDialog.Builder(c)
                    .setTitle("GPS tidak aktif.")
                    .setMessage("Silakan aktifkan GPS untuk melanjutkan.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AYHelper.GPS_SETTING);
                    })
                    .create();
            dialog_gps_setting.show();
        } else if (AYHelper.isUsingFakeGPS(c)) {
            AYHelper.alert_dialog(c, "INFORMASI", "Anda menggunakan lokasi palsu. Permintaan anda tidak dapat dilanjutkan.");
        } else {
            final String custno = cons_custid;
            lat = gps.getLatitude();
            log = gps.getLongitude();
            String url_waktumulai_cust = AYHelper.BASE_URL + "api_collector/update_all_waktu_mulai";
            Map<String, String> params_update_all_waktu_mulai = new HashMap<>();
            params_update_all_waktu_mulai.put("collector", sesi.getIduser());
            params_update_all_waktu_mulai.put("cust_no", cons_custid);
            params_update_all_waktu_mulai.put("cust_name", cons_custname);
            params_update_all_waktu_mulai.put("cust_alamat", cons_custalamat);
            params_update_all_waktu_mulai.put("txt_lat", AYHelper.s(lat));
            params_update_all_waktu_mulai.put("txt_log", AYHelper.s(log));
            params_update_all_waktu_mulai.put("txt_wkt_device", AYHelper.tglJamSql());

            ProgressDialog pd = new ProgressDialog(c);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setInverseBackgroundForced(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Loading....");

            try {
                AYHelper.pre("url : " + url_waktumulai_cust + ", params: " + params_update_all_waktu_mulai.toString());

                icGetData.update_all_waktu_mulai(params_update_all_waktu_mulai).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String hasil = Retro.getString(response);
                        if (hasil != null) {
                            try {
                                JSONObject json = new JSONObject(hasil);
                                String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    Bundle bundle_update_all = new Bundle();
                                    bundle_update_all.putString("TitleScan", "Update Hasil Faktur");
                                    bundle_update_all.putString("cust_no", custno);
                                    dialog_update_all_faktur dialog_update_all = new dialog_update_all_faktur(customer_faktur.this, bundle_update_all);
                                    dialog_update_all.setOnDismissListener(dialog -> {
                                        if (hasilupdateall.equalsIgnoreCase("true")) {
                                            if (AYHelper.isStringEmpyt(selesai_updateall.get("ket"))) {
                                                AYHelper.alert_dialog(c, "Peringatan", "Untuk Tipe Kolom Keterangan Harus Di Isi.");
                                            } else {
                                                update_faktur(selesai_updateall);
                                            }
                                        }
                                    });
                                } else {
                                    AYHelper.alert_dialog(c, "PERHATIAN", pesan);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AYHelper.alert_dialog(c, "Error", e.getMessage());
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                                AYHelper.alert_dialog(c, "Error Parsing Data", e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        AYHelper.alert_dialog(c, "ERROR", throwable.getMessage());
                    }
                });
            } catch (Exception e) {
                AYHelper.alert_dialog(c, "ERROR", e.getMessage());
            }
        }
    }

    private void multiple_selesai(Map<String, String> selesai_update_multiple) {
        hasilupdat_multiple = "false";
        GPSTracker gps = new GPSTracker(c);
        if (!gps.canGetLocation() && AYHelper.isGPSActive(c)) {
            dialog_gps_setting = new AlertDialog.Builder(c)
                    .setTitle("GPS tidak aktif.")
                    .setMessage("Silakan aktifkan GPS untuk melanjutkan.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AYHelper.GPS_SETTING);
                    })
                    .create();
            dialog_gps_setting.show();
        } else if (AYHelper.isUsingFakeGPS(c)) {
            AYHelper.alert_dialog(c, "INFORMASI", "Anda menggunakan lokasi palsu. Permintaan anda tidak dapat dilanjutkan.");
        } else {
            lat = gps.getLatitude();
            log = gps.getLongitude();
            String url_selesai_update_multiple = AYHelper.BASE_URL + "api_collector/update_multiple_hasil_kunjungan_faktur";
            selesai_update_multiple.put("collector", sesi.getIduser());
            selesai_update_multiple.put("cust_no", cons_custid);
            selesai_update_multiple.put("cust_name", cons_custname);
            selesai_update_multiple.put("cust_alamat", cons_custalamat);
            selesai_update_multiple.put("txt_lat", AYHelper.s(lat));
            selesai_update_multiple.put("txt_log", AYHelper.s(log));
            selesai_update_multiple.put("txt_wkt_device", AYHelper.tglJamSql());

            ProgressDialog pd = new ProgressDialog(c);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setInverseBackgroundForced(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Loading....");

            try {
                AYHelper.pre("url : " + url_selesai_update_multiple + ", params: " + selesai_update_multiple.toString());

                icGetData.update_multiple_hasil_kunjungan_faktur(selesai_update_multiple).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String hasil = Retro.getString(response);
                        if (hasil != null) {
                            try {
                                JSONObject json = new JSONObject(hasil);
                                String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    Refresh_voley();
                                } else {
                                    AYHelper.alert_dialog(c, "PERHATIAN", pesan);
                                    Refresh_voley();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AYHelper.alert_dialog(c, "Error", e.getMessage());
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                                AYHelper.alert_dialog(c, "Error Parsing Data", e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        AYHelper.alert_dialog(c, "ERROR", throwable.getMessage());
                    }
                });
            } catch (Exception e) {
                AYHelper.alert_dialog(c, "PERHATIAN", e.getMessage());
            }
        }
    }

    private void update_faktur(Map<String, String> selesai_updateall) {
        hasilupdateall = "false";
        GPSTracker gps = new GPSTracker(c);
        if (!gps.canGetLocation() && AYHelper.isGPSActive(c)) {
            dialog_gps_setting = new AlertDialog.Builder(c)
                    .setTitle("GPS tidak aktif.")
                    .setMessage("Silakan aktifkan GPS untuk melanjutkan.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AYHelper.GPS_SETTING);
                    })
                    .create();
            dialog_gps_setting.show();
        } else if (AYHelper.isUsingFakeGPS(c)) {
            AYHelper.alert_dialog(c, "INFORMASI", "Anda menggunakan lokasi palsu. Permintaan anda tidak dapat dilanjutkan.");
        } else {
            lat = gps.getLatitude();
            log = gps.getLongitude();
            String url_selesai_updateall = AYHelper.BASE_URL + "api_collector/update_all_hasil_kunjungan_faktur";
            selesai_updateall.put("collector", sesi.getIduser());
            selesai_updateall.put("cust_no", cons_custid);
            selesai_updateall.put("cust_name", cons_custname);
            selesai_updateall.put("cust_alamat", cons_custalamat);
            selesai_updateall.put("txt_lat", AYHelper.s(lat));
            selesai_updateall.put("txt_log", AYHelper.s(log));
            selesai_updateall.put("txt_wkt_device", AYHelper.tglJamSql());

            ProgressDialog pd = new ProgressDialog(c);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setInverseBackgroundForced(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Loading....");
            try {
                AYHelper.pre("url : " + url_selesai_updateall + ", params: " + selesai_updateall.toString());

                icGetData.update_all_hasil_kunjungan_faktur(selesai_updateall).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String hasil = Retro.getString(response);
                        if (hasil != null) {
                            try {
                                JSONObject json = new JSONObject(hasil);
                                String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    Refresh_voley();
                                } else {
                                    AYHelper.alert_dialog(c, "PERHATIAN", pesan);
                                    Refresh_voley();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AYHelper.alert_dialog(c, "Error", e.getMessage());
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                                AYHelper.alert_dialog(c, "Error Parsing Data", e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        AYHelper.alert_dialog(c, "ERROR", throwable.getMessage());
                    }
                });
            } catch (Exception e) {
                AYHelper.alert_dialog(c, "PERHATIAN", e.getMessage());
            }
        }
    }

    private void get_all_faktur(String cons_custid) {
        GPSTracker gps = new GPSTracker(c);
        if (!gps.canGetLocation() && AYHelper.isGPSActive(c)) {
            dialog_gps_setting = new AlertDialog.Builder(c)
                    .setTitle("GPS tidak aktif.")
                    .setMessage("Silakan aktifkan GPS untuk melanjutkan.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AYHelper.GPS_SETTING);
                    })
                    .create();
            dialog_gps_setting.show();
        } else if (AYHelper.isUsingFakeGPS(c)) {
            AYHelper.alert_dialog(c, "INFORMASI", "Anda menggunakan lokasi palsu. Permintaan anda tidak dapat dilanjutkan.");
        } else {
            lat = gps.getLatitude();
            log = gps.getLongitude();
            String url_faktur = AYHelper.BASE_URL + "api_collector/get_all_faktur";
            Map<String, String> paramsfaktur = new HashMap<>();
            paramsfaktur.put("cust_no", cons_custid);
            paramsfaktur.put("cust_name", getIntent().getStringExtra(Constan.CUST_NAMA));
            paramsfaktur.put("cust_alamat", getIntent().getStringExtra(Constan.CUST_ALAMAT));
            paramsfaktur.put("collector", sesi.getIduser());
            paramsfaktur.put("txt_lat", AYHelper.s(lat));
            paramsfaktur.put("txt_log", AYHelper.s(log));
            paramsfaktur.put("txt_wkt_device", AYHelper.tglJamSql());

            ProgressDialog pd = new ProgressDialog(c);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setInverseBackgroundForced(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Loading....");
            try {
                AYHelper.pre("url : " + url_faktur + ", params: " + paramsfaktur.toString());

                icGetData.get_all_faktur(paramsfaktur).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String hasil = Retro.getString(response);
                        if (hasil != null) {
                            AYHelper.pre("Respon : " + hasil);
                            try {
                                JSONObject json = new JSONObject(hasil);
                                String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    Refresh_voley();
                                } else {
                                    AYHelper.alert_dialog(c, "Informasi", pesan);
                                    Refresh_voley();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AYHelper.alert_dialog(c, "Error", e.getMessage());
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                                AYHelper.alert_dialog(c, "Error Parsing Data", e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        AYHelper.alert_dialog(c, "ERROR", throwable.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                AYHelper.pesan(c, "Error, " + e);
            }
        }

    }

    private void callvolleyfaktur(String hasilnofaktur) {
        selectedStrings.clear();
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);
        //final String nofaktur = hasilnofaktur.replace("/","%20");
        String nofaktur = Base64.encodeToString(hasilnofaktur.getBytes(), Base64.DEFAULT);
        System.out.println(url_select + cons_custid + "/" + cons_rowson + "/" + sesi.getIduser() + "/" + nofaktur);

        icGetData.sett_list_invoice(cons_custid, cons_rowson, sesi.getIduser(), nofaktur).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d(TAG, response.toString());
                // Parsing json
                try {
                    String respon = Retro.getString(response);
                    JSONArray jArray = new JSONArray(respon);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject obj = jArray.getJSONObject(i);
                        DataFaktur item = new DataFaktur();
                        item.setId(obj.getString(TAG_ID));
                        item.setInvoceno(obj.getString(TAG_FAKTURNO));
                        item.setTanda(obj.getInt(TAG_TANDA));
                        item.setNourut(obj.getInt("nourut"));

                        // menambah item ke array
                        itemList.add(item);
                    }
                } catch (JSONException e) {
                    AYHelper.pesan(customer_faktur.this, e.toString());
                }
                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                AYHelper.alert_dialog(c, "ERROR", t.getMessage());
                swipe.setRefreshing(false);
            }
        });
    }

    private void startScan() {
        Intent intent = new Intent(this, Bridge.class);
        Bundle bundle = new Bundle();
        bundle.putString("TitleScan", "Scan No. Faktur");
        bundle.putString("TitleInput", "Input Invoice Manual :");
        bundle.putString("InputHint", "Masukkan No. Faktur");
        bundle.putString("InputHintText", "No. Faktur");
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, INTENT_SCANNING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AYHelper.GPS_SETTING) {
            checkGPS();
        } else if (requestCode == AYHelper.DATE_TIME_SETTING) {
            checkDateTimeSetting();
        } else {
            settingView();
        }
    }

    private void sendto_database(final String stringExtra) {
        GPSTracker gps = new GPSTracker(c);
        if (!gps.canGetLocation() && AYHelper.isGPSActive(c)) {
            dialog_gps_setting = new AlertDialog.Builder(c)
                    .setTitle("GPS tidak aktif.")
                    .setMessage("Silakan aktifkan GPS untuk melanjutkan.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AYHelper.GPS_SETTING);
                    })
                    .create();
            dialog_gps_setting.show();
        } else if (AYHelper.isUsingFakeGPS(c)) {
            AYHelper.alert_dialog(c, "INFORMASI", "Anda menggunakan lokasi palsu. Permintaan anda tidak dapat dilanjutkan.");
        } else {
            lat = gps.getLatitude();
            log = gps.getLongitude();
            String url = AYHelper.BASE_URL + "api_collector/submit_faktur_customer";
            Map<String, String> params = new HashMap<>();
            //params.put("device", AYHelper.getDeviceUUID(c));
            params.put("invoiceno", stringExtra);
            params.put("cust_no", cons_custid);
            params.put("cust_name", cons_custname);
            params.put("cust_alamat", cons_custalamat);
            params.put("collector", sesi.getIduser());
            params.put("txt_lat", AYHelper.s(lat));
            params.put("txt_log", AYHelper.s(log));
            params.put("txt_wkt_device", AYHelper.tglJamSql());

            ProgressDialog pd = new ProgressDialog(c);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setInverseBackgroundForced(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Loading....");
            try {
                AYHelper.pre("url : " + url + ", params: " + params.toString());

                icGetData.submit_faktur_customer(params).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String hasil = Retro.getString(response);
                        if (hasil != null) {
                            AYHelper.pre("Respon : " + hasil);
                            try {
                                JSONObject json = new JSONObject(hasil);
                                String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    Refresh_voley();
                                } else {
                                    AYHelper.alert_dialog(c, "ERROR", pesan);
                                    Refresh_voley();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AYHelper.alert_dialog(c, "Error", e.getMessage());
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                                AYHelper.alert_dialog(c, "Error Parsing Data", e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        AYHelper.alert_dialog(c, "ERROR", throwable.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                AYHelper.pesan(c, "Error, " + e);
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(c, main_menu.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    private void callVolley() {
        selectedStrings.clear();
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        icGetData.sett_list_invoice(cons_custid, cons_rowson, sesi.getIduser(), "").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                // Parsing json
                try {
                    String respon = Retro.getString(response);
                    JSONArray jArray = new JSONArray(respon);
                    fakturcari = new String[jArray.length()];
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject obj = jArray.getJSONObject(i);
                        DataFaktur item = new DataFaktur();
                        item.setId(obj.getString(TAG_ID));
                        item.setInvoceno(obj.getString(TAG_FAKTURNO));
                        item.setTanda(obj.getInt(TAG_TANDA));
                        item.setNourut(obj.getInt("nourut"));

                        // menambah item ke array
                        fakturcari[i] = obj.getString(TAG_FAKTURNO);
                        itemList.add(item);
                    }
                } catch (Exception e) {
                    AYHelper.pesan(customer_faktur.this, e.getMessage());
                }
                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                AYHelper.alert_dialog(c, "ERROR", t.getMessage());
                swipe.setRefreshing(false);
            }
        });
    }
}
