package id.co.datascrip.app_collector_systems.object;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import id.co.datascrip.app_collector_systems.BuildConfig;

/**
 * Created by alamsyah_putra on 4/5/2017.
 */
public class MockApp implements Parcelable {
    public static final Creator<MockApp> CREATOR = new Creator<MockApp>() {
        @Override
        public MockApp createFromParcel(Parcel in) {
            return new MockApp(in);
        }

        @Override
        public MockApp[] newArray(int size) {
            return new MockApp[size];
        }
    };
    private ArrayList<String> PackageName = new ArrayList<>();

    public MockApp() {
    }

    protected MockApp(Parcel in) {
        PackageName = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(PackageName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<String> getPackageName() {
        return PackageName;
    }

    public void setPackageName(ArrayList<String> packageName) {
        PackageName = packageName;
    }

    public int getSize() {
        return this.PackageName.size();
    }

    public boolean isMockLocationEnabled(Context mContext) {
        boolean isMockLocation = false;
        try {
            //if marshmallow
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AppOpsManager opsManager = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
                isMockLocation = (opsManager.checkOp(AppOpsManager.OPSTR_MOCK_LOCATION, android.os.Process.myUid(), BuildConfig.APPLICATION_ID) == AppOpsManager.MODE_ALLOWED);
            } else {
                // in marshmallow this will always return true
                isMockLocation = !android.provider.Settings.Secure.getString(mContext.getContentResolver(), "mock_location").equals("0");
            }
        } catch (Exception e) {
            return false;
        }

        return isMockLocation;
    }
}
