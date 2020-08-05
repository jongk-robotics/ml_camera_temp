package org.tensorflow.lite.examples.detection;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

public class Photo {
    private String url;
    private ArrayList<String> friends;
    private String timeStamp;
    private GeoPoint location;
    private PhotoType photoType;
    private boolean isLiked;

    public static enum PhotoType {PLACE, FRIEND}


    public Photo() {

    }

    public Photo(String url,ArrayList<String> friends, String timeStamp, GeoPoint location, PhotoType photoType, boolean isLiked)
    {
        this.url = url;
        this.friends = friends;
        this.timeStamp = timeStamp;
        this.location = location;
        this.photoType = photoType;
        this.isLiked = isLiked;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setFriends(ArrayList<String> friends)
    {
        this.friends = friends;
    }

    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public void setLocation(GeoPoint location)
    {
        this.location = location;
    }

    public void setIsLiked(boolean isLiked)
    {
        this.isLiked = isLiked;
    }

    public String getUrl() {
        return url;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("url", url);
        result.put("friends", friends);
        result.put("timeStamp", timeStamp);
        result.put("location", location);
        result.put("photoType", photoType);
        result.put("isLiked", isLiked);

        return result;
    }
}
