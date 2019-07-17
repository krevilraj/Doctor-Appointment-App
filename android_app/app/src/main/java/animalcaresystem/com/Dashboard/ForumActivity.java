package animalcaresystem.com.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Helper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import animalcaresystem.com.Adapter.ForumAdapter;
import animalcaresystem.com.HelperClass;
import animalcaresystem.com.Model.ForumModel;
import animalcaresystem.com.R;
import animalcaresystem.com.WebService.RetrofitAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    LinearLayoutManager manager;
    ForumAdapter adapter;
    List<ForumModel> items = new ArrayList<>();
    ImageButton img_add;


    ForumModel info;
    Boolean isUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_forum);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Forum List");
        ImageButton img_btn_back = findViewById(R.id.nav_back_button);
        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initView();
    }

    public void initView() {
        img_add = findViewById(R.id.img_add);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, AddForumActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        manager = new LinearLayoutManager(this);
        adapter = new ForumAdapter(this, items);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {

        final Call<List<ForumModel>> postList = RetrofitAPI.getService().getForum();
        postList.enqueue(new Callback<List<ForumModel>>() {
            @Override
            public void onResponse(Call<List<ForumModel>> call, Response<List<ForumModel>> response) {
                List<ForumModel> list = response.body();
                HelperClass.logme("size" + list.size());
                items.addAll(list);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<ForumModel>> call, Throwable t) {
                Toast.makeText(ForumActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });

    }
}
