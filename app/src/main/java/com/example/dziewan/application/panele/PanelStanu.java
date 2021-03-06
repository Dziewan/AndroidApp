package com.example.dziewan.application.panele;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dziewan.application.R;
import com.example.dziewan.application.model.ExtendedPlyta;
import com.example.dziewan.application.model.KonwersjaZdjec;
import com.example.dziewan.application.model.Plyta;
import com.example.dziewan.application.model.Wartosci;
import com.example.dziewan.application.service.RestService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PanelStanu extends AppCompatActivity {

    Button stanMagazynu, usun, edytuj;
    ListView listaPlyt;
    TextView materialLabel, gruboscLabel, wymiarLabel, regalLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stan);

        stanMagazynu = findViewById(R.id.button2);
        usun = findViewById(R.id.button3);
        edytuj = findViewById(R.id.button4);
        listaPlyt = findViewById(R.id.listaPlyt);
        materialLabel = findViewById(R.id.textView5);
        wymiarLabel = findViewById(R.id.textView6);
        gruboscLabel = findViewById(R.id.textView7);
        regalLabel = findViewById(R.id.textView8);

        // elo

        materialLabel.setTextColor(Color.BLACK);
        wymiarLabel.setTextColor(Color.BLACK);
        gruboscLabel.setTextColor(Color.BLACK);
        regalLabel.setTextColor(Color.BLACK);

        stanMagazynu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Plyta> plyty = null;
                try {
                    plyty = (List<Plyta>) new RestService(Wartosci.ZNAJDZ_WSZYSTKIE).execute(Wartosci.LISTA_WSZYSTKICH_PLYT).get().getBody();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listaPlyt.setAdapter(new AdapterStanu(getBaseContext(), R.layout.layout_plyty, plyty));
            }
        });
    }

    private class AdapterStanu extends ArrayAdapter<Plyta> {

        private int layout;
        List<Plyta> plytas = new ArrayList<>();

        public AdapterStanu(@NonNull Context context, int resource, @NonNull List<Plyta> objects) {
            super(context, resource, objects);
            this.layout = resource;
            this.plytas = objects;
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);

                viewHolder.material = convertView.findViewById(R.id.materialView);
                viewHolder.grubosc = convertView.findViewById(R.id.gruboscView);
                viewHolder.wymiar = convertView.findViewById(R.id.wymiarView);
                viewHolder.miejsce = convertView.findViewById(R.id.miejsceView);
                viewHolder.tableRow = convertView.findViewById(R.id.tableRow);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), PanelEdycjiPlyty.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("ID", plytas.get(position).getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            viewHolder.material.setText(plytas.get(position).getMaterial());
            viewHolder.grubosc.setText(""+plytas.get(position).getGrubosc());
            viewHolder.wymiar.setText(plytas.get(position).getWymiar());
            viewHolder.miejsce.setText(plytas.get(position).getMiejsce());

            return convertView;
        }
    }

    public class ViewHolder {
        TextView material, wymiar, grubosc, miejsce;
        CheckBox zaznacz;
        TableRow tableRow;
    }
}
