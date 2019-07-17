package animalcaresystem.com.Adapter;

import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import animalcaresystem.com.Dashboard.ForumDetailActivity;
import animalcaresystem.com.HelperClass;
import animalcaresystem.com.Model.ForumModel;
import animalcaresystem.com.R;


public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.PostViewHolder> {

    private Context context;
    private List<ForumModel> items;

    public ForumAdapter(Context context, List<ForumModel> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_forum, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        final ForumModel item = items.get(position);

        holder.v_title.setText(item.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.v_description.setText(Html.fromHtml(item.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.v_description.setText(Html.fromHtml(item.getDescription()));
        }

        /* Glide.with(context).load(item.getUploadVehiclePath()).into(holder.v_image);*/

        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "i: " + item.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ForumDetailActivity.class);
                intent.putExtra(HelperClass.SEND_DATA, item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView v_title;
        TextView v_description;
        Button btn_more;

        public PostViewHolder(View itemView) {
            super(itemView);
            v_title = (TextView) itemView.findViewById(R.id.title);
            v_description = (TextView) itemView.findViewById(R.id.description);
            btn_more = itemView.findViewById(R.id.btn_more);
        }
    }
}
