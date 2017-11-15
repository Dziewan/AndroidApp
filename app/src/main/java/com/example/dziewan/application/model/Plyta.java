package com.example.dziewan.application.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Plyta {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String nazwaPlyty;

    @JsonProperty("material")
    private String material;

    @JsonProperty("place")
    private String miejsce;

    @JsonProperty("size")
    private String wymiar;

    @JsonProperty("thickness")
    private Double grubosc;

    @JsonProperty("obrazek")
    private Boolean obrazek;

    @Override
    public String toString() {
        return "id: "+id+" "+" material: "+material+" wymiar: "+wymiar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwaPlyty() {
        return nazwaPlyty;
    }

    public void setNazwaPlyty(String name) {
        this.nazwaPlyty = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMiejsce() {
        return miejsce;
    }

    public void setMiejsce(String miejsce) {
        this.miejsce = miejsce;
    }

    public String getWymiar() {
        return wymiar;
    }

    public void setWymiar(String wymiar) {
        this.wymiar = wymiar;
    }

    public Double getGrubosc() {
        return grubosc;
    }

    public void setGrubosc(Double grubosc) {
        this.grubosc = grubosc;
    }

    public Boolean getObrazek() {
        return obrazek;
    }

    public void setObrazek(Boolean obrazek) {
        this.obrazek = obrazek;
    }
}
