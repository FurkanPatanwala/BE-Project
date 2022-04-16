package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity  extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMEOUT= 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_activity);

        Animation fadeOut= new AlphaAnimation(1,0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(600);
        fadeOut.setDuration(2200);
        ImageView image = findViewById(R.id.imageView);
        ImageView image2 = findViewById(R.id.lottieAnimationView);
        image.setAnimation(fadeOut);
        image2.setAnimation(fadeOut);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String packageContext;
                Intent intent= new Intent(SplashActivity.this, HomePage.class );
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}
