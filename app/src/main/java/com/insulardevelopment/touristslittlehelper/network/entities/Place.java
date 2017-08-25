package com.insulardevelopment.touristslittlehelper.network.entities;

import com.google.gson.annotations.SerializedName;
import com.insulardevelopment.touristslittlehelper.model.Photo;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import static com.insulardevelopment.touristslittlehelper.model.Place.DATABASE_NAME;
import static com.insulardevelopment.touristslittlehelper.network.entities.Place.Geometry.GEOMETRY_TABLE;
import static com.insulardevelopment.touristslittlehelper.network.entities.Place.Location.LOCATION_TABLE;
import static com.insulardevelopment.touristslittlehelper.network.entities.Place.OpeningHours.OPENING_HOURS_TABLE;

/**
 * Класс, содержащий информацию о месте
 */
@DatabaseTable(tableName = DATABASE_NAME)
public class Place {

    public static final String DATABASE_NAME = "places";
    public static final String START_PLACE = "start";
    public static final String FINISH_PLACE = "finish";

    @DatabaseField(generatedId = true, canBeNull = false, columnName = "id")
    private int idPlace;

    @DatabaseField(columnName = "name")
    @SerializedName("name")
    private String name;

    @DatabaseField(columnName = "address")
    @SerializedName("formatted_address")
    private String formattedAddress;

    @DatabaseField(columnName = "phone_number")
    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;

    @DatabaseField(columnName = "icon")
    @SerializedName("icon")
    private String icon;

    @DatabaseField(columnName = "place_id")
    @SerializedName("place_id")
    private String placeId;

    @DatabaseField(columnName = "rating")
    @SerializedName("rating")
    private double rating;

//    @DatabaseField(columnName = "opening_hours", foreign = true, foreignAutoCreate = true)
    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    @DatabaseField(columnName = "website")
    @SerializedName("website")
    private String webSite;

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("reviews")
    private List<Review> reviews;

    @SerializedName("photos")
    private List<Photo> photos;

    @DatabaseField(columnName = "route", foreign = true, foreignAutoCreate = true)
    private Route route;

    private boolean chosen = true;

    public Place() {
    }

    public Place(String name, String formattedAddress, String formattedPhoneNumber, String icon, String placeId, double rating, OpeningHours openingHours, String webSite, Geometry geometry, List<Review> reviews, List<Photo> photos) {
        this.name = name;
        this.formattedAddress = formattedAddress;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.icon = icon;
        this.placeId = placeId;
        this.rating = rating;
        this.openingHours = openingHours;
        this.webSite = webSite;
        this.geometry = geometry;
        this.reviews = reviews;
        this.photos = photos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    @DatabaseTable(tableName = GEOMETRY_TABLE)
    public class Geometry{

        public static final String GEOMETRY_TABLE = "geometry";

        @DatabaseField(generatedId = true, canBeNull = false, columnName = "id")
        private int idGeom;

        @DatabaseField(columnName = "location")
        @SerializedName("location")
        private Location location;

        public Geometry() {
        }

        public Geometry(Location location) {
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    @DatabaseTable(tableName = LOCATION_TABLE)
    public class Location{

        public static final String LOCATION_TABLE = "location";

        @DatabaseField(generatedId = true, canBeNull = false, columnName = "id")
        private int idLoc;

        @DatabaseField(columnName = "latitude")
        @SerializedName("lat")
        private double latitude;

        @DatabaseField(columnName = "longitude")
        @SerializedName("lng")
        private double longitude;

        public Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Location() {
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    @DatabaseTable(tableName = OPENING_HOURS_TABLE)
    public class OpeningHours{

        public static final String OPENING_HOURS_TABLE = "opening hours";

        @DatabaseField(generatedId = true, canBeNull = false, columnName = "id")
        private int idOpen;

        @DatabaseField(columnName = "weekday_text")
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
}
