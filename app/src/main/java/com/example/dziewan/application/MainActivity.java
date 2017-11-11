package com.example.dziewan.application;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import com.example.dziewan.application.model.KonwersjaZdjec;
import com.example.dziewan.application.panele.PanelDodaniaPlyty;
import com.example.dziewan.application.panele.PanelStanu;
import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity {

    ImageButton nowaPlyta, stanMagazynu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nowaPlyta = findViewById(R.id.plus);
        stanMagazynu = findViewById(R.id.rownosc);
    }

    public void panelDodaniaPlyty(View view) {
        startActivity(new Intent(this, PanelDodaniaPlyty.class));
    }

    public void panelStanu(View view) {
        startActivity(new Intent(this, PanelStanu.class));
    }
 }

