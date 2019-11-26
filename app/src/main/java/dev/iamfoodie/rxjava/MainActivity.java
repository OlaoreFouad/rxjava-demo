package dev.iamfoodie.rxjava;

import androidx.appcompat.app.AppCompatActivity;
import static io.reactivex.Observable.*;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
