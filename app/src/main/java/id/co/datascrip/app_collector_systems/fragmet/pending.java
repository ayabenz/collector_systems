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
import id.co.datascrip.app_collector_systems.data.DataPending;
import id.co.datascrip.app_collector_systems.helper.AYHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class pending extends Fragment {
    private ListView lvData;
    private ArrayList<DataPending> data;
    //private AQuery query;
    private SessionManager sesi;
    private CustomAdapter adapter;


    public pending() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        lvData = (ListView)view.findViewById(R.id.list_pending);
        //query = new AQuery(getActivity());
        sesi = new SessionManager(getActivity());
        data = new ArrayList<>();

        if (!AYHelper.isOnline(getActivity())){
            AYHelper.alertMessageNoInternet(getActivity());
        }
        else{
            getData();
        }
    }

    /*
    private void getData() {
        String url = AYHelper.BASE_URL + "api_collector/get_data_history";
        Map<String, String> param = new HashMap<>();
        param.put("colector", sesi.getIduser());
        param.put("tipe", "kosong");
        try {
            AYHelper.pre("Url : " + url + ", params " + param.toString());
            query.ajax(url, param, String.class,new AjaxCallback<String>(){
                @Override
                public void callback(String url, String hasil, AjaxStatus status) {
                    if (hasil != null) {
                        AYHelper.pre("Respon : " + hasil);
                        try {
                            JSONObject json = new JSONObject(hasil);
                            String result = json.getString("result");
                            String pesan = json.getString("msg");
                            if(result.equalsIgnoreCase("true")){
                                JSONArray jsonArray = json.getJSONArray("data");
                                for(int a=0; a<jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    DataPending b = new DataPending();
                                    b.setId(object.getString("id"));
                                    b.setFakturno(object.getString("no_inv"));
                                    b.setCust_no(object.getString("customer_no"));
                                    b.setCust_name(object.getString("cust_name"));
                                    b.setAlamat(object.getString("cust_address"));
                                    b.setHasildesc(object.getString("hasil_desc"));
                                    b.setHasilkunjung(object.getString("hasil_kunjungan"));
                                    b.setJampickup(object.getString("pickup"));
                                    b.setJammulai(object.getString("mulai"));
                                    b.setJamselesai(object.getString("selesai"));
                                    //memasukkan data kedalam model booking
                                    data.add(b);
                                    //measukkan data arraylist kedalam custom adapter
                                    adapter = new CustomAdapter(getActivity(), data);
                                    lvData.setAdapter(adapter);
                                }
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            AYHelper.alert_dialog(getActivity(), "Pehatian",e.getMessage());
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            AYHelper.alert_dialog(getActivity(), "Pehatian",e.getMessage());
        }

    }
    */

    private void getData() {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            AYHelper.IC_History icHistory = AYHelper.createRetrofit(getActivity()).create(AYHelper.IC_History.class);
            icHistory.getHistory(sesi.getIduser(), "Gagal").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String hasil = response.body().string();
                        JSONObject json = new JSONObject(hasil);
                        String result = json.getString("result");
                        String pesan = json.getString("msg");
                        if(result.equalsIgnoreCase("true")){
                            JSONArray jsonArray = json.getJSONArray("data");
                            for(int a=0; a<jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);
                                DataPending b = new DataPending();
                                b.setId(object.getString("id"));
                                b.setFakturno(object.getString("no_inv"));
                                b.setCust_no(object.getString("customer_no"));
                                b.setCust_name(object.getString("cust_name"));
                                b.setAlamat(object.getString("cust_address"));
                                b.setHasildesc(object.getString("hasil_desc"));
                                b.setHasilkunjung(object.getString("hasil_kunjungan"));
                                b.setJampickup(object.getString("pickup"));
                                b.setJammulai(object.getString("mulai"));
                                b.setJamselesai(object.getString("selesai"));
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
        private ArrayList<DataPending> datas;
        private LayoutInflater inflater = null;

        public CustomAdapter(FragmentActivity activity, ArrayList<DataPending> data) {
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
            View v = inflater.inflate(R.layout.list_row_pending, null);
            TextView txt_id = (TextView) v.findViewById(R.id.txt_p_id);
            TextView txt_inv = (TextView) v.findViewById(R.id.txt_p_invoice_no);
            TextView txt_pickup = (TextView) v.findViewById(R.id.txt_p_pickup);
            TextView txt_hasil = (TextView) v.findViewById(R.id.txt_p_hasil);

            DataPending b = datas.get(position);

            txt_id.setText(b.getId());
            txt_inv.setText(b.getFakturno());
            txt_pickup.setText("Pickup : "+AYHelper.tglJamToInd(b.getJampickup()));
            txt_hasil.setText(b.getCust_no()+" "+b.getCust_name()+", Alamat : "+b.getAlamat());
            return  v;
        }
    }

}
