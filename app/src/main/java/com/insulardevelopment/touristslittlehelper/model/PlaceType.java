package com.insulardevelopment.touristslittlehelper.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Класс, содержащий информацию о типе мест
 */
@DatabaseTable(tableName = "place_types")
public class PlaceType implements Serializable{

    @DatabaseField(id = true, canBeNull = false, columnName = "id")
    private int id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "type_name")
    private String typesName;

    @DatabaseField(dataType = DataType.STRING, columnName = "rus_name")
    private String rusName;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = "chosen")
    private boolean chosen;

    @DatabaseField(dataType = DataType.STRING, columnName = "icon_url")
    private String iconUrl;

    @DatabaseField(columnName = "icon_id")
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
