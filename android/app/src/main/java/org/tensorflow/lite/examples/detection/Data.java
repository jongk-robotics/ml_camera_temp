package org.tensorflow.lite.examples.detection;

import android.graphics.Bitmap;

import com.google.firebase.Timestamp;

public class Data {

    private String name;
    //private String time;
    private String profile;
    private Long closeCount; // 승현 추가
    private Timestamp timeStamp; //승현 추가
    private String diff; //승현 추가

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

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getDiff() {
        return diff;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

//    public String getTime() {
//        return time;
//    }
//    public void setTime(String time) {
//        this.time = time;
//    }


}
