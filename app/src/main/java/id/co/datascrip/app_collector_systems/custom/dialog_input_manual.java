package id.co.datascrip.app_collector_systems.custom;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputLayout;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.activity.form_scan;

public class dialog_input_manual extends AlertDialog {

    private Context context;

    private TextView tx_dialog_title;
    private EditText tx_input_text;
    private TextInputLayout tx_input_hint;
    private Button bt_ok, bt_cancel;
    private Bundle bundle;

    public dialog_input_manual(Context context, Bundle bundle) {
        super(context);
        this.context = context;
        this.bundle = bundle;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_dialog_input_manual, null);

        tx_dialog_title = (TextView) view.findViewById(R.id.tx_dialog_title);
        tx_input_text = (EditText) view.findViewById(R.id.tx_input_text);
        tx_input_hint = (TextInputLayout) view.findViewById(R.id.tx_input_hint);
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        tx_dialog_title.setText(bundle.getString("TitleInput"));
        tx_input_hint.setHint(bundle.getString("InputHint"));
        tx_input_text.setHint(bundle.getString("InputHintText"));
        tx_input_text.setImeActionLabel("Done", KeyEvent.KEYCODE_ENTER);

        tx_input_text.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        tx_input_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String teks_1, teks_2;
                teks_1 = s.toString().toUpperCase();
                if (teks_1.contains("\n")) {
                    teks_2 = teks_1.replace("\n", "").replace(".", "");
                    tx_input_text.setText(teks_2.toUpperCase());
                    bt_ok.performClick();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx_input_text.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tx_input_text.getWindowToken(), 0);
                form_scan.no = "";
                dismiss();
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx_input_text.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tx_input_text.getWindowToken(), 0);
                form_scan.no = tx_input_text.getText().toString().toUpperCase().trim();
                dismiss();
            }
        });

        setView(view);
        show();
    }
}
