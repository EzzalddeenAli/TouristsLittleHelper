package com.insulardevelopment.touristslittlehelper.placetype;

/**
 * Created by админ on 28.11.2016.
 */

public class PlacesTypes {
    private String typesName;
    private String rusName;
    private boolean chosen;

    public PlacesTypes() {
    }

    public PlacesTypes(String typesName, String rusName, boolean chosen) {
        this.typesName = typesName;
        this.rusName = rusName;
        this.chosen = chosen;
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
}
