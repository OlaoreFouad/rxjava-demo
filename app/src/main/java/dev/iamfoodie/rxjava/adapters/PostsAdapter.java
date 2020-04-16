package dev.iamfoodie.rxjava.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.iamfoodie.rxjava.R;
import dev.iamfoodie.rxjava.models.Post;

public class PostsAdapter extends ListAdapter<Post, PostsAdapter.PostViewHolder> {

    private List<Post> posts;
    private Context ctx;
    private static final String TAG = "PostsAdapter";

    public PostsAdapter(Context context) {
        super(new PostsDiffUtilItemCallback());
        this.posts = new ArrayList<>();
        this.ctx = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(this.ctx).inflate(R.layout.post_and_comment_amount_item, parent, false);
        return new PostViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = getCurrentList().get(position);
        holder.bind(post);
    }

    @Override
    public void submitList(@Nullable List<Post> list) {
        super.submitList(list);
        Log.d("FlatMapActivity", "setPosts - adapter: " + getCurrentList().size());
    }

    public void setPosts(List<Post> posts) {
        Log.d("FlatMapActivity", "setPosts: " + posts.size());
        this.posts = posts;
        Log.d("FlatMapActivity", "setPosts - adapter: " + this.posts.size());

        notifyDataSetChanged();

        Log.d(TAG, "Size of posts list: " + this.posts.size());
    }

    public void updatePostAt(Post post) {
        Log.d("FlatMapActivity", "updatePostAt: " + post.getComments().size());
        getCurrentList().set(getCurrentList().indexOf(post), post);
        submitList(getCurrentList());
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView content;
        private TextView comment;
        private ProgressBar progressBar;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.post_text);
            comment = itemView.findViewById(R.id.post_comments);
            progressBar = itemView.findViewById(R.id.progress_spinner);

        }

        public void bind(Post post) {
            Log.d("FlatMapActivity", post.toString());
            content.setText(post.getTitle() + "\n" + post.getContent());
            if (post.getComments() == null) {
                comment.setVisibility(View.INVISIBLE);
                showProgress(true);
            } else {
                comment.setVisibility(View.VISIBLE);
                comment.setText(String.valueOf(post.getComments().size()));
                showProgress(false);
            }
        }

        private void showProgress(boolean show) {
            progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

}

class PostsDiffUtilItemCallback extends DiffUtil.ItemCallback<Post> {

    @Override
    public boolean areItemsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Post oldItem, @NonNull Post newItem) {
        return oldItem.getContent().equals(newItem.getContent());
    }
}

