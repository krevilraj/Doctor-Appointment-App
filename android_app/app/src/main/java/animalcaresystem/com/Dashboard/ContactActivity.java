package animalcaresystem.com.Dashboard;

import android.content.Context;
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

public class ContactActivity extends AppCompatActivity {

    private TextInputLayout mName;
    private TextInputLayout mEmail;
    private TextInputLayout mPhone;
    private EditText mEdtMessage;
    private Button mBtnSend;
    String name = "", email = "", phone = "", message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
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
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPhone = findViewById(R.id.phone);
        mEdtMessage = findViewById(R.id.edt_message);
        mBtnSend = findViewById(R.id.btn_send);

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mName.getEditText().getText().toString();
                email = mEmail.getEditText().getText().toString();
                phone = mPhone.getEditText().getText().toString();
                message = mEdtMessage.getText().toString();
                if (HelperClass.v_input_lyt(mName) && HelperClass.v_input_lyt(mEmail) && HelperClass.v_input_lyt(mPhone) && HelperClass.v_edt_txt(mEdtMessage)) {
                    String url = RetrofitAPI.url + "contact";

                    final Call<ResponseModel> userInfo = RetrofitAPI.getService().send_contact(url, name, email, phone, message);
                    userInfo.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            ResponseModel server_response = response.body();
                            Toast.makeText(ContactActivity.this, "" + server_response.getSuccess(), Toast.LENGTH_SHORT).show();
                            resetAllField();
                            HelperClass.givenotification(ContactActivity.this, server_response.getSuccess());
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(ContactActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
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
        mName.getEditText().setText("");
        mEmail.getEditText().setText("");
        mPhone.getEditText().setText("");
        mEdtMessage.setText("");
    }
}
