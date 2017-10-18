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

public class MainActivity extends AppCompatActivity {

    private Button mCall;
    private TextView mTvVersion;
    private static final int REQ_PERMISSION = 0;
    private SharedPreferences sharedPreferences;
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private final String[] mPhoneNumbers = {"17188211261", "17051285279", "18117814517", "18117814607"};
    private TextView mLoveSentence;

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
