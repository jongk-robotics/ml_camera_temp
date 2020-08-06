package org.tensorflow.lite.examples.detection;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Photo implements Serializable {
    private String url;
    private String userEmail;

    private Long seconds;
    private int nanoSeconds;

    private String locationName;
    private String memo = "";

    private ArrayList<String> friends = new ArrayList<>();

    private double latitude;
    private double longitude;

    private ArrayList<String> likedPeople = new ArrayList<>();

    private boolean isShared = false;
    private boolean isPeople = false;

    Photo()
    {

    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setFriends(ArrayList<String> friends)
    {
        this.friends = friends;
    }

    public void setTimeStamp(Timestamp timeStamp)
    {
        this.seconds = timeStamp.getSeconds();
        this.nanoSeconds = timeStamp.getNanoseconds();
    }

    public void setLocation(GeoPoint location)
    {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public void setLikedPeople(ArrayList<String> likedPeople) {
        this.likedPeople = likedPeople;
    }

    public void addLikedPeople(String userEmail)
    {
        likedPeople.add(userEmail);
    }

    public void removeLikedPeople(String userEmail)
    {
        likedPeople.remove(userEmail);
    }

    public ArrayList<String> getLikedPeople() {
        return likedPeople;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public void setPeople(boolean people) {
        isPeople = people;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUrl() {
        return url;
    }

    public GeoPoint getLocation() {
        return new GeoPoint(latitude, longitude);
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getMemo() {
        return memo;
    }

    public Timestamp getTimeStamp() {
        return new Timestamp(seconds, nanoSeconds);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean getPeolple()
    {
        return isPeople;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("url", url);
        result.put("userEmail", userEmail);
        result.put("friends", friends);
        result.put("timeStamp", new Timestamp(seconds, nanoSeconds));
        result.put("location", new GeoPoint(latitude, longitude));
        result.put("locationName", locationName);
        result.put("memo", memo);
        result.put("likedPeople", likedPeople);
        result.put("isShared", isShared);
        result.put("isPeople", isPeople);

        return result;
    }

    public Photo copy()
    {
        Photo copy  = new Photo();
        copy.url = url;
        copy.userEmail = userEmail;
        copy.seconds = seconds;
        copy.nanoSeconds = nanoSeconds;
        copy.locationName = locationName;
        copy.memo = memo;
        copy.friends.addAll(friends);
        copy.latitude = latitude;
        copy.longitude = longitude;
        copy.likedPeople.addAll(likedPeople);
        copy.isPeople = isPeople;
        copy.isShared = isShared;

        return copy;
    }

}
