package com.example.dziewan.application.panele;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dziewan.application.R;
import com.example.dziewan.application.model.KonwersjaZdjec;
import com.example.dziewan.application.model.Plyta;
import com.example.dziewan.application.model.Wartosci;
import com.example.dziewan.application.service.RestService;
import com.example.dziewan.application.service.Walidacja;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PanelDodaniaPlyty extends AppCompatActivity implements KonwersjaZdjec {

    public static final int REQUEST_CAPTURE = 1;

    Button dodaj;
    ImageButton obrazek;
    EditText material, grubosc, wymiar, miejsce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodawanie_plyty);

        dodaj = findViewById(R.id.dodaj);
        obrazek = findViewById(R.id.obrazek);
        material = findViewById(R.id.material);
        grubosc = findViewById(R.id.grubosc);
        wymiar = findViewById(R.id.wymiar);
        miejsce = findViewById(R.id.miejsce);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dodajPlyte(view);
            }
        });

        obrazek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zrobZdjecie(view);
            }
        });

        if (!posiadaAparat()) {
            obrazek.setEnabled(false);
        }
    }

    public void dodajPlyte(View view) {
        Walidacja walidacja = new Walidacja(wymiar.getText().toString());
        List<String> odp = walidacja.waliduj();
        if (!odp.isEmpty()) {
            Toast.makeText(getApplicationContext(), odp.get(0), Toast.LENGTH_LONG).show();
        } else {
            Plyta plyta = new Plyta();
            plyta.setMaterial(material.getText().toString());
            plyta.setWymiar(wymiar.getText().toString());
            plyta.setGrubosc(Double.valueOf(grubosc.getText().toString()));
            plyta.setMiejsce(miejsce.getText().toString());
            plyta.setObrazek(zakodujZdjecie(((BitmapDrawable) obrazek.getDrawable()).getBitmap()));

            new RestService(plyta).execute(Wartosci.LISTA_WSZYSTKICH_PLYT);
        }
    }

    public boolean posiadaAparat() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void zrobZdjecie(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            obrazek.setImageBitmap(photo);
        }
    }

    @Override
    public String zakodujZdjecie(Bitmap zdjecie) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        zdjecie.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    @Override
    public Bitmap odkodujZdjecie(String array) {
        byte[] decodedByte = Base64.decode(array, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0,      decodedByte.length);
    }
}
