package id.co.datascrip.app_collector_systems.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.rengwuxian.materialedittext.MaterialEditText;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.SessionManager;
import id.co.datascrip.app_collector_systems.helper.BaseActivity;

public class Bridge extends BaseActivity {
    public static final int INTENT_SCANNING = 1000;
    MaterialEditText tx_no, tx_scan_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (new SessionManager(this).getTipeGadget() == SessionManager.TIPE_HANDPHONE) {
            Intent intent = new Intent(this, form_scan.class);
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, INTENT_SCANNING);
            //finish();
        } else {
            final MaterialEditText tx_no = findViewById(R.id.tx_no);
            MaterialEditText tx_scan_title = findViewById(R.id.tx_scan_title);
            Toolbar toolbar = findViewById(R.id.toolbar);
            String title = bundle.getString("TitleScan");
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            tx_no.setHint(bundle.getString("InputHintText"));
            tx_scan_title.setText(bundle.getString("TitleScan"));

            tx_no.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String teks_1, teks_2;
                    teks_1 = s.toString();
                    if (teks_1.contains("\n")) {
                        teks_2 = teks_1.replace("\n", "").replace(".", "");
                        tx_no.setText(teks_2);
                        Intent intent = new Intent();
                        intent.putExtra(getString(R.string.intent_result_scan), tx_no.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_SCANNING && resultCode == RESULT_OK && data != null) {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.intent_result_scan), data.getStringExtra(getString(R.string.intent_result_scan)));
            setResult(RESULT_OK, intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

}
