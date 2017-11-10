package com.example.dziewan.application.service;


import java.util.ArrayList;
import java.util.List;

public class Walidacja {

    String wymiar;
    List<String> odpowiedz;

    public Walidacja(String wymiar) {
        this.wymiar = wymiar;
        odpowiedz = new ArrayList<>();
    }

    public List<String> waliduj() {
        walidujWymiar();
        return odpowiedz;
    }

    public void walidujWymiar() {
        int x = 0;
        boolean nieLiczba = false;
        for (char znak : wymiar.toCharArray()) {
            if (znak == 'x') {
                x++;
                continue;
            }
            if (!Character.isDigit(znak)) {
                nieLiczba = true;
            }
        }
        if (x != 1 || nieLiczba) odpowiedz.add("Błędne dane w wymiarze");
    }
}

