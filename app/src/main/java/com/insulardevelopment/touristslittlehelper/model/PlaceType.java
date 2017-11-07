package com.insulardevelopment.touristslittlehelper.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Класс, содержащий информацию о типе мест
 */
@Entity(tableName = "place_type")
public class PlaceType implements Serializable{

    @PrimaryKey
    private int id;
    private String typesName;
    private String rusName;
    private boolean chosen;
    private String iconUrl;
    private int iconId;

    public PlaceType() {
    }

    public PlaceType(int id, String typesName, String rusName, boolean chosen, String iconUrl, int iconId) {
        this.typesName = typesName;
        this.rusName = rusName;
        this.chosen = chosen;
        this.iconUrl = iconUrl;
        this.iconId = iconId;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypesName() {
        return typesName;
    }

    public void setTypesName(String typesName) {
        this.typesName = typesName;
    }

    public String getRusName() {
        return rusName;
    }

    public void setRusName(String rusName) {
        this.rusName = rusName;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
