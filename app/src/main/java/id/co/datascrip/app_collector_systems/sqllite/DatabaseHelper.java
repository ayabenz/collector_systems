package id.co.datascrip.app_collector_systems.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alamsyah_putra on 4/3/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, ConstSQLite.DATABASE_NAME_DEMO, null, ConstSQLite.DATABASE_VERSION);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConstSQLite.CREATE_TABLE_ALASAN);
        db.execSQL(ConstSQLite.CREATE_TABLE_CUSTOMER_FAKTUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
