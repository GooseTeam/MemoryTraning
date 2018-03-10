package ru.team.gouse.testprojectremember;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// Базовый класс
public class BaseGame extends AppCompatActivity {
    protected int levelLimit = 0;
    protected ArrayList<String> startDataImages = new ArrayList<>();
    protected ArrayList<String> saveDataImages = new ArrayList<>();

    // Инициализируем data для игры
    public void initGame() {}

    // Основной метод для начала игры
    protected void startGame() {}

    // Основной метод для завершения игры
    protected void endGame() {}

    // Задаем список изображений
    protected void setImagesData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference("images");

        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String key = childDataSnapshot.getKey();
                    String value = (String) dataSnapshot.child(key).child("main_url").getValue();
                    startDataImages.add(value);
                }

                if (startDataImages.size() < 1) {
                    Log.v("Error", "Empty data list");
                    System.exit(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("Error", "No init database");
            }
        });
    }
}
