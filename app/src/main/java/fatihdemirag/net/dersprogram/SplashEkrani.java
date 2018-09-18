package fatihdemirag.net.dersprogram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashEkrani extends Activity {

    TextView baslik;

    ImageView icon;

    LinearLayout container;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_ekrani);

        baslik = findViewById(R.id.baslik);
        icon = findViewById(R.id.icon);
        container = findViewById(R.id.container);

        animation = AnimationUtils.loadAnimation(this, R.anim.splash_ekrani_yazi);

        container.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    Thread.sleep(1500);
                    Intent intent = new Intent(SplashEkrani.this, MainPage.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
