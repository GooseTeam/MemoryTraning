package ru.team.gouse.testprojectremember;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
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
    private int maxImages = 4;
    private int timer = 0;

    public ArrayList<String> LinksList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference("images");


        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String key = childDataSnapshot.getKey();
                    String value = (String) dataSnapshot.child(key).child("main_url").getValue();
                    LinksList.add(value);
                }

                if (LinksList.size() > 1) {
                    try {
                        startGame(LinksList);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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

    public void startGame(ArrayList LinksList) throws InterruptedException {
        //Log.v("DEBUG", "" + LinksList);
        //Log.v("DEBUG", "" + LinksList.get((new Random()).nextInt(LinksList.size())));

        String urlImages = (String) LinksList.get((new Random()).nextInt(LinksList.size()));
        setImage(urlImages);

        //Handler testHandler = new Handler().postDelayed;


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v("DEBUG", "HANDLER");
                /*startActivity(newIntent(
                        SplashActivity.this, MainMenuActivity.class));
                finish();*/
            }
        }, 2000);

        //Handler testHandle = new Handler().postDelayed(()->doubleBackToExitPressedOnce = false, 3000);
        Thread.sleep(10000);
    }


    private void setImage(String imageUrl) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
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
