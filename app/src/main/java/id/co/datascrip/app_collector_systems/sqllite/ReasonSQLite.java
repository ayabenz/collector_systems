package id.co.datascrip.app_collector_systems.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import id.co.datascrip.app_collector_systems.Function;
import id.co.datascrip.app_collector_systems.data.DataReason;

/**
 * Created by alamsyah_putra on 4/3/2017.
 */
public class ReasonSQLite {
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ReasonSQLite(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    public boolean Post(DataReason reason) {
        return !isExist(reason) && insert(reason);
    }

    public boolean insert(DataReason reason) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ConstSQLite.ALASAN_CODE, reason.getCode());
            values.put(ConstSQLite.ALASAN_DESCRIPTION, reason.getDescription());
            values.put(ConstSQLite.ALASAN_TIPE, reason.getTipe());

            db.insert(ConstSQLite.TABLE_ALASAN, null, values);
            db.close();

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            new Function().writeToText("insert Reason=>", e.toString());
            return false;
        }
    }

    private boolean isExist(DataReason reason) {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_ALASAN + " WHERE " +
                ConstSQLite.ALASAN_CODE + " = '" + reason.getCode() + "'";
        Cursor c = db.rawQuery(query, null);
        boolean exist = c.getCount() > 0;
        db.close();
        c.close();
        return exist;
    }


    public DataReason getAlasanNotIn(int id){
        db = dbHelper.getReadableDatabase();
        DataReason alasan = new DataReason();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_ALASAN +
                " WHERE " + ConstSQLite.ALASAN_CODE + " NOT IN (" + String.valueOf(id) + ")";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            alasan.setID(c.getInt(c.getColumnIndex(ConstSQLite.ALASAN_ID)));
            alasan.setCode(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_CODE)));
            alasan.setDescription(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_DESCRIPTION)));
            alasan.setTipe(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_TIPE)));
        }
        db.close();
        c.close();
        return alasan;
    }

    public DataReason getAlasanReject(int id) {
        db = dbHelper.getReadableDatabase();
        DataReason alasan = new DataReason();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_ALASAN +
                " WHERE " + ConstSQLite.ALASAN_ID + " = " + String.valueOf(id);
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            alasan.setID(c.getInt(c.getColumnIndex(ConstSQLite.ALASAN_ID)));
            alasan.setCode(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_CODE)));
            alasan.setDescription(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_DESCRIPTION)));
            alasan.setTipe(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_TIPE)));
        }
        db.close();
        c.close();
        return alasan;
    }

    public ArrayList<DataReason> getReasonsByCode(String code) {
        db = dbHelper.getReadableDatabase();
        ArrayList<DataReason> listReason = new ArrayList<>();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_ALASAN +
                " WHERE " + ConstSQLite.ALASAN_CODE + " = '" + code + "'";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                DataReason reason = new DataReason();
                reason.setID(c.getInt(c.getColumnIndex(ConstSQLite.ALASAN_ID)));
                reason.setCode(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_CODE)));
                reason.setDescription(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_DESCRIPTION)));
                reason.setTipe(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_TIPE)));
                listReason.add(reason);
            }
            while (c.moveToNext());
        }
        db.close();
        c.close();
        return listReason;
    }

    public ArrayList<DataReason> getAll() {
        db = dbHelper.getReadableDatabase();
        ArrayList<DataReason> listReason = new ArrayList<>();
        String query = "SELECT * FROM " + ConstSQLite.TABLE_ALASAN;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                DataReason reason = new DataReason();
                reason.setID(c.getInt(c.getColumnIndex(ConstSQLite.ALASAN_ID)));
                reason.setCode(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_CODE)));
                reason.setDescription(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_DESCRIPTION)));
                reason.setTipe(c.getString(c.getColumnIndex(ConstSQLite.ALASAN_TIPE)));
                listReason.add(reason);
            }
            while (c.moveToNext());
        }
        db.close();
        c.close();
        return listReason;
    }

    public void deleteAll() {
        db = dbHelper.getReadableDatabase();
        String query = "DELETE FROM " + ConstSQLite.TABLE_ALASAN;
        db.execSQL(query);
        db.close();
    }
}
