package com.example.flexiblenetworks;
/*好友类，记录好友相关信息
* 如昵称，头像等...
* */
public class Friend {
    private String name;
    private int imageId;
    public Friend(String name,int imageId)
    {
        this.name=name;
        this.imageId=imageId;
    }
    public String getName()
    {
        return name;
    }
    public int getImageId()
    {
        return imageId;
    }
}
