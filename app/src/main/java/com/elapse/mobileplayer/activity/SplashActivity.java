package com.elapse.mobileplayer.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.elapse.mobileplayer.R;

public class SplashActivity extends Activity {

    private Handler mHandler = new Handler();
    private boolean isMainStarted = false;
    private boolean permissionGranted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
         != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            permissionGranted = true;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }
            },2000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionGranted = true;
                }else{
                    Toast.makeText(this,"Permission denied !",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void startMainActivity() {
        if (!isMainStarted){
            isMainStarted = true;
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (permissionGranted)
            startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        isMainStarted = false;
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
