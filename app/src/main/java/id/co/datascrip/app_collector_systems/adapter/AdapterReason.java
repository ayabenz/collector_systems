package id.co.datascrip.app_collector_systems.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.data.DataReason;

/**
 * Created by alamsyah_putra on 4/3/2017.
 */
public class AdapterReason extends ArrayAdapter<DataReason> {
    private Context context;
    private ArrayList<DataReason> list_reason;
    private int selectedPosition = 0;
    private LayoutInflater inflater;

    public AdapterReason(Context context, int resource, ArrayList<DataReason> list_reason) {
        super(context, resource, list_reason);
        this.context = context;
        this.list_reason = list_reason;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private class ViewHolder {
        public TextView tx_code, tx_description;
        public LinearLayout layout_alasan;
    }

    @Override
    public DataReason getItem(int position) {
        return list_reason.get(position);
    }

    @Override
    public int getPosition(DataReason item) {
        int position = -1;
        for (int i = 0; i < list_reason.size(); i++) {
            if (list_reason.get(i).getID() == item.getID())
                return i;
        }
        return position;
    }

    public int getPosition(String code) {
        if (code == null)
            return -1;
        int position = -1;
        for (int i = 0; i < list_reason.size(); i++) {
            if (list_reason.get(i).getCode().toLowerCase().trim().equals(code.toLowerCase().trim())) {
                position = i;
                break;
            }
        }
        return position;
    }

    public void setPosition(int position) {
        this.selectedPosition = position;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.adapter_reason, null);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tx_code = (TextView) view.findViewById(R.id.tx_code);
        viewHolder.tx_description = (TextView) view.findViewById(R.id.tx_description);
        viewHolder.layout_alasan = (LinearLayout) view.findViewById(R.id.layout_alasan);

        DataReason reason = list_reason.get(position);
        viewHolder.tx_code.setText(reason.getCode());
        viewHolder.tx_description.setText(reason.getDescription());

        if (position == selectedPosition)
            viewHolder.layout_alasan.setBackgroundColor(ContextCompat.getColor(context, R.color.item_selected));
        else    viewHolder.layout_alasan.setBackgroundColor(ContextCompat.getColor(context, R.color.zxing_transparent));

        return view;
    }
}
