package ru.team.gouse.testprojectremember;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private String newImageUrl = "";
    private ArrayList<String> startLinksList = new ArrayList<>();
    private ArrayList<String> saveLinksList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference("images");


        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Добавить ограничение
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String key = childDataSnapshot.getKey();
                    String value = (String) dataSnapshot.child(key).child("main_url").getValue();
                    startLinksList.add(value);
                }

                if (startLinksList.size() > 1) {
                    startGame();
                } else {
                    Log.v("Error", "Empty url list");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("Error", "No init database");
            }
        });
    }

    public void startGame() {
        Runnable timeUpdaterRunnable = new Runnable() {
            public void run() {
                int size = startLinksList.size();

                if (size > 1) {
                    int currentIndex = (new Random()).nextInt(startLinksList.size());
                    String urlImages = startLinksList.get(currentIndex);

                    saveLinksList.add(urlImages);
                    startLinksList.remove(currentIndex);

                    setImage(urlImages);
                    startTimer();

                    mHandler.postDelayed(this, 16000);
                } else {
                    // Последний элемент
                    newImageUrl = startLinksList.get(0);
                    saveLinksList.add(startLinksList.get(0));

                    if (saveLinksList.size() > 1) {
                        endGame();
                    } else {
                        Log.v("Error", "Missing images");
                    }
                }
            }
        };
        mHandler.postDelayed(timeUpdaterRunnable, 100);
    }

    private void endGame() {
        setContentView(R.layout._end_game);

        Display display = getWindowManager().getDefaultDisplay();

        LinearLayout mainLayer = (LinearLayout) findViewById(R.id.end_game);
        LayoutParams viewParams = new LayoutParams(LayoutParams.MATCH_PARENT, 240); // TODO Исправить размерность

        for (int index = 0; index < saveLinksList.size(); index++) {
            ImageButton imageButton = new ImageButton(this);

            Picasso.with(this)
                    .load(saveLinksList.get(index))
                    .resize(display.getWidth(), 240) // TODO Исправить размерность
                    .error(R.drawable.ic_android_black_24dp)
                    .into(imageButton);

            imageButton.setLayoutParams(viewParams);
            mainLayer.addView(imageButton);
        }

        // Обработка клика
    }

    private void setImage(String imageUrl) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Picasso.with(this)
                .load(imageUrl)
                .error(R.drawable.ic_android_black_24dp)
                .into(imageView);
    }

    public void startTimer() {
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
