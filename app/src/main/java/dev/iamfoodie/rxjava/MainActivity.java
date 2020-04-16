package dev.iamfoodie.rxjava;

import androidx.appcompat.app.AppCompatActivity;
import static io.reactivex.Observable.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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

        Button flatmapButton = findViewById(R.id.start_flatmap_activity);
        flatmapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FlatMapActivity.class));
            }
        });

        Observable<Integer> integerObservable = Observable.just(1, 2, 3, 4, 5)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer + 1;
                    }
                })
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 2;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        integerObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
                Log.d(TAG, "subscribed on: " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "next number received as: " + integer + " on thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


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
        intervalOp();
        timerOp();

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
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer + 10;
                    }
                })
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

    public void intervalOp() {
        Observable<Long> intervalObs = interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        Log.d("MainActivity", "Interval Value: " + aLong + " Thread: " + Thread.currentThread().getName());
                        return aLong <= 5;
                    }
                }).observeOn(AndroidSchedulers.mainThread());

        intervalObs.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.d("MainActivity", "Value: " + aLong);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                Log.d("MainActivity", "Emission Complete!");
            }
        });
    }

    public void timerOp() {
        Observable<String> timerObs = timer(7, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        return String.valueOf(aLong) + "0";
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        timerObs.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String aLong) {
                Log.d("MainActivity", "From timer: " + aLong);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
