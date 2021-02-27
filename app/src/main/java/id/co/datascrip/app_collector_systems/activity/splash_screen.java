package id.co.datascrip.app_collector_systems.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.helper.AYHelper;
import id.co.datascrip.app_collector_systems.helper.BaseActivity;
import id.co.datascrip.app_collector_systems.proses.Proses_Get_Data;

public class splash_screen extends BaseActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Proses_Get_Data(this, false).getBlacklistApp();
        final Handler handler = new Handler();

        TextView versi_text = (TextView)findViewById(R.id.text_version);
        versi_text.setText("Version : "+ AYHelper.GetVersion().toString());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!sesi.isLogin()) {
                    startActivity(new Intent(c, login.class));
                    finish();
                } else {
                    startActivity(new Intent(c, main_menu.class));
                        finish();
                }
            }
        },2000L);
    }
}
