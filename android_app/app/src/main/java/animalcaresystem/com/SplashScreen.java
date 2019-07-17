package animalcaresystem.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import animalcaresystem.com.Dashboard.DashboardActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences pref = getSharedPreferences("UserToken", 0); // 0 - for private mode
        String s_email = pref.getString("email", null);
        String s_password = pref.getString("password", null);
        String s_token = pref.getString("token", null);
        final Intent intent;
        if (s_email != null && s_password != null && s_token != null) {
             intent = new Intent(SplashScreen.this, DashboardActivity.class);
        } else {
             intent = new Intent(SplashScreen.this, MainActivity.class);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
