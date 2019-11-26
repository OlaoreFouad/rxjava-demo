package dev.iamfoodie.rxjava;

import androidx.appcompat.app.AppCompatActivity;
import static io.reactivex.Observable.*;
import android.os.Bundle;
import android.util.Log;

import dev.iamfoodie.rxjava.data.DataSource;
import dev.iamfoodie.rxjava.models.Task;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private Observable<Task> obs;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        obs = fromIterable(DataSource.getTasks())
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) {
                        return task.isComplete();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        obs.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
                Log.d(TAG, "onSubscribe called!, Thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext called!, Thread: " + Thread.currentThread().getName() + ", Task: " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error occured: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Subscription completed!");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
