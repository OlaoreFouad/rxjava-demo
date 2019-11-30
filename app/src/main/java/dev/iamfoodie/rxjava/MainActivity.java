package dev.iamfoodie.rxjava;

import androidx.appcompat.app.AppCompatActivity;
import static io.reactivex.Observable.*;
import android.os.Bundle;
import android.util.Log;

import dev.iamfoodie.rxjava.data.DataSource;
import dev.iamfoodie.rxjava.models.Task;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
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

        createOp();
        justOp();
        rangeOp();

    }

    private void createOp() {
        Observable<Task> taskObservable = create(new ObservableOnSubscribe<Task>() {
            @Override
            public void subscribe(ObservableEmitter<Task> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    emitter.onNext(new Task("Slap that rude bitch!", 10, false));
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "Task gotten. Description: " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Emission Complete!");
            }
        });
    }

    private void justOp() {
        Observable<Integer> integerObservable = just(1, 2, 3, 4, 5, 6, 7)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        integerObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "Just Observable Started!");
                disposable.add(d);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "Integer: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Message: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Subscription completed!");
            }
        });
    }

    private void rangeOp() {
        Observable<Integer> rangeObs = range(1, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        rangeObs.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "Range Obs started!");
                disposable.add(d);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "Int: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Error: " + e.getMessage());
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
