package com.github.cyc.handlerthread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final int DO_TASK1 = 1;
    private static final int DO_TASK2 = 2;

    private Looper mLooper;
    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 创建HandlerThread线程
        createHandlerThread();
    }

    private void createHandlerThread() {
        // 创建HandlerThread线程，并启动该线程
        HandlerThread thread = new HandlerThread("HandlerThread");
        thread.start();
        // 获取线程的Looper，创建与该线程相关联的Handler
        mLooper = thread.getLooper();
        mHandler = new MyHandler(mLooper);
        // 发送任务消息
        sendMessage();
    }

    private void sendMessage() {
        Message message1 = mHandler.obtainMessage(DO_TASK1);
        Message message2 = mHandler.obtainMessage(DO_TASK2);
        mHandler.sendMessage(message1);
        mHandler.sendMessage(message2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出Looper消息循环
        mLooper.quit();
    }

    private static class MyHandler extends Handler {
        private static final String TAG = "MyHandler";

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DO_TASK1:
                    // 执行任务1
                    doTask1();
                    break;
                case DO_TASK2:
                    // 执行任务2
                    doTask2();
                    break;
                default:
                    break;
            }
        }

        private void doTask1() {
            Log.i(TAG, "doTask1(), start");
            Log.i(TAG, "doTask1(), thread name is " + Thread.currentThread().getName());

            // 模拟耗时任务
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.i(TAG, "doTask1(), end");
        }

        private void doTask2() {
            Log.i(TAG, "doTask2(), start");
            Log.i(TAG, "doTask2(), thread name is " + Thread.currentThread().getName());

            // 模拟耗时任务
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.i(TAG, "doTask2(), end");
        }
    }
}
