package ru.team.gouse.testprojectremember;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Random;

public class FaceRemember extends BaseGame {
    private Handler mHandler = new Handler();
    private String newImageUrl = ""; // Картинка, которая была новой

    public void initGame() {
        setContentView(R.layout.face_remember);
        setImagesData();
        startGame();
    }

    protected void startGame() {
        Runnable timeUpdaterRunnable = new Runnable() {
            public void run() {
                int dataSize = startDataImages.size();

                if (dataSize > 1) {
                    // Получаем случайную перваю картинку
                    int currentIndex = (new Random()).nextInt(startDataImages.size());
                    String urlImages = startDataImages.get(currentIndex);

                    saveDataImages.add(urlImages);
                    startDataImages.remove(currentIndex); // Удаляем из общего списка

                    setCurrentImage(urlImages); // Устанавливаем картинку
                    startGameTimer(); // Запускаем таймер

                    mHandler.postDelayed(this, 16000);
                } else {
                    // Массив закончился
                    newImageUrl = startDataImages.get(0); // Долбавление новой картинки
                    saveDataImages.add(startDataImages.get(0));

                    if (saveDataImages.size() > 1) {
                        endGame();
                    } else {
                        Log.v("Error", "Missing images");
                    }
                }
            }
        };
        mHandler.postDelayed(timeUpdaterRunnable, 100);
    }

    protected void endGame(){}

    private void setCurrentImage(String imageUrl) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Picasso.with(this)
                .load(imageUrl)
                .error(R.drawable.ic_android_black_24dp)
                .into(imageView);
    }

    private void startGameTimer() {
        final TextView timerView = (TextView) findViewById(R.id.timerView);

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
}
