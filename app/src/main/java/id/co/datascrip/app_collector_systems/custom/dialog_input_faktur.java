package id.co.datascrip.app_collector_systems.custom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.activity.customer_faktur;
import id.co.datascrip.app_collector_systems.adapter.AdapterReason;
import id.co.datascrip.app_collector_systems.helper.AYHelper;
import id.co.datascrip.app_collector_systems.sqllite.ReasonSQLite;

public class dialog_input_faktur extends AlertDialog {

    private Context context;
    private Spinner sp_alasan;
    private AdapterReason adapter_reason;
    private TextView tx_dialog_title,tx_dialog_invoice_no,tx_dialog_id;
    private MaterialEditText tx_dialog_jml_bayar,tx_dialog_desc;
    private String kode_alasan = "";
    private String desc_alasan = "";
    private String tipe_alasan = "";
    private String tipe_code_alasan = "";
    private Button bt_ok, bt_cancel;
    private Bundle bundle;

    public dialog_input_faktur(Context context, Bundle bundle) {
        super(context);
        this.context = context;
        this.bundle = bundle;
        init();
    }

    private void init(){
        View view = LayoutInflater.from(context).inflate(R.layout.activity_dialog_input_faktur, null);

        sp_alasan = (Spinner) view.findViewById(R.id.sp_alasan);
        tx_dialog_title = (TextView) view.findViewById(R.id.tx_dialog_title);
        tx_dialog_invoice_no = (TextView) view.findViewById(R.id.tx_dialog_invoice_no);
        tx_dialog_id = (TextView) view.findViewById(R.id.tx_dialog_id);
        tx_dialog_jml_bayar = (MaterialEditText) view.findViewById(R.id.tx_dialog_jumlah_bayar);
        tx_dialog_desc = (MaterialEditText) view.findViewById(R.id.tx_dialog_desc);
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        tx_dialog_title.setText(bundle.getString("TitleScan"));
        tx_dialog_id.setText(bundle.getString("id"));
        tx_dialog_invoice_no.setText(bundle.getString("inv"));

        adapter_reason = new AdapterReason(context,R.layout.adapter_reason,new ReasonSQLite(context).getAll());
        sp_alasan.setAdapter(adapter_reason);
        sp_alasan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kode_alasan = adapter_reason.getItem(position).getCode();
                desc_alasan = adapter_reason.getItem(position).getDescription();
                tipe_alasan = adapter_reason.getItem(position).getTipe();
                tipe_code_alasan = adapter_reason.getItem(position).getTipe_code();
                if(position == 6){
                    //tipe_alasan = "Berhasil";
                    tx_dialog_jml_bayar.setVisibility(View.VISIBLE);
                    tx_dialog_desc.setVisibility(View.VISIBLE);
                }else{
                    /*
                    if((position == 5)||(position == 7)||(position == 8)){
                        tipe_alasan = "Berhasil";
                    }else{
                        tipe_alasan = "Gagal";
                    }
                    */
                    tx_dialog_desc.setVisibility(View.VISIBLE);
                    tx_dialog_jml_bayar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customer_faktur.hasildialog = "false";
                customer_faktur.selesai = new HashMap<String, String>();
                dismiss();
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kode_alasan.equalsIgnoreCase("8") && AYHelper.isEmpty(tx_dialog_jml_bayar)) {
                    tx_dialog_jml_bayar.setError("Untuk Tipe Berhasil Terima Pembayaran, Kolom Jumlah Bayar Harus Di Isi.");
                    tx_dialog_jml_bayar.requestFocus();
                }else if(AYHelper.isEmpty(tx_dialog_desc)){
                    tx_dialog_desc.setError("Kolom Keterangan Harus Di Isi.");
                    tx_dialog_desc.requestFocus();
                }else {
                    customer_faktur.hasildialog = "true";
                    customer_faktur.selesai.put("id", tx_dialog_id.getText().toString());
                    customer_faktur.selesai.put("invoiceno", tx_dialog_invoice_no.getText().toString());
                    customer_faktur.selesai.put("kode_alasan", kode_alasan.toString());
                    customer_faktur.selesai.put("desc_alasan", desc_alasan.toString());
                    customer_faktur.selesai.put("tipe", tipe_alasan.toString());
                    customer_faktur.selesai.put("jml_bayar", tx_dialog_jml_bayar.getText().toString());
                    customer_faktur.selesai.put("ket", tx_dialog_desc.getText().toString());
                    dismiss();
                }
            }
        });

        setView(view);
        show();
    }

}
