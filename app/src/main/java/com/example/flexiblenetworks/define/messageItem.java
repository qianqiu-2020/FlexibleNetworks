package com.example.flexiblenetworks.define;

public class messageItem {
    private String name;
    private int ImageId;
    private String partMessage;
    public messageItem(String name,int imageId,String partMessage){
        this.name=name;
        this.ImageId=imageId;
        this.partMessage=partMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public String getPartMessage() {
        return partMessage;
    }

    public void setPartMessage(String partMessage) {
        this.partMessage = partMessage;
    }
}
