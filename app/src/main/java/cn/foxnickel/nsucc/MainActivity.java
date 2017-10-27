package cn.foxnickel.nsucc;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button mAdd;
    private TextView mTvVersion;
    private EditText mLoveSentence;
    private static final OkHttpClient sOkHttpClient = new OkHttpClient();
    private static final String URL = "http://www.foxnickel.cn:520/";
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void addSentence() {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                String loveSentence = mLoveSentence.getText().toString();
                if (TextUtils.isEmpty(loveSentence)) {
                    e.onError(new Exception("输入句子，傻逼"));
                }
                MediaType mediaType = MediaType.parse("text;charset=utf-8");
                RequestBody requestBody = RequestBody.create(mediaType, loveSentence);
                Request request = new Request.Builder().url(URL + loveSentence).post(requestBody).build();
                Response response = sOkHttpClient.newCall(request).execute();
                if (response != null) {
                    String responseStr = response.body().string();
                    Log.i(TAG, "subscribe: " + responseStr);
                    e.onNext("添加成功");
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull String s) {
                        Log.i(TAG, "onNext: s " + s);
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initView() {
        mAdd = (Button) findViewById(R.id.bt_get_pass);
        mTvVersion = (TextView) findViewById(R.id.tv_version);
        mLoveSentence = (EditText) findViewById(R.id.et_add_love_sentence);
        /*设置界面显示的版本号*/
        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String versionInfo = "版本：" + version;
            mTvVersion.setText(versionInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSentence();
            }
        });
    }
}
