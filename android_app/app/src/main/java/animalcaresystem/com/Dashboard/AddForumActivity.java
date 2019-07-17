package animalcaresystem.com.Dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import animalcaresystem.com.HelperClass;
import animalcaresystem.com.Model.ResponseModel;
import animalcaresystem.com.R;
import animalcaresystem.com.WebService.RetrofitAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddForumActivity extends AppCompatActivity {

    private TextInputLayout mEdtTitle;
    private EditText mEdtDescription;
    private Button mBtnPost;
    String title = "", description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forum);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Forum List");
        ImageButton img_btn_back = findViewById(R.id.nav_back_button);
        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        init_view();

    }

    private void init_view() {
        mEdtTitle = findViewById(R.id.edt_title);
        mEdtDescription = findViewById(R.id.edt_description);
        mBtnPost = findViewById(R.id.btn_post);
        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = mEdtTitle.getEditText().getText().toString();
                description = mEdtDescription.getText().toString();
                SharedPreferences pref = getSharedPreferences("UserToken", 0); // 0 - for private mode
                String s_token = pref.getString("token", null);
                String s_user_id = pref.getString("user_id", null);
                String s_username = pref.getString("username", null);

                if (HelperClass.v_input_lyt(mEdtTitle) && HelperClass.v_edt_txt(mEdtDescription)) {
                    String url = RetrofitAPI.url + "forumPost";

                    final Call<ResponseModel> userInfo = RetrofitAPI.getService().forum_post(url, title, description, s_username, s_token, s_user_id);
                    userInfo.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            ResponseModel server_response = response.body();
                            Toast.makeText(AddForumActivity.this, "Success!!!", Toast.LENGTH_SHORT).show();
                            resetAllField();
                            HelperClass.givenotification(AddForumActivity.this, server_response.getSuccess());
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(AddForumActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                        }

                    });
                }else{
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                }
            }
        });
    }

    private void resetAllField() {
        mEdtTitle.getEditText().setText("");
        mEdtDescription.setText("");
    }
}
