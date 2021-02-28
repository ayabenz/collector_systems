package id.co.datascrip.app_collector_systems.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.data.Data;

/**
 * Created by alamsyah_putra on 3/29/2017.
 */
public class Adapter extends BaseAdapter {
    private final Activity activity;
    private LayoutInflater inflater;
    private final List<Data> items;

    public Adapter(Activity activity, List<Data> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView id = convertView.findViewById(R.id.cust_no);
        TextView rowno = convertView.findViewById(R.id.cust_row);
        TextView nama = convertView.findViewById(R.id.cust_nama);
        TextView alamat = convertView.findViewById(R.id.cust_alamat);

        Data data = items.get(position);

        id.setText(data.getId());
        rowno.setText(data.getRowno());
        nama.setText(data.getId());
        alamat.setText("Nama : " + data.getNama() + ",  Alamat : " + data.getAlamat());

        return convertView;
    }

}
