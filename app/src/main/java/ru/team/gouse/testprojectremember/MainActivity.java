package ru.team.gouse.testprojectremember;

import android.hardware.camera2.params.Face;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button buttonFaceRemember = (Button) findViewById(R.id.face_remember);

        // Обработать разные классы
        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceRemember faceRememberGame = new FaceRemember();
                faceRememberGame.initGame();
            }
        };

        buttonFaceRemember.setOnClickListener(oclBtnOk);
    }
}
