package com.app.luberack.ModelClasses;

/**
 * Created by Farid Mushtaq on 5/4/2018.
 */

public class CarTyreData {
    String id;
    String image;
    String name;
    String location;
    String reviews;
    String lat;
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {

        return type;
    }
    public CarTyreData(String id, String image, String name, String location, String reviews, String lat, String type, String lng) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.location = location;
        this.reviews = reviews;
        this.lat = lat;
        this.type = type;
        this.lng = lng;
    }

    String type;
    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {

        return lat;
    }

    public String getLng() {
        return lng;
    }

    String lng;

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getReviews() {
        return reviews;
    }

    public CarTyreData(String id, String image, String name, String location, String reviews) {

        this.id = id;
        this.image = image;
        this.name = name;
        this.location = location;
        this.reviews = reviews;
    }
}
