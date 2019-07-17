package animalcaresystem.com.Dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;

import animalcaresystem.com.MainActivity;
import animalcaresystem.com.R;

public class DashboardActivity extends AppCompatActivity {
    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        mainGrid = findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);
    }


    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (finalI == 0) {
                        Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                    if (finalI == 1) {
                        Intent intent = new Intent(DashboardActivity.this, AppointmentActivity.class);
                        startActivity(intent);
                    }
                    if (finalI == 2) {
                        Intent intent = new Intent(DashboardActivity.this, ContactActivity.class);
                        startActivity(intent);
                    }
                    if (finalI == 3) {
                        Intent intent = new Intent(DashboardActivity.this, ForumActivity.class);
                        startActivity(intent);
                    }
                    if (finalI == 4) {
                        Intent intent = new Intent(DashboardActivity.this, DonateActivity.class);
                        startActivity(intent);
                    }
                    if (finalI == 5) {
                        SharedPreferences preferences = getSharedPreferences("UserToken", 0);
                        preferences.edit().remove("email").commit();
                        preferences.edit().remove("password").commit();
                        preferences.edit().remove("token").commit();
                        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }
    }
}
