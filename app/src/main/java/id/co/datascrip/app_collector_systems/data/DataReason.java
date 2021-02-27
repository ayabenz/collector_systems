package id.co.datascrip.app_collector_systems.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamsyah_putra on 4/3/2017.
 */
public class DataReason implements Parcelable {

    public static final String CODE = "code";
    public static final String DESCRIPTION = "desc";
    public static final String TIPE = "tipe";
    public static final String TIPE_CODE = "tipe_code";

    private int ID;
    private String Code, Description, Tipe, Tipe_code;

    public DataReason() {
    }

    public DataReason(String description) {
        this.Description = description;
    }

    protected DataReason(Parcel in) {
        ID = in.readInt();
        Code = in.readString();
        Description = in.readString();
        Tipe = in.readString();
        Tipe_code = in.readString();
    }

    public static final Creator<DataReason> CREATOR = new Creator<DataReason>() {
        @Override
        public DataReason createFromParcel(Parcel in) {
            return new DataReason(in);
        }

        @Override
        public DataReason[] newArray(int size) {
            return new DataReason[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Code);
        dest.writeString(Description);
        dest.writeString(Tipe);
        dest.writeString(Tipe_code);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setTipe(String tipe){ Tipe = tipe;}

    public String getTipe(){return Tipe;}

    public void setTipe_code(String tipe_code){
        Tipe_code = tipe_code;
    }

    public String getTipe_code(){return Tipe_code;}

}
