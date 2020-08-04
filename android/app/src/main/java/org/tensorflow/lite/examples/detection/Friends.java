package org.tensorflow.lite.examples.detection;

import java.util.ArrayList;
import java.util.HashMap;

public class Friends {

    private String name;
    private String imgUrl;

    public Friends() {

    }

    public Friends(String name, String imgUrl)
    {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result=new HashMap<>();
        result.put("name", name);
        result.put("imgUrls", imgUrl);

        return result;
    }
}
