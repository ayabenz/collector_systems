package id.co.datascrip.app_collector_systems.sqllite;

/**
 * Created by alamsyah_putra on 4/3/2017.
 */
public class ConstSQLite {
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "SA_Invoice.sqlite";
    public static final String DATABASE_NAME_DEMO = "SA_Invoice_demo.sqlite";

    public static final String TABLE_ALASAN = "tbl_alasan";
    public static final String ALASAN_ID = "ID";
    public static final String ALASAN_CODE = "CODE";
    public static final String ALASAN_DESCRIPTION = "Description";
    public static final String ALASAN_TIPE = "Tipe";

    public static final String TABLE_CUSTOMER_FAKTUR = "tbl_customer_faktur";
    public static final String C_ID = "ID";
    public static final String C_CODE = "CODE";
    public static final String C_NAME = "NAMA";
    public static final String C_ALAMAT = "ALAMAT";

    public static final String CREATE_TABLE_ALASAN = "CREATE TABLE " + TABLE_ALASAN + "(" +
            ALASAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            ALASAN_CODE + " INTEGER ," +
            ALASAN_DESCRIPTION + " TEXT,"+
            ALASAN_TIPE + " TEXT )";

    public static final String CREATE_TABLE_CUSTOMER_FAKTUR = "CREATE TABLE " + TABLE_CUSTOMER_FAKTUR + "(" +
            C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            C_CODE + " TEXT ," +
            C_NAME + " TEXT,"+
            C_ALAMAT + " TEXT )";
}
