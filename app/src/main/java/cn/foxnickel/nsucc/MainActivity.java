package cn.foxnickel.nsucc;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button mCall;
    private TextView mTvVersion;
    private static final int REQ_PERMISSION = 0;
    private SharedPreferences sharedPreferences;
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private final String[] mPhoneNumbers = {"17188211261", "17051285279", "18117814517", "18117814607"};
    private TextView mLoveSentence;
    private final String[] mLoveSentences = {
            "遇见你，是我一生的春暖花开",
            "我喜欢你，在所有时候，也喜欢有些人，在他们偶尔像你的时候。",
            "我把我整个的灵魂都给你，连同它的怪癖，耍小脾气，忽明忽暗， 一千八百种坏毛病。它真讨厌，只有一点好，爱你。",
            "Someone say hi, someone say bye. So Someone smile and someone cry. Someone will give up, but someone always try. Someone may forget you, but never I.",
            "时光静好，与君语；细水流年，与君同；繁华落尽，与君老。",
            "和我做一切疯狂大胆不计后果的事情吧，趁我们都年轻，死去还能活，趁我们都勇敢，趁我还爱你。",
            "如果我爱你，将是很久很久的事。",
            "我喜欢你是寂静的。从此以后，我爱你，君权神授，责无旁贷。",
            "有那么一天，有一个人， 会走进你的生活，让你明白， 为什么你和其他人都没有结果。",
            "你便是落了我牙，歪了我嘴，瘸了我腿，折了我手，我也要向着这烟花路上走。",
            "如果不是你，那么是谁都一样。",
            "饭在锅里，我在床上。",
            "你在我身边也好，在天边也罢，想到有一个你，就觉得整个世界也变得温柔安定。",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("phone_number", MODE_PRIVATE);
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQ_PERMISSION);
            }
        }
    }

    private String createSentence() {
        Random random = new Random();
        int pos = random.nextInt(mLoveSentences.length);
        return "#" + mLoveSentences[pos] + "#";
    }

    private void makeCall() {
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int choice = sharedPreferences.getInt(KEY_PHONE_NUMBER, 0);
                String phoneNumber;
                switch (choice) {
                    case 0:
                        phoneNumber = mPhoneNumbers[0];
                        break;
                    case 1:
                        phoneNumber = mPhoneNumbers[1];
                        break;
                    case 2:
                        phoneNumber = mPhoneNumbers[2];
                        break;
                    case 3:
                        phoneNumber = mPhoneNumbers[3];
                        break;
                    default:
                        phoneNumber = mPhoneNumbers[0];
                        break;
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                String callSetting = "tel:" + phoneNumber;
                intent.setData(Uri.parse(callSetting));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "请授予软件打电话的权限", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    private void initView() {
        mCall = (Button) findViewById(R.id.bt_get_pass);
        mTvVersion = (TextView) findViewById(R.id.tv_version);
        mLoveSentence = (TextView) findViewById(R.id.tv_love_sentence);
        mLoveSentence.setText(createSentence());
        /*设置界面显示的版本号*/
        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String versionInfo = "版本：" + version;
            mTvVersion.setText(versionInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Toast.makeText(this, "请授予软件打电话的权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setPhoneNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("号码选择")
                .setSingleChoiceItems(mPhoneNumbers, sharedPreferences.getInt(KEY_PHONE_NUMBER, 0), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(KEY_PHONE_NUMBER, which);
                        editor.apply();
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "已保存", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}
