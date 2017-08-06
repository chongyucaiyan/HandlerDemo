package com.github.cyc.handlerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private static final int SET_COUNT = 1;

    private TextView mTvCount;

    private MainHandler mMainHandler = new MainHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initContentView();
        // 在子线程中更新计数
        updateCount();
    }

    private void initContentView() {
        mTvCount = (TextView) findViewById(R.id.tv_main_count);
        setCount(0);
    }

    private void setCount(int count) {
        mTvCount.setText(String.format(getString(R.string.main_count_format), count));
    }

    private void updateCount() {
        new Thread() {

            @Override
            public void run() {
                int count = 0;
                while (true) {
                    try {
                        Thread.sleep(1000);
                        count++;
                        Message message = mMainHandler.obtainMessage(SET_COUNT, count, -1);
                        mMainHandler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }.start();
    }

    private static class MainHandler extends Handler {
        private WeakReference<MainActivity> mMainActivityRef;

        MainHandler(MainActivity mainActivity) {
            mMainActivityRef = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_COUNT:
                    MainActivity mainActivity = mMainActivityRef.get();
                    if (mainActivity != null) {
                        mainActivity.setCount(msg.arg1);
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
