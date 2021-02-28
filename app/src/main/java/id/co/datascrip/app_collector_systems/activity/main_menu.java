package id.co.datascrip.app_collector_systems.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.adapter.Adapter;
import id.co.datascrip.app_collector_systems.custom.dialog_cari_customer;
import id.co.datascrip.app_collector_systems.data.Data;
import id.co.datascrip.app_collector_systems.helper.AYHelper;
import id.co.datascrip.app_collector_systems.helper.BaseActivity;
import id.co.datascrip.app_collector_systems.helper.CircularImageView;
import id.co.datascrip.app_collector_systems.helper.Constan;
import id.co.datascrip.app_collector_systems.helper.GPSTracker;
import id.co.datascrip.app_collector_systems.network.Retro;
import id.co.datascrip.app_collector_systems.proses.Proses_Load_Data;
import id.co.datascrip.app_collector_systems.proses.interfaces.IC_Get_Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class main_menu extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_ROWNO = "rowno";
    private static final String TAG = main_menu.class.getSimpleName();
    private static final String url_select = AYHelper.BASE_URL + "api_collector/sett_list_data_ttf/";
    public static String hasildialog = "";
    public static String hasilnamacust = "";
    static String NAME = "nama";
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    Adapter adapter;
    String[] customer;
    private TextView txtnama, txtjabatan;
    private CircularImageView imfoto;
    private EditText editsearch;
    private double lat, log;
    private AlertDialog dialog_gps_setting, dialog_dt_setting;

    private IC_Get_Data icGetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        icGetData = AYHelper.createRetrofit(this).create(IC_Get_Data.class);
        settingView();
        checkGPS();
        checkDateTimeSetting();
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
            //AYHelper.alert(c,"PESAN","onActivityResult");
        }
    }

    private void settingView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        txtnama = headerview.findViewById(R.id.nama_user);
        txtjabatan = headerview.findViewById(R.id.jabatan_user);
        imfoto = headerview.findViewById(R.id.imageView_user);
        String fullnama = sesi.getNama();
        String fulljabatan = sesi.getJabatan();
        String fullphoto = sesi.getPhoto();
        txtnama.setText(Html.fromHtml("<b>" + fullnama + "</b>"));
        txtjabatan.setText(Html.fromHtml("<i>" + fulljabatan + "</i>"));
        String URL_PHOTO = AYHelper.BASE_URL_IMAGE + fullphoto;
        try {
            Picasso.with(this)
                    .load(URL_PHOTO.replace(" ", "%20"))
                    .placeholder(R.drawable.ic_action_person)
                    .error(R.drawable.ic_action_person)
                    .into(imfoto);
        } catch (Exception e) {

        }


        // menghubungkan variablel pada layout dan pada java
        swipe = findViewById(R.id.swipe_refresh_layout);
        list = findViewById(R.id.list);
        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new Adapter(main_menu.this, itemList);
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

        list.setOnItemClickListener((adapterView, view, position, id) -> {
            final String idx = itemList.get(position).getId();
            final String namax = itemList.get(position).getNama();
            final String alamatx = itemList.get(position).getAlamat();
            final String rownox = itemList.get(position).getRowno();
            checkin(idx, namax, alamatx, rownox);
        });

        LoadData();
    }

    private void checkin(String idx, String namax, String alamatx, String rownox) {
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
            final String cust_no = idx;
            final String cust_name = namax;
            final String alamat = alamatx;
            final String rowno = rownox;
            lat = gps.getLatitude();
            log = gps.getLongitude();
            String url_checkin = AYHelper.BASE_URL + "api_collector/CheckIn";
            Map<String, String> params_checkin = new HashMap<>();
            params_checkin.put("collector", sesi.getIduser());
            params_checkin.put("cust_no", cust_no);
            params_checkin.put("cust_name", cust_name);
            params_checkin.put("cust_alamat", alamat);
            params_checkin.put("txt_lat", AYHelper.s(lat));
            params_checkin.put("txt_log", AYHelper.s(log));
            params_checkin.put("txt_wkt_device", AYHelper.tglJamSql());

            ProgressDialog pd = new ProgressDialog(c);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setInverseBackgroundForced(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Loading....");

            try {
                AYHelper.pre("url : " + url_checkin + ", params: " + params_checkin.toString());

                icGetData.CheckIn(params_checkin).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String hasil = Retro.getString(response);
                        if (hasil != null) {
                            try {
                                JSONObject json = new JSONObject(hasil);
                                String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if (result.equalsIgnoreCase("true")) {
                                    Intent i = new Intent(c, customer_faktur.class);
                                    i.putExtra(Constan.CUST_ID, cust_no);
                                    i.putExtra(Constan.CUST_NAMA, cust_name);
                                    i.putExtra(Constan.CUST_ALAMAT, alamat);
                                    i.putExtra(Constan.CUST_ROWNO, rowno);
                                    startActivityForResult(i, Constan.TAKE_PLACE);
                                    finish();
                                } else if (result.equalsIgnoreCase("true_false")) {
                                    Intent i = new Intent(c, customer_faktur.class);
                                    i.putExtra(Constan.CUST_ID, cust_no);
                                    i.putExtra(Constan.CUST_NAMA, cust_name);
                                    i.putExtra(Constan.CUST_ALAMAT, alamat);
                                    i.putExtra(Constan.CUST_ROWNO, rowno);
                                    startActivityForResult(i, Constan.TAKE_PLACE);
                                    finish();
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
                        AYHelper.pesan(c, "Error, " + throwable.getMessage());
                    }
                });
                /*
                query.progress(pd).ajax(url_checkin,params_checkin,String.class, new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String hasil, AjaxStatus status) {
                        if(hasil != null){
                            try {
                                JSONObject json = new JSONObject(hasil);
                                String result = json.getString("result");
                                String pesan = json.getString("msg");
                                if(result.equalsIgnoreCase("true")) {
                                    Intent i = new Intent(c, customer_faktur.class);
                                    i.putExtra(Constan.CUST_ID, cust_no);
                                    i.putExtra(Constan.CUST_NAMA, cust_name);
                                    i.putExtra(Constan.CUST_ALAMAT, alamat);
                                    i.putExtra(Constan.CUST_ROWNO, rowno);
                                    startActivityForResult(i, Constan.TAKE_PLACE);
                                    finish();
                                }else if(result.equalsIgnoreCase("true_false")){
                                    Intent i = new Intent(c, customer_faktur.class);
                                    i.putExtra(Constan.CUST_ID, cust_no);
                                    i.putExtra(Constan.CUST_NAMA, cust_name);
                                    i.putExtra(Constan.CUST_ALAMAT, alamat);
                                    i.putExtra(Constan.CUST_ROWNO, rowno);
                                    startActivityForResult(i, Constan.TAKE_PLACE);
                                    finish();
                                }else{
                                    AYHelper.alert_dialog(c,"PERHATIAN",pesan);
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                                AYHelper.alert_dialog(c, "Error", e.getMessage());
                            } catch (Exception e){
                                e.getLocalizedMessage();
                                AYHelper.alert_dialog(c, "Error Parsing Data", e.getMessage());
                            }
                        }

                    }
                });
                */
            } catch (Exception e) {
                AYHelper.alert_dialog(c, "ERROR", e.getMessage());
            }
        }
    }

    private void LoadData() {
        final ProgressDialog progressDialog = new ProgressDialog(c);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        //progressDialog.show();
        new Proses_Load_Data(this, isSuccess -> {
            if (isSuccess) {
                progressDialog.dismiss();
            } else new AlertDialog.Builder(main_menu.this)
                    .setMessage("Tidak dapat mendapatkan data dari Server.\nSilakan coba lagi.")
                    .setPositiveButton("Coba Lagi", (dialogInterface, i) -> LoadData())
                    .create()
                    .show();
        });
    }


    private void callvolleycust(String hasilnamacust) {
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);
        // membuat request JSON
        String base64 = Base64.encodeToString(hasilnamacust.getBytes(), Base64.DEFAULT);
        System.out.println(url_select + sesi.getIduser() + "/" + base64);

        icGetData.sett_list_data_ttf(sesi.getIduser(), base64).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d(TAG, response.toString());
                try {
                    String respon = Retro.getString(response);
                    JSONArray jArray = new JSONArray(respon);
                    if (jArray.length() > 0) {
                        // Parsing json
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject obj = jArray.getJSONObject(i);

                            Data item = new Data();
                            item.setId(obj.getString(TAG_ID));
                            item.setNama(obj.getString(TAG_NAMA));
                            item.setAlamat(obj.getString(TAG_ALAMAT));
                            item.setRowno(obj.getString(TAG_ROWNO));

                            // menambah item ke array
                            itemList.add(item);
                        }
                    } else {
                        AYHelper.pre("Data Kosong...");
                    }
                } catch (Exception e) {
                    AYHelper.pesan(main_menu.this, e.toString());
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                AYHelper.alert_dialog(main_menu.this, "ERROR", t.getMessage());
                swipe.setRefreshing(false);
            }
        });
    }

    private void callVolley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        icGetData.sett_list_data_ttf(sesi.getIduser(), "").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d(TAG, response.toString());
                String respon = Retro.getString(response);
                try {
                    JSONArray jArray = new JSONArray(respon);
                    customer = new String[jArray.length()];
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject obj = jArray.getJSONObject(i);
                        Data item = new Data();
                        item.setId(obj.getString(TAG_ID));
                        item.setNama(obj.getString(TAG_NAMA));
                        item.setAlamat(obj.getString(TAG_ALAMAT));
                        item.setRowno(obj.getString(TAG_ROWNO));
                        // menambah item ke array
                        customer[i] = obj.getString(TAG_NAMA);
                        itemList.add(item);
                    }
                } catch (Exception e) {
                    Log.e("json", e.toString());
                }
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                AYHelper.alert_dialog(main_menu.this, "ERROR", t.getMessage());
                swipe.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda Akan Keluar Applikasi ?");
        builder.setPositiveButton("Batal", (dialog, arg1) -> {
            // TODO Auto-generated method stub
            dialog.dismiss();
        });
        builder.setNegativeButton("Lanjutkan", (arg0, arg1) -> {
            // TODO Auto-generated method stub

            sesi.logout();
            finish();
        });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_history) {
            Intent i = new Intent(c, history_collector.class);
            startActivity(i);
            finish();
            return true;
        } else if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Anda Akan Keluar Applikasi ?");
            builder.setPositiveButton("Batal", (dialog, arg1) -> {
                // TODO Auto-generated method stub
                dialog.dismiss();
            });
            builder.setNegativeButton("Lanjutkan", (arg0, arg1) -> {
                // TODO Auto-generated method stub
                sesi.logout();
                finish();
            });
            builder.create().show();
            return true;
        } else if (id == R.id.action_cari_cust) {
            if (customer != null && customer.length > 0) {
                Bundle bundle_customer = new Bundle();
                bundle_customer.putString("TitleInput", "Cari Nama Customer");
                bundle_customer.putStringArray("arraycust", customer);
                dialog_cari_customer dialog = new dialog_cari_customer(main_menu.this, bundle_customer);
                dialog.setOnDismissListener(dialog1 -> {
                    if (hasildialog.equalsIgnoreCase("true")) {
                        //System.out.println(hasilnamacust.toString());
                        callvolleycust(hasilnamacust);
                    }
                });
            } else {
                AYHelper.alert_dialog(c, "PERHATIAN", "Data List Faktur Masih Kosong.");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            Intent i = new Intent(c, history_collector.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Anda Akan Keluar Applikasi ?");
            builder.setPositiveButton("Batal", (dialog, arg1) -> {
                // TODO Auto-generated method stub
                dialog.dismiss();
            });
            builder.setNegativeButton("Lanjutkan", (arg0, arg1) -> {
                // TODO Auto-generated method stub
                sesi.logout();
                finish();
            });
            builder.create().show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void refresh_voley() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    private void checkDateTimeSetting() {
        if (dialog_dt_setting != null)
            dialog_dt_setting.dismiss();
        if (!AYHelper.isAutoTime(this)) {
            dialog_dt_setting = new AlertDialog.Builder(this)
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

    public void checkGPS() {
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


}
