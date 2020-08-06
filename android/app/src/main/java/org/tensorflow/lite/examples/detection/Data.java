package org.tensorflow.lite.examples.detection;

import android.graphics.Bitmap;

public class Data {

    private String name;
    private String time;
    private String profile;
    private Long closeCount; // 승현 추가

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

    public Long getCloseCount() {
        return closeCount;
    }
    public void setCloseCount(Long closeCount) {
        this.closeCount = closeCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
