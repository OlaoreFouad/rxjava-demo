package dev.iamfoodie.rxjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Random;

import dev.iamfoodie.rxjava.adapters.PostsAdapter;
import dev.iamfoodie.rxjava.api.ServiceGenerator;
import dev.iamfoodie.rxjava.models.Comment;
import dev.iamfoodie.rxjava.models.Post;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FlatMapActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;
    private CompositeDisposable disposable;

    private static final String TAG = "FlatMapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_map);
        disposable = new CompositeDisposable();

        initializeRecyclerView();


        getPostsObservable()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Post, ObservableSource<Post>>() {
                    @Override
                    public ObservableSource<Post> apply(Post post) throws Exception {
                        return getCommentsForPost(post);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Post post) {
                        updatePost(post);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updatePost(Post post) {
        postsAdapter.updatePostAt(post);
    }

    private Observable<Post> getCommentsForPost(final Post post) {
        return ServiceGenerator.getService()
                .getCommentsForPost(post.getId())
                .map(new Function<List<Comment>, Post>() {
                    @Override
                    public Post apply(List<Comment> comments) throws Exception {

                        Log.d(TAG, "Size: " + comments.size());

                        Thread.sleep((new Random(5).nextInt() + 1) * 1000);
                        post.setComments(comments);

                        return post;

                    }
                })
                .subscribeOn(Schedulers.io());
    }

    private void initializeRecyclerView() {
        postsRecyclerView = findViewById(R.id.posts_recycler_view);
        postsRecyclerView.setHasFixedSize(true);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        postsAdapter = new PostsAdapter(this);
        this.postsRecyclerView.setAdapter(postsAdapter);

    }

    public Observable<Post> getPostsObservable() {

        return ServiceGenerator.getService()
                .getPosts()
                .flatMap(new Function<List<Post>, ObservableSource<Post>>() {
                    @Override
                    public ObservableSource<Post> apply(List<Post> posts) throws Exception {
                        Log.d(TAG, "Posts size: " + posts.size());
                        postsAdapter.submitList(posts);
                        return Observable.fromIterable(
                                posts
                        ).subscribeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
