package com.insulardevelopment.touristslittlehelper.network.entities.Place;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Маргарита on 29.08.2017.
 */

public class OpeningHours implements Serializable {

    @SerializedName("weekday_text")
    private List<String> weekdayText;

    public OpeningHours() {
    }

    public OpeningHours(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }
}
