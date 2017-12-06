package com.oreo.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private String message = "Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new CustomAsyncTask<String>() {

            @Override
            public void onPreExecute() {
                //Do your pre execute stuff here
            }

            @Override
            public void onPostExecute(String result) {
                //Result is received on main thread.
            }

            @Override
            public String doInBackground() {
                //Perform background operation here
                return message;
            }
        }.execute();
    }


    private static abstract class CustomAsyncTask<T> {
        public abstract void onPreExecute();

        public abstract void onPostExecute(T result);

        public abstract T doInBackground();

        void execute() {
            BackgroundThread backgroundThread = new BackgroundThread();
            Log.d(TAG, "onPreExecute Thread name " + Thread.currentThread().getName());
            onPreExecute();
            backgroundThread.start();
        }

        class BackgroundThread extends Thread {
            Handler handler;

            @Override
            public void run() {
                Log.d(TAG, "doInBackground Thread name " + Thread.currentThread().getName());
                handler = new Handler(Looper.getMainLooper());
                final T result = doInBackground();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute(result);
                        Log.d(TAG, "onPostExecute Thread name " + Thread.currentThread().getName());
                    }
                });
            }
        }
    }
}
