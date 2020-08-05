package org.tensorflow.lite.examples.detection;

import java.util.ArrayList;
import java.util.HashMap;

public class Friends {

    private String name;
    private Photo photo;
    private String profileUrl;

    public Friends() {

    }

    public Friends(String name, Photo photo, String profileUrl)
    {
        this.name = name;
        this.photo = photo;
        this.profileUrl = profileUrl;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Photo getPhoto() {
        return photo;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("name", name);
        result.put("photo", photo);

        return result;
    }
}
