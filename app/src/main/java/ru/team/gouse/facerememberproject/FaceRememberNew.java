package ru.team.gouse.facerememberproject;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class FaceRememberNew extends BaseGame {
    public long switchTime = 0;
    public Handler mHandler = new Handler();
    public ArrayList<String> linksImagesList = new ArrayList<>();
    public ArrayList<String> linksImagesEndList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_menu);

        ImageButton initGameButton = (ImageButton) findViewById(R.id.new_game);

        View.OnClickListener initGameClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGame();
            }
        };

        initGameButton.setOnClickListener(initGameClick);
    }

    // TODO решение для обхода onDataChange
    protected void initGame() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference("levels");

        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long countLevels = dataSnapshot.getChildrenCount();

                if (countLevels > 0) {
                    if (generateLevels(countLevels)) {
                        // click checker

                        initGameSettings(dataSnapshot, 1);
                        if (switchTime != 0 && linksImagesList.size() > 0) {
                            startGame();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("Error", "No init database");
            }
        });
    }

    // TODO Добавить settings для кнопки
    public boolean generateLevels(long countLevels) {
        if (countLevels == 0) {
            return false;
        }

        setContentView(R.layout.levels_list);
        LinearLayout container = (LinearLayout) findViewById(R.id.levels_list);

        for (int currentNumber = 0; currentNumber < countLevels; currentNumber++) {
            Button button = new Button(this);
            button.setText("Уровень " + currentNumber);
            container.addView(button);
        }

        return true;
    }

    // Инициализируем время на переключение слайдов и сами слайды
    public void initGameSettings(DataSnapshot dataSnapshot, int level) {
        DataSnapshot currentLevel = dataSnapshot.child("level_" + level);

        if (currentLevel.getChildrenCount() > 0) {
            switchTime = (long) currentLevel.child("timer").getValue();
            setLinksImagesList(currentLevel);
        }
    }

    // Задаем все ссылки на картинки.
    public void setLinksImagesList(DataSnapshot currentLevel) {
        for (DataSnapshot imageItem : currentLevel.child("images").getChildren()) {
            String value = (String) imageItem.child("main_url").getValue();
            linksImagesList.add(value);
            linksImagesEndList.add(value);
        }
    }

    protected void startGame() {
        setContentView(R.layout.game_layout);

        Runnable timeUpdaterRunnable = new Runnable() {
            public void run() {
                int dataSize = linksImagesList.size();

                if (dataSize > 1) {
                    int currentIndex = (new Random()).nextInt(dataSize);

                    startGameTimer();
                    setCurrentImage(linksImagesList.get(currentIndex));

                    linksImagesList.remove(currentIndex);
                } else {
                    endGame();
                }

                mHandler.postDelayed(this, switchTime);
            }
        };

        mHandler.removeCallbacks(timeUpdaterRunnable);
        mHandler.postDelayed(timeUpdaterRunnable, 100);
    }

    protected void endGame() {
        setContentView(R.layout.game_end);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight() / linksImagesEndList.size();

        TableLayout tableImages = (TableLayout) findViewById(R.id.table_end_images);

        View.OnClickListener gameOver = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Вы проиграли!", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        View.OnClickListener gameWin = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Вы выиграли!", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        for (int index = 0; index < linksImagesEndList.size(); index++) {
            TableRow row = new TableRow(this);
            ImageButton imageButton = new ImageButton(this);

            imageButton.setOnClickListener(gameWin);

            // TODO Add random
            //imageButton.setOnClickListener(gameOver);

            Picasso.with(this)
                    .load(linksImagesEndList.get(index))
                    .resize(width, height)
                    .error(R.drawable.ic_android_black_24dp)
                    .into(imageButton);

            row.addView(imageButton);
            tableImages.addView(row);
        }
    }

    protected void startGameTimer() {
        final TextView timerView = (TextView) findViewById(R.id.timerView);

        new CountDownTimer(switchTime + 1000, 1000) {
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
