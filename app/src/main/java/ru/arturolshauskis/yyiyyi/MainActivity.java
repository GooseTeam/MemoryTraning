package ru.arturolshauskis.yyiyyi;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    public ImageView imageView;
    public TextView timerView;

    private String[] images = {
            "images_1",
            "images_2",
            "images_3",
            "images_4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setImage(images[0]);
        startTimer();
    }

    public void setImage(String imageUrl) {
        imageView = (ImageView) findViewById(R.id.imageView);

        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
                .error(R.drawable.ic_android_black_24dp)
                .into(imageView);
    }

    public void startTimer() {
        timerView = (TextView) findViewById(R.id.timerView);

        new CountDownTimer(16000, 1000) {
            @Override
            public void onTick(long second) {
                if (second < 10000) {
                    timerView.setText("00:0" + second / 1000);
                } else {
                    timerView.setText("00:" + second / 1000);
                }
            }

            @Override
            public void onFinish() {
                timerView.setText("-");
            }
        }.start();
    }


    /*public void onMyButtonClick(View view)
    {
        // выводим сообщение
        Toast.makeText(this, "Зачем вы нажали?", Toast.LENGTH_SHORT).show();
    }*/
}
