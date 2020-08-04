package org.tensorflow.lite.examples.detection;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;

public class Places {
    private GeoPoint location;
    private String placeName;
    private String imgUrl;

    public Places() {

    }

    public Places(GeoPoint location, String placeName, String imgUrl)
    {
        this.location = location;
        this.placeName = placeName;
        this.imgUrl = imgUrl;
    }

    public void setLocation(GeoPoint location)
    {
        this.location = location;
    }

    public void setPlaceName(String placeName)
    {
        this.placeName = placeName;
    }

    public void setImgUrls(String imgUrls)
    {
        this.imgUrl = imgUrl;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPlaceName() {
        return placeName;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("location", location);
        result.put("placeName", placeName);
        result.put("imgUrl", imgUrl);

        return result;
    }
}
