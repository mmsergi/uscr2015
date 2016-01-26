package com.kuvi.cuantarazon;

/**
 * Created by oriolferrer on 11/11/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}