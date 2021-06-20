package com.jbit.fams;

import android.content.ContextWrapper;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pixplicity.easyprefs.library.Prefs;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            new Prefs.Builder()
                    .setContext(this)
                    .setMode(ContextWrapper.MODE_PRIVATE)
                    .setPrefsName(getPackageName())
                    .setUseDefaultSharedPreference(true)
                    .build();
            startActivity(new Intent(MainActivity.this, EditCourses.class));
            finish();
        }


        setContentView(R.layout.activity_main);



        (findViewById(R.id.goToAppButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(100);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
