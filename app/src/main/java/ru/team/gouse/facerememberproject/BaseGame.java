package ru.team.gouse.facerememberproject;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BaseGame extends AppCompatActivity {
    protected long switchTime = 0;
    protected ArrayList<String> linksImagesList = new ArrayList<>();
    protected ArrayList<String> linksImagesEndList = new ArrayList<>();

    // Инициализируем data для игры
    protected void initGame() {
    }

    // Основной метод для начала игры
    protected void startGame() {
    }

    // Основной метод для завершения игры
    protected void endGame() {
    }

    // Задаем список изображений
    protected void setCurrentImage(String imageLink) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Picasso.with(this)
                .load(imageLink)
                .resize(imageView.getHeight(), imageView.getWidth())
                .error(R.drawable.ic_android_black_24dp)
                .into(imageView);
    }

    // Запускает игровой таймер
    protected void startGameTimer() {
    }

    protected long getSwitchTime() {
        return switchTime;
    }

    protected ArrayList<String> getLinksImagesList() {
        return linksImagesList;
    }

    protected ArrayList<String> getLinksImagesEndList() {
        return linksImagesEndList;
    }
}