package com.luobotie.kingshun.mychat;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qcloud.Module.Wenzhi;
import com.qcloud.QcloudApiModuleCenter;
import com.qcloud.Utilities.Json.JSONObject;

import java.util.TreeMap;

public class FellingActivity extends AppCompatActivity {

    public static final String SECRET_KEY = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String SECRET_ID = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String TAG = "FellingActivity";
    public static final String BASE_URL = "https://wenzhi.api.qcloud.com";
    private TextView mTextView;
    private String signature;
    private double resultPositive;
    private String felling;
    private ImageView mFelling;
    private ProgressBar mProgress;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            mProgress.setVisibility(View.GONE);
            if (resultPositive > 0.8) {
                mFelling.setImageResource(R.drawable.happy_two);
            }
            if (0.8 >= resultPositive && resultPositive > 0.6) {
                mFelling.setImageResource(R.drawable.happy_one);
            }
            if (0.6 >= resultPositive && resultPositive > 0.4) {
                mFelling.setImageResource(R.drawable.normal);
            }
            if (0.4 >= resultPositive && resultPositive > 0.2) {
                mFelling.setImageResource(R.drawable.sad_one);
            }
            if (resultPositive <= 0.2) {
                mFelling.setImageResource(R.drawable.sad_two);
            }
            mTextView.setText("当前情绪值为:" + (int) (resultPositive * 100) + "%");
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_felling);
        felling = getIntent().getStringExtra(MainActivity.EXTRA_FELLING);
        mTextView = (TextView) findViewById(R.id.tv_felling);
        mFelling = (ImageView) findViewById(R.id.iv_fellings);
        mProgress = (ProgressBar) findViewById(R.id.progressbar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }).start();
    }

    private void init() {
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        config.put("SecretId", SECRET_ID);
        config.put("SecretKey", SECRET_KEY);
        /* 请求方法类型 POST、GET */
        config.put("RequestMethod", "GET");
        /* 区域参数，可选: gz:广州; sh:上海; hk:香港; ca:北美;等。 */
        config.put("DefaultRegion", "gz");
        QcloudApiModuleCenter center = new QcloudApiModuleCenter(new Wenzhi(), config);
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        /* 将需要输入的参数都放入 params 里面，必选参数是必填的。 */
        /* DescribeInstances 接口的部分可选参数如下 */
        params.put("content", felling);
        params.put("type", 4);

        String result = null;
        try {
            /* call 方法正式向指定的接口名发送请求，并把请求参数params传入，返回即是接口的请求结果。 */
            result = center.call("TextSentiment", params);
            JSONObject json_result = new JSONObject(result);
            resultPositive = json_result.getDouble("positive");
            System.out.println(json_result);
            Log.d(TAG, "onCreate:json_result" + resultPositive);
            mHandler.sendEmptyMessage(0);
        } catch (Exception e) {
            System.out.println("error..." + e.getMessage());
            Toast.makeText(getApplicationContext(), "出现错误即将返回", Toast.LENGTH_SHORT).show();
            this.finish();
            Log.d(TAG, "onCreate: errorgetMessage");
        }
    }
}
