package org.tensorflow.lite.examples.detection;

import android.graphics.Bitmap;

public class Data {

    private String name;
    private String time;
    private String profile;
    public String getProfile(){
        return profile;
    }
    public void setProfile(String uri){
        this.profile=uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
