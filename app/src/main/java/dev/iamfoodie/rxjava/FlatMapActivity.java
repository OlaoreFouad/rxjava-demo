package dev.iamfoodie.rxjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import dev.iamfoodie.rxjava.adapters.PostsAdapter;
import dev.iamfoodie.rxjava.api.ServiceGenerator;
import dev.iamfoodie.rxjava.models.Post;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FlatMapActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_map);
        disposable = new CompositeDisposable();

        initializeRecyclerView();

    }

    private void initializeRecyclerView() {
        postsRecyclerView = findViewById(R.id.posts_recycler_view);
        postsRecyclerView.setHasFixedSize(true);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        postsAdapter = new PostsAdapter(this, null);
        this.postsRecyclerView.setAdapter(postsAdapter);

    }

    public Observable<Post> getPostsObservable() {

        return ServiceGenerator.getService()
                .getPosts()
                .flatMap(new Function<List<Post>, ObservableSource<Post>>() {
                    @Override
                    public ObservableSource<Post> apply(List<Post> posts) throws Exception {
                        postsAdapter.setPosts(posts);
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
