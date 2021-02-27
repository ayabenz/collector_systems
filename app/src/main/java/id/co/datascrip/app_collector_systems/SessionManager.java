package id.co.datascrip.app_collector_systems;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


public class SessionManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Context c;
    int PRIVATE_MODE = 0;
    String PREF_NAME = "CollectorSystem";

    private static final String KEY_TOKEN = "tokenLogin";
    private static final String KEY_LOGIN = "isLogin" ;

    public static final String KEY_NAMA = "keynama";
    public static final String KEY_JABATAN = "keyjabatan";
    public static final String KEY_USERNO = "keyuserno";
    public static final String KEY_IDSES = "keyidsesion";
    public static final String KEY_PHOTO = "keyphoto";

    private static final String TIPE_GADGET = "dts_collector_tipe_gadget";
    public static final int TIPE_HANDPHONE = 0;

    public SessionManager(Context c){
        this.c = c;
        preferences = c.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void  createLoginSession(String token){
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(KEY_LOGIN, true);
        editor.commit();
    }

    public String getToken(){
        return  preferences.getString(KEY_TOKEN, null);
    }

    public boolean isLogin(){
        return preferences.getBoolean(KEY_LOGIN, false);
    }

    public void setNama(String name){
        editor.putString(KEY_NAMA, name);
        editor.commit();
    }
    public void setPhoto(String photo){
        editor.putString(KEY_PHOTO, photo);
        editor.commit();
    }
    public void setIduser(String iduser) {
        editor.putString(KEY_USERNO, iduser);
        editor.commit();
    }

    public void setIdsesion(String idsesion){
        editor.putString(KEY_IDSES, idsesion);
        editor.commit();
    }
    public void setJabatan(String jabatan){
        editor.putString(KEY_JABATAN, jabatan);
        editor.commit();
    }
    public String getNama(){
        return preferences.getString(KEY_NAMA, null);
    }
    public String getIduser() {
        return preferences.getString(KEY_USERNO, null);
    }
    public String getIdsesion(){return  preferences.getString(KEY_IDSES,null);}
    public String getJabatan(){return preferences.getString(KEY_JABATAN,null);}
    public String getPhoto(){return preferences.getString(KEY_PHOTO,null);}

    public void setTipeGadget(int tipeGadget) {
        editor.putInt(TIPE_GADGET, tipeGadget);
        editor.apply();
    }

    public int getTipeGadget() {
        return preferences.getInt(TIPE_GADGET, 0);
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAMA, preferences.getString(KEY_NAMA, null));
        user.put(KEY_JABATAN, preferences.getString(KEY_JABATAN, null));
        user.put(KEY_PHOTO,preferences.getString(KEY_PHOTO,null));
        user.put(KEY_IDSES, preferences.getString(KEY_IDSES, null));
        user.put(KEY_USERNO,preferences.getString(KEY_USERNO,null));
        return user;
    }

}
