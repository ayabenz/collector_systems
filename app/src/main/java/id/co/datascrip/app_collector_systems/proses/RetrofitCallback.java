package id.co.datascrip.app_collector_systems.proses;

import java.util.ArrayList;

import id.co.datascrip.app_collector_systems.data.DataReason;


/**
 * Created by benny_aziz on 06/03/2016.
 */
public class RetrofitCallback {

    public interface CBKonfirmasi {
        void OnCB(boolean isSuccess, String msg);
    }

    public interface CBSendKelengkapan {
        void OnCB(boolean success);
    }

    public interface CBLoadReason {
        void OnCB(boolean isSuccess, ArrayList<DataReason> list_reason);
    }

    public interface CBLoadData {
        void OnCB(boolean isSuccess);
    }
}
