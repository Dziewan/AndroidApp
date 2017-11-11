package com.example.dziewan.application.panele;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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

import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Created by Dziewan on 10.11.2017.
 */

public class PanelEdycjiPlyty extends PanelDodaniaPlyty implements KonwersjaZdjec {

    Button zapisz;
    ImageButton obrazekEdit;
    EditText materialEdit, wymiarEdit, gruboscEdit, miejsceEdit;
    Long ID;
    Plyta plyta;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodawanie_plyty);
        ID = getIntent().getExtras().getLong("ID");

        zapisz = findViewById(R.id.dodaj);
        obrazekEdit = findViewById(R.id.obrazek);
        materialEdit = findViewById(R.id.material);
        gruboscEdit = findViewById(R.id.grubosc);
        wymiarEdit = findViewById(R.id.wymiar);
        miejsceEdit = findViewById(R.id.miejsce);
        zapisz.setText("Zapisz");
        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zapiszPlyte(view);
            }
        });
        try {
            plyta = new RestService().execute(Wartosci.LISTA_WSZYSTKICH_PLYT+"/"+ID).get().getBody()[0];
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (plyta.getObrazek() != null) obrazekEdit.setImageBitmap(odkodujZdjecie(plyta.getObrazek()));
        materialEdit.setText(plyta.getMaterial());
        wymiarEdit.setText(plyta.getWymiar());
        gruboscEdit.setText(plyta.getGrubosc()+"");
        miejsceEdit.setText(plyta.getMiejsce());

        if (!posiadaAparat()) {
            obrazekEdit.setEnabled(false);
        }

        obrazekEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zrobZdjecie(view);
            }
        });
    }

    public void zapiszPlyte(View view) {
        Walidacja walidacja = new Walidacja(wymiarEdit.getText().toString());
        List<String> odp = walidacja.waliduj();
        if (!odp.isEmpty()) {
            Toast.makeText(getApplicationContext(), odp.get(0), Toast.LENGTH_LONG).show();
        } else {
            Plyta plyta = new Plyta();
            plyta.setMaterial(materialEdit.getText().toString());
            plyta.setWymiar(wymiarEdit.getText().toString());
            plyta.setGrubosc(Double.valueOf(gruboscEdit.getText().toString()));
            plyta.setMiejsce(miejsceEdit.getText().toString());
            plyta.setObrazek(zakodujZdjecie(((BitmapDrawable) obrazekEdit.getDrawable()).getBitmap()));

            HttpStatus httpStatus = null;
            try {
                httpStatus = new RestService(plyta).execute(Wartosci.LISTA_WSZYSTKICH_PLYT+"/"+ID).get().getStatusCode();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (HttpStatus.OK.equals(httpStatus)) {
                Toast.makeText(this, "Płyta zapisana", Toast.LENGTH_LONG).show();
            }
        }
    }
}
