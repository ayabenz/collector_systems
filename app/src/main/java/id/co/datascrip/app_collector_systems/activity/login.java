package id.co.datascrip.app_collector_systems.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.SessionManager;
import id.co.datascrip.app_collector_systems.helper.AYHelper;
import id.co.datascrip.app_collector_systems.helper.BaseActivity;
import id.co.datascrip.app_collector_systems.helper.GPSTracker;
import id.co.datascrip.app_collector_systems.network.Retro;
import id.co.datascrip.app_collector_systems.proses.interfaces.IC_Get_Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends BaseActivity {
    private MaterialEditText textsername, textpasswd;
    Button btn_login;
    private double lat, log;
    private AlertDialog dialog_gps_setting, dialog_dt_setting;

    private static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    private static final int REQUEST_ALL_PERMISSION = 1234;

    final String DIALOG_MESSAGE_RATIONALE = "Aplikasi membutuhkan semua permission.\nKlik OK untuk melanjutkan.";
    final String DIALOG_MESSAGE_DENIED = "Aplikasi membutuhkan semua permission.\n\nKlik tombol di bawah untuk melanjutkan, lalu pilih Permission";
    private boolean alreadyAsk, isDialogShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settingView();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (checkAndRequestPermission()) {
                initLocation();
            }
        } else {
            initLocation();
        }

    }

    private boolean checkAndRequestPermission() {
        int permissionFineLocation = ActivityCompat.checkSelfPermission(this, PERMISSION_LOCATION);
        int permissionWriteStorage = ActivityCompat.checkSelfPermission(this, PERMISSION_STORAGE);
        int permissionCamera = ActivityCompat.checkSelfPermission(this, PERMISSION_CAMERA);
        final ArrayList<String> listPermissionNeeded = new ArrayList<>();
        if (permissionFineLocation != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(PERMISSION_LOCATION);
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(PERMISSION_STORAGE);
        if (permissionCamera != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(PERMISSION_CAMERA);

        if (!listPermissionNeeded.isEmpty()) {
            if (!alreadyAsk) {
                new AlertDialog.Builder(this)
                        .setMessage("Aplikasi memerlukan permission untuk dapat berjalan.\nSilakan klik OK untuk melanjutkan.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(login.this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), REQUEST_ALL_PERMISSION);
                            }
                        })
                        .setCancelable(false)
                        .show();
                alreadyAsk = true;
            } else
                ActivityCompat.requestPermissions(login.this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), REQUEST_ALL_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ALL_PERMISSION:

                Map<String, Integer> permission = new HashMap<>();
                permission.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                permission.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                permission.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                permission.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                permission.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                permission.put(Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        permission.put(permissions[i], grantResults[i]);

                    if (permission.get(PERMISSION_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(PERMISSION_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                        initLocation();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_CAMERA)) {
                            new AlertDialog.Builder(this)
                                    .setMessage(DIALOG_MESSAGE_RATIONALE)
                                    .setPositiveButton("Coba Lagi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            checkAndRequestPermission();
                                        }
                                    })
                                    .setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                        /*
                        else {
                            new AlertDialog.Builder(this)
                                    .setMessage(DIALOG_MESSAGE_DENIED)
                                    .setPositiveButton("Aktifkan Permission", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            openAppSetting();
                                        }
                                    })
                                    .setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                        */
                    }
                }
                break;
        }
    }

    private void initLocation() {
        checkGPS();
        checkDateTimeSetting();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), AYHelper.GPS_SETTING);
                        }
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
                    .setMessage("Silakan aktifkan Automatic Date/Time pada Setting -> Date & Time.")
                    .setCancelable(false)
                    .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_dt_setting.dismiss();
                            startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), AYHelper.DATE_TIME_SETTING);
                        }
                    })
                    .create();
            dialog_dt_setting.show();
        }
    }

    private void settingView() {
        //AYHelper.pesan(c, AYHelper.s(lat)+" / "+AYHelper.s(log));
        TextView versi_text = (TextView) findViewById(R.id.version_login);
        textsername = (MaterialEditText) findViewById(R.id.username_login);
        textpasswd = (MaterialEditText) findViewById(R.id.passwd_login);
        btn_login = (Button) findViewById(R.id.btn_login);

        versi_text.setText("Version : " + AYHelper.GetVersion().toString());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(btnAnimasi);
                loginvalidate();
            }
        });
    }

    private void loginvalidate() {
        textsername.setError(null);
        textpasswd.setError(null);

        boolean cancel = false;
        View focusView = null;
        if (AYHelper.isEmpty(textsername)) {
            textsername.setError("User Name Harus di isi.");
            focusView = textsername;
            cancel = true;
        } else if (AYHelper.isEmpty(textpasswd)) {
            textpasswd.setError("Password Harus di isi.");
            focusView = textpasswd;
            cancel = true;
        }

        if (AYHelper.isOnline(this)) {
            GPSTracker gps = new GPSTracker(c);
            if (!gps.canGetLocation() && !AYHelper.isGPSActive(c)) {
                AYHelper.showSettingGps(c);
                finish();
            } else if (AYHelper.isUsingFakeGPS(c)) {
                AYHelper.alert_dialog(c, "INFORMASI", "Anda menggunakan lokasi palsu. Permintaan anda tidak dapat dilanjutkan.");
            } else {
                lat = gps.getLatitude();
                log = gps.getLongitude();
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    String url = AYHelper.BASE_URL + "api_collector/login";
                    Map<String, String> params = new HashMap<>();
                    params.put("txt_username", textsername.getText().toString());
                    params.put("txt_passwd", textpasswd.getText().toString());
                    params.put("txt_lat", AYHelper.s(lat));
                    params.put("txt_log", AYHelper.s(log));
                    params.put("txt_sts", "1");
                    params.put("txt_version", AYHelper.GetVersion().toString());

                    ProgressDialog pd = new ProgressDialog(c);
                    pd.setIndeterminate(true);
                    pd.setCancelable(false);
                    pd.setInverseBackgroundForced(false);
                    pd.setCanceledOnTouchOutside(false);
                    pd.setMessage("Loading....");


                    try {
                        //AYHelper.pre("url : " + url + ", params: "+params.toString());
                        IC_Get_Data ic_get_data = AYHelper.createRetrofit(this).create(IC_Get_Data.class);
                        ic_get_data.login(params).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                String hasil = Retro.getString(response);

                                if (hasil != null) {
                                    AYHelper.pre("Respon : " + hasil);
                                    try {
                                        JSONObject json = new JSONObject(hasil);
                                        String result = json.getString("result");
                                        String pesan = json.getString("msg");
                                        //AYHelper.pesan(c, pesan);
                                        if (result.equalsIgnoreCase("true")) {
                                            //membuat Session
                                            String token = json.getString("token");
                                            SessionManager sessionManager = new SessionManager(c);
                                            sessionManager.createLoginSession(token);
                                            JSONObject obj = json.getJSONObject("data");
                                            sessionManager.setNama(obj.getString("user_full_name"));
                                            sessionManager.setPhoto(obj.getString("user_photo"));
                                            sessionManager.setJabatan(obj.getString("user_title"));
                                            sessionManager.setIduser(obj.getString("user_no"));

                                            Intent i = new Intent(c, main_menu.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            AYHelper.alert_dialog(c, "Informasi", pesan);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        AYHelper.pesan(c, "Error, " + e.getMessage());
                                    } catch (Exception e) {
                                        e.getLocalizedMessage();
                                        AYHelper.pesan(c, "Error Parsing Data : " + e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                AYHelper.pesan(c, "Error, " + throwable.getMessage());
                            }
                        });

                        /*
                        query.progress(pd).ajax(url, params, String.class, new AjaxCallback<String>() {
                            @Override
                            public void callback(String url, String hasil, AjaxStatus status) {
                                if (hasil != null) {
                                    AYHelper.pre("Respon : " + hasil);
                                    try {
                                        JSONObject json = new JSONObject(hasil);
                                        String result = json.getString("result");
                                        String pesan = json.getString("msg");
                                        //AYHelper.pesan(c, pesan);
                                        if (result.equalsIgnoreCase("true")) {
                                            //membuat Session
                                            String token = json.getString("token");
                                            SessionManager sessionManager = new SessionManager(c);
                                            sessionManager.createLoginSession(token);
                                            JSONObject obj = json.getJSONObject("data");
                                            sessionManager.setNama(obj.getString("user_full_name"));
                                            sessionManager.setPhoto(obj.getString("user_photo"));
                                            sessionManager.setJabatan(obj.getString("user_title"));
                                            sessionManager.setIduser(obj.getString("user_no"));

                                            Intent i = new Intent(c, main_menu.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            AYHelper.alert_dialog(c, "Informasi", pesan);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        AYHelper.pesan(c, "Error, " + e.getMessage());
                                    } catch (Exception e) {
                                        e.getLocalizedMessage();
                                        AYHelper.pesan(c, "Error Parsing Data : " + e.getMessage());
                                    }

                                }
                            }
                        });
                        */
                    } catch (Exception e) {
                        e.printStackTrace();
                        AYHelper.pesan(c, "Error, Get Data : " + e.getMessage());
                    }
                }
            }
        } else {
            AYHelper.alertMessageNoInternet(c);
        }


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Applikasi Akan Ditutup ?");
        builder.setPositiveButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Lanjutkan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        builder.create().show();
    }
}
