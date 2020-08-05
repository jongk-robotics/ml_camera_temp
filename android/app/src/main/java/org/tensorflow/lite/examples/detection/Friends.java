package org.tensorflow.lite.examples.detection;

import java.util.ArrayList;
import java.util.HashMap;

public class Friends {

    private String name;
    private Photo photo;

    public Friends() {

    }

    public Friends(String name, Photo photo)
    {
        this.name = name;
        this.photo = photo;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Photo getPhoto() {
        return photo;
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
