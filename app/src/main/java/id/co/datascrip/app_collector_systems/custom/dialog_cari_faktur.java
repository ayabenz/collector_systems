package id.co.datascrip.app_collector_systems.custom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.activity.customer_faktur;
import id.co.datascrip.app_collector_systems.activity.main_menu;

public class dialog_cari_faktur extends AlertDialog {

    private final Context context;

    private TextView tx_dialog_title;
    private AutoCompleteTextView tx_input_text;
    private Button bt_ok, bt_cancel;
    private final Bundle bundle;

    public dialog_cari_faktur(Context context, Bundle bundle) {
        super(context);
        this.context = context;
        this.bundle = bundle;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_dialog_cari_faktur, null);
        tx_dialog_title = view.findViewById(R.id.tx_dialog_title);
        tx_input_text = view.findViewById(R.id.ac_faktur_no);
        bt_ok = view.findViewById(R.id.bt_ok);
        bt_cancel = view.findViewById(R.id.bt_cancel);

        tx_dialog_title.setText(bundle.getString("TitleInput"));
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, bundle.getStringArray("arrayfaktur"));
        tx_input_text.setAdapter(adapter);
        tx_input_text.setThreshold(1);

        bt_cancel.setOnClickListener(v -> {
            main_menu.hasildialog = "false";
            dismiss();
        });

        bt_ok.setOnClickListener(v -> {
            if (tx_input_text.length() > 0) {
                customer_faktur.hasilcarifaktur = "true";
                customer_faktur.hasilnofaktur = tx_input_text.getText().toString();
                dismiss();
            } else {
                tx_input_text.setError("Customer Name Harus Di Isi.");
                tx_input_text.requestFocus();
            }
        });

        setView(view);
        show();
    }


}
