package id.co.datascrip.app_collector_systems.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.greysonparrelli.permiso.Permiso;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.helper.AYHelper;
import id.co.datascrip.app_collector_systems.helper.BaseActivity;
import id.co.datascrip.app_collector_systems.proses.Proses_Get_Data;

public class splash_screen extends BaseActivity {

    private static final int REQUEST_OPEN_SETTING = 1001;

    private static final String[] list_permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final String[] list_permissions_string = {"LOCATION", "STORAGE", "CAMERA"};
    final String DIALOG_MESSAGE_RATIONALE = "Aplikasi membutuhkan permission : \n[PERMISSIONS]\n\nKlik OK untuk melanjutkan.";
    final String DIALOG_MESSAGE_DENIED = "Aplikasi membutuhkan permission : \n[PERMISSIONS]\n\nKlik tombol di bawah untuk melanjutkan, lalu pilih Permission";
    final String DIALOG_TITLE = "Access Permission Required";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Permiso.getInstance().setActivity(this);
        new Proses_Get_Data(this, false).getBlacklistApp();
        final Handler handler = new Handler();

        TextView versi_text = findViewById(R.id.text_version);
        versi_text.setText("Version : " + AYHelper.GetVersion());

        //handler.postDelayed(() -> {
        //}, 2000L);
        checkPermission();
    }



    private void checkPermission() {
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {
                    if (!sesi.isLogin()) {
                        startActivity(new Intent(c, login.class));
                    } else {
                        startActivity(new Intent(c, main_menu.class));
                    }
                    finish();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showAlertDialog(DIALOG_MESSAGE_DENIED.replace("[PERMISSIONS]", getPermissionsDenied()));
                        }
                    }
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(DIALOG_TITLE, DIALOG_MESSAGE_RATIONALE.replace("[PERMISSIONS]", getPermissionsDenied()), null, callback);
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    private void showAlertDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(DIALOG_TITLE)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Aktifkan Permission", (dialog, which) -> openAppSetting())
                .create()
                .show();
    }

    private void openAppSetting() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(i, REQUEST_OPEN_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private String getPermissionsDenied() {
        StringBuilder list_permission_required = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            for (int i = 0; i < list_permissions.length; i++) {
                if (checkSelfPermission(list_permissions[i]) != PackageManager.PERMISSION_GRANTED)
                    list_permission_required.append("\n" + "- ").append(list_permissions_string[i]);
            }
        return list_permission_required.toString();
    }
}
