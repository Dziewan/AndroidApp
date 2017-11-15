package com.example.dziewan.application.panele;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.dziewan.application.model.ExtendedPlyta;
import com.example.dziewan.application.model.KonwersjaZdjec;
import com.example.dziewan.application.model.Plyta;
import com.example.dziewan.application.model.Wartosci;
import com.example.dziewan.application.service.RestService;
import com.example.dziewan.application.service.Walidacja;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by Dziewan on 10.11.2017.
 */

public class PanelEdycjiPlyty extends PanelDodaniaPlyty implements KonwersjaZdjec {

    Button zapisz;
    ImageButton obrazekEdit;
    EditText materialEdit, wymiarEdit, gruboscEdit, miejsceEdit;
    Long ID;
    ExtendedPlyta plyta;

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
            plyta = (ExtendedPlyta) new RestService(Wartosci.ZNAJDZ_PO_ID).execute(Wartosci.LISTA_WSZYSTKICH_PLYT+"/"+ID).get().getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (plyta.getObraz() != null) obrazekEdit.setImageBitmap(odkoduj(plyta.getObraz()));
        materialEdit.setText(plyta.getPlyta().getMaterial());
        wymiarEdit.setText(plyta.getPlyta().getWymiar());
        gruboscEdit.setText(plyta.getPlyta().getGrubosc()+"");
        miejsceEdit.setText(plyta.getPlyta().getMiejsce());

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            obrazekEdit.setImageBitmap(photo);
            haveImage = true;
        }
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
            plyta.setObrazek(haveImage);

            byte[] array = zakoduj(((BitmapDrawable) obrazek.getDrawable()).getBitmap());
            HttpStatus httpStatus = null;
            try {
                httpStatus = new RestService(plyta, array, Wartosci.DODAJ_NOWA_PLYTE).execute(Wartosci.LISTA_WSZYSTKICH_PLYT+"/"+ID).get().getStatusCode();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (HttpStatus.OK.equals(httpStatus)) {
                Toast.makeText(this, "Płyta zapisana", Toast.LENGTH_LONG).show();
            }
        }
    }
}
