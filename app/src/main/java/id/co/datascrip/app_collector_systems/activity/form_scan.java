package id.co.datascrip.app_collector_systems.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scandit.barcodepicker.BarcodePicker;
import com.scandit.barcodepicker.OnScanListener;
import com.scandit.barcodepicker.ScanSession;
import com.scandit.barcodepicker.ScanSettings;
import com.scandit.barcodepicker.ScanditLicense;
import com.scandit.recognition.Barcode;

import java.util.Locale;

import id.co.datascrip.app_collector_systems.Function;
import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.custom.dialog_input_manual;
import id.co.datascrip.app_collector_systems.helper.BaseActivity;

public class form_scan extends BaseActivity implements OnScanListener {

    private static final String API_KEY = "9ToLgTNKw2is3mEcnf99yF0mRyLLdVzRNWxCw0KCM58";

    private BarcodePicker mPicker;
    private boolean mDeniedCameraAccess = false;
    private final static int CAMERA_PERMISSION_REQUEST = 5;

    public static String no = "";
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getBundleExtra("bundle");
        initScanner();

        RelativeLayout overlayView = mPicker.getOverlayView();
        final FloatingActionButton floatingActionButton = new FloatingActionButton(this);
        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_create_white_18dp));

        TextView tx_title = new TextView(this);
        tx_title.setText(bundle.getString("TitleScan"));
        TextView tx_input_manual = new TextView(this);
        tx_input_manual.setText(bundle.getString("TitleInput").trim().replace(":", ""));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_input_manual dialog = new dialog_input_manual(form_scan.this, bundle);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (!Function.isEmpty(no)) {
                            Intent intent = new Intent();
                            intent.putExtra(getString(R.string.intent_result_scan), no);
                            setResult(RESULT_OK, intent);
                            no = "";
                            finish();
                        }
                    }
                });
            }
        });

        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rParams.topMargin = 60;
        rParams.rightMargin = 20;

        RelativeLayout.LayoutParams infoParam = new RelativeLayout.LayoutParams(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
        infoParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        infoParam.topMargin = 20;
        infoParam.rightMargin = 10;
        tx_input_manual.setTextColor(ContextCompat.getColor(this, R.color.GREEN));
        tx_title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Felbridge-Light.otf"));

        RelativeLayout.LayoutParams titleParam = new RelativeLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
        titleParam.topMargin = 250;
        titleParam.leftMargin = 50;
        tx_title.setTextSize(24);
        tx_title.setTextColor(ContextCompat.getColor(this, R.color.GREEN));
        tx_title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Felbridge-Bold.otf"));

        overlayView.addView(floatingActionButton, rParams);
        overlayView.addView(tx_input_manual, infoParam);
        overlayView.addView(tx_title, titleParam);
    }

    private ScanSettings getScanSetting() {
        ScanSettings settings = ScanSettings.create();
        for (int simbology : Barcode.ALL_SYMBOLOGIES)
            settings.setSymbologyEnabled(simbology, true);
        return settings;
    }

    private void initScanner() {
        ScanditLicense.setAppKey(API_KEY);
        boolean emulatePortraitMode = !BarcodePicker.canRunPortraitPicker();
        if (emulatePortraitMode) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        mPicker = new BarcodePicker(this, getScanSetting());

        mPicker.setOnScanListener(this);
        mPicker.getOverlayView().setTorchEnabled(true);
        mPicker.switchTorchOn(true);
        setContentView(mPicker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            mDeniedCameraAccess = !(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void didScan(ScanSession scanSession) {
        for (Barcode code : scanSession.getNewlyRecognizedCodes()) {
            String data = code.getData();
            String tipe = code.getSymbologyName().toUpperCase(Locale.US);
            //Toast.makeText(this, "Data : " + data + "; Tipe : " + tipe, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.intent_result_scan), data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void grantCameraPermissions() {
        if (this.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (!mDeniedCameraAccess) {
                this.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST);
            }
        } else {
            mPicker.startScanning();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            grantCameraPermissions();
        } else {
            mPicker.startScanning();
        }
    }

    @Override
    protected void onPause() {
        mPicker.stopScanning();
        super.onPause();
    }

}
