package id.co.datascrip.app_collector_systems.fragmet;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.datascrip.app_collector_systems.R;
import id.co.datascrip.app_collector_systems.SessionManager;
import id.co.datascrip.app_collector_systems.data.DataBerhasil;
import id.co.datascrip.app_collector_systems.helper.AYHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class berhasil extends Fragment {
    private ListView lvData;
    private ArrayList<DataBerhasil> data;
    //private AQuery query;
    private SessionManager sesi;
    private CustomAdapter adapter;

    public berhasil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_berhasil, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        lvData = (ListView) view.findViewById(R.id.list_berhasil);
        //query = new AQuery(getActivity());
        sesi = new SessionManager(getActivity());
        data = new ArrayList<>();


        if (!AYHelper.isOnline(getActivity())) {
            AYHelper.alertMessageNoInternet(getActivity());
        } else {
            getData();
        }
    }

    private void getData() {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            AYHelper.IC_History icHistory = AYHelper.createRetrofit(getActivity()).create(AYHelper.IC_History.class);
            icHistory.getHistory(sesi.getIduser(), "Berhasil").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String hasil = response.body().string();
                        //AYHelper.pre("Respon : " + hasil);
                        JSONObject json = new JSONObject(hasil);
                        String result = json.getString("result");
                        String pesan = json.getString("msg");
                        if (result.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = json.getJSONArray("data");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);
                                DataBerhasil b = new DataBerhasil();
                                b.setId(object.getString("id"));
                                b.setFakturno(object.getString("no_inv"));
                                b.setCust_no(object.getString("customer_no"));
                                b.setCust_name(object.getString("cust_name"));
                                b.setHasildesc(object.getString("hasil_desc"));
                                b.setHasilkunjung(object.getString("hasil_kunjungan"));
                                b.setJampickup(object.getString("pickup"));
                                b.setJammulai(object.getString("mulai"));
                                b.setJamselesai(object.getString("complete_date"));
                                //memasukkan data kedalam model booking
                                data.add(b);
                                //measukkan data arraylist kedalam custom adapter
                                adapter = new CustomAdapter(getActivity(), data);
                                lvData.setAdapter(adapter);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        AYHelper.alert_dialog(getActivity(), "Perhatian", e.getMessage());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    AYHelper.alert_dialog(getActivity(), "Perhatian", throwable.getMessage());
                    progressDialog.dismiss();
                }
            });
        } catch (
                Exception e) {
            e.printStackTrace();
            AYHelper.alert_dialog(getActivity(), "Perhatian", e.getMessage());
        }
    }

    private class CustomAdapter extends BaseAdapter {
        private Context c;
        private ArrayList<DataBerhasil> datas;
        private LayoutInflater inflater = null;

        public CustomAdapter(FragmentActivity activity, ArrayList<DataBerhasil> data) {
            this.c = activity;
            this.datas = data;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.list_row_berhasil, null);
            TextView txt_id = (TextView) v.findViewById(R.id.txt_b_id);
            TextView txt_inv = (TextView) v.findViewById(R.id.txt_b_invoice_no);
            TextView txt_mulai = (TextView) v.findViewById(R.id.txt_b_mulai);
            TextView txt_selesai = (TextView) v.findViewById(R.id.txt_b_complete);
            TextView txt_hasil = (TextView) v.findViewById(R.id.txt_b_hasil);

            DataBerhasil b = datas.get(position);
            txt_id.setText(b.getId());
            txt_inv.setText(b.getFakturno());
            txt_mulai.setText("Mulai : " + AYHelper.tglJamToInd(b.getJampickup()));
            txt_selesai.setText("Selesai : " + AYHelper.tglJamToInd(b.getJamselesai()));
            txt_hasil.setText(b.getCust_name() + ", " + b.getHasildesc() + ", " + b.getHasilkunjung());

            return v;
        }
    }

}
