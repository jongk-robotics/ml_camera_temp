package org.tensorflow.lite.examples.detection;

import android.graphics.Bitmap;

public class Data {

    private String name;
    private String number;
    private Bitmap profile;
    public Bitmap getProfile(){
        return profile;
    }
    public void setProfile(Bitmap profile){
        this.profile=profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number =number;
    }


}
