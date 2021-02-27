package id.co.datascrip.app_collector_systems.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import id.co.datascrip.app_collector_systems.Function;
import id.co.datascrip.app_collector_systems.data.Data;

/**
 * Created by alamsyah_putra on 4/12/2017.
 */
public class Customer_Faktur_SQLLite {
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public Customer_Faktur_SQLLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    public boolean Post(Data customer) {
        return !isExist(customer) && insert(customer);
    }

    private boolean insert(Data customer) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstSQLite.C_CODE, customer.getId());
            values.put(ConstSQLite.C_NAME, customer.getNama());
            values.put(ConstSQLite.C_ALAMAT, customer.getAlamat());

            db.insert(ConstSQLite.TABLE_CUSTOMER_FAKTUR, null, values);
            db.close();

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            new Function().writeToText("insert Reason=>", e.toString());
            return false;
        }
    }

    private boolean isExist(Data customer) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_CUSTOMER_FAKTUR + " WHERE " +
                ConstSQLite.C_CODE + " = '" + customer.getId() + "'";
        Cursor c = db.rawQuery(query, null);
        boolean exist = c.getCount() > 0;
        db.close();
        c.close();
        return exist;
    }

    public ArrayList<Data> getAll() {
        db = dbHelper.getReadableDatabase();
        ArrayList<Data> listcustomer = new ArrayList<>();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_CUSTOMER_FAKTUR;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                Data customer = new Data();
                customer.setId(c.getString(c.getColumnIndex(ConstSQLite.C_CODE)));
                customer.setNama(c.getString(c.getColumnIndex(ConstSQLite.C_NAME)));
                customer.setAlamat(c.getString(c.getColumnIndex(ConstSQLite.C_ALAMAT)));
                listcustomer.add(customer);
            }
            while (c.moveToNext());
        }
        db.close();
        c.close();
        return listcustomer;
    }

    public void deleteAll() {
        db = dbHelper.getReadableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_CUSTOMER_FAKTUR;
        db.execSQL(query);
        db.close();
    }

}
