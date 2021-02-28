package id.co.datascrip.app_collector_systems.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.activity.customer_faktur;
import id.co.datascrip.app_collector_systems.data.DataFaktur;

/**
 * Created by alamsyah_putra on 3/30/2017.
 */
public class AdapterFaktur extends BaseAdapter {
    private final Activity activity;
    private LayoutInflater inflater;
    private final List<DataFaktur> items;

    public AdapterFaktur(Activity activity, List<DataFaktur> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_invoice, null);

        final TextView id_kunjungan = convertView.findViewById(R.id.id_kunjungan);
        TextView invoice_no = convertView.findViewById(R.id.invoice_no);
        TextView no_urut = convertView.findViewById(R.id.id_no_urut);
        CheckBox cbSelected = convertView.findViewById(R.id.cbBox);

        DataFaktur data = items.get(position);

        id_kunjungan.setText(data.getId());

        no_urut.setText(data.getNourut() + ".");
        no_urut.setTextColor(Color.parseColor("#000000"));

        invoice_no.setText(data.getInvoceno());

        cbSelected.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                customer_faktur.selectedStrings.add(id_kunjungan.getText().toString());
            } else {
                customer_faktur.selectedStrings.remove(id_kunjungan.getText().toString());
            }
        });

        if (data.getTanda() == 1) {
            invoice_no.setTextColor(Color.parseColor("#04d119"));
        } else if (data.getTanda() == 2) {
            invoice_no.setTextColor(Color.parseColor("#e01f09"));
        } else {
            invoice_no.setTextColor(Color.parseColor("#000000"));
        }

        return convertView;
    }

}
