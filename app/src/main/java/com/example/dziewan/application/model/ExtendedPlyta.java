package com.example.dziewan.application.model;

import java.io.Serializable;

/**
 * Created by coderion on 15.11.17.
 */

public class ExtendedPlyta implements Serializable {

    private Plyta plyta;

    private byte[] obrazek;

    public ExtendedPlyta(Plyta plyta, byte[] obrazek) {
        this.plyta = plyta;
        this.obrazek = obrazek;
    }

    public ExtendedPlyta() {
    }

    public byte[] getObrazek() {
        return obrazek;
    }

    public void setObrazek(byte[] obrazek) {
        this.obrazek = obrazek;
    }

    public Plyta getPlyta() {
        return plyta;
    }

    public void setPlyta(Plyta plyta) {
        this.plyta = plyta;
    }
}
