package com.example.pokedex.modelos;

import com.google.gson.annotations.SerializedName;

public class Pokemon {

    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    private String number;

    public int getNumber() {
        String[] urlPartes= url.split("/");
        return Integer.parseInt(urlPartes[urlPartes.length-1]);
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
