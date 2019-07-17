package animalcaresystem.com.Dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RestrictTo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import animalcaresystem.com.Adapter.CommentAdapter;
import animalcaresystem.com.Adapter.ForumAdapter;
import animalcaresystem.com.HelperClass;
import animalcaresystem.com.Model.ForumCommentModel;
import animalcaresystem.com.Model.ForumModel;
import animalcaresystem.com.Model.ResponseModel;
import animalcaresystem.com.R;
import animalcaresystem.com.WebService.RetrofitAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumDetailActivity extends AppCompatActivity {
    ForumModel info;
    private TextView mLblTitle;
    private TextView mLblDescription;
    private ImageButton mImgComment;
    private TextView mTxtCancel;
    private EditText mEdtComment;
    private ImageButton mBtnSend;
    private RelativeLayout mRltFilter;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    CommentAdapter adapter;
    List<ForumCommentModel> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        Bundle extras = getIntent().getExtras();

        if (savedInstanceState == null) {
            if (extras == null) {
                info = new ForumModel();
            } else {
                info = (ForumModel) getIntent().getSerializableExtra(HelperClass.SEND_DATA);
            }
        } else {
            info = (ForumModel) savedInstanceState.getSerializable(HelperClass.SEND_DATA);

        }

        init_view();
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Forum Detail");
        ImageButton img_btn_back = findViewById(R.id.nav_back_button);
        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void init_view() {
        mLblTitle = findViewById(R.id.lbl_title);
        mLblDescription = findViewById(R.id.lbl_description);
        mImgComment = findViewById(R.id.img_comment);
        mTxtCancel = findViewById(R.id.txt_cancel);
        mEdtComment = findViewById(R.id.edt_comment);
        mBtnSend = findViewById(R.id.btn_send);
        mRltFilter = findViewById(R.id.rlt_filter);

        mLblTitle.setText(info.getTitle());
        mLblDescription.setText(info.getDescription());
        mImgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRltFilter.setVisibility(View.VISIBLE);
            }
        });
        mTxtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRltFilter.setVisibility(View.GONE);
            }
        });
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = mEdtComment.getText().toString();
                if (comment.equals("") || comment == null) {
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    Toast.makeText(ForumDetailActivity.this, "Write something on comment", Toast.LENGTH_SHORT).show();
                } else {
                    post_comment(comment);
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        manager = new LinearLayoutManager(this);
        adapter = new CommentAdapter(this, items);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void post_comment(String comment) {
        String url = RetrofitAPI.url + "post_comment";
        SharedPreferences pref = getSharedPreferences("UserToken", 0); // 0 - for private mode
        String s_token = pref.getString("token", null);
        String s_user_id = pref.getString("user_id", null);
        String s_username = pref.getString("username", null);
        String forum_id = info.getId();


        final Call<ResponseModel> userInfo = RetrofitAPI.getService().comment_post(url, s_username, comment, s_user_id, forum_id, s_token);
        userInfo.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel server_response = response.body();
                Toast.makeText(ForumDetailActivity.this, "" + server_response.getSuccess(), Toast.LENGTH_SHORT).show();
                resetAllField();
                getData();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ForumDetailActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void resetAllField() {
        mEdtComment.setText("");
    }

    private void getData() {

        final Call<List<ForumCommentModel>> postList = RetrofitAPI.getService().getComment(RetrofitAPI.url + "forum_comment/" + info.getId());
        postList.enqueue(new Callback<List<ForumCommentModel>>() {
            @Override
            public void onResponse(Call<List<ForumCommentModel>> call, Response<List<ForumCommentModel>> response) {
                List<ForumCommentModel> list = response.body();
                HelperClass.logme("size" + list.size());
                adapter.clear();
                items.addAll(list);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<ForumCommentModel>> call, Throwable t) {
                Toast.makeText(ForumDetailActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });

    }
}
