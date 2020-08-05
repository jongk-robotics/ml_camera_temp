package org.tensorflow.lite.examples.detection;

public class FriendImage {
    private String name;
    private String url;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    FriendImage(String name, String url)
    {
        this.name = name;
        this.url = url;
    }
}
