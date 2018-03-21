package ru.team.gouse.facerememberproject;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FaceRememberNewTest {
    // Проблема с подключением
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myReference = database.getReference("levels");

    @Test
    public void testInitGameSettings() throws Exception {
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int level = 1;
                FaceRememberNew faceRememberNew = new FaceRememberNew();

                faceRememberNew.initGameSettings(dataSnapshot, level);
                assertEquals(faceRememberNew.getSwitchTime(), 5000);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("Error", "No init database");
            }
        });
    }

    @Test
    public void testGenerateLevels() throws Exception {
        long countLevels = 0;
        boolean result;

        FaceRememberNew faceRememberNew = new FaceRememberNew();

        result = faceRememberNew.generateLevels(countLevels);
        assertFalse(result);

        countLevels = 5;
        result = faceRememberNew.generateLevels(countLevels);
        assertTrue(result);
    }

    @Test
    public void testSetLinksImagesList() throws Exception {
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FaceRememberNew faceRememberNew = new FaceRememberNew();

                faceRememberNew.setLinksImagesList(dataSnapshot);
                assertNotNull(faceRememberNew.getLinksImagesList());
                assertNotNull(faceRememberNew.getLinksImagesEndList());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("Error", "No init database");
            }
        });
    }
}
