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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import animalcaresystem.com.Dashboard.ForumDetailActivity;
import animalcaresystem.com.HelperClass;
import animalcaresystem.com.Model.ForumCommentModel;
import animalcaresystem.com.R;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.PostViewHolder> {

    private Context context;
    private List<ForumCommentModel> items;

    public CommentAdapter(Context context, List<ForumCommentModel> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_comment, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        final ForumCommentModel item = items.get(position);

        holder.v_author.setText(item.getAuthor());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.v_comment.setText(Html.fromHtml(item.getDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.v_comment.setText(Html.fromHtml(item.getDescription()));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView v_author;
        TextView v_comment;

        public PostViewHolder(View itemView) {
            super(itemView);
            v_author = (TextView) itemView.findViewById(R.id.lbl_author);
            v_comment = (TextView) itemView.findViewById(R.id.lbl_comment);
        }
    }

    public void clear() {
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }
}
