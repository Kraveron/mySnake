package com.example.asusmr.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by AsusMR on 05/01/2018.
 */

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void nextActivity(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
