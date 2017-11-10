package com.example.dziewan.application;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.dziewan.application.model.KonwersjaZdjec;
import com.example.dziewan.application.model.Plyta;
import com.example.dziewan.application.model.Wartosci;
import com.example.dziewan.application.panele.PanelDodaniaPlyty;
import com.example.dziewan.application.panele.PanelStanu;
import com.example.dziewan.application.panele.PanelEdytowaniaPlyty;
import com.example.dziewan.application.service.RestService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements KonwersjaZdjec {

    ImageButton plus, minus, rownosc;
    ImageView obrazek;
    Button zaladuj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        rownosc = findViewById(R.id.rownosc);
        obrazek = findViewById(R.id.obrazePlyty);
        zaladuj = findViewById(R.id.zaladuj);

        zaladuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRestResponse(view);
            }
        });
    }

    public void panelDodaniaPlyty(View view) {
        startActivity(new Intent(this, PanelDodaniaPlyty.class));
    }

    public void panelUsuwaniaPlyty(View view) {
        startActivity(new Intent(this, PanelEdytowaniaPlyty.class));
    }

    public void panelStanu(View view) {
        startActivity(new Intent(this, PanelStanu.class));
    }

    public void getRestResponse(View view) {
        final String strings = Wartosci.LISTA_WSZYSTKICH_PLYT;
        ResponseEntity<Plyta[]> entity = null;
        try {
            entity = new RestService().execute(strings).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] arr = null;
        List<Plyta> list = new ArrayList<>(Arrays.asList(entity.getBody()));
        for (Plyta plyta : list) {
            if (plyta.getObrazek() != null) {
                obrazek.setImageBitmap(odkodujZdjecie(plyta.getObrazek()));
                break;
            }
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
    public Bitmap odkodujZdjecie(String fotka) {
        byte[] decodedByte = Base64.decode(fotka, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0,      decodedByte.length);
    }
}
