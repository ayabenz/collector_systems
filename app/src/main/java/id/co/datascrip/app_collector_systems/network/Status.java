package id.co.datascrip.app_collector_systems.network;

import android.os.Parcel;
import android.os.Parcelable;

public class Status implements Parcelable {

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };
    private boolean isSuccess;
    private String Message, Query;

    public Status() {
    }

    public Status(boolean isSuccess, String message, String query) {
        this.isSuccess = isSuccess;
        this.Message = message;
        this.Query = query;
    }

    protected Status(Parcel in) {
        isSuccess = in.readByte() != 0;
        Message = in.readString();
        Query = in.readString();
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        this.Query = query.replace("''", "'");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isSuccess ? 1 : 0));
        parcel.writeString(Message);
        parcel.writeString(Query);
    }
}
