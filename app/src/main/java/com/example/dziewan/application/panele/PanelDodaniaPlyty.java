package com.example.dziewan.application.panele;


import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dziewan.application.R;
import com.example.dziewan.application.model.KonwersjaZdjec;
import com.example.dziewan.application.model.Plyta;
import com.example.dziewan.application.model.Wartosci;
import com.example.dziewan.application.service.RestService;
import com.example.dziewan.application.service.Walidacja;

import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.List;

public class PanelDodaniaPlyty extends AppCompatActivity implements KonwersjaZdjec {

    public static final int REQUEST_CAPTURE = 1;
    private Uri imageUri;

    Button dodaj;
    ImageButton obrazek;
    EditText material, grubosc, wymiar, miejsce;
    Boolean haveImage = false;

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
            plyta.setObrazek(haveImage);

            byte[] array = zakoduj(((BitmapDrawable) obrazek.getDrawable()).getBitmap());
            HttpStatus httpStatus = null;
            try {
                httpStatus = new RestService(plyta, array, Wartosci.DODAJ_NOWA_PLYTE).execute(Wartosci.LISTA_WSZYSTKICH_PLYT).get().getStatusCode();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (HttpStatus.OK.equals(httpStatus)) {
                Toast.makeText(this, "Płyta zapisana", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean posiadaAparat() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void zrobZdjecie(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = null;
        try {
            photo = this.createTemporaryFile("picture", ".jpg");
            photo.delete();
        } catch(Exception e) {
            Log.v("BLAD", "Can't create file to take picture!");
            Toast.makeText(this, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
        }
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public void grabImage(ImageView imageView) {
        this.getContentResolver().notifyChange(imageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d("BLAD", "Failed to load", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap photo = (Bitmap) extras.get("data");
            this.grabImage(obrazek);
            haveImage = true;
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
    public byte[] zakoduj(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public Bitmap odkoduj(byte[] array) {
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    @Override
    public Bitmap odkodujZdjecie(String array) {
        byte[] decodedByte = Base64.decode(array, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0,      decodedByte.length);
    }
}
