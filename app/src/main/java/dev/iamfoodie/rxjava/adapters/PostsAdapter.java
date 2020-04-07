package dev.iamfoodie.rxjava.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.iamfoodie.rxjava.R;
import dev.iamfoodie.rxjava.models.Post;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private List<Post> posts;
    private Context ctx;

    public PostsAdapter(Context context, List<Post> posts) {
        this.posts = posts;
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
        Post post = posts.get(position);
        holder.bind(post);
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void updatePostAt(Post post) {
        this.posts.set(posts.indexOf(post), post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
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
            content.setText(post.getTitle() + "\n" + post.getContent());
            if (post.getComments() == null) {
                comment.setVisibility(View.INVISIBLE);
                showProgress(true);
            } else {
                comment.setText(post.getComments().size());
                showProgress(false);
            }
        }

        private void showProgress(boolean show) {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

}
