package org.tensorflow.lite.examples.detection;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;

public class Places {
    private GeoPoint location;
    private String placeName;
    private Photo photo;

    public Places() {

    }

    public Places(GeoPoint location, String placeName, Photo photo)
    {
        this.location = location;
        this.placeName = placeName;
        this.photo = photo;
    }

    public void setLocation(GeoPoint location)
    {
        this.location = location;
    }

    public void setPlaceName(String placeName)
    {
        this.placeName = placeName;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public Photo getPhoto() {
        return photo;
    }

    public String getPlaceName() {
        return placeName;
    }

    public HashMap<String, Object> PlaceToMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("location", location);
        result.put("placeName", placeName);

        return result;
    }

    public HashMap<String, Object> PhotoToMap(){
        return photo.toMap();
    }
}
