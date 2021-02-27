package id.co.datascrip.app_collector_systems.helper;

import android.content.Context;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;

import com.androidquery.AQuery;

import id.co.datascrip.app_collector_systems.SessionManager;

/**
 * Created by alamsyah_putra on 3/10/2017.
 */
public class BaseActivity extends AppCompatActivity {
    protected Context c;
    protected AQuery query;
    protected SessionManager sesi;
    protected AlphaAnimation btnAnimasi = new AlphaAnimation(1F,0.5F);

    @Override
    protected void onCreate(Bundle savedInstanceStates){
        super.onCreate(savedInstanceStates);
        c = this;
        query = new AQuery(c);
        sesi = new SessionManager(c);
    }

}
