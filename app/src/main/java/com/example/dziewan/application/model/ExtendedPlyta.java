package com.example.dziewan.application.model;

import java.io.Serializable;

/**
 * Created by coderion on 15.11.17.
 */

public class ExtendedPlyta implements Serializable {

    private Plyta plyta;

    private byte[] obraz;

    public ExtendedPlyta(Plyta plyta, byte[] obrazek) {
        this.plyta = plyta;
        this.obraz = obrazek;
    }

    public ExtendedPlyta() {
    }

    public byte[] getObraz() {
        return obraz;
    }

    public void setObrazek(byte[] obrazek) {
        this.obraz = obrazek;
    }

    public Plyta getPlyta() {
        return plyta;
    }

    public void setPlyta(Plyta plyta) {
        this.plyta = plyta;
    }
}
