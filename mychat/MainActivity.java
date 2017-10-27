package com.luobotie.kingshun.mychat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.luobotie.kingshun.mychat.adapter.MyAdapter;
import com.luobotie.kingshun.mychat.net.GetChatRequest;
import com.luobotie.kingshun.mychat.net.chatBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener, MyAdapter.onItemClickListener {

    public static final int IMM_HIDE_FLAGS = 0;
    public static final String CHAT_DATA = "chat_data";
    private static final String TAG = "MainActivity";
    public static final String BASE_URL = "http://jisuznwd.market.alicloudapi.com";
    public static final String APPCODE = "APPCODE XXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String EXTRA_FELLING = "felling_string";
    public static final String CLIP_DATA_LABLE = "clip_data";
    private EditText mContent;
    private RecyclerView mRecyclerView;
    private ImageView mImageSend;
    private View mMask;
    private MyAdapter mAdapter;
    //数据集
    private List<String> mDatas;
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //实例化数据集
        mContent = (EditText) findViewById(R.id.et_message);
        mImageSend = (ImageView) findViewById(R.id.iv_send);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_chat);
//        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter(this, mDatas);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mMask = findViewById(R.id.maskForETFocus);
        //将mask移除以便可以点击rv子项
        mMask.setVisibility(View.GONE);
        setListenerForET();
    }

    private void setListenerForET() {
        mContent.setOnFocusChangeListener(this);
    }

    /**
     * 这里是用于给控件设置焦点监听
     *
     * @param view 视图
     * @param b    返回值代表是不是焦点
     */
    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            Log.d(TAG, "onFocusChange: 已聚焦");
            mMask.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "onFocusChange: 取消聚焦");
            //键盘的强制隐藏
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mContent.getWindowToken(), IMM_HIDE_FLAGS);
            //将mask取消存在
            mMask.setVisibility(View.GONE);
        }
    }

    public void clickToClearFocus(View v) {
        mContent.clearFocus();
    }

    /**
     * @param v 发送消息的按钮-》onclick
     */
    public void sendMsg(View v) {
        String message = mContent.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            //不为空
            Log.d(TAG, "sendMsg: " + message);
            swapDatas(message);
            mContent.setText("");
            //调用机器人回复数据
            returnData(message);
        }
    }

    private void swapDatas(String message) {
        if (mDatas == null) {
            Log.d(TAG, "swapDatas: mDatas是空！！！！！！");
            mDatas = new ArrayList<>();
        }
        mDatas.add(message);
        mAdapter.swapData(mDatas);
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    private void returnData(String question) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetChatRequest request = retrofit.create(GetChatRequest.class);
        Call<chatBean> call = request.getChat(question, APPCODE);
        call.enqueue(new Callback<chatBean>() {
            @Override
            public void onResponse(Call<chatBean> call, Response<chatBean> response) {
                String returnStr = response.body().getResult().getContent();
                swapDatas(returnStr);
            }
            @Override
            public void onFailure(Call<chatBean> call, Throwable t) {
                Toast.makeText(MainActivity.this, "机器人崩溃", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putStringArrayList(CHAT_DATA, (ArrayList<String>) mDatas);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: ");
        super.onRestoreInstanceState(savedInstanceState);
        mDatas = savedInstanceState.getStringArrayList(CHAT_DATA);
    }

    /**
     * @param position item点击事件
     */
    @Override
    public void onClick(int position) {
        showMyDialog(position, this);
    }

    @Override
    public void onLongClick(int position) {
        copyText(position,this);
    }

    /**
     * 用于复制文本消息字符串
     */
    private void copyText(final int position, Context context) {
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("您需要复制这句话吗！！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData myClip;
                        myClip = ClipData.newPlainText(CLIP_DATA_LABLE, mDatas.get(position));
                        cm.setPrimaryClip(myClip);
                        Toast.makeText(getApplicationContext(), "复制成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("否", null)
                .show();
    }



    private void showMyDialog(final int position, Context context) {
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage(String.format("您需要分析“%s”的情绪值吗？", mDatas.get(position)))
                // TODO: 2017/10/24 点击完确定后去查询情绪值
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intentToFelling = new Intent(MainActivity.this, FellingActivity.class);
                        intentToFelling.putExtra(EXTRA_FELLING, mDatas.get(position));
                        startActivity(intentToFelling);
                    }
                })
                .setNegativeButton("否", null)
                .show();
    }
}
